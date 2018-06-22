package com.mowmaster.dust.items.armors.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

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

        public ModelCrystalHelmet(float scale) {
            super(scale, 0, 64, 64);
            //this.textureWidth = 64;
            //this.textureHeight = 64;
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
