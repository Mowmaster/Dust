package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustFireBlock;
import com.mowmaster.dust.Features.DustComposition.DustResearch;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

// Fire
//   Self     Fire Resistance
//   Block    like the Magical Flint and Steel: a dropped item with dust in it, where the fire would go, burns in its
//            colour (and a player caster notes the colour); otherwise it lights the block: a campfire / candle is lit,
//            or fire is placed against the face hit
//   Entity   Scorch (sets it alight, more for higher potency)
public class FireEffect implements ElementEffect
{
    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, MobEffects.FIRE_RESISTANCE);
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        // Something that can be lit (campfires, candles, furnaces' LIT is driven by burning, so only blocks that are
        // lit by flint and steel: those whose LIT a player can toggle)
        if (state.hasProperty(BlockStateProperties.LIT) && !state.getValue(BlockStateProperties.LIT)
                && (state.is(net.minecraft.tags.BlockTags.CAMPFIRES) || state.is(net.minecraft.tags.BlockTags.CANDLES) || state.is(net.minecraft.tags.BlockTags.CANDLE_CAKES))) {
            level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
            return;
        }
        BlockPos firePos = pos.relative(face);
        // A dropped item with dust in it burns in its colour, as with the Magical Flint and Steel
        for (ItemEntity dropped : level.getEntitiesOfClass(ItemEntity.class, new AABB(firePos))) {
            ItemStack stack = dropped.getItem();
            DustComposition composition = DustCompositions.of(stack);
            if (composition == null) continue;
            DustFireBlock fire = (DustFireBlock) DustBlockRegistry.DUST_FIRE.get();
            if (!level.getBlockState(firePos).canBeReplaced() || !fire.light(level, firePos, composition.color())) return;
            ItemStack burned = stack.copyWithCount(1);
            stack.shrink(1);
            dropped.setItem(stack);
            if (cast.caster() instanceof ServerPlayer player) DustResearch.noteBurned(player, ResearchTabs.entryForItem(burned), composition.color());
            return;
        }
        if (BaseFireBlock.canBePlacedAt(level, firePos, face)) level.setBlockAndUpdate(firePos, BaseFireBlock.getState(level, firePos));
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        // Scorch is instant: duration 1, its strength from the potency
        if (target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(DustEffectsRegistry.EFFECT_SCORCH, 1, cast.stats().amplifier()), cast.caster());
    }
}
