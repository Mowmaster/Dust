package com.mowmaster.dust.Capabilities.Dust;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/*
 * Implement this interface as a capability which should handle fluids, generally storing them in
 * one or more internal {@link IFluidTank} objects.
 * <p>
 * A reference implementation is provided {@link TileFluidHandler}.
 */
public interface IDustHandler
{


    enum DustAction {
        EXECUTE, SIMULATE;

        public boolean execute() {
            return this == EXECUTE;
        }

        public boolean simulate() {
            return this == SIMULATE;
        }
    }

    /*
     * Returns the number of dust storage units ("tanks") available
     *
     * @return The number of tanks available
     */
    int getTanks();

    /*
     * Returns the DustMagic in a given tank.
     *
     * IMPORTANT: This DustMagic MUST NOT be modified.
     * This method is not for altering internal contents.
     * Any implementers who are able to detect modification via this method should throw an exception.
     * It is ENTIRELY reasonable and likely that the magic returned here will be a copy.
     *
     * SERIOUSLY: DO NOT MODIFY THE RETURNED DustMagic
     *
     * @param tank Tank to query.
     * @return DustMagic in a given tank. new DustMagic(-1, 0) if the tank is empty.
     */

    @Nonnull
    DustMagic getDustMagicInTank(int tank);

    /*
     * Retrieves the maximum fluid amount for a given tank.
     *
     * @param tank Tank to query.
     * @return     The maximum fluid amount held by the tank.
     */
    int getTankCapacity(int tank);

    /*
     * This function is a way to determine which fluids can exist inside a given handler. General purpose tanks will
     * basically always return TRUE for this.
     *
     * @param tank  Tank to query for validity
     * @param stack Stack to test with for validity
     * @return TRUE if the tank can hold the FluidStack, not considering current state.
     * (Basically, is a given fluid EVER allowed in this tank?) Return FALSE if the answer to that question is 'no.'
     */
    boolean isDustValid(int tank, @NotNull DustMagic dustIn);

    /*
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param action   If SIMULATE, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    int fill(DustMagic dust, IDustHandler.DustAction action);

    /*
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Nonnull
    DustMagic drain(DustMagic dust, IDustHandler.DustAction action);

    /*
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p>
     * This method is not Fluid-sensitive.
     *
     * @param maxDrain Maximum amount of fluid to drain.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Nonnull
    DustMagic drain(int maxDrain, IDustHandler.DustAction action);
}
