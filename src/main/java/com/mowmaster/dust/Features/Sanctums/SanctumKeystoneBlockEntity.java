package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Labyrinth.LabyrinthData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// A sanctum's records, kept by its keystone (set as the sanctum is built: SanctumPiece). One placed by hand (to build a
// sanctum template) does nothing until a sanctum sets it up (ready).
//   set         its element
//   spawners    where its Arcane Spawners are
//   portal      the bottom corner of the portal's opening (PORTAL_WIDTH across along axis, PORTAL_HEIGHT up)
//   returnPos   where a player comes back to from the labyrinth (in front of the portal, facing away: returnYaw)
//   slot, run   the labyrinth run its portal leads into now (a party going in together shares one: Labyrinth.enter)
// Every second it opens the portal when every spawner has been beaten (resting), and closes it when one wakes again.
// When a run from this sanctum ends with its Archon beaten, the spawners are set waiting again (LabyrinthData's pending
// resets), so the next run means fighting through the sanctum again.
public class SanctumKeystoneBlockEntity extends BlockEntity
{
    public static final int PORTAL_WIDTH = 2, PORTAL_HEIGHT = 3;

    private ElementalArmorSet set = ElementalArmorSet.PYREBORN;
    private final List<BlockPos> spawners = new ArrayList<>();
    private BlockPos portal = BlockPos.ZERO;
    private Direction.Axis axis = Direction.Axis.X;
    private Vec3 returnPos = Vec3.ZERO;
    private float returnYaw;
    private int slot = -1;
    private long run = -1;
    private boolean ready;

    public SanctumKeystoneBlockEntity(BlockPos pos, BlockState state)
    {
        super(Sanctums.KEYSTONE_ENTITY.get(), pos, state);
    }

    // Set by the sanctum as it's built
    public void setup(ElementalArmorSet set, List<BlockPos> spawners, BlockPos portal, Direction.Axis axis, Vec3 returnPos, float returnYaw)
    {
        this.set = set;
        this.spawners.clear();
        this.spawners.addAll(spawners);
        this.portal = portal;
        this.axis = axis;
        this.returnPos = returnPos;
        this.returnYaw = returnYaw;
        this.ready = true;
        setChanged();
    }

    public ElementalArmorSet set() { return set; }
    public Vec3 returnPos() { return returnPos; }
    public float returnYaw() { return returnYaw; }
    public int slot() { return slot; }
    public long run() { return run; }

    public void setRun(int slot, long run)
    {
        this.slot = slot;
        this.run = run;
        setChanged();
    }

    void serverTick(ServerLevel level)
    {
        if (!ready || level.getGameTime() % 20 != 0) return;
        LabyrinthData data = LabyrinthData.get(level.getServer());
        // A run from here was won: the sanctum must be fought through again
        if (data.takeReset(GlobalPos.of(level.dimension(), worldPosition))) {
            for (BlockPos pos : spawners) {
                if (level.getBlockEntity(pos) instanceof ArcaneSpawnerBlockEntity spawner) spawner.reset(level);
            }
            setRun(-1, -1);
        }
        boolean cleared = true;
        for (BlockPos pos : spawners) {
            if (level.getBlockEntity(pos) instanceof ArcaneSpawnerBlockEntity spawner && spawner.phase() != ArcaneSpawnerBlock.Phase.COOLDOWN) {
                cleared = false;
                break;
            }
        }
        boolean open = level.getBlockState(portal).getBlock() instanceof ArcanePortalBlock;
        if (cleared && !open) setPortal(level, true);
        else if (!cleared && open) setPortal(level, false);
    }

    private void setPortal(ServerLevel level, boolean open)
    {
        Direction along = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockState sheet = ArcanePortalBlock.state(axis, set, false);
        for (int across = 0; across < PORTAL_WIDTH; across++) {
            for (int up = 0; up < PORTAL_HEIGHT; up++) {
                BlockPos pos = portal.relative(along, across).above(up);
                BlockState there = level.getBlockState(pos);
                if (open && there.canBeReplaced()) level.setBlock(pos, sheet, 2);
                else if (!open && there.getBlock() instanceof ArcanePortalBlock) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
        Vec3 middle = Vec3.atCenterOf(portal.relative(along, 1).above(1));
        level.playSound(null, portal, open ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, open ? 0.8F : 1.2F);
        if (open) level.sendParticles(new DustParticleOptions(set.element().color(), 2.0F), middle.x, middle.y, middle.z, 40, 0.8, 1.2, 0.8, 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("set", ElementalArmorSet.CODEC, set);
        output.store("spawners", BlockPos.CODEC.listOf(), List.copyOf(spawners));
        output.store("portal", BlockPos.CODEC, portal);
        output.putString("axis", axis.getSerializedName());
        output.store("return_pos", Vec3.CODEC, returnPos);
        output.putFloat("return_yaw", returnYaw);
        output.putInt("slot", slot);
        output.putLong("run", run);
        output.putBoolean("ready", ready);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        set = input.read("set", ElementalArmorSet.CODEC).orElse(ElementalArmorSet.PYREBORN);
        spawners.clear();
        input.read("spawners", BlockPos.CODEC.listOf()).ifPresent(spawners::addAll);
        portal = input.read("portal", BlockPos.CODEC).orElse(worldPosition.above());
        axis = "z".equals(input.getStringOr("axis", "x")) ? Direction.Axis.Z : Direction.Axis.X;
        returnPos = input.read("return_pos", Vec3.CODEC).orElse(Vec3.atBottomCenterOf(worldPosition.above()));
        returnYaw = input.getFloatOr("return_yaw", 0.0F);
        slot = input.getIntOr("slot", -1);
        run = input.getLongOr("run", -1L);
        // Saved before "ready" was: a sanctum's (it had a portal)
        ready = input.getBooleanOr("ready", input.read("portal", BlockPos.CODEC).isPresent());
    }
}
