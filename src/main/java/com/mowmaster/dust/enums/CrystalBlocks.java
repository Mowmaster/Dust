package com.mowmaster.dust.enums;

import net.minecraft.util.IStringSerializable;

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
        private CrystalColors(String name, int ID)
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