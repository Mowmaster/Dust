package com.mowmaster.dust.Features.CrystalTools.Item;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.Optional;

// An axe + shovel in one: mines everything in #dust:mineable/mattock, strips logs like an axe and makes paths /
// puts out campfires like a shovel.
//
// 26.3: AxeItem, ShovelItem and HoeItem are gone. Stripping, pathing and tilling are now "block transformers": data
// (data/minecraft/block_transformer/axe.json etc.) that an item points at with the BLOCK_TRANSFORMER component.
// An item can only hold one transformer, so the mattock looks up vanilla's axe and shovel transformers itself and
// tries them in order. Using vanilla's entries means datapacks (and NeoForge data maps) that add strippable or
// flattenable blocks work with the mattock too.
public class CrystalToolMattock extends Item {

    // Tried in this order; the first one that changes the block wins (same order as the old code: strip, then path)
    private static final List<ResourceKey<BlockTransformer>> TRANSFORMERS = List.of(BlockTransformers.AXE, BlockTransformers.SHOVEL);

    public CrystalToolMattock(ToolMaterial toolMaterial, float attackDamageBaseline, float attackSpeedBaseline, Properties properties) {
        super(properties.tool(toolMaterial, DustTags.Blocks.MATTOCK_MINEABLE, attackDamageBaseline, attackSpeedBaseline, 1f));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        HolderLookup.RegistryLookup<BlockTransformer> lookup = context.getLevel().registryAccess().lookupOrThrow(Registries.BLOCK_TRANSFORMER);
        for (ResourceKey<BlockTransformer> key : TRANSFORMERS) {
            Optional<Holder.Reference<BlockTransformer>> transformer = lookup.get(key);
            if (transformer.isPresent()) {
                // transformBlock handles everything vanilla's tools do: the offhand-shield check, sounds, particles,
                // durability, advancements and game events. It returns PASS when it has nothing to do with the block.
                InteractionResult result = transformer.get().value().transformBlock(context, transformer.get());
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
        }
        return super.useOn(context);
    }
}
