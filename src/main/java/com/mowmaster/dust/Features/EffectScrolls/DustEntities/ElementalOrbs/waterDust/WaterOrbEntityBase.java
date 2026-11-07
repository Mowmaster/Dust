package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.waterDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class WaterOrbEntityBase extends ElementalOrbEntityBase {

    public WaterOrbEntityBase(EntityType<WaterOrbEntityBase> type, Level level) {
        super(type, level);
    }

    @Override
    public EnumElement getElement() {
        return EnumElement.WATER;
    }
}
