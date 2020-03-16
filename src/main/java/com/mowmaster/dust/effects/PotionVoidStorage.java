package com.mowmaster.dust.effects;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.potion.EffectType;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

import static net.minecraft.block.EnderChestBlock.field_220115_d;

public class PotionVoidStorage extends PotionBasePlayerRClickBlock {

    public PotionVoidStorage(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    public void tick(PlayerInteractEvent.RightClickBlock event) {
        World worldIn = event.getWorld();
        BlockPos blockClickedOn = event.getPos();
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.isCrouching()) {
                if (!worldIn.getBlockState(blockClickedOn).getBlock().equals(Blocks.AIR)) {
                    EnderChestInventory inventoryenderchest = player.getInventoryEnderChest();
                    INamedContainerProvider named = new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return null;
                        }
                    };
                    player.openContainer(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
                        return ChestContainer.createGeneric9X3(p_226928_1_, p_226928_2_, inventoryenderchest);
                    }, field_220115_d));
                    player.addStat(Stats.OPEN_ENDERCHEST);
                }

            }
        }
    }

}
