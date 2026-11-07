package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.chaosDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ChaosOrbEntityBase extends ElementalOrbEntityBase {

    public ChaosOrbEntityBase(EntityType<ChaosOrbEntityBase> type, Level level) {
        super(type, level);
    }

    @Override
    public EnumElement getElement() {
        return EnumElement.CHAOS;
    }
}
