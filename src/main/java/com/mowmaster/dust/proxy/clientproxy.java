package com.mowmaster.dust.proxy;

import com.mowmaster.dust.init.items;

public class clientproxy extends commonproxy{
    @Override
    public void registerRenders(){
        items.registerRenders();
    }
}
