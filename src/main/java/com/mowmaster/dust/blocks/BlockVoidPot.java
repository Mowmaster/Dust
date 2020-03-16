package com.mowmaster.dust.blocks;

import com.mowmaster.dust.dust;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class BlockVoidPot extends Block {

    public BlockVoidPot(Properties builder)
    {
        super(builder);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        Random rand = new Random();
        if(entityIn instanceof ItemEntity || entityIn instanceof ExperienceOrbEntity) {
            entityIn.remove();

            double d0 = (double) pos.getX() + 0.55D - (double) (rand.nextFloat() * 0.1F);
            double d1 = (double) pos.getY() + 1.0D - (double) (rand.nextFloat() * 0.1F);
            double d2 = (double) pos.getZ() + 0.55D - (double) (rand.nextFloat() * 0.1F);
            double d3 = (double) (0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

            worldIn.addParticle(ParticleTypes.WITCH, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.0D, (double) pos.getZ() + 0.5D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
        }
        else if(entityIn instanceof LivingEntity) {
            float damage = 0f;
            int level = 0;
            damage = 1f;
            if(entityIn.fallDistance > 0) {
                damage += entityIn.fallDistance * 1.5f + 2f;
            }
            entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, damage);
            //((LivingEntity) entityIn).addPotionEffect(new PotionEffect(PotionRegistry.POTION_VOIDSTORAGE, 10, level));

            double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d1 = (double)pos.getY() + 1.0D - (double)(rand.nextFloat() * 0.1F);
            double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

            worldIn.addParticle(ParticleTypes.WITCH, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.0D, (double) pos.getZ() + 0.5D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
        }
    }

    @Deprecated
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private static final ResourceLocation RESLOC_VOIDPOT = new ResourceLocation(MODID, "voidpot");

    public static final Block BLOCK_VOIDPOT  = new BlockVoidPot(Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_VOIDPOT);

    public static final Item ITEM_VOIDPOT  = new BlockItem(BLOCK_VOIDPOT , new Item.Properties().group(dust.ITEM_GROUP)) {}.setRegistryName(RESLOC_VOIDPOT);

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_VOIDPOT );
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(BLOCK_VOIDPOT );
    }
}
