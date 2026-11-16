package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// A modifier pylon's part in a spell tree: which Spell Pylon it belongs to, and what it drew from the deck (a
// modifier and its tier, or a spell break), or for the Shape Pylon, the shape. The Spell Pylon does the drawing and
// keeps the order (SpellPylonBlockEntity); this just holds the result.
//   card       the deck card it took (back in the deck if it's broken before it locks; for a Fragment slot, the slot)
//   levels     how many levels of its modifier it gives the spell: 1 to start, more for each upgrade node filled
//              (nodes of maxNodes; each costs a spell level and adds the modifier's levels per node)
//   legendary  a Legendary pylon: its levels count double
//   stop       a null corruption: nothing more can follow it in its branch
//   locked     can't be broken or changed: set once a node is filled, straight away for blessed / corrupted /
//              legendary / stop pylons, and for the Shape Pylon once a pylon follows it
//   powered    a spell break with redstone power (the line carries on)
// All of it is synced: the client tints the pylon from it (Client/PylonTint).
public class PylonBlockEntity extends CBSyncedBlockEntity implements com.mowmaster.mowlib.reading.IReadableBlock
{
    private @Nullable BlockPos main;
    private @Nullable Identifier modifier;
    private ModifierTier tier = ModifierTier.NORMAL;
    private boolean spellBreak;
    private @Nullable Identifier shape;
    private @Nullable Identifier card;
    private int maxNodes;
    private int nodes;
    private int levels;
    private boolean legendary;
    private boolean stop;
    private boolean locked;
    private boolean powered;

