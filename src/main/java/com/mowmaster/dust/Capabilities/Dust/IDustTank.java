package com.mowmaster.dust.Capabilities.Dust;

import javax.annotation.Nonnull;

/*
* This interface represents a Dust Tank.
* IT IS NOT REQUIRED but is provided for convenience.
* You are free to handle Dust in any way that you wish - this is simply an easy default way.
* DO NOT ASSUME that these objects are used internally in all cases.
*/
public interface IDustTank
{
    /*
     * @return DustMagic representing the Dust in the tank, null if the tank is empty.
     */
    @Nonnull
    DustMagic getDustMagic();

    /*
     * @return Current amount of dust in the tank.
     */
    int getDustMagicAmount();

    /*
     * @return Capacity of this dust tank.
     */
    int getTankCapacity();

    /*
     * @param stack DustMagic holding the Dust to be queried.
     * @return If the tank can hold the dust (EVER, not at the time of query).
     */
    boolean isDustValid(DustMagic dust);

    /*
     * @param resource DustMagic attempting to fill the tank.
     * @param action   If SIMULATE, the fill will only be simulated.
     * @return Amount of dust that was accepted (or would be, if simulated) by the tank.
     */
    int fill(DustMagic dust, IDustHandler.DustAction action);

    /*
     * @param maxDrain Maximum amount of dust to be removed from the container.
     * @param action   If SIMULATE, the drain will only be simulated.
     * @return Amount of dust that was removed (or would be, if simulated) from the tank.
     */
    @Nonnull
    DustMagic drain(int maxDrain, IDustHandler.DustAction action);

    /*
     * @param resource Maximum amount of dust to be removed from the container.
     * @param action   If SIMULATE, the drain will only be simulated.
     * @return DustMagic representing dust that was removed (or would be, if simulated) from the tank.
     */
    @Nonnull
    DustMagic drain(DustMagic dust, IDustHandler.DustAction action);
}
