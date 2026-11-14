package com.mowmaster.dust.Features.Spells.caster;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.dust.Features.Spells.SpellCasting;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.grimoire.GrimoireItem;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.LinkedHashMap;
import java.util.Map;

// What a Spell Caster holds, and its casting:
//   scroll   the Spell Scroll whose spell it casts
//   dust     dust of each element, fed in as element dust items (one dust each), up to DUST_CAPACITY of each
//   energy   what powers a cast in place of mana: an energy crystal is ENERGY_PER_CRYSTAL energy, and a cast takes as
//            much energy as a player would pay in mana (PlayerMagicHelper.manaForDust, at least 1)
// A cast costs the spell's dust (no attunement discount: a block isn't attuned to anything) and its energy, and only if
// it lands on something. Blocks don't give the spell's tree experience: only players practising do.
// Hoppers and pipes reach it through `inputs` (one slot for dust, one for energy crystals), which it empties into its
// stores every tick.
@EventBusSubscriber(modid = DustReferences.MODID)
public class SpellCasterBlockEntity extends CBSyncedBlockEntity
{
    public static final int DUST_CAPACITY = 256;
    public static final int ENERGY_PER_CRYSTAL = 20;
    public static final int ENERGY_CAPACITY = 640;

    private ItemStack scroll = ItemStack.EMPTY;
    private final Map<DustElement, Integer> dust = new LinkedHashMap<>();
    private int energy;

    // What hoppers and pipes put in: slot 0 element dust, slot 1 energy crystals. Emptied into the stores straight away.
    private final ItemStacksResourceHandler inputs = new ItemStacksResourceHandler(2)
    {
        @Override
        public boolean isValid(int index, ItemResource resource)
        {
            ItemStack stack = resource.toStack(1);
            return index == 0 ? CrystalElements.ofDust(stack) != null : isEnergyCrystal(stack);
        }

        // Changing the slots from inside a hopper's transaction isn't safe, so this only notes it; serverTick takes the
        // items in
        @Override
        protected void onContentsChanged(int index, ItemStack previous)
        {
            setChanged();
        }
    };

