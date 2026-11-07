package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.orderDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class OrderOrbEntityBase extends ElementalOrbEntityBase {

    public OrderOrbEntityBase(EntityType<OrderOrbEntityBase> type, Level level) {
        super(type, level);
    }

    @Override
    public EnumElement getElement() {
        return EnumElement.ORDER;
    }
}
