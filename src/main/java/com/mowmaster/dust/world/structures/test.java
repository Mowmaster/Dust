package com.mowmaster.dust.world.structures;

import com.google.gson.JsonObject;
import com.mowmaster.dust.references.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;


public class test
{
    JsonObject object = new JsonObject();

    public test(FMLPreInitializationEvent event)
    {


        File fileslister = new File(event.getModConfigurationDirectory() + "/" + Reference.MODID+ "/" + "structures"+"/"+"small");
        File[] listOfiles = fileslister.listFiles();
        System.out.println("JHLHLKJHKLJHKIUJKHVMJHGLKJBKJHGLIHNBKJBGLIHKLJHBLIUH:LHNO:HO:H:OHHU:OHLIUYO:HLIUGUIFTYUFRO*IUTGIUYUITUKYFRUYGYFUTUIYGKYFUOGUKYGIUGUIOYGUIYUIGFUI");
        System.out.println(listOfiles[0].getName());
        System.out.println(listOfiles[1].getName());
        System.out.println(listOfiles[2].getName());
        //files found in structure folders, will be randomized and called when needing to generate.
    }
}
