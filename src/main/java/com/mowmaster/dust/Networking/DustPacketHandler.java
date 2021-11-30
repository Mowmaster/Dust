package com.mowmaster.dust.Networking;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import static com.mowmaster.dust.References.Constants.MODID;

//For learning and testing:
//https://github.com/baileyholl/Ars-Nouveau/blob/0cdb8fbb483ca0f945de26c633955cfb1c05c925/src/main/java/com/hollingsworth/arsnouveau/common/network/Networking.java#L87
public class DustPacketHandler {
    public static SimpleChannel INSTANCE;

    private static int ID = 0;
    public static int nextID(){return ID++;}
    public static void registerMessages(){
        //System.out.println("Registering packets!!");
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "network"), () -> "1.0", s->true, s->true);

        INSTANCE.registerMessage(nextID(),
                DustPacketParticles.class,
                DustPacketParticles::encode,
                DustPacketParticles::decode,
                DustPacketParticles.Handler::handle);
    }

    public static void sendToNearby(Level world, BlockPos pos, Object toSend){
        if (world instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) world;
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Level world, Entity e, Object toSend) {
        sendToNearby(world, e.getOnPos(), toSend);
    }
}
