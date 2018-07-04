package com.mowmaster.dust.items.armors.models;

import com.mowmaster.dust.enchantments.EnchantmentQuickPace;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.handlers.EnchantAndEffectHandlers;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ModelCrystalHelmet extends ModelBiped
{

    public ModelRenderer HelmTop;
    public ModelRenderer HelmLeftSide;
    public ModelRenderer HelmRightSide;
    public ModelRenderer HelmBackSide;
    public ModelRenderer HelmFrontSide;
    public ModelRenderer CrystalLeft;
    public ModelRenderer CrystalRight;
    public ModelRenderer CrystalSide1;
    public ModelRenderer CrystalSide1_1;
    public ModelRenderer CrystalSide1_2;
    public ModelRenderer CrystalSide1_3;
    public ModelRenderer CrystalSide1_4;
    public ModelRenderer CrystalSide1_5;
    public ModelRenderer CrystalSide1_6;
    public ModelRenderer CrystalSide1_7;

    public ModelRenderer crystal;
    public ModelRenderer crystal2;

    public ModelRenderer e0;
    public ModelRenderer e1;
    public ModelRenderer e2;
    public ModelRenderer e3;
    public ModelRenderer e4;
    public ModelRenderer e5;
    public ModelRenderer e6;
    public ModelRenderer e7;
    public ModelRenderer e8;
    public ModelRenderer e9;


    public ModelCrystalHelmet(float scale) {
        super(scale, 0, 64, 64);
        textureWidth = 64;
        textureHeight = 64;

        this.e0 = new ModelRenderer(this, 8, 32);
        this.e0.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e0.addBox(-0.5F, -19F, -0.5F, 1, 8, 1, 0.0F);
        this.e1 = new ModelRenderer(this, 12, 32);
        this.e1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e1.addBox(-0.5F, -18F, -1.5F, 1, 6, 1, 0.0F);
        this.e2 = new ModelRenderer(this, 12, 32);
        this.e2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e2.addBox(-0.5F, -18F, 0.5F, 1, 6, 1, 0.0F);
        this.e3 = new ModelRenderer(this, 12, 32);
        this.e3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e3.addBox(0.5F, -18F, -0.5F, 1, 6, 1, 0.0F);
        this.e4 = new ModelRenderer(this, 12, 32);
        this.e4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e4.addBox(-1.5F, -18F, -0.5F, 1, 6, 1, 0.0F);//had to edit the model texture UV and the height of the modeled item to get it to work

        this.e5 = new ModelRenderer(this, 8, 32);
        this.e5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e5.addBox(-0.5F, -19F, -0.5F, 1, 8, 1, 0.0F);
        this.e6 = new ModelRenderer(this, 12, 32);
        this.e6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e6.addBox(-0.5F, -18F, -1.5F, 1, 6, 1, 0.0F);
        this.e7 = new ModelRenderer(this, 12, 32);
        this.e7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e7.addBox(-0.5F, -18F, 0.5F, 1, 6, 1, 0.0F);
        this.e8 = new ModelRenderer(this, 12, 32);
        this.e8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e8.addBox(0.5F, -18F, -0.5F, 1, 6, 1, 0.0F);
        this.e9 = new ModelRenderer(this, 12, 32);
        this.e9.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e9.addBox(-1.5F, -18F, -0.5F, 1, 6, 1, 0.0F);

        this.crystal = new ModelRenderer(this, 0, 32);
        this.crystal.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.crystal.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);

        this.crystal2 = new ModelRenderer(this, 0, 32);
        this.crystal2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.crystal2.addBox(1.0F, 0.0F, 1.0F, 1, 8, 1, 0.0F);


        /*
        this.HelmTop = new ModelRenderer(this, 0, 32);
        this.HelmTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmTop.addBox(-5.0F, -9.0F, -5.0F, 10, 1, 10, 0.0F);
        this.HelmFrontSide = new ModelRenderer(this, 30, 39);
        this.HelmFrontSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmFrontSide.addBox(-4.0F, -8.0F, -5.0F, 8, 2, 1, 0.0F);
        this.HelmLeftSide = new ModelRenderer(this, 40, 32);
        this.HelmLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmLeftSide.addBox(4.0F, -8.0F, -5.0F, 1, 4, 10, 0.0F);
        this.HelmRightSide = new ModelRenderer(this, 0, 43);
        this.HelmRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmRightSide.addBox(-5.0F, -8.0F, -5.0F, 1, 4, 10, 0.0F);
        this.HelmBackSide = new ModelRenderer(this, 30, 32);
        this.HelmBackSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmBackSide.addBox(-4.0F, -8.0F, 4.0F, 8, 6, 1, 0.0F);
        this.CrystalSide1_3 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_3.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.CrystalSide1_3.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_1 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_1.setRotationPoint(-1.0F, 3.0F, 0.0F);
        this.CrystalSide1_1.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1.setRotationPoint(1.0F, 3.0F, 0.0F);
        this.CrystalSide1.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_5 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_5.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.CrystalSide1_5.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_4 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_4.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.CrystalSide1_4.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_6 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_6.setRotationPoint(0.0F, 1.0F, 1.0F);
        this.CrystalSide1_6.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_2 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_2.setRotationPoint(0.0F, 3.0F, 1.0F);
        this.CrystalSide1_2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalSide1_7 = new ModelRenderer(this, 55, 0);
        this.CrystalSide1_7.setRotationPoint(0.0F, 1.0F, -1.0F);
        this.CrystalSide1_7.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1, 0.0F);
        this.CrystalRight = new ModelRenderer(this, 50, 0);
        this.CrystalRight.setRotationPoint(-4.0F, -13.0F, -6.0F);
        this.CrystalRight.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.setRotateAngle(CrystalRight, 0.0F, 0.0F, -0.2617993877991494F);//Remove???
        this.CrystalLeft = new ModelRenderer(this, 50, 0);
        this.CrystalLeft.setRotationPoint(4.0F, -15.0F, -6.0F);
        this.CrystalLeft.addBox(0.0F, 2.0F, 0.0F, 1, 8, 1, 0.0F);
        this.setRotateAngle(CrystalLeft, 0.0F, 0.0F, 0.2617993877991494F);//Remove???
        this.CrystalLeft.addChild(this.CrystalSide1_3);
        this.CrystalLeft.addChild(this.CrystalSide1_1);
        this.CrystalLeft.addChild(this.CrystalSide1);
        this.CrystalRight.addChild(this.CrystalSide1_5);
        this.CrystalRight.addChild(this.CrystalSide1_4);
        this.CrystalRight.addChild(this.CrystalSide1_6);
        this.CrystalLeft.addChild(this.CrystalSide1_2);
        this.CrystalRight.addChild(this.CrystalSide1_7);
        bipedHead.addChild(HelmFrontSide);
        bipedHead.addChild(HelmLeftSide);
        bipedHead.addChild(HelmRightSide);
        bipedHead.addChild(HelmBackSide);
        bipedHead.addChild(HelmTop);
        bipedHead.addChild(CrystalLeft);
        bipedHead.addChild(CrystalRight);
         */
        this.crystal.addChild(e0);
        this.crystal.addChild(e1);
        this.crystal.addChild(e2);
        this.crystal.addChild(e3);
        this.crystal.addChild(e4);

        this.crystal2.addChild(e5);
        this.crystal2.addChild(e6);
        this.crystal2.addChild(e7);
        this.crystal2.addChild(e8);
        this.crystal2.addChild(e9);

        bipedHead.addChild(crystal);
        bipedHead.addChild(crystal2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity,f,f1,f2,f3,f4,f5);
        setRotationAngles(f,f1,f2,f3,f4,f5,entity);
        //this.HelmTop.render(f5);
        //this.HelmRightSide.render(f5);
        //this.HelmLeftSide.render(f5);
        //this.HelmFrontSide.render(f5);
        //this.HelmBackSide.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}