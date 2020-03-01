package com.mowmaster.dust.entities;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class EntityDustRegistry
{
    public static int modEntityIndex = 0;


    public static final EntityEntry dustTippedArrow = EntityEntryBuilder.create()
            .entity(EntityDustTippedArrow.class)
            .id(new ResourceLocation(Reference.MODID, ItemRegistry.dustTippedArrow.getUnlocalizedName()), modEntityIndex++)
            .name(ItemRegistry.dustTippedArrow.getUnlocalizedName())
            .tracker(64, 5, true)
            .build();

    public static final EntityEntry[] ENTITIES = {
            dustTippedArrow };

    public static void entities()
    {



    }



    public static void register()
    {

    }

    public static void registerRender()
    {
        registerRenderArrow();
    }

    private static void registerRenderArrow(/*Item item*/)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityDustTippedArrow.class, new RenderTippedArrow(null));
        //Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        //System.out.println(item.getRegistryName());
    }

}
