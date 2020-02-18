package com.mowmaster.dust.blocks.utility;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileDustStructureSpawner;
import com.mowmaster.dust.tiles.TileLootBlock;
import com.mowmaster.dust.world.structures.DustStructureGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.devBlocks;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;
import static com.mowmaster.dust.misc.DustyTab.DUSTCREATIVE;


public class BlockStructureSpawner extends BlockBasic implements ITileEntityProvider
{

    public static Block structure1;
    //public static Block structure2;
    public static Boolean inDev = devBlocks;


    public BlockStructureSpawner(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(9999);
        this.setResistance(9999);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(DUSTCREATIVE);
    }


    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Random rand = new Random();

        if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.akashic) && playerIn.getHeldItemOffhand().isEmpty())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                ((TileDustStructureSpawner) tileentity).generate();
            }
            return true;
        }

        if(playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.akashic) && playerIn.getHeldItemMainhand().isEmpty())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                ((TileDustStructureSpawner) tileentity).setName(playerIn.getHeldItemOffhand().getDisplayName().toString());
                sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"n");
            }
            return true;
        }

        if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.crystalWrench) && playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.akashic))
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                ((TileDustStructureSpawner) tileentity).modifyRotation(1);
                sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"r");
            }
            return true;
        }

        if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.akashic) && playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.crystalWrench))
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                ((TileDustStructureSpawner) tileentity).modifyRotation(-1);
                sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"r");
            }
            return true;
        }

        if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.crystalWrench) && playerIn.getHeldItemOffhand().isEmpty())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                switch (facing) {
                    case UP:
                        ((TileDustStructureSpawner) tileentity).modifyY(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"y");
                        return true;
                    case DOWN:
                        ((TileDustStructureSpawner) tileentity).modifyY(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"y");
                        return true;
                    case EAST:
                        ((TileDustStructureSpawner) tileentity).modifyX(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"x");
                        return true;
                    case WEST:
                        ((TileDustStructureSpawner) tileentity).modifyX(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"x");
                        return true;
                    case SOUTH:
                        ((TileDustStructureSpawner) tileentity).modifyZ(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"z");
                        return true;
                    case NORTH:
                        ((TileDustStructureSpawner) tileentity).modifyZ(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"z");
                        return true;
                }
            }
        }

        if(playerIn.getHeldItemOffhand().getItem().equals(ItemRegistry.crystalWrench) && playerIn.getHeldItemMainhand().isEmpty())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileDustStructureSpawner) {
                switch (facing) {
                    case UP:
                        ((TileDustStructureSpawner) tileentity).modifyY(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"y");
                        return true;
                    case DOWN:
                        ((TileDustStructureSpawner) tileentity).modifyY(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"y");
                        return true;
                    case EAST:
                        ((TileDustStructureSpawner) tileentity).modifyX(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"x");
                        return true;
                    case WEST:
                        ((TileDustStructureSpawner) tileentity).modifyX(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"x");
                        return true;
                    case SOUTH:
                        ((TileDustStructureSpawner) tileentity).modifyZ(-1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"z");
                        return true;
                    case NORTH:
                        ((TileDustStructureSpawner) tileentity).modifyZ(1);
                        sendMessageToPlayer(worldIn,pos,playerIn,tileentity,"z");
                        return true;
                }
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    private void sendMessageToPlayer(World world,BlockPos pos, EntityPlayer playerIn, TileEntity tile, String xyzrn)
    {
        if(world.isRemote)
        {
            if (tile instanceof TileDustStructureSpawner) {
                switch (xyzrn.toLowerCase())
                {
                    case "x":
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE +"X: " + ((TileDustStructureSpawner) tile).x));
                        break;
                    case "y":
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE +"Y: " + ((TileDustStructureSpawner) tile).y));
                        break;
                    case "z":
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE +"Z: " + ((TileDustStructureSpawner) tile).z));
                        break;
                    case "r":
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE +"Rotation: " + ((TileDustStructureSpawner) tile).rot));
                        break;
                    case "n":
                        playerIn.sendMessage(new TextComponentString(TextFormatting.WHITE +"Structure Set To: " + ((TileDustStructureSpawner) tile).structureName));
                        break;

                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileDustStructureSpawner();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDustStructureSpawner();
    }

    public static void Init()
    {
        structure1 = new BlockStructureSpawner("structure1","structure1");
        //structure2 = new BlockStructureSpawner("structure2","structure2");
    }

    public static void Register()
    {
        registerBlock(structure1);
        //registerBlock(structure2);

    }

    public static void RegisterRender()
    {
        registerRender(structure1);
        //registerRender(structure2);

    }


}
