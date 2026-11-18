package com.mowmaster.dust.Features.Lamps;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import com.mowmaster.dust.Features.Lamps.Client.LampClient;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

// What the lamps share (LampBlock: the Growth Lamp, the Lovely Lamp):
//   fuel      element dust, up to CAPACITY units, gathered from GATHER_RANGE blocks round it: element orbs of its element
//             lying there (absorbed whole, as much as fits) and Dust Jars holding it (drawn out), each shown as a stream
//             of dust in the element's colour flowing to the lamp (DustStream). It only refills once it's run low
//             (refillBelow: under 10 units), and then fills right up, so jars aren't drawn on a little at a time and
//             orbs it doesn't need are left for players. Checked every second.
//   burning   while it has fuel it's lit: each unit lasts TICKS_PER_UNIT (10 seconds); what it does while lit is the
//             lamp's own (work): within RADIUS blocks, and only what it can see (a clear line from the lamp)
// Clients only need to know whether it's lit (the ambient particles and the area's hue: Client/LampClient), so it
// syncs when that changes.
public abstract class LampBlockEntity extends CBSyncedBlockEntity
{
    public static final int CAPACITY = 100;
    public static final int TICKS_PER_UNIT = 200;
    public static final int GATHER_RANGE = 8;
    public static final int RADIUS = 9;

    private int fuel;
    private int burn;

    protected LampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // The element it burns
    public abstract DustElement element();

    // Its colour (0xRRGGBB): the particles and the area's hue
    public abstract int color();

    // What it does each tick while it's lit (server)
    protected abstract void work(ServerLevel level);

    public int fuel() { return fuel; }
    public boolean lit() { return fuel > 0; }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, LampBlockEntity lamp)
    {
        if (!(level instanceof ServerLevel server)) return;
        // Spread out over the second by position, so lamps placed together don't all gather on the same tick
        if (lamp.fuel < refillBelow(CAPACITY) && (level.getGameTime() + pos.asLong()) % 20 == 0) lamp.gather(server);
        if (lamp.fuel <= 0) return;
        lamp.work(server);
        if (++lamp.burn >= TICKS_PER_UNIT) {
            lamp.burn = 0;
            lamp.use(1);
        }
    }

    // Client: while it's lit, a few motes drift in its area, and it counts towards the area's hue (LampClient)
    public static void clientTick(Level level, BlockPos pos, BlockState state, LampBlockEntity lamp)
    {
        if (lamp.lit()) LampClient.litTick(lamp);
    }

    // ---- fuel ----

    // How low a store of this size has to run before it refills: 10 units, or a tenth of it for one smaller than 100
    public static int refillBelow(int capacity)
    {
        return Math.min(10, Math.max(1, capacity / 10));
    }

    // Uses up some fuel (a bred animal's unit, a burnt-out unit). False if there wasn't that much.
    protected boolean use(int units)
    {
        if (fuel < units) return false;
        boolean wasLit = lit();
        fuel -= units;
        if (lit() != wasLit) sync();
        else setChanged();
        return true;
    }

    private void add(int units)
    {
        boolean wasLit = lit();
        fuel = Math.min(CAPACITY, fuel + units);
        if (lit() != wasLit) sync();
        else setChanged();
    }

    // Element orbs of its element and Dust Jars holding it, within GATHER_RANGE: as much as there's room for, each move
    // shown as the element's dust flowing to the lamp
    private void gather(ServerLevel level)
    {
        DustElement element = element();
        Vec3 here = Vec3.atCenterOf(worldPosition);
        AABB area = new AABB(worldPosition).inflate(GATHER_RANGE);
        for (ElementOrbEntity orb : level.getEntitiesOfClass(ElementOrbEntity.class, area, orb -> orb.isAlive() && element.equals(orb.getElement()))) {
            int room = CAPACITY - fuel;
            if (room <= 0) return;
            int taken = Math.min(room, orb.getValue());
            if (taken >= orb.getValue()) orb.discard();
            else orb.setValue(orb.getValue() - taken);
            add(taken);
            showMove(level, orb.position(), here, taken);
        }
        // Dust Jars holding its element (DustJars: nearest first)
        for (DustJarBlockEntity jar : com.mowmaster.dust.Features.DustStorage.DustJars.nearby(level, worldPosition, GATHER_RANGE)) {
            int room = CAPACITY - fuel;
            if (room <= 0) return;
            if (!element.equals(jar.element())) continue;
            int taken = jar.extract(room);
            if (taken <= 0) continue;
            add(taken);
            showMove(level, Vec3.atCenterOf(jar.getBlockPos()), here, taken);
        }
    }

    // The element's dust flowing from there to the lamp: a winding stream in its colour (DustStream, drawn by the client)
    private void showMove(ServerLevel level, Vec3 from, Vec3 to, int amount)
    {
        com.mowmaster.dust.Features.DustStorage.DustStream.send(level, from, to, element().color(), amount);
    }

    // ---- what it reaches ----

    // A random block within RADIUS (a sphere), or null if the pick fell outside it
    protected BlockPos randomInRange(RandomSource random)
    {
        int dx = random.nextInt(2 * RADIUS + 1) - RADIUS, dy = random.nextInt(2 * RADIUS + 1) - RADIUS, dz = random.nextInt(2 * RADIUS + 1) - RADIUS;
        if (dx * dx + dy * dy + dz * dz > RADIUS * RADIUS) return null;
        return worldPosition.offset(dx, dy, dz);
    }

    // Whether there's a clear line from the lamp to that point (only solid blocks stand in the way, so crops don't hide
    // the crops behind them). The line starts just outside the lamp itself, whose own shape would block it otherwise.
    protected boolean sees(Level level, Vec3 to, BlockPos target)
    {
        Vec3 from = Vec3.atCenterOf(worldPosition);
        Vec3 way = to.subtract(from);
        double length = way.length();
        if (length < 1.0E-3) return true;
        from = from.add(way.scale(Math.min(0.75, length) / length));
        BlockHitResult hit = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
        return hit.getType() == HitResult.Type.MISS || hit.getBlockPos().equals(target);
    }

    // ---- the player's view ----

    // "Growth Lamp: 54/100 Growth dust"
    public void tellFuel(Player player)
    {
        player.sendOverlayMessage(net.minecraft.network.chat.Component.translatable("dust.lamp.fuel", getBlockState().getBlock().getName(), fuel, CAPACITY,
                com.mowmaster.dust.Features.DustComposition.ElementNames.forViewer(element(), player)));
    }

    // ---- saving (and what clients get) ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("fuel", fuel);
        output.putInt("burn", burn);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        fuel = input.getIntOr("fuel", 0);
        burn = input.getIntOr("burn", 0);
    }

    // A small coloured mote (dust particle) in its colour
    public DustParticleOptions mote(float scale)
    {
        return new DustParticleOptions(color(), scale);
    }
}
