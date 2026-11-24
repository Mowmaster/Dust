package com.mowmaster.dust.Features.Trees.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Trees.ElementalChestBlock;
import com.mowmaster.dust.Features.Trees.ElementalChestBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.Nullable;

// Draws an elemental wood chest as vanilla does, with its wood's texture from the chest atlas:
// textures/entity/chest/<wood>.png, and <wood>_left / <wood>_right for the two halves of a double chest
public class ElementalChestRenderer extends ChestRenderer<ElementalChestBlockEntity>
{
    public ElementalChestRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    protected @Nullable SpriteId getCustomSprite(ElementalChestBlockEntity blockEntity, ChestRenderState renderState)
    {
        if (!(blockEntity.getBlockState().getBlock() instanceof ElementalChestBlock chest)) return null;
        String suffix = renderState.type == ChestType.LEFT ? "_left" : renderState.type == ChestType.RIGHT ? "_right" : "";
        return Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(DustReferences.MODID, chest.wood().id + suffix));
    }
}
