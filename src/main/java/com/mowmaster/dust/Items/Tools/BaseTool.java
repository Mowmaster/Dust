package com.mowmaster.dust.Items.Tools;


import com.mowmaster.dust.Block.Pedestal.BasePedestalBlock;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.mowmaster.dust.Block.Pedestal.BasePedestalBlock.FACING;

public class BaseTool extends Item
{

    public BaseTool(Properties p_41383_) {
        super(p_41383_.stacksTo(1));
    }



    public void spawnParticleAroundPedestalBase(Level world, int tick, BlockPos pos, int r, int g, int b)
    {
        double dx = (double)pos.getX();
        double dy = (double)pos.getY();
        double dz = (double)pos.getZ();

        BlockState state = world.getBlockState(pos);
        Direction enumfacing = Direction.UP;
        if(state.getBlock() instanceof BasePedestalBlock)
        {
            enumfacing = state.getValue(FACING);
        }


        switch (enumfacing)
        {
            case UP:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.75D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.75D,r,g,b));
                return;
            case DOWN:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.75D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.75D,r,g,b));
                return;
            case NORTH:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.25D,dz+ 0.5D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.75D,dz+ 0.5D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.25D,dz+ 0.5D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.75D,dz+ 0.5D,r,g,b));
                return;
            case SOUTH:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.25D,dz+ 0.5D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.75D,dz+ 0.5D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.25D,dz+ 0.5D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.75D,dz+ 0.5D,r,g,b));
                return;
            case EAST:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.25D,dz+ 0.25D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.25D,dz+ 0.75D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.75D,dz+ 0.25D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.75D,dz+ 0.75D,r,g,b));
                return;
            case WEST:
                if (tick%20 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.25D,dz+ 0.25D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.25D,dz+ 0.75D,r,g,b));
                if (tick%15 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.75D,dz+ 0.25D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.5D,dy+0.75D,dz+ 0.75D,r,g,b));
                return;
            default:
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%35 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.25D,dy+0.5D,dz+ 0.75D,r,g,b));
                if (tick%25 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.25D,r,g,b));
                if (tick%30 == 0) DustPacketHandler.sendToNearby(world,pos,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,dx+ 0.75D,dy+0.5D,dz+ 0.75D,r,g,b));
                return;
        }
    }
}
