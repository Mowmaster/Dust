package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustUtils.DustReferences;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = DustReferences.MODID)
public class DustItemGen {

    @SubscribeEvent
    public static void gatherClientDate(GatherDataEvent.Client event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();



        generator.addProvider(true, new DustModelProvider(packOutput));


    }
}
