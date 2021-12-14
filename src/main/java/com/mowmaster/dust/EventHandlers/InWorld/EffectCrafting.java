package com.mowmaster.dust.EventHandlers.InWorld;

import com.mowmaster.dust.Configs.DustEffectConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.Tools.LinkingTool;
import com.mowmaster.dust.Items.Tools.LinkingToolBackwards;
import com.mowmaster.dust.Recipes.MobEffectColorRecipe;
import com.mowmaster.dust.Recipes.MobEffectColorRecipeCorrupted;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import com.mowmaster.dust.References.EffectPickerReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Mod.EventBusSubscriber
public class EffectCrafting
{

    @SubscribeEvent()
    public static void DustEffectCrafting(PlayerInteractEvent.RightClickBlock event)
    {
        //Added to keep fake players from canning this every time?
        if(!(event.getPlayer() instanceof FakePlayer))
        {
            Level worldIn = event.getWorld();
            InteractionHand hand = event.getHand();
            BlockState state = worldIn.getBlockState(event.getPos());
            Player player = event.getPlayer();
            BlockPos pos = event.getPos();

            int posX = event.getPos().getX();
            int posY = event.getPos().getY();
            int posZ = event.getPos().getZ();
            int currentColor = -1;

            int potency = 0;

            /*
            int colorOrigin = ColorReference.getColorFromStateInt(blockstateOrigin);
            int colorToMix = ColorReference.getColorFromStateInt(p_152729_.getBlockState(blockpos));
            if(colorOrigin != colorToMix)
            {
                int colorMix = ColorReference.mixColorsInt(colorOrigin,colorToMix);
                BlockState blockstate1 = ColorReference.addColorToBlockState(block.defaultBlockState(),colorMix);
                p_152729_.setBlockAndUpdate(blockpos, blockstate1);
            }*/
            boolean corrupted = false;

            int dustDuration=0;

            if(!worldIn.isClientSide) {
                if ((player.getItemInHand(hand) != null)) {
                    if (player.getItemInHand(hand).getItem() instanceof LinkingTool || player.getItemInHand(hand).getItem() instanceof LinkingToolBackwards) {
                        //List<EntityItem> item = player.level.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1));
                        List<ItemEntity> items = player.level.getEntitiesOfClass(ItemEntity.class, new AABB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));

                        for (ItemEntity item : items) {
                            ItemStack stack = item.getItem();

                            if(stack.getItem().equals(DeferredRegisterItems.COLORED_CRYSTAL_DUST.get()))
                            {
                                dustDuration +=stack.getCount();
                                if(currentColor<=0)currentColor=ColorReference.getColorFromItemStackInt(stack);
                                else
                                {
                                    int colorToMix = ColorReference.getColorFromItemStackInt(stack);
                                    if(currentColor != colorToMix) { currentColor = ColorReference.mixColorsInt(currentColor,colorToMix); }
                                }
                                item.remove(Entity.RemovalReason.DISCARDED);
                            }
                            else if(stack.getItem().equals(Items.FERMENTED_SPIDER_EYE))
                            {
                                corrupted = true;
                                item.remove(Entity.RemovalReason.DISCARDED);
                            }
                        }

                        if(dustDuration > 0)
                        {
                            worldIn.explode(new ItemEntity(worldIn, posX, posY, posZ,new ItemStack(Items.PAPER)),(DamageSource)null,new EntityBasedExplosionDamageCalculator(player), posX + 0.5, posY + 2.0, posZ + 0.25, 0.0F,false, Explosion.BlockInteraction.NONE);
                            if(dustDuration>0)
                            {



                                int finalPotency = (potency>DustEffectConfig.COMMON.effectMaxPotency.get())?(DustEffectConfig.COMMON.effectMaxPotency.get()):(potency)+1;
                                MobEffectInstance effectPlayer = new MobEffectInstance(EffectPickerReference.getEffectForColor(worldIn, corrupted, currentColor),(dustDuration*10)/finalPotency,finalPotency-1,false,false,false,null);
                                /*public MobEffectInstance(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, @Nullable MobEffectInstance hiddenEffect)*/
                                player.addEffect(effectPlayer);
                            }
                        }
                    }
                }
            }
        }
    }



}
