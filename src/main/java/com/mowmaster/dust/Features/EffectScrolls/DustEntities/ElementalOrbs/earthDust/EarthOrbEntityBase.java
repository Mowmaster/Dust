package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.earthDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EarthOrbEntityBase extends ElementalOrbEntityBase {

    public EarthOrbEntityBase(EntityType<EarthOrbEntityBase> type, Level level) {
        super(type, level);
    }

    @Override
    public EnumElement getElement() {
        return EnumElement.EARTH;
    }
}
