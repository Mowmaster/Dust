package com.mowmaster.dust.proxy;

import com.mowmaster.dust.init.ModItems;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by KingMowmaster on 1/8/2016.
 */
public class ClientRenderProxy extends RenderProxy {

    private void registerItemModel(Item item){
        final String resourceName = item.getUnlocalizedName().substring(5);
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(resourceName, "inventory")
        );
    }

    private void registerItemRenderers()
    {

        registerItemModel(ModItems.crystalred);

    }

}
