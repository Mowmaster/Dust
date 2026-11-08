package com.mowmaster.dust;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.DustRegistries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DustReferences.MODID)
public class Dust {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dust(IEventBus modEventBus, ModContainer modContainer) {
        DustItemRegistry.register(modEventBus);
        DustBlockRegistry.register(modEventBus);
        DustBlockEntityRegistry.register(modEventBus);
        DustEntityRegistry.register(modEventBus);

        DustAttachmentTypeRegistry.register(modEventBus);

        DustConsumeEffectsRegistry.register(modEventBus);
        DustEffectsRegistry.register(modEventBus);
        DustPotionRegistry.register(modEventBus);

        DustLootRegistry.register(modEventBus);
        DustRecipeRegistry.register(modEventBus);
        DustComponentDataRegistry.register(modEventBus);
        DustMenuRegistry.register(modEventBus);
        DustFeatureRegistry.register(modEventBus);

        DustCreativeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);

        DustParticleRegistry.PARTICLE_TYPES.register(modEventBus);
    }
}
