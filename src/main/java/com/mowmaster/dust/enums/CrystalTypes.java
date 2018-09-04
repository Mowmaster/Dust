package com.mowmaster.dust.enums;

import net.minecraft.util.IStringSerializable;

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class CrystalTypes
{
    public static enum EffectTypes implements IStringSerializable
    {

        CRYSTAL("crystal",0),
        DUST("dust",1);




        private int ID;
        private String name;
        private EffectTypes(String name, int ID)
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