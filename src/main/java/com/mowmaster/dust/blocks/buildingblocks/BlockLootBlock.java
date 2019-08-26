package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.blocks.item.ItemBlockOre;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileLootBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTCREATIVE;


public class BlockLootBlock extends BlockBasic implements ITileEntityProvider, IMetaBlockName
{
    public static final PropertyEnum TYPE = PropertyEnum.create("lootblock", CrystalBlocks.CrystalLoot.class);
    public static Block lootblock;

    public BlockLootBlock(String unloc)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, CrystalBlocks.CrystalLoot.COMMON));
        this.setHardness(50);
        this.setResistance(50);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(DUSTCREATIVE);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{TYPE});
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        int blockmeta = placer.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
        return getStateFromMeta(blockmeta);
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalLoot colors = (CrystalBlocks.CrystalLoot) state.getValue(TYPE);
        return colors.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE,CrystalBlocks.CrystalLoot.values()[meta]);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < CrystalBlocks.CrystalLoot.values().length; i++)
        {
            items.add(new ItemStack(this,1,i));
        }
    }

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalLoot.values()[stack.getItemDamage()].getName();
    }


    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileLootBlock();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLootBlock();
    }

    private String getTitleText(int meta)
    {
        String displayText;
        switch (meta)
        {
            case 0:
                displayText = "Common (Air)";
                break;
            case 1:
                displayText = "Uncommon (Air)";
                break;
            case 2:
                displayText = "Rare (Air)";
                break;
            case 3:
                displayText = "Legendary (Air)";
                break;
            case 4:
                displayText = "Exotic (Air)";
                break;
            case 5:
                displayText = "Common";
                break;
            case 6:
                displayText = "Uncommon";
                break;
            case 7:
                displayText = "Rare";
                break;
            case 8:
                displayText = "Legendary";
                break;
            case 9:
                displayText = "Exotic";
                break;
            case 10:
                displayText = "Crystal Cluster";
                break;
            case 11:
                displayText = "Pillar Loot";
                break;
            case 12:
                displayText = "Spawner Hostile";
                break;
            case 13:
                displayText = "Spawner Passive";
                break;
            default:
                displayText = "Loot Block";
        }

        return displayText;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.GOLD + getTitleText(stack.getMetadata()));
        tooltip.add("Shift + R-Click in world to change page");
    }

    public static void Init()
    {
        lootblock = new BlockLootBlock("blockloot");
    }

    public static void Register()
    {
        registerBlock(lootblock, new ItemBlockOre(lootblock));
    }

    public static void RegisterRender()
    {
        for (int i = 0; i < CrystalBlocks.CrystalLoot.values().length; i++)
        {
            registerRender(lootblock,i,"blockloot_" + CrystalBlocks.CrystalLoot.values()[i].getName());
        }
    }
    public static void bakeBlock()
    {
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockLootBlock.lootblock),
                new ResourceLocation(Reference.MODID, "blockloot_commona"),
                new ResourceLocation(Reference.MODID, "blockloot_uncommona"),
                new ResourceLocation(Reference.MODID, "blockloot_rarea"),
                new ResourceLocation(Reference.MODID, "blockloot_legendarya"),
                new ResourceLocation(Reference.MODID, "blockloot_exotica"),
                new ResourceLocation(Reference.MODID, "blockloot_common"),
                new ResourceLocation(Reference.MODID, "blockloot_uncommon"),
                new ResourceLocation(Reference.MODID, "blockloot_rare"),
                new ResourceLocation(Reference.MODID, "blockloot_legendary"),
                new ResourceLocation(Reference.MODID, "blockloot_exotic"),
                new ResourceLocation(Reference.MODID, "blockloot_cluster"),
                new ResourceLocation(Reference.MODID, "blockloot_pillar"),
                new ResourceLocation(Reference.MODID, "blockloot_spawnh"),
                new ResourceLocation(Reference.MODID, "blockloot_spawnp")
        );
    }

}
