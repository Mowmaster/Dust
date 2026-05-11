package com.mowmaster.dust.Features.EffectScrolls.KeyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mowmaster.dust.DustReferences;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class DustKeyMappings
{
    private static final KeyMapping DUST_KEYMAPPINGACTION = new KeyMapping(DustReferences.KEYMAP + "dust_keyaction",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, KeyMapping.Category.MISC);

    public static final Lazy<KeyMapping> PRESSKEY = Lazy.of(() -> DUST_KEYMAPPINGACTION);

    public static void register()
    {

    }
}
