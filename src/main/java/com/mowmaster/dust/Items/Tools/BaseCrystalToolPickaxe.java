package com.mowmaster.dust.Items.Tools;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class BaseCrystalToolPickaxe extends PickaxeItem {
    public BaseCrystalToolPickaxe(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
    }

    public static DustMagic getDustMagicFromStack(ItemStack stackIn)
    {
        return DustMagic.getDustMagicInItemStack(stackIn);
    }

    public static int getColor(ItemStack stackIn)
    {
        return getDustMagicFromStack(stackIn).getDustColor();
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return super.getBarColor(p_150901_);
    }

    @Override
    public int getBarWidth(ItemStack p_150900_) {
        return super.getBarWidth(p_150900_);
    }

    /*
    @Deprecated // Use ItemStack sensitive version.
   public final int getMaxDamage() {
      return this.maxDamage;
   }

   public boolean canBeDepleted() {
      return this.maxDamage > 0;
   }

   public boolean isBarVisible(ItemStack p_150899_) {
      return p_150899_.isDamaged();
   }

   public int getBarWidth(ItemStack p_150900_) {
      return Math.round(13.0F - (float)p_150900_.getDamageValue() * 13.0F / (float)this.maxDamage);
   }

   public int getBarColor(ItemStack p_150901_) {
      float f = Math.max(0.0F, ((float)this.maxDamage - (float)p_150901_.getDamageValue()) / (float)this.maxDamage);
      return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
   }
     */
}