    public PylonBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.PYLON.get(), pos, state);
    }

    public @Nullable BlockPos main() { return main; }
    public @Nullable Identifier modifier() { return modifier; }
    public ModifierTier tier() { return tier; }
    public boolean isSpellBreak() { return spellBreak; }
    public @Nullable Identifier shape() { return shape; }
    public @Nullable Identifier card() { return card; }
    public int maxNodes() { return maxNodes; }
    public int nodes() { return nodes; }
    public int levels() { return levels; }
    public boolean legendary() { return legendary; }
    public boolean stop() { return stop; }
    public boolean locked() { return locked; }
    public boolean powered() { return powered; }

    // ---- client only: its card rising out of the top while a player is near, poofing away when they leave, like the
    // Spell Pylon's element icon (clientTick; Client/PylonCardRenderer draws it) ----
    private int appear;
    private int lastAppear;

    public static void clientTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, PylonBlockEntity pylon)
    {
        pylon.lastAppear = pylon.appear;
        if (pylon.modifier == null) {
            pylon.appear = 0;
            return;
        }
        boolean near = false;
        for (net.minecraft.world.entity.player.Player player : level.players()) {
            if (player.isLocalPlayer() && !player.isSpectator()
                    && player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos)) <= SpellPylonBlockEntity.NEAR_DISTANCE * SpellPylonBlockEntity.NEAR_DISTANCE) near = true;
        }
        var random = level.getRandom();
        if (near) {
            if (pylon.appear < SpellPylonBlockEntity.APPEAR_TICKS) {
                pylon.appear++;
                // Sparkles curling out of the top as it rises
                level.addParticle(net.minecraft.core.particles.ParticleTypes.ENCHANT, pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3,
                        pos.getY() + 1.0, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3, 0, 0.05, 0);
            }
        } else if (pylon.appear > 0) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 1.0 + SpellPylonBlockEntity.ICON_RISE, pos.getZ() + 0.5,
                        (random.nextDouble() - 0.5) * 0.08, (random.nextDouble() - 0.5) * 0.08, (random.nextDouble() - 0.5) * 0.08);
            }
            pylon.appear = 0;
            pylon.lastAppear = 0;
        }
    }

    // How far out the card is, 0 (gone) to 1 (out), smoothed between ticks
    public float appear(float partialTick)
    {
        return Mth.lerp(partialTick, lastAppear, appear) / SpellPylonBlockEntity.APPEAR_TICKS;
    }

    // ---- MowLib's reading HUD: what it holds (PylonReading) ----

    @Override
    public com.mowmaster.mowlib.reading.@Nullable ReadingPaper read(com.mowmaster.mowlib.reading.ReadingContext context)
    {
        if (SpellPylonReading.holdsResearchBook(context.player())) return null;
        return com.mowmaster.mowlib.reading.ReadingPaper.of(PylonReading.lines(this));
    }

    public boolean canUpgrade()
    {
        return main != null && modifier != null && nodes < maxNodes;
    }

    // One more node filled, adding this many levels of its modifier (a levelled pylon is locked)
    void upgrade(int addedLevels)
    {
        nodes++;
        levels += addedLevels;
        locked = true;
        sync();
    }

    void lock()
    {
        if (locked) return;
        locked = true;
        sync();
    }

    public PylonType type()
    {
        return getBlockState().getBlock() instanceof PylonBlock pylon ? pylon.type() : PylonType.SHAPE;
    }

    // Joined to a tree with what it drew (modifier null, spellBreak and stop false: there was nothing to draw)
    void link(BlockPos main, @Nullable Identifier card, @Nullable Identifier modifier, ModifierTier tier, boolean spellBreak,
              @Nullable Identifier shape, int maxNodes, boolean legendary, boolean stop)
    {
        this.card = card;
        this.maxNodes = maxNodes;
        this.nodes = 0;
        this.levels = modifier == null ? 0 : 1;
        this.legendary = legendary;
        this.stop = stop;
        this.main = main;
        this.modifier = modifier;
        this.tier = tier;
        this.spellBreak = spellBreak;
        this.shape = shape;
        // Blessed / corrupted / legendary / stop pylons are locked as soon as they're placed
        this.locked = tier != ModifierTier.NORMAL || legendary || stop;
        this.powered = spellBreak && level != null && level.hasNeighborSignal(worldPosition);
        sync();
    }

    void setShape(Identifier shape)
    {
        this.shape = shape;
        sync();
    }

    // Cut off from its tree (the pylon before it was broken): it stays, but does nothing until broken and placed again
    void unlink()
    {
        main = null;
        modifier = null;
        tier = ModifierTier.NORMAL;
        spellBreak = false;
        shape = null;
        card = null;
        maxNodes = 0;
        nodes = 0;
        levels = 0;
        legendary = false;
        stop = false;
        locked = false;
        powered = false;
        updateLook(null, PylonLinks.NONE);
        sync();
    }

    // A spell break's redstone changed
    void updatePower()
    {
        boolean now = level != null && level.hasNeighborSignal(worldPosition);
        if (now == powered) return;
        powered = now;
        // The model follows (broken / connected); the arms stay as they are
        updateLook(null, getBlockState().getValue(PylonBlock.LINKS));
        sync();
    }

    // The block state to match: which way its parent is (null: leave it), which sides lead on, how many nodes. The
    // block entity stays when only the state changes (same block).
    void updateLook(@Nullable Direction parent, PylonLinks links)
    {
        if (level == null || !(getBlockState().getBlock() instanceof PylonBlock)) return;
        PylonBreak look = main == null || !spellBreak ? PylonBreak.NONE : powered ? PylonBreak.CONNECTED : PylonBreak.BROKEN;
        BlockState now = getBlockState().setValue(PylonBlock.LINKS, links).setValue(PylonBlock.NODES, Mth.clamp(maxNodes, 1, PylonConfig.MAX_NODES))
                .setValue(PylonBlock.BREAK, look);
        if (parent != null && parent.getAxis().isHorizontal()) now = now.setValue(PylonBlock.PARENT, parent);
        if (now != getBlockState()) level.setBlock(worldPosition, now, Block.UPDATE_ALL);
    }

    // Broken: the Spell Pylon hears about it (a reroll, or the line cut here)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !level.isClientSide() && main != null && level.getBlockEntity(main) instanceof SpellPylonBlockEntity spellPylon) {
            spellPylon.pylonRemoved(this);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (main != null) output.store("main", BlockPos.CODEC, main);
        if (modifier != null) output.store("modifier", Identifier.CODEC, modifier);
        output.store("tier", ModifierTier.CODEC, tier);
        output.putBoolean("spell_break", spellBreak);
        if (shape != null) output.store("shape", Identifier.CODEC, shape);
        output.putInt("max_nodes", maxNodes);
        output.putInt("nodes", nodes);
        if (card != null) output.store("card", Identifier.CODEC, card);
        output.putInt("levels", levels);
        output.putBoolean("legendary", legendary);
        output.putBoolean("stop", stop);
        output.putBoolean("locked", locked);
        output.putBoolean("powered", powered);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        main = input.read("main", BlockPos.CODEC).orElse(null);
        modifier = input.read("modifier", Identifier.CODEC).orElse(null);
        tier = input.read("tier", ModifierTier.CODEC).orElse(ModifierTier.NORMAL);
        spellBreak = input.getBooleanOr("spell_break", false);
        shape = input.read("shape", Identifier.CODEC).orElse(null);
        maxNodes = input.getIntOr("max_nodes", 0);
        nodes = input.getIntOr("nodes", 0);
        card = input.read("card", Identifier.CODEC).orElse(null);
        levels = input.getIntOr("levels", 0);
        legendary = input.getBooleanOr("legendary", false);
        stop = input.getBooleanOr("stop", false);
        locked = input.getBooleanOr("locked", false);
        powered = input.getBooleanOr("powered", false);
        // On the client: the tints (body, lockout, nodes) come from here, so ask for the block to be drawn again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
