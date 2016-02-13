package com.mowmaster.dust.item.Tools.Misc;

import com.mowmaster.dust.init.dustCreativeTabs;
import com.mowmaster.dust.item.ItemDust;

/**
 * Created by KingMowmaster on 2/13/2016.
 */
public class toolHammer extends ItemDust {

    public toolHammer ()
    {
        super("toolDustHammer");
        this.setCreativeTab(dustCreativeTabs.dustTool);
        this.setMaxDamage(64);
        maxStackSize = 1;
    }

}
