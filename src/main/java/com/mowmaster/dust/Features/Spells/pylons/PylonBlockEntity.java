package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// A modifier pylon's part in a spell tree: which Spell Pylon it belongs to, and what was rolled onto it (a modifier
// and its tier, or a spell break), or for the Shape Pylon, the shape. The Spell Pylon does the rolling and keeps the
// order (SpellPylonBlockEntity); this just holds the result.
//   nodes      a normal modifier gets upgrade nodes (maxNodes, PylonConfig): each filled one (nodes) cost a level and
//              added potency points (points) to the spell
//   strength   a blessed / corrupted modifier gets a strength instead (PylonConfig.MIN_STRENGTH-MAX_STRENGTH percent)
//   locked     can't be broken or changed: set once a node is filled, straight away for blessed / corrupted
//              modifiers, and for the Shape Pylon once a pylon follows it
//   powered    a spell break with redstone power (the line carries on)
// All of it is synced: the client tints the pylon from it (Client/PylonTint).
public class PylonBlockEntity extends CBSyncedBlockEntity
{
    private @Nullable BlockPos main;
    private @Nullable Identifier modifier;
    private ModifierTier tier = ModifierTier.NORMAL;
    private boolean spellBreak;
    private @Nullable Identifier shape;
    private int maxNodes;
    private int nodes;
    private int points;
    private int strength;
    private boolean locked;
    private boolean powered;

    public PylonBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.PYLON.get(), pos, state);
    }

    public @Nullable BlockPos main() { return main; }
    public @Nullable Identifier modifier() { return modifier; }
    public ModifierTier tier() { return tier; }
    public boolean isSpellBreak() { return spellBreak; }
    public @Nullable Identifier shape() { return shape; }
    public int maxNodes() { return maxNodes; }
    public int nodes() { return nodes; }
    public int points() { return points; }
    public int strength() { return strength; }
    public boolean locked() { return locked; }
    public boolean powered() { return powered; }

    public boolean canUpgrade()
    {
        return main != null && modifier != null && nodes < maxNodes;
    }

    // One more node filled, worth this many potency points (a levelled pylon is locked)
    void upgrade(int worth)
    {
        nodes++;
        points += worth;
        locked = true;
        sync();
    }

    void lock()
    {
        if (locked) return;
        locked = true;
        sync();
    }

    public PylonType type()
    {
        return getBlockState().getBlock() instanceof PylonBlock pylon ? pylon.type() : PylonType.SHAPE;
    }

    // Joined to a tree with this result (modifier null and spellBreak false: nothing could be rolled)
    void link(BlockPos main, @Nullable Identifier modifier, ModifierTier tier, boolean spellBreak, @Nullable Identifier shape, int maxNodes, int strength)
    {
        this.maxNodes = maxNodes;
        this.nodes = 0;
        this.points = 0;
        this.strength = strength;
        this.main = main;
        this.modifier = modifier;
        this.tier = tier;
        this.spellBreak = spellBreak;
        this.shape = shape;
        // Blessed / corrupted modifiers are locked as soon as they're rolled
        this.locked = tier != ModifierTier.NORMAL;
        this.powered = spellBreak && level != null && level.hasNeighborSignal(worldPosition);
        sync();
    }

    void setShape(Identifier shape)
    {
        this.shape = shape;
        sync();
    }

    // Cut off from its tree (the pylon before it was broken): it stays, but does nothing until broken and placed again
    void unlink()
    {
        main = null;
        modifier = null;
        tier = ModifierTier.NORMAL;
        spellBreak = false;
        shape = null;
        maxNodes = 0;
        nodes = 0;
        points = 0;
        strength = 0;
        locked = false;
        powered = false;
        updateLook(null, PylonLinks.NONE);
        sync();
    }

    // A spell break's redstone changed
    void updatePower()
    {
        boolean now = level != null && level.hasNeighborSignal(worldPosition);
        if (now == powered) return;
        powered = now;
        sync();
    }

    // The block state to match: which way its parent is (null: leave it), which sides lead on, how many nodes. The
    // block entity stays when only the state changes (same block).
    void updateLook(@Nullable Direction parent, PylonLinks links)
    {
        if (level == null || !(getBlockState().getBlock() instanceof PylonBlock)) return;
        BlockState now = getBlockState().setValue(PylonBlock.LINKS, links).setValue(PylonBlock.NODES, Mth.clamp(maxNodes, 1, PylonConfig.MAX_NODES));
        if (parent != null && parent.getAxis().isHorizontal()) now = now.setValue(PylonBlock.PARENT, parent);
        if (now != getBlockState()) level.setBlock(worldPosition, now, Block.UPDATE_ALL);
    }

    // Broken: the Spell Pylon hears about it (a reroll, or the line cut here)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !level.isClientSide() && main != null && level.getBlockEntity(main) instanceof SpellPylonBlockEntity spellPylon) {
            spellPylon.pylonRemoved(this);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (main != null) output.store("main", BlockPos.CODEC, main);
        if (modifier != null) output.store("modifier", Identifier.CODEC, modifier);
        output.store("tier", ModifierTier.CODEC, tier);
        output.putBoolean("spell_break", spellBreak);
        if (shape != null) output.store("shape", Identifier.CODEC, shape);
        output.putInt("max_nodes", maxNodes);
        output.putInt("nodes", nodes);
        output.putInt("points", points);
        output.putInt("strength", strength);
        output.putBoolean("locked", locked);
        output.putBoolean("powered", powered);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        main = input.read("main", BlockPos.CODEC).orElse(null);
        modifier = input.read("modifier", Identifier.CODEC).orElse(null);
        tier = input.read("tier", ModifierTier.CODEC).orElse(ModifierTier.NORMAL);
        spellBreak = input.getBooleanOr("spell_break", false);
        shape = input.read("shape", Identifier.CODEC).orElse(null);
        maxNodes = input.getIntOr("max_nodes", 0);
        nodes = input.getIntOr("nodes", 0);
        points = input.getIntOr("points", 0);
        strength = input.getIntOr("strength", 0);
        locked = input.getBooleanOr("locked", false);
        powered = input.getBooleanOr("powered", false);
        // On the client: the tints (body, lockout, nodes) come from here, so ask for the block to be drawn again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
