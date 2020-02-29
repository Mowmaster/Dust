package com.mowmaster.dust.blocks.machines;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockVoidPot extends BlockBasic
{
    public static Block voidpot;
    private static AxisAlignedBB pot1 = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.99D, 0.875D);

    public BlockVoidPot(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
        this.setTickRandomly(true);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return pot1;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /*
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);


        worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
        //Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleRosePetals(worldIn,d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, 1,1,1));

    }
     */

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        Random rand = new Random();
        if(entityIn instanceof EntityItem || entityIn instanceof EntityXPOrb)
        {
            entityIn.setDead();

            double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d1 = (double)pos.getY() + 1.0D - (double)(rand.nextFloat() * 0.1F);
            double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);


            worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
        }
        else if(entityIn instanceof EntityLivingBase) {
            float damage = 0f;
            int level = 0;
            damage = 1f;
            if(entityIn.fallDistance > 0) {
                damage += entityIn.fallDistance * 1.5f + 2f;
            }
            entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, damage);
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(PotionRegistry.POTION_VOIDSTORAGE, 10, level));

            double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d1 = (double)pos.getY() + 1.0D - (double)(rand.nextFloat() * 0.1F);
            double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);


            worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
        }
    }
/*
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("When looking down inside of the pot it seems to have no bottom, but surely it does! I seem to be able to drop an endless amount of objects into the vessel, and it never fills up! Unfortunately I haven't been able to get any of those back out though...");
        tooltip.add("- Player753");
    }
*/

    public static void Init()
    {
        voidpot = new BlockVoidPot("voidpot","voidpot");
    }

    public static void Register()
    {
        registerBlock(voidpot);
    }

    public static void RegisterRender()
    {
        registerRender(voidpot);
    }
}
