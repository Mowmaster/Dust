/*
//Made with Blockbench, a free, modern block model editor by JannisX11

package net.minecraft.src;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBlockbench extends ModelBase {

    //fields
    ModelRenderer e0;
    ModelRenderer e1;
    ModelRenderer e2;
    ModelRenderer e3;
    ModelRenderer e4;
    ModelRenderer e5;
    ModelRenderer e6;
    ModelRenderer e7;
    ModelRenderer e8;
    ModelRenderer e9;
    ModelRenderer e10;
    ModelRenderer e11;
    ModelRenderer e12;
    ModelRenderer e13;
    ModelRenderer e14;
    ModelRenderer e15;
    ModelRenderer e16;
    ModelRenderer e17;
    ModelRenderer e18;
    ModelRenderer e19;
    ModelRenderer e20;
    ModelRenderer e21;
    ModelRenderer e22;
    ModelRenderer e23;
    ModelRenderer e24;

    public ModelBlockbench() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.e0 = new ModelRenderer(this, 0, 0);
        this.e0.addBox(-10.250000000000002F, -15.000000000000002F, 11.75F, 1, 4, 1, 1.0F);
        this.e1 = new ModelRenderer(this, 0, 0);
        this.e1.addBox(-11.250000000000002F, -14.000000000000002F, 11.75F, 1, 4, 1, 1.0F);
        this.e2 = new ModelRenderer(this, 0, 0);
        this.e2.addBox(-11.250000000000002F, -14.000000000000002F, 10.75F, 1, 3, 1, 1.0F);
        this.e3 = new ModelRenderer(this, 0, 0);
        this.e3.addBox(-11.250000000000002F, -16F, 12.75F, 1, 5, 1, 1.0F);
        this.e4 = new ModelRenderer(this, 0, 0);
        this.e4.addBox(-12.25F, -16F, 11.75F, 1, 5, 1, 1.0F);
        this.e5 = new ModelRenderer(this, 0, 0);
        this.e5.addBox(-3.2500000000000018F, -18.5F, 0F, 1, 4, 1, 1.0F);
        this.e6 = new ModelRenderer(this, 0, 0);
        this.e6.addBox(-4.25F, -18.5F, 0F, 1, 3, 1, 1.0F);
        this.e7 = new ModelRenderer(this, 0, 0);
        this.e7.addBox(-3.2500000000000018F, -18.5F, 1F, 1, 3, 1, 1.0F);
        this.e8 = new ModelRenderer(this, 0, 0);
        this.e8.addBox(-3.2500000000000018F, -18.5F, -0.9999999999999999F, 1, 3, 1, 1.0F);
        this.e9 = new ModelRenderer(this, 0, 0);
        this.e9.addBox(-2.2500000000000018F, -18.5F, 0F, 1, 3, 1, 1.0F);
        this.e10 = new ModelRenderer(this, 0, 0);
        this.e10.addBox(7.949999999999999F, -16.5F, -8.599999999999998F, 1, 4, 1, 1.0F);
        this.e11 = new ModelRenderer(this, 0, 0);
        this.e11.addBox(6.950000000000002F, -16.5F, -8.599999999999998F, 1, 3, 1, 1.0F);
        this.e12 = new ModelRenderer(this, 0, 0);
        this.e12.addBox(7.949999999999999F, -16.5F, -7.6000000000000005F, 1, 3, 1, 1.0F);
        this.e13 = new ModelRenderer(this, 0, 0);
        this.e13.addBox(7.949999999999999F, -15.5F, -9.6F, 1, 2, 1, 1.0F);
        this.e14 = new ModelRenderer(this, 0, 0);
        this.e14.addBox(8.95F, -15.5F, -8.599999999999998F, 1, 2, 1, 1.0F);
        this.e15 = new ModelRenderer(this, 0, 0);
        this.e15.addBox(-12F, -15.500000000000002F, -9.249999999999998F, 1, 4, 1, 1.0F);
        this.e16 = new ModelRenderer(this, 0, 0);
        this.e16.addBox(-13F, -17F, -9.249999999999998F, 1, 4, 1, 1.0F);
        this.e17 = new ModelRenderer(this, 0, 0);
        this.e17.addBox(-12F, -15.500000000000002F, -8.249999999999998F, 1, 3, 1, 1.0F);
        this.e18 = new ModelRenderer(this, 0, 0);
        this.e18.addBox(-12F, -15.500000000000002F, -10.249999999999998F, 1, 3, 1, 1.0F);
        this.e19 = new ModelRenderer(this, 0, 0);
        this.e19.addBox(-11F, -14.500000000000002F, -8.999999999999998F, 1, 2, 1, 1.0F);
        this.e20 = new ModelRenderer(this, 0, 0);
        this.e20.addBox(7.4999999999999964F, -15.499999999999998F, 12.25F, 1, 4, 1, 1.0F);
        this.e21 = new ModelRenderer(this, 0, 0);
        this.e21.addBox(6.499999999999999F, -16.749999999999996F, 12.25F, 1, 4, 1, 1.0F);
        this.e22 = new ModelRenderer(this, 0, 0);
        this.e22.addBox(7.4999999999999964F, -18.749999999999996F, 13.25F, 1, 6, 1, 1.0F);
        this.e23 = new ModelRenderer(this, 0, 0);
        this.e23.addBox(7.4999999999999964F, -14.499999999999998F, 11.25F, 1, 2, 1, 1.0F);
        this.e24 = new ModelRenderer(this, 0, 0);
        this.e24.addBox(8.499999999999996F, -15.749999999999998F, 12.25F, 1, 3, 1, 1.0F);

    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.e0.render(f5);
        this.e1.render(f5);
        this.e2.render(f5);
        this.e3.render(f5);
        this.e4.render(f5);
        this.e5.render(f5);
        this.e6.render(f5);
        this.e7.render(f5);
        this.e8.render(f5);
        this.e9.render(f5);
        this.e10.render(f5);
        this.e11.render(f5);
        this.e12.render(f5);
        this.e13.render(f5);
        this.e14.render(f5);
        this.e15.render(f5);
        this.e16.render(f5);
        this.e17.render(f5);
        this.e18.render(f5);
        this.e19.render(f5);
        this.e20.render(f5);
        this.e21.render(f5);
        this.e22.render(f5);
        this.e23.render(f5);
        this.e24.render(f5);

    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
 */