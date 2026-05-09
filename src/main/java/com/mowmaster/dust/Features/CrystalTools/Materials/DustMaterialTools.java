package com.mowmaster.dust.Features.CrystalTools.Materials;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.world.item.ToolMaterial;

public class DustMaterialTools {
    public static final ToolMaterial CRYSTAL = new ToolMaterial(DustTags.Blocks.INCORRECT_FOR_CRYSTAL_TOOL,
            1000, 12f, 3f, 27, DustTags.Items.CRYSTAL_REPAIRABLES);
    public static final ToolMaterial CRYSTAL_BLOCK = new ToolMaterial(DustTags.Blocks.INCORRECT_FOR_CRYSTAL_TOOL,
            9000, 3f, 1f, 27, DustTags.Items.CRYSTAL_BLOCK_REPAIRABLES);
}
