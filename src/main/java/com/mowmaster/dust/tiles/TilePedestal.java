package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;

import static com.mowmaster.dust.references.Reference.MODID;


public class TilePedestal extends TileEntity implements ITickableTileEntity {

    public static TileEntityType<TilePedestal> pedestal_stone = TileEntityType.Builder.create(TilePedestal::new, BlockPedestal.BLOCK_PEDESTAL_STONE).build(null);

    public TilePedestal()
    {
        super(pedestal_stone);
    }

    int i = 0;

    @Override
    public void tick() {
        if(!world.isRemote())
        {
            i++;
            if(i >= 20)
            {
                System.out.println("Hello!");
                i = 0;
            }
        }
    }





    private static final ResourceLocation RESLOC_TILE_PEDESTAL = new ResourceLocation(MODID, "tile/pedestal");

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        r.register(pedestal_stone.setRegistryName(RESLOC_TILE_PEDESTAL));
    }
}
