package com.mowmaster.dust.proxy;

import com.mowmaster.dust.item.ItemRenderRegistry;


public class ClientProxy extends CommonProxy {

    public void registerPostObjects(){

    }

    public void registerRenders()
    {
        ItemRenderRegistry.registerItemRenders();
    }

}
