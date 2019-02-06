package com.mowmaster.dust.enums;

import net.minecraft.util.IStringSerializable;


public class CrystalItems
{
    public static enum CrystalTypes implements IStringSerializable
    {
        RED("red",0),
        BLUE("blue",1),
        YELLOW("yellow",2),
        PURPLE("purple",3),
        GREEN("green",4),
        ORANGE("orange",5),
        WHITE("white",6),
        BLACK("black",7),
        CLEAR("clear",8);



        private int ID;
        private String name;
        private CrystalTypes(String name, int ID)
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

    public static enum DustTypes implements IStringSerializable
    {
        RED("red",0),
        BLUE("blue",1),
        YELLOW("yellow",2),
        PURPLE("purple",3),
        GREEN("green",4),
        ORANGE("orange",5),
        WHITE("white",6),
        BLACK("black",7),
        STONE("stone",8),
        IRON("iron",9),
        GOLD("gold",10);



        private int ID;
        private String name;
        private DustTypes(String name, int AID)
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
	
	public static enum BitTypes implements IStringSerializable
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
        private BitTypes(String name, int ID)
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

    public static enum CharcoalTypes implements IStringSerializable
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
        private CharcoalTypes(String name, int ID)
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

    public static enum CountTypes implements IStringSerializable
    {
        ONE("one",0),
        TWO("two",1),
        THREE("three",2),
        FOUR("four",3),
        FIVE("five",4),
        SIX("six",5),
        SEVEN("seven",6),
        EIGHT("eight",7),
        NINE("nine",8),
        TEN("ten",9),
        ELEVEN("eleven",10),
        TWELVE("twelve",11),
        THIRTEEN("thirteen",12),
        FOURTEEN("fourteen",13),
        FIFTEEN("fifteen",14),
        SIXTEEN("sixteen",15);





        private int ID;
        private String name;
        private CountTypes(String name, int ID)
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
