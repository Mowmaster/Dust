package com.mowmaster.dust.items;

import com.mowmaster.dust.blocks.machines.BlockCrystalFurnace;
import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.blocks.machines.BlockVoidPot;
import com.mowmaster.dust.blocks.utility.BlockPath;
import com.mowmaster.dust.blocks.utility.BlockSpike;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.research.GuiWikiNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemWikiScroll extends Item
{
    private final ResourceLocation guiUtils = new ResourceLocation(Reference.MODID, "textures/guis/guiutils.png");
    String title = "";
    String contents = "";
    String author = "";
    ItemStack itemDisplay = ItemStack.EMPTY;

    public ItemWikiScroll(String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unlocName));
        this.setHasSubtypes(true);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for(int i = 0; i <CrystalItems.CountTypes.values().length; i++)
        {
            if(stack.getItemDamage() == i)
            {
                return this.getUnlocalizedName() + "." + CrystalItems.CountTypes.values()[i].getName();
            }
            else {
                continue;
            }
        }
        return this.getUnlocalizedName() + "." + CrystalItems.CountTypes.ONE.getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){

        if(world.isRemote)
        {
            if(player.getHeldItemMainhand().getItem().equals(ItemRegistry.wikiscroll))
            {


                if(player.isSneaking())
                {
                    if(player.getHeldItem(hand).getMetadata()>=15)
                    {
                        player.getHeldItem(hand).setItemDamage(0);
                    }
                    else
                    {
                        player.getHeldItem(hand).setItemDamage(player.getHeldItem(hand).getMetadata()+1);
                    }
                }
                else
                {
                    switch (player.getHeldItemMainhand().getMetadata())
                    {
                        case 0:
                            title = "Dear Player,";
                            contents = "Firstly, This mod has documentation online at:      https://github.com/Mowmaster/Dust/wiki               " +
                                    "Lastly, Shift-Right Clicking this item will change 'pages'                         ";
                            /*+
                            " Lastly, If you want to learn about the mod in game, craft this page into the 'Travelers Guide'. To add an entry, Shift-R-Click the guide on blocks, or" +
                                    " with items in the off-hand (From Dust) to learn more."*/
                            author = "Enjoy! ~Mow";
                            itemDisplay = new ItemStack(ItemRegistry.scrollD);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,205,2.0f,2.0f));
                            break;
                        case 1:
                            title = "World Generation";
                            contents = "The Ore Spawns rarely in small veins close to bedrock, It does spawn more frequently in more magical biomes though." +
                                    "There exist a few structures to go out and explore, these contain some nice loot and spawners you cant get normally." +
                                    "There are also Special trees, It is recommended to find all 8 as early as possible.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.dust);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,210,2.0f,2.0f));
                            break;
                        case 2:
                            title = "Spell Crafting";
                            contents = "Toss Dust on the ground, light a fire with flint and steel nearby, if you have enough dust on the ground itll produce an effect." +
                                    "Adding paper, pressure plates, arrows, and coins can transfer the effects to these other mediums for other uses.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.spellPaper);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,210,2.0f,2.0f));
                            break;
                        case 3:
                            title = "Batch Spell Crafting";
                            contents = "Same as before, but use more paper, arrows, or coins. The effect duration and potency will be split between each material added." +
                                    "A Warning though, if the effect duration falls under 5 seconds per extra material, the whole batch could be ruined.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.effectUpgrade);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 4:
                            title = "Crystal Clusters";
                            contents = "By placing mined crystals on the ground you can make a very stable cluster of crystals." +
                                    " Shift+R.Clicking a crystal will start a cluster on the clicked surface. Add to it by R.Clicking it with more crystals" +
                                    " Shift+R.Clicking will remove crystals from a cluster, until none are left." +
                                    " Range of Effect increases with cluster size, color mixing is the same as for spell crafting." +
                                    " Adding glowstone block makes it shine, and a redstone signal will stop all effects.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.crystal,1,1);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,145,190,2.0f,2.0f));
                            break;
                        case 5:
                            title = "Crystal Machines";
                            contents = "Machines can use crystal energy to augment the processing.                  " +
                                    "Red: increase to fuel value " +
                                    "Blue: chance to double output                       " +
                                    "Yellow: speed increase       " +
                                    "Purple: [WIP]                    " +
                                    "Green: chance to double food output                     " +
                                    "Orange: chance to not use fuel                       ";
                            author = "";
                            itemDisplay = new ItemStack(BlockCrystalFurnace.crystalfurnace);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 6:
                            title = "Item Pedestals";
                            contents = "Item Pedestals can be used to Display items, Or transport Items when linked to another Pedestal." +
                                    " To link Pedestals together, Shift+R.Click the pedestal receiving items with a Crystal Wrench, then R.Click on the pedestal sending items." +
                                    " You can unlink pedestals in the same manner. Shift+R.Clicking the ground will clear a saved location." +
                                    " Stone Type Pedestals link to any Type, Color Types can only link to the same color or the Stone Type";
                            author = "";
                            itemDisplay = new ItemStack(BlockPedestal.pedestalstone);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,205,2.0f,2.0f));
                            break;
                        case 7:
                            title = "Pedestal Filters";
                            contents = "There are 6 filter upgrades for pedestals: Exact, Blacklist Exact, Fuzzy, Blacklist Fuzzy, Mod, and Blacklist Mod." +
                                    " By R.Clicking a filter upgrade on a pedestal it will only accept items that match those in the connected INVENTORY, and can send the items on afterwards." +
                                    " Example: If the pedestal was placed on the ground the INVENTORY needs to be underneath.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.filterUpgrade);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 8:
                            title = "Pedestal Crafting";
                            contents = "Upgrades for 1x1, 2x2, and 3x3 recipes." +
                                    " Like for filters, you need a connected INVENTORY." +
                                    " The crafter checks the INVENTORY in slot order," +
                                    " in an INVENTORY or 2x2/3x3 Crafting grid, slots are numbered from left to right, top to bottom. " +
                                    "for 1x1 each slot is a recipe, for 2x2 every 4 slots, and for 3x3 every 9 slots." +
                                    " The crafter will leave 1 item of a stack in the INVENTORY at all times unless its a tool or Bucket item." +
                                    " Crafting Placeholders can be used to fill blank spots in the INVENTORY. (optional)";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.crafter9Upgrade);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,150,210,2.0f,2.0f));
                            break;
                        case 9:
                            title = "Pedestal Upgrades";
                            contents = "There is about 34 in total, craft them up and check advancements for more detailed info on what they do, or check the wiki before crafting.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.placerUpgrade);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,140,210,2.0f,2.0f));
                            break;
                        case 10:
                            title = "Crystal Armor / Tools";
                            contents = "Made using Crystals, Is a bit more durable then diamond, and due to its magical nature, they are a bit more willing to accept enchantments.";
                            author = "";
                            itemDisplay = new ItemStack(ItemArmorAndToolsRegistry.crystalChestplate);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 11:
                            title = "Utility Blocks";
                            contents = "Enchanted Farmland: Crops can be planted on it, they will grow faster and do not require water nearby." +
                                    " Sinister Soil: Infused with the properties of evil, this block will cause mobs to spawn in low light conditions." +
                                    " Path Blocks: By combining colors of stone together and further infusion dust into it you can make blocks that decrease the friction between your feet and the ground as you run.";
                            author = "";
                            itemDisplay = new ItemStack(BlockPath.path4);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,205,2.0f,2.0f));
                            break;
                        case 12:
                            title = "Crystal Spikes";
                            contents = "Using black crystals and dust you have been able to fabricate a placeable spike that slows and damages entities. The more you place in one area the more deadly they become.";
                            author = "";
                            itemDisplay = new ItemStack(BlockSpike.spike5);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 13:
                            title = "Enchantments";
                            contents = "AOE Miner, Flight[WIP], Smelter[WIP], Step Up, Range / Operation Speed (For Pedestal Upgrades)";
                            author = "";
                            itemDisplay = new ItemStack(Items.ENCHANTED_BOOK);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 14:
                            title = "Spell Effects";
                            contents = "Drowning, Envigoration[WIP], Flight, Grower, Harvester, Magnetism , Petrified, Planter[Vanilla, Pams HC, & IE crops], Quickness, Slowfall, Step Assist, Tiller, Water Quickness.";
                            author = "";
                            itemDisplay = new ItemStack(ItemRegistry.finnisher);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        case 15:
                            title = "Pot of the Abyss";
                            contents = "When looking down inside of the pot it seems to have no bottom, but surely it does! I seem to be able to drop an endless amount of objects into the vessel," +
                                    " and it never fills up! Unfortunately I haven't been able to get any of those back out though...";
                            author = "- Player753";
                            itemDisplay = new ItemStack(BlockVoidPot.voidpot);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x61008E,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                            break;
                        default:
                            title = "Hello World";
                            contents = "Hello World???";
                            author = "- Mowmaster";
                            itemDisplay = new ItemStack(ItemRegistry.finnisher);
                            Minecraft.getMinecraft().displayGuiScreen(new GuiWikiNotes(title,0x000000,guiUtils,35,1,1,126,20,contents,0x000000,60, author,0x0000ff,itemDisplay,75,200,2.0f,2.0f));
                    }
                }
            }
        }

        return super.onItemRightClick(world,player,hand);
    }

    private String getTitleText(int meta)
    {
        String displayText;
        switch (meta)
        {
            case 0:
                displayText = "A Message From The Dev";
                break;
            case 1:
                displayText = "Spell and Effect System";
                break;
            case 2:
                displayText = "Traps, Tipped Arrows, Spell Scrolls, and Effect Upgrades";
                break;
            case 3:
                displayText = "Spell Batch Crafting";
                break;
            case 4:
                displayText = "Crystal Clusters";
                break;
            case 5:
                displayText = "Crystal Machine Aguments";
                break;
            case 6:
                displayText = "Pedestals and Linking";
                break;
            case 7:
                displayText = "Pedestal Filtering Upgrades";
                break;
            case 8:
                displayText = "Pedestal Crafting Upgrades";
                break;
            case 9:
                displayText = "Pedestal Upgrades";
                break;
            case 10:
                displayText = "Crystal Armor";
                break;
            case 11:
                displayText = "Utility Blocks";
                break;
            case 12:
                displayText = "Crystal Spikes";
                break;
            case 13:
                displayText = "Enchantments";
                break;
            case 14:
                displayText = "Effects";
                break;
            case 15:
                displayText = "Intro to Void Magic";
                break;
            default:
                displayText = "Getting Started";
        }

        return displayText;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.GOLD + getTitleText(stack.getMetadata()));
        tooltip.add("Shift + R-Click in world to change page");
    }

    public static void bakeItem()
    {
        ModelBakery.registerItemVariants(ItemRegistry.wikiscroll,
                new ResourceLocation(Reference.MODID,"wikiscroll_one"),
                new ResourceLocation(Reference.MODID,"wikiscroll_two"),
                new ResourceLocation(Reference.MODID,"wikiscroll_three"),
                new ResourceLocation(Reference.MODID,"wikiscroll_four"),
                new ResourceLocation(Reference.MODID,"wikiscroll_five"),
                new ResourceLocation(Reference.MODID,"wikiscroll_six"),
                new ResourceLocation(Reference.MODID,"wikiscroll_seven"),
                new ResourceLocation(Reference.MODID,"wikiscroll_eight"),
                new ResourceLocation(Reference.MODID,"wikiscroll_nine"),
                new ResourceLocation(Reference.MODID,"wikiscroll_ten"),
                new ResourceLocation(Reference.MODID,"wikiscroll_eleven"),
                new ResourceLocation(Reference.MODID,"wikiscroll_twelve"),
                new ResourceLocation(Reference.MODID,"wikiscroll_thirteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_fourteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_fifteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_sixteen")
        );
    }
}
