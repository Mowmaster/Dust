package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustMenuRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

// Hangman for an ancient word. The server runs the game; the screen (Client/DecipherScreen) shows it and sends
// guesses as menu "button clicks" (button 0-25 = guess a-z), vanilla's way for a screen to tell its menu something
// (enchanting table options, loom patterns...).
//
// - Letters whose glyph the player already knows start filled in, and can't be guessed (nothing to learn).
// - A right guess fills every position of that letter; a wrong one adds a mistake.
// - All letters filled: deciphered. The player learns every glyph in the word and the tablet is used up.
// - MAX_WRONG mistakes: the tablet crumbles, nothing is learned.
// - Guesses and mistakes are saved on the tablet after every guess, so closing the screen doesn't reset the game.
//
// The state is synced to the client with DataSlots. A DataSlot is sent as a 16-bit short, so the 26 guessed letters
// are split over two slots (a-m, n-z) and words are at most 15 letters (one bit per position).
public class DecipherMenu extends AbstractContainerMenu
{
    public static final int MAX_WRONG = 6;
    public static final int MAX_LENGTH = 15;

    public static final int PLAYING = 0;
    public static final int WON = 1;
    public static final int LOST = 2;

    private final String word;
    private final InteractionHand hand;
    private final Player player;

    private final DataSlot revealed = DataSlot.standalone();      // bit per position
    private final DataSlot guessedLow = DataSlot.standalone();    // a-m
    private final DataSlot guessedHigh = DataSlot.standalone();   // n-z
    private final DataSlot wrong = DataSlot.standalone();
    private final DataSlot state = DataSlot.standalone();

    // Client side: built from the extra data AncientWritingItem sent when it opened the menu
    public DecipherMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf)
    {
        this(containerId, inventory, buf.readUtf(), buf.readEnum(InteractionHand.class));
    }

    // Server side (and the end of the client constructor above)
    public DecipherMenu(int containerId, Inventory inventory, String word, InteractionHand hand)
    {
        super(DustMenuRegistry.DECIPHER.get(), containerId);
        this.word = AncientWords.clean(word);
        this.hand = hand;
        this.player = inventory.player;
        addDataSlot(revealed);
        addDataSlot(guessedLow);
        addDataSlot(guessedHigh);
        addDataSlot(wrong);
        addDataSlot(state);

        if (!player.level().isClientSide()) {
            // Carry on from where the player left off (saved on the tablet), and fill in what they can already read
            ItemStack held = player.getItemInHand(hand);
            int savedGuesses = held.getOrDefault(DustComponentDataRegistry.DECIPHER_GUESSES.get(), 0);
            wrong.set(held.getOrDefault(DustComponentDataRegistry.DECIPHER_MISTAKES.get(), 0));
            for (int glyph = 0; glyph < AncientLanguage.GLYPH_COUNT; glyph++) {
                if (AncientLanguage.knows(player, glyph) || (savedGuesses & (1 << glyph)) != 0) {
                    setGuessed(glyph);
                    revealLetter(glyph);
                }
            }
        }
    }

    // ---- read by the screen ----

    public String getWord() { return word; }
    public int getWrong() { return wrong.get(); }
    public int getState() { return state.get(); }

    public boolean isRevealed(int position)
    {
        return (revealed.get() & (1 << position)) != 0;
    }

    public boolean isGuessed(int glyph)
    {
        return glyph < 13 ? (guessedLow.get() & (1 << glyph)) != 0 : (guessedHigh.get() & (1 << (glyph - 13))) != 0;
    }

    // ---- the game (server) ----

    @Override
    public boolean clickMenuButton(Player player, int buttonId)
    {
        if (state.get() != PLAYING || buttonId < 0 || buttonId >= AncientLanguage.GLYPH_COUNT || isGuessed(buttonId)) {
            return false;
        }
        setGuessed(buttonId);
        if (revealLetter(buttonId)) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 1.2F);
            if (allRevealed()) win();
        } else {
            wrong.set(wrong.get() + 1);
            player.level().playSound(null, player.blockPosition(), SoundEvents.STONE_HIT, SoundSource.PLAYERS, 1.0F, 0.6F);
            if (wrong.get() >= MAX_WRONG) lose();
        }
        if (state.get() == PLAYING) saveProgress();
        return true;
    }

    private void saveProgress()
    {
        ItemStack held = player.getItemInHand(hand);
        if (isOurWriting(held)) {
            held.set(DustComponentDataRegistry.DECIPHER_GUESSES.get(), guessedLow.get() | (guessedHigh.get() << 13));
            held.set(DustComponentDataRegistry.DECIPHER_MISTAKES.get(), wrong.get());
        }
    }

    private void setGuessed(int glyph)
    {
        if (glyph < 13) guessedLow.set(guessedLow.get() | (1 << glyph));
        else guessedHigh.set(guessedHigh.get() | (1 << (glyph - 13)));
    }

    // Reveals every position holding this letter; false if the word doesn't contain it
    private boolean revealLetter(int glyph)
    {
        boolean found = false;
        int mask = revealed.get();
        for (int i = 0; i < word.length(); i++) {
            if (AncientLanguage.glyphIndex(word.charAt(i)) == glyph) {
                mask |= 1 << i;
                found = true;
            }
        }
        revealed.set(mask);
        return found;
    }

    private boolean allRevealed()
    {
        return revealed.get() == (1 << word.length()) - 1;
    }

    private void win()
    {
        state.set(WON);
        if (player instanceof ServerPlayer serverPlayer) {
            for (char c : word.toCharArray()) {
                AncientLanguage.learn(serverPlayer, AncientLanguage.glyphIndex(c));   // false (and silent) if already known
            }
            serverPlayer.level().playSound(null, serverPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.6F, 1.4F);
        }
        useUpWriting();
    }

    private void lose()
    {
        state.set(LOST);
        player.sendSystemMessage(Component.translatable("message.dust.ancient.crumbled").withStyle(ChatFormatting.DARK_GRAY));
        player.level().playSound(null, player.blockPosition(), SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
        useUpWriting();
    }

    // Removes the tablet / transcription that was deciphered (not in creative, as with other consumed items)
    private void useUpWriting()
    {
        ItemStack held = player.getItemInHand(hand);
        if (isOurWriting(held)) {
            held.consume(1, player);
        }
    }

    private boolean isOurWriting(ItemStack stack)
    {
        return stack.getItem() instanceof AncientWritingItem && AncientWritingItem.getWord(stack).equals(word);
    }

    // Closes the screen if the tablet leaves the hand mid-game (progress is saved on it, so nothing is lost)
    @Override
    public boolean stillValid(Player player)
    {
        return state.get() != PLAYING || isOurWriting(player.getItemInHand(hand));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        return ItemStack.EMPTY;   // no slots
    }
}
