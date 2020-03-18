package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.blocks.BlockTrap;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBaseFilter;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;


public class TileTrap extends TileEntity {

    EffectInstance potionEffect = new EffectInstance(Effects.UNLUCK);

    public TileTrap()
    {
        super(TILETRAPTYPE);
    }

    private void update()
    {
        markDirty();
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),1);
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),2);
    }

    public void setTrapEffect(EffectInstance trapEffect)
    {
        potionEffect=trapEffect;
        update();
    }

    public EffectInstance getTrapEffect()
    {
        return potionEffect;
    }


    @Override
    public void read(CompoundNBT tag) {
        potionEffect = new EffectInstance(Effect.get(tag.getInt(Reference.MODID + "Potion")),tag.getInt(Reference.MODID + "Duration"),tag.getInt(Reference.MODID + "Amplifier"));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt(Reference.MODID + "Potion",Effect.getId(potionEffect.getPotion()));
        tag.putInt(Reference.MODID + "Amplifier",potionEffect.getAmplifier());
        tag.putInt(Reference.MODID + "Duration",potionEffect.getDuration());
        return super.write(tag);
    }


    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }

    private static Block[] trapArray = new Block[]{BlockTrap.BLOCKTRAPPLAYER,BlockTrap.BLOCKTRAPMOB};

    private static final ResourceLocation TILETRAP = new ResourceLocation(MODID, "tile/trap");

    public static TileEntityType<TileTrap> TILETRAPTYPE = TileEntityType.Builder.create(TileTrap::new, trapArray).build(null);

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        r.register(TILETRAPTYPE.setRegistryName(TILETRAP));
    }
}