package com.mowmaster.dust.proxies;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.ItemRenderRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientProxy extends CommonProxy
{
    @Override
    @SideOnly(Side.CLIENT)
    public void PreInit()
    {
        ItemRenderRegistry.ItemRR();
    }

}
