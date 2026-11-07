package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.fireDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FireOrbEntityBase extends ElementalOrbEntityBase {

    public FireOrbEntityBase(EntityType<FireOrbEntityBase> type, Level level) {
        super(type, level);
    }

    @Override
    public EnumElement getElement() {
        return EnumElement.FIRE;
    }
}
