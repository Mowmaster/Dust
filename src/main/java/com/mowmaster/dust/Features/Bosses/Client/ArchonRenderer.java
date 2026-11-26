package com.mowmaster.dust.Features.Bosses.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.ArchonEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

import java.util.EnumMap;
import java.util.Map;

// Draws an Archon: a humanoid in its own skin (textures/entity/archon/<set>.png) wearing its set (the armour layer,
// which draws the set's 3D models: Features/Armor/Client), with glowing eyes in its element's colour (ArchonGlowLayer).
// Gathering a spell, it holds both arms out towards its target (vanilla's bow pose, which raises both); the 3D armour
// follows, since its models pose from the same state.
public class ArchonRenderer extends HumanoidMobRenderer<ArchonEntity, ArchonRenderState, HumanoidModel<ArchonRenderState>>
{
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(DustReferences.MODID, "archon"), "main");
    private static final Map<ElementalArmorSet, Identifier> TEXTURES = new EnumMap<>(ElementalArmorSet.class);

    static {
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            TEXTURES.put(set, Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/entity/archon/" + set.id + ".png"));
        }
    }

    public ArchonRenderer(EntityRendererProvider.Context context)
    {
        super(context, new HumanoidModel<>(context.bakeLayer(LAYER)), 0.6F);
        addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.<HumanoidModel<ArchonRenderState>>bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), HumanoidModel::new),
                context.getEquipmentRenderer()));
        addLayer(new ArchonGlowLayer(this));
    }

    // A plain humanoid (as a zombie's), on a 64x64 skin
    public static LayerDefinition createLayer()
    {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
    }

    @Override
    public ArchonRenderState createRenderState()
    {
        return new ArchonRenderState();
    }

    @Override
    public void extractRenderState(ArchonEntity archon, ArchonRenderState state, float partialTicks)
    {
        super.extractRenderState(archon, state, partialTicks);
        state.set = archon.set();
        state.casting = archon.isCasting();
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(ArchonEntity archon, HumanoidArm arm)
    {
        if (archon.isCasting() && arm == archon.getMainArm()) return HumanoidModel.ArmPose.BOW_AND_ARROW;
        return super.getArmPose(archon, arm);
    }

    @Override
    public Identifier getTextureLocation(ArchonRenderState state)
    {
        return TEXTURES.get(state.set);
    }
}
