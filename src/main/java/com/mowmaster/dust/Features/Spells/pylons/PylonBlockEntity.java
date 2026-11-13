package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// A modifier pylon's part in a spell tree: which Spell Pylon it belongs to, and what was rolled onto it (a modifier
// and its tier, or a spell break), or for the Shape Pylon, the shape. The Spell Pylon does the rolling and keeps the
// order (SpellPylonBlockEntity); this just holds the result.
// A pylon with a modifier also gets upgrade nodes (maxNodes, PylonConfig): each filled one (nodes) cost a level and
// added potency points (points) to the spell.
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

    public boolean canUpgrade()
    {
        return main != null && modifier != null && nodes < maxNodes;
    }

    // One more node filled, worth this many potency points
    void upgrade(int worth)
    {
        nodes++;
        points += worth;
        sync();
    }

    public PylonType type()
    {
        return getBlockState().getBlock() instanceof PylonBlock pylon ? pylon.type() : PylonType.SHAPE;
    }

    // Joined to a tree with this result (modifier null and spellBreak false: nothing could be rolled)
    void link(BlockPos main, @Nullable Identifier modifier, ModifierTier tier, boolean spellBreak, @Nullable Identifier shape, int maxNodes)
    {
        this.maxNodes = maxNodes;
        this.nodes = 0;
        this.points = 0;
        this.main = main;
        this.modifier = modifier;
        this.tier = tier;
        this.spellBreak = spellBreak;
        this.shape = shape;
        updateState();
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
        updateState();
        sync();
    }

    // The block state's "state" to match (PylonState)
    void updateState()
    {
        if (level == null || !(getBlockState().getBlock() instanceof PylonBlock)) return;
        PylonState state;
        if (main == null) state = PylonState.INERT;
        else if (spellBreak) state = level.hasNeighborSignal(worldPosition) ? PylonState.BREAK_ON : PylonState.BREAK_OFF;
        else if (tier == ModifierTier.BLESSED) state = PylonState.BLESSED;
        else if (tier == ModifierTier.CORRUPTED) state = PylonState.CORRUPTED;
        else state = PylonState.LINKED;
        if (getBlockState().getValue(PylonBlock.STATE) != state) level.setBlockAndUpdate(worldPosition, getBlockState().setValue(PylonBlock.STATE, state));
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
    }
}
