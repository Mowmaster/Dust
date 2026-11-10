package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.jspecify.annotations.Nullable;

// Living things give up their dust when they end: one element orb for each element they're made of (an orb worth 1;
// an element listed twice gives two orbs).
//   Mobs     killed by a player: what the mob is made of (the dust:elements entity data map)
//   Crops    a fully grown crop broken by a player: what its produce is made of (the item data map)
// Only for players, like experience, so mob farms and auto-harvesters don't make dust on their own.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DustOrbDrops
{
    private DustOrbDrops() {}

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event)
    {
        LivingEntity mob = event.getEntity();
        // getEntity() on a damage source is who's responsible (the player who shot the arrow)
        if (!(mob.level() instanceof ServerLevel level) || mob instanceof Player || !(event.getSource().getEntity() instanceof Player)) return;
        // builtInRegistryHolder(): the entity type's registry holder, which data maps are attached to
        DustComposition composition = mob.getType().builtInRegistryHolder().getData(DustDataMaps.ENTITY_ELEMENTS);
        if (composition != null) spawnOrbs(level, composition, mob.position().add(0, mob.getBbHeight() / 2, 0));
    }

    @SubscribeEvent
    public static void onCropHarvested(BlockDropsEvent event)
    {
        if (!(event.getBreaker() instanceof Player) || !isGrown(event.getState())) return;
        DustComposition composition = produceOf(event);
        if (composition != null) spawnOrbs(event.getLevel(), composition, Vec3.atCenterOf(event.getPos()));
    }

    // Fully grown: wheat, carrots, potatoes, beetroots, torchflowers (CropBlock), nether wart, cocoa
    private static boolean isGrown(BlockState state)
    {
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) return crop.isMaxAge(state);
        if (block instanceof NetherWartBlock) return state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE;
        if (block instanceof CocoaBlock) return state.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE;
        return false;
    }

    // What the harvest is made of: the first drop that isn't the crop's own seed (wheat, not wheat seeds), or the
    // crop's own item when that's all it drops (carrots and potatoes are their own seeds)
    private static @Nullable DustComposition produceOf(BlockDropsEvent event)
    {
        Item seed = event.getState().getBlock().asItem();
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.getItem() != seed && DustCompositions.of(stack) != null) return DustCompositions.of(stack);
        }
        return DustCompositions.of(new ItemStack(seed));
    }

    private static void spawnOrbs(ServerLevel level, DustComposition composition, Vec3 at)
    {
        for (DustElement element : composition.elements()) ElementOrbEntity.spawn(level, element, 1, at);
    }
}
