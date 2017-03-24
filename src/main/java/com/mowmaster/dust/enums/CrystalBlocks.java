package com.mowmaster.dust.enums;

import net.minecraft.util.IStringSerializable;

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class CrystalBlocks
{
    public static enum CrystalOres implements IStringSerializable
    {

        ORE("ore",0),
        FIFTH("five",1),
        FOURTH("four",2),
        THIRD("three",3),
        SECOND("two",4),
        FIRST("one",5),
        BASE("base",6);




        private int ID;
        private String name;
        private CrystalOres(String name, int ID)
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