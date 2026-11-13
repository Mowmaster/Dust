package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// How a player first finds their magic:
//   1. Burning Alchemical Dust with a Magical Flint and Steel lets out one of its element's three effects, at random
//      (on the player, on the block it burns on, or on the nearest creature within 5 blocks), and costs 1 mana. They
//      note the effect in their research and learn the shape it came by (SpellKnowledge): Self for the self effect,
//      Touch for the others.
//   2. Mana doesn't regenerate yet, so it runs out. The first time burning dust hurts them (no mana left to pay), they
//      feel exhausted and need to sleep.
//   3. Sleeping it off, they dream, and wake awakened: the mana HUD appears and mana regenerates from then on.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Awakening
{
    private Awakening() {}

    // Called by the Magical Flint and Steel after it burned one Alchemical Dust of this element, by the fire at firePos
    public static void burnedDust(ServerPlayer player, BlockPos firePos, DustElement element)
    {
        ServerLevel level = player.level();
        Vec3 centre = Vec3.atCenterOf(firePos);
        List<SpellTarget> targets = new ArrayList<>(List.of(SpellTarget.SELF, SpellTarget.BLOCK));
        // The nearest living thing other than the player, if any are close enough
        LivingEntity creature = level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(firePos).inflate(SpellConfig.NEARBY),
                        entity -> entity != player && entity.isAlive() && entity.position().distanceTo(centre) <= SpellConfig.NEARBY)
                .stream().min(Comparator.comparingDouble(entity -> entity.position().distanceTo(centre))).orElse(null);
        if (creature != null) targets.add(SpellTarget.ENTITY);
        SpellTarget target = targets.get(level.getRandom().nextInt(targets.size()));

        Spell spell = new Spell(element, target == SpellTarget.SELF ? SpellRegistries.SELF.getId() : SpellRegistries.TOUCH.getId(), java.util.Map.of(), List.of());
        SpellStats stats = spell.stats(1);
        SpellCast cast = new SpellCast(level, player, spell, stats, centre, new Vec3(0, -1, 0));
        switch (target) {
            case SELF -> cast.applySelf(player);
            case BLOCK -> cast.applyBlock(firePos.below(), Direction.UP);
            case ENTITY -> cast.applyEntity(creature);
        }

        // It costs 1 mana; without any, it costs health, and the first time that happens they're exhausted
        PlayerMagic magic = PlayerMagicHelper.get(player);
        boolean hurts = magic.mana() <= 0;
        PlayerMagicHelper.spendMana(player, 1);
        if (hurts && !magic.awakened() && !magic.exhausted()) {
            PlayerMagicHelper.set(player, PlayerMagicHelper.get(player).withAwakening(true, false));
            player.sendSystemMessage(Component.translatable("dust.magic.exhausted").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    // Waking from a proper sleep (isSleepingLongEnough: asleep long enough to count, not just lying down) while
    // exhausted: they dream, and wake with their magic
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !player.isSleepingLongEnough()) return;
        PlayerMagic magic = PlayerMagicHelper.get(player);
        if (!magic.exhausted() || magic.awakened()) return;
        awaken(player);
        player.sendSystemMessage(Component.translatable("dust.magic.dream").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
    }

    // Their magic awakens: the HUD shows and mana regenerates (also /unlockMana)
    public static void awaken(ServerPlayer player)
    {
        PlayerMagicHelper.set(player, PlayerMagicHelper.get(player).withAwakening(false, true));
    }
}
