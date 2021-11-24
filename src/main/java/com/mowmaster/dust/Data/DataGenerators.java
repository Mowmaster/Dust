package com.mowmaster.dust.Data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.mowmaster.dust.References.Constants.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void getData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existFileHelper = event.getExistingFileHelper();

        //gen.addProvider(new RecipeProviderGen(gen));
        gen.addProvider(new CraftingRecipeProvider(gen));
    }
}
