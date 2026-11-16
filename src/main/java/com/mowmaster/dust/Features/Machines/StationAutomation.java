package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jspecify.annotations.Nullable;

// Starting research stations without a player's hand (StationBlockEntity.activate):
//   a redstone pulse                          onNeighborChanged, from each station block's neighborChanged
//   a dispenser with a Magical Flint and Steel  facing a station: starts it (one durability); facing anything else it
//                                             does nothing (a click, like an empty dispenser)
//   a fire spell landing on a station         FireEffect calls activate
public final class StationAutomation
{
    private StationAutomation() {}

    // From a station block's neighborChanged (server): starts it when the power has just come on
    public static void onNeighborChanged(Level level, BlockPos pos)
    {
        if (!(level instanceof ServerLevel serverLevel) || !(level.getBlockEntity(pos) instanceof StationBlockEntity station)) return;
        if (station.newPulse(level.hasNeighborSignal(pos))) station.activate(serverLevel, null);
    }

    // Starts the station at pos if there is one and it can start. True if it did something.
    public static boolean activate(ServerLevel level, BlockPos pos, @Nullable Player by)
    {
        return level.getBlockEntity(pos) instanceof StationBlockEntity station && station.activate(level, by);
    }

    // The dispenser behaviour is registered once the game has set up (enqueueWork: on the main thread, since the
    // dispenser's table isn't safe to change from the loading threads)
    @EventBusSubscriber(modid = DustReferences.MODID)
    public static final class Setup
    {
        private Setup() {}

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event)
        {
            event.enqueueWork(() -> DispenserBlock.registerBehavior(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.get(), new FlintDispense()));
        }
    }

    // OptionalDispenseItemBehavior: setSuccess(false) makes the dispenser click instead of shooting the item out
    private static final class FlintDispense extends OptionalDispenseItemBehavior
    {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack dispensed)
        {
            ServerLevel level = source.level();
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos target = source.pos().relative(facing);
            boolean started = activate(level, target, null);
            setSuccess(started);
            if (started) {
                level.playSound(null, target, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                // No one is holding it: hurtAndBreak with the level instead of an owner
                dispensed.hurtAndBreak(1, level, null, item -> {});
            }
            return dispensed;
        }
    }
}
