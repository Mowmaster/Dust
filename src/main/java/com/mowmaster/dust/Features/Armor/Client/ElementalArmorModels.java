package com.mowmaster.dust.Features.Armor.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.HashMap;
import java.util.Map;

// The elemental armor sets' 3D models: for each set, one model per piece (so the robe's skirt doesn't show with the
// leggings, nor the boots' cuffs with them), built from ArmorGeometry's cubes on the humanoid model's parts, which the
// wearer's model poses (NeoForge copies the pose and which parts show). One 128x128 texture per set
// (textures/entity/equipment/robe/<set>.png) covers all four pieces.
public final class ElementalArmorModels
{
    private static final Map<String, HumanoidModel<?>> BAKED = new HashMap<>();

    private ElementalArmorModels() {}

    static String key(ElementalArmorSet set, ArmorType type)
    {
        return set.id + "/" + switch (type) {
            case HELMET -> "head";
            case CHESTPLATE -> "chest";
            case LEGGINGS -> "legs";
            default -> "feet";
        };
    }

    public static ModelLayerLocation layer(ElementalArmorSet set, ArmorType type)
    {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(DustReferences.MODID, "armor/" + key(set, type).replace('/', '_')), "main");
    }

    public static Identifier texture(ElementalArmorSet set)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/entity/equipment/robe/" + set.id + ".png");
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            for (ArmorType type : com.mowmaster.dust.Features.Armor.ElementalArmor.PIECES) {
                String data = ArmorGeometry.CUBES.getOrDefault(key(set, type), "");
                event.registerLayerDefinition(layer(set, type), () -> build(data));
            }
        }
    }

    // The baked model for one piece (made the first time it's needed)
    public static HumanoidModel<?> model(ElementalArmorSet set, ArmorType type)
    {
        return BAKED.computeIfAbsent(key(set, type), k -> new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(layer(set, type))));
    }

    // A humanoid model with no cubes of its own (its parts where the player's are), and the piece's cubes on its parts
    static LayerDefinition build(String data)
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5, 2, 0));
        PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5, 2, 0));
        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12, 0));
        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12, 0));
        PartDefinition[] parts = {head, body, rightArm, leftArm, rightLeg, leftLeg};
        int index = 0;
        for (String cube : data.split(";")) {
            if (cube.isBlank()) continue;
            String[] values = cube.split(",");
            float[] f = new float[values.length];
            for (int i = 0; i < values.length; i++) f[i] = Float.parseFloat(values[i]);
            parts[(int) f[0]].addOrReplaceChild("cube" + index++, CubeListBuilder.create().texOffs((int) f[1], (int) f[2])
                            .addBox(f[3], f[4], f[5], f[6], f[7], f[8], new CubeDeformation(f[9])),
                    PartPose.offsetAndRotation(f[10], f[11], f[12], (float) Math.toRadians(f[13]), (float) Math.toRadians(f[14]), (float) Math.toRadians(f[15])));
        }
        return LayerDefinition.create(mesh, 128, 128);
    }
}
