package com.mowmaster.dust.enums;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class CrystalBlocks
{
    public static enum CrystalLeaves implements IStringSerializable
    {

        RED("red",0),
        BLUE("blue",1),
        YELLOW("yellow",2),
        PURPLE("purple",3),
        GREEN("green",4),
        ORANGE("orange",5),
        WHITE("white",6),
        BLACK("black",7);




        private int ID;
        private String name;
        private CrystalLeaves(String name, int ID)
        {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public int getID()
        {
            return ID;
        }

        @Override
        public String toString()
        {
            return getName();
        }

    }

    public static enum CrystalColors implements IStringSerializable
    {

        RED("red",0, 11546150, TextFormatting.DARK_RED),
        BLUE("blue",1, 3949738, TextFormatting.DARK_BLUE),
        YELLOW("yellow",2, 16701501, TextFormatting.YELLOW),
        PURPLE("purple",3, 8991416, TextFormatting.DARK_PURPLE),
        GREEN("green",4, 6192150, TextFormatting.DARK_GREEN),
        ORANGE("orange",5, 16351261, TextFormatting.GOLD),
        WHITE("white",6, 16383998, TextFormatting.WHITE),
        BLACK("black",7, 1908001, TextFormatting.BLACK);




        private int ID;
        private String name;
        private int color;
        private TextFormatting chatColor;
        private CrystalColors(String name, int ID, int colorValueIn, TextFormatting chatColorIn)
        {
            this.ID = ID;
            this.name = name;
            this.color = colorValueIn;
            this.chatColor = chatColorIn;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public int getID()
        {
            return ID;
        }

        @SideOnly(Side.CLIENT)
        public int getColor(){return color;}

        public TextFormatting getChatColor(){return chatColor;}

        @Override
        public String toString()
        {
            return getName();
        }

    }

    public static enum CrystalLoot implements IStringSerializable
    {

        COMMONA("commona",0),
        UNCOMMONA("uncommona",1),
        RAREA("rarea",2),
        LEGENDARYA("legendarya",3),
        EXOTICA("exotica",4),
        COMMON("common",5),
        UNCOMMON("uncommon",6),
        RARE("rare",7),
        LEGENDARY("legendary",8),
        EXOTIC("exotic",9),
        CLUSTER("cluster",10),
        PILLAR("pillar",11),
        SPAWNH("spawnh",12),
        SPAWNP("spawnp",13);

        private int ID;
        private String name;
        private CrystalLoot(String name, int ID)
        {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public int getID()
        {
            return ID;
        }

        @Override
        public String toString()
        {
            return getName();
        }

    }


}