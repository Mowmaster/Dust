package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.EnumSet;

// Skeletons with a Fire Wand and strays with an Ice Wand (WAND_CHANCE of them, when they first spawn). Instead of
// arrows they cast the wand's Projectile spell at their target, and a player hit by one learns the Projectile shape.
// They drop the wand like any held item (vanilla's 8.5%, more with Looting).
// The wand goes in when the mob joins the world for the first time (EntityJoinLevelEvent, not loaded from disk: by
// then vanilla has given it its bow). Goals aren't saved, so every time a wand-holder joins (after a reload too) it
// gets the casting goal again.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class WandMobs
{
    public static final float WAND_CHANCE = 0.05F;

    private WandMobs() {}

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event)
    {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof AbstractSkeleton skeleton)) return;
        Item wand = event.getEntity() instanceof Stray ? DustItemRegistry.SPELL_WAND_ICE.get()
                : event.getEntity() instanceof Skeleton ? DustItemRegistry.SPELL_WAND_FIRE.get() : null;
        if (wand == null) return;
        // setItemSlot also makes the skeleton drop its bow attack (AbstractSkeleton#reassessWeaponGoal)
        if (!event.loadedFromDisk() && skeleton.getRandom().nextFloat() < WAND_CHANCE) {
            skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(wand));
        }
        if (skeleton.getMainHandItem().getItem() instanceof SpellWandItem) {
            // Priority 3: ahead of the skeleton's own attack goals (4), which it would otherwise walk up and punch with
            skeleton.goalSelector.addGoal(3, new CastWandGoal(skeleton));
        }
    }

    // Keeps its distance like a skeleton with a bow, and casts its wand at its target every CAST_TICKS while it can see
    // it. MOVE and LOOK: no other goal moves it or turns its head meanwhile.
    public static class CastWandGoal extends Goal
    {
        private static final int CAST_TICKS = 50;
        private static final double RANGE = 15.0;
        private static final double SPEED = 1.0;

        private final Mob mob;
        private int cooldown;

        public CastWandGoal(Mob mob)
        {
            this.mob = mob;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        private boolean holdingWand()
        {
            return mob.getMainHandItem().getItem() instanceof SpellWandItem;
        }

        @Override
        public boolean canUse()
        {
            LivingEntity target = mob.getTarget();
            return holdingWand() && target != null && target.isAlive();
        }

        @Override
        public void start()
        {
            cooldown = CAST_TICKS / 2;
        }

        @Override
        public void stop()
        {
            mob.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick()
        {
            return true;
        }

        @Override
        public void tick()
        {
            LivingEntity target = mob.getTarget();
            if (target == null) return;
            boolean sees = mob.getSensing().hasLineOfSight(target);
            double distance = mob.distanceTo(target);
            // Close in until it's in range and can see; then stand and cast
            if (distance > RANGE || !sees) mob.getNavigation().moveTo(target, SPEED);
            else mob.getNavigation().stop();
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--cooldown > 0 || !sees || distance > RANGE) return;
            cooldown = CAST_TICKS;
            if (mob.getMainHandItem().getItem() instanceof SpellWandItem wand) SpellWandItem.castAt(mob, wand.element(), target);
        }
    }
}
