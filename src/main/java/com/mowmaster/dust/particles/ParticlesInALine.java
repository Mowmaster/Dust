package com.mowmaster.dust.particles;

import com.mowmaster.dust.dust;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.Random;

/**
 * Created by KingMowmaster on 10/30/2018.
 */
public class ParticlesInALine
{
    private float startX;
    private float startY;
    private float startZ;

    private float endX;
    private float endY;
    private float endZ;

    private float speed;
    private int color;
    private float scale;
    private World world;

    public ParticlesInALine(World worldin, float startX, float startY, float startZ, float endX, float endY, float endZ, float speed, int color, float scale) {
        this.world = worldin;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
        this.speed = speed;
        this.color = color;
        this.scale = scale;

        sendLine();
    }



        @SideOnly(Side.CLIENT)
        public void sendLine() {

                Vector3f dir = new Vector3f(
                        endX - startX,
                        endY - startY,
                        endZ - startZ);
                if (dir.length() > 0) {
                    int maxAge = (int) (dir.length() / speed);
                    dir.normalize();

                    IBlockState stateIn = world.getBlockState(new BlockPos(startX,startY,startZ));
                    Random rand = new Random();
                    double d0 = (double)startX + 0.55D - (double)(rand.nextFloat() * 0.1F);
                    double d1 = (double)startY + 0.55D - (double)(rand.nextFloat() * 0.1F);
                    double d2 = (double)startZ + 0.55D - (double)(rand.nextFloat() * 0.1F);
                    double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

                    //world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.CRIT,  endX, endY, endZ,dir.x * speed, dir.y * speed, dir.z * speed, new int[0]);


                    dust.proxy.spawnMagicParticle(Minecraft.getMinecraft().world, startX, startY, startZ,dir.x * speed, dir.y * speed, dir.z * speed,color, scale, maxAge, 0F, false, false);
                }

        }

}