    public SpellCasterBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.SPELL_CASTER.get(), pos, state);
    }

    // Hoppers and pipes reach the inputs from every side
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(Capabilities.Item.BLOCK, DustBlockEntityRegistry.SPELL_CASTER.get(), (caster, side) -> caster.inputs);
    }

    private static boolean isEnergyCrystal(ItemStack stack)
    {
        DustElement element = CrystalElements.of(stack);
        return element != null && element.equals(DustElements.ENERGY);
    }

    // ---- feeding ----

    public boolean accepts(ItemStack stack)
    {
        return CrystalElements.ofDust(stack) != null || isEnergyCrystal(stack);
    }

    // As much of the stack as fits goes in (the stack shrinks by that much)
    public void feed(ItemStack stack)
    {
        DustElement element = CrystalElements.ofDust(stack);
        if (element != null) {
            int room = DUST_CAPACITY - dust.getOrDefault(element, 0);
            int taken = Math.min(room, stack.getCount());
            if (taken <= 0) return;
            dust.merge(element, taken, Integer::sum);
            stack.shrink(taken);
        } else if (isEnergyCrystal(stack)) {
            int taken = Math.min((ENERGY_CAPACITY - energy) / ENERGY_PER_CRYSTAL, stack.getCount());
            if (taken <= 0) return;
            energy += taken * ENERGY_PER_CRYSTAL;
            stack.shrink(taken);
        }
        setChanged();
    }

    public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, SpellCasterBlockEntity caster)
    {
        caster.absorbInputs();
    }

    // Whatever hoppers put in goes into the stores (what doesn't fit stays in the input slot, and hoppers stop)
    private void absorbInputs()
    {
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack stack = inputs.copyToList().get(i);
            if (stack.isEmpty()) continue;
            int before = stack.getCount();
            feed(stack);
            if (stack.getCount() != before) inputs.set(i, stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack), stack.getCount());
        }
    }

    // A scroll whose spell is Self-shaped: a block can't cast those (it has no self)
    public static boolean isSelfSpell(ItemStack scroll)
    {
        Spell spell = scroll.get(DustComponentDataRegistry.SPELL.get());
        return spell != null && spell.shape().equals(com.mowmaster.dust.Features.Spells.SpellRegistries.SELF.getId());
    }

    // Puts a scroll in (or takes it out, with EMPTY), handing back the one that was there
    public ItemStack swapScroll(ItemStack newScroll)
    {
        ItemStack old = scroll;
        scroll = newScroll;
        setChanged();
        sync();
        return old;
    }

    // ---- casting ----

    public void cast(ServerLevel level, Direction facing)
    {
        Spell spell = scroll.get(DustComponentDataRegistry.SPELL.get());
        SpellShape shape = spell == null ? null : spell.shapeType();
        // (A Self scroll can't be put in, but one might already be in from before that rule)
        if (spell == null || shape == null || isSelfSpell(scroll)) return;
        Map<DustElement, Integer> cost = spell.dustCost();
        int total = 0;
        for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
            if (dust.getOrDefault(need.getKey(), 0) < need.getValue()) {
                fizzle(level, facing);
                return;
            }
            total += need.getValue();
        }
        int energyCost = Math.max(1, PlayerMagicHelper.manaForDust(total));
        if (energy < energyCost) {
            fizzle(level, facing);
            return;
        }
        // From just in front of its face, straight out
        Vec3 direction = Vec3.atLowerCornerOf(facing.getUnitVec3i());
        Vec3 origin = Vec3.atCenterOf(worldPosition).add(direction.scale(0.7));
        SpellCast cast = new SpellCast(level, null, spell, spell.stats(1.0F), origin, direction);
        if (!SpellCasting.deliver(cast, shape)) {
            fizzle(level, facing);
            return;
        }
        cost.forEach((element, amount) -> dust.merge(element, -amount, Integer::sum));
        dust.values().removeIf(amount -> amount <= 0);
        energy -= energyCost;
        setChanged();
        level.sendParticles(ParticleTypes.ENCHANT, origin.x, origin.y, origin.z, 12, 0.2, 0.2, 0.2, 0.5);
        level.playSound(null, worldPosition, SoundEvents.EVOKER_CAST_SPELL, SoundSource.BLOCKS, 0.6F, 1.4F);
    }

    // Nothing to cast with, or nothing to land on: a puff of smoke, like an empty dispenser's click
    private void fizzle(ServerLevel level, Direction facing)
    {
        Vec3 at = Vec3.atCenterOf(worldPosition).add(Vec3.atLowerCornerOf(facing.getUnitVec3i()).scale(0.6));
        level.sendParticles(ParticleTypes.SMOKE, at.x, at.y, at.z, 5, 0.1, 0.1, 0.1, 0.01);
        level.playSound(null, worldPosition, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.8F, 1.2F);
    }

    // What it holds, in chat for whoever clicked
    public void report(ServerPlayer player)
    {
        Component spell = scroll.isEmpty() ? Component.translatable("dust.caster.no_scroll") : GrimoireItem.spellName(scroll);
        StringBuilder dusts = new StringBuilder();
        dust.forEach((element, amount) -> dusts.append(dusts.isEmpty() ? "" : ", ").append(amount).append(' ').append(element.name().getString()));
        player.sendSystemMessage(Component.translatable("dust.caster.report", spell, dusts.isEmpty() ? "-" : dusts.toString(), energy));
    }

    // Broken: the scroll and anything still in the input slots drop (the stored dust and energy are lost)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        if (!scroll.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, scroll);
        for (ItemStack stack : inputs.copyToList()) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        }
    }

    // ---- saving ----

    private static final Codec<Map<DustElement, Integer>> DUST_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!scroll.isEmpty()) output.store("scroll", ItemStack.CODEC, scroll);
        output.store("dust", DUST_CODEC, dust);
        output.putInt("energy", energy);
        inputs.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        scroll = input.read("scroll", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        dust.clear();
        dust.putAll(input.read("dust", DUST_CODEC).orElse(Map.of()));
        energy = input.getIntOr("energy", 0);
        inputs.deserialize(input);
    }
}
