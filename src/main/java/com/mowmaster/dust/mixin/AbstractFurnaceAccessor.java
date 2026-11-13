package com.mowmaster.dust.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Reaches a furnace's private fuel timer (Energy's block effect adds fuel). An accessor mixin: Mixin fills in these
// methods to get and set the named private fields, and any furnace can be cast to this interface.
@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceAccessor
{
    @Accessor("litTimeRemaining")
    int dust$getLitTimeRemaining();

    @Accessor("litTimeRemaining")
    void dust$setLitTimeRemaining(int ticks);

    @Accessor("litTotalTime")
    int dust$getLitTotalTime();

    @Accessor("litTotalTime")
    void dust$setLitTotalTime(int ticks);
}
