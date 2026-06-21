package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.security.PublicKey;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DustProviderDatapack extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DustDamageTypes::bootstrap);

    public DustProviderDatapack (PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output,registries, BUILDER, Set.of(DustReferences.MODID));
    }
}
