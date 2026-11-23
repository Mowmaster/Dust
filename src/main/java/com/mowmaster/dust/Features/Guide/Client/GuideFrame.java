package com.mowmaster.dust.Features.Guide.Client;

import com.mowmaster.dust.Features.Guide.GuideStep;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

// What a guide scene shows at one moment (GuidePlayback.at): everything the renderer and the screen draw.
//   blocks   each block, lifted (dropping in / lifting away) and sized (shrinking away), with what its block entity
//            holds (data: null for none), the colour its tinted parts are drawn (tint: null to tint it as in the world)
//            and when the scene last set it (since: ticks into the scene; its block entity plays on from then)
//   items    items resting or sliding, and their spin (degrees turned from facing the viewer)
//   texts    text boxes, each pointing at a point (or none), fading in and out (alpha)
//   boxes    highlight outlines
//   clicks   click indicators (progress 0-1 through their time)
//   yaw      how far rotate steps have turned the view so far (degrees)
//   time     ticks into the scene (fractions allowed)
public record GuideFrame(List<BlockDraw> blocks, List<ItemDraw> items, List<TextDraw> texts, List<BoxDraw> boxes,
                         List<ClickDraw> clicks, float yaw, float time)
{
    public record BlockDraw(BlockPos pos, BlockState state, float lift, float size, @Nullable CompoundTag data, @Nullable Integer tint, float since)
    {
        // The same block, moved (dropping in, lifting away)
        public BlockDraw moved(float lift, float size)
        {
            return new BlockDraw(pos, state, lift, size, data, tint, since);
        }
    }

    public record ItemDraw(ItemStack stack, Vec3 at, float spin, float scale) {}
    public record TextDraw(Component text, @Nullable Vec3 point, GuideStep.Side side, float alpha) {}
    public record BoxDraw(AABB box, int color, float alpha) {}
    public record ClickDraw(Vec3 point, boolean right, ItemStack held, boolean sneak, float progress) {}
}
