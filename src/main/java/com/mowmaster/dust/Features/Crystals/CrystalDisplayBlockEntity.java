package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustStorage.IDustHolder;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// A Crystal Display holding a Large Crystal (picked with Silk Touch): it gives off its element's effect in the area
// round it (DisplayGroup.area: 9 x 9 blocks, from the display up 9) every PULSE_TICKS, the way DisplayEffects picks:
//   each creature (or block) it reaches uses one dust of the crystal's charge (16, its value: CrystalSize.LARGE), and
//   gets the effect for BASE_DURATION (20 seconds) at potency BASE_POTENCY (2); it isn't given it again until it has
//   less than REFRESH_TICKS (5 seconds) of it left. A spent crystal (no charge) turns grey and stops; tubes of its
//   element fill it again (it's an IDustHolder taking its own element, never giving any out).
//   A spell can be set on it from a Spell Scroll or a Grimoire's open page: a self spell of the crystal's element. It's
//   given instead of the element's own effect (its self effect, with its modifiers).
// Displays in a 2x2 or 3x3 square, all holding crystals, work as one under one larger crystal (DisplayGroup): the
// master runs the whole group. Each element in it is given off once: for every crystal of that element past the first,
// 10 seconds longer (EXTRA_DURATION) and one potency stronger, and one more dust from that element's crystals for each
// creature reached. The big crystal's colour is the mix of all of them.
// Synced to clients: the crystal, its charge, whether a spell is set, and the group (for Client/CrystalDisplayRenderer).
public class CrystalDisplayBlockEntity extends CBSyncedBlockEntity implements IDustHolder
{
    public static final int PULSE_TICKS = 20;
    public static final int BASE_DURATION = 20 * 20;
    public static final int EXTRA_DURATION = 10 * 20;
    public static final float BASE_POTENCY = 2;
    public static final int REFRESH_TICKS = 5 * 20;
    public static final int MAX_CHARGE = LargeCrystalItem.MAX_CHARGE;

    private @Nullable DustElement element;
    private int charge;
    private @Nullable Spell spell;
    // The group (synced): its master (null: none, this display works on its own) and size; on the master (or a lone
    // display), the colour its crystal shows and whether it's spent (shown grey)
    private @Nullable BlockPos master;
    private int groupSize = 1;
    private int groupColor = 0xFFFFFF;
    private boolean groupSpent = true;
    // Server only: when each creature's effect from each element wears off, and when each element next works a block
    private final Map<UUID, Map<DustElement, Long>> appliedUntil = new HashMap<>();
    private final Map<DustElement, Long> nextBlock = new HashMap<>();

    public CrystalDisplayBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.CRYSTAL_DISPLAY.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public int charge() { return charge; }
    public @Nullable Spell spell() { return spell; }
    public int groupSize() { return groupSize; }
    public int groupColor() { return groupColor; }
    public boolean groupSpent() { return groupSpent; }

    // Whether it draws the crystal of its group (a lone display, or the master of a square)
    public boolean leads()
    {
        return master == null || master.equals(worldPosition);
    }

    // ---- the crystal ----

    // Sets a Large Crystal on it (one off the stack, keeping its charge); false if it already holds one
    public boolean insertCrystal(ItemStack stack)
    {
        if (element != null || !(stack.getItem() instanceof LargeCrystalItem item) || !(item.getBlock() instanceof ElementCrystal crystal)) return false;
        element = crystal.element();
        charge = LargeCrystalItem.charge(stack);
        spell = null;
        afterChange();
        return true;
    }

    // Its crystal as an item (with the charge it has left), or empty
    private ItemStack crystalStack()
    {
        if (element == null) return ItemStack.EMPTY;
        return LargeCrystalItem.withCharge(new ItemStack(ElementCrystals.LARGE_ITEMS.get(element).get()), charge);
    }

    // Takes its crystal off (with the charge it has left), or empty
    public ItemStack takeCrystal()
    {
        if (element == null) return ItemStack.EMPTY;
        ItemStack stack = crystalStack();
        element = null;
        charge = 0;
        spell = null;
        afterChange();
        return stack;
    }

    // Sets a spell on it: a self spell of its crystal's element. Returns why not (a message key), or null when set.
    public @Nullable String setSpell(Spell candidate)
    {
        if (element == null) return "dust.display.no_crystal";
        if (!SpellRegistries.SELF.getId().equals(candidate.shape())) return "dust.display.not_self";
        if (!element.equals(candidate.element())) return "dust.display.wrong_element";
        spell = candidate.withStack(List.of());
        afterChange();
        return null;
    }

    public boolean clearSpell()
    {
        if (spell == null) return false;
        spell = null;
        afterChange();
        return true;
    }

    private void afterChange()
    {
        appliedUntil.clear();
        nextBlock.clear();
        sync();
        if (level != null && !level.isClientSide()) refresh(level, worldPosition);
    }

    // ---- the group ----

    // Works out the group the display at pos is in, and tells every member (its master, size, colour, light)
    public static void refresh(Level level, BlockPos pos)
    {
        if (level.isClientSide() || !(level.getBlockEntity(pos) instanceof CrystalDisplayBlockEntity)) return;
        DisplayGroup group = DisplayGroup.find(level, pos);
        List<CrystalDisplayBlockEntity> members = new ArrayList<>();
        for (BlockPos at : group.members()) if (level.getBlockEntity(at) instanceof CrystalDisplayBlockEntity display) members.add(display);
        long red = 0, green = 0, blue = 0;
        int counted = 0;
        boolean spent = true;
        for (CrystalDisplayBlockEntity display : members) {
            if (display.element == null) continue;
            int c = display.element.color();
            red += (c >> 16) & 0xFF;
            green += (c >> 8) & 0xFF;
            blue += c & 0xFF;
            counted++;
            if (display.charge > 0 && (display.spell != null || DisplayEffects.mode(display.element) != DisplayEffects.Mode.INERT)) spent = false;
        }
        int color = counted == 0 ? 0xFFFFFF : (int) ((red / counted) << 16 | (green / counted) << 8 | (blue / counted));
        BlockPos master = group.size() > 1 ? group.master() : null;
        for (CrystalDisplayBlockEntity display : members) {
            boolean lone = master == null;
            boolean changed = !java.util.Objects.equals(display.master, master) || display.groupSize != group.size();
            display.master = master;
            display.groupSize = group.size();
            int ownColor = lone ? (display.element == null ? 0xFFFFFF : display.element.color()) : color;
            boolean ownSpent = lone ? (display.element == null || display.charge <= 0
                    || (display.spell == null && DisplayEffects.mode(display.element) == DisplayEffects.Mode.INERT)) : spent;
            changed |= display.groupColor != ownColor || display.groupSpent != ownSpent;
            display.groupColor = ownColor;
            display.groupSpent = ownSpent;
            if (changed) display.sync();
            // Lit while its crystal (or its group's) is working
            BlockState state = display.getBlockState();
            boolean lit = !ownSpent && display.element != null;
            if (state.hasProperty(CrystalDisplayBlock.LIT) && state.getValue(CrystalDisplayBlock.LIT) != lit) {
                level.setBlock(display.worldPosition, state.setValue(CrystalDisplayBlock.LIT, lit), Block.UPDATE_CLIENTS);
            }
        }
    }

    // ---- giving off its effect ----

    public void serverTick(ServerLevel level)
    {
        if ((level.getGameTime() + worldPosition.asLong()) % PULSE_TICKS != 0) return;
        if (!leads()) return;
        long now = level.getGameTime();
        appliedUntil.values().removeIf(byElement -> {
            byElement.values().removeIf(until -> until < now);
            return byElement.isEmpty();
        });
        DisplayGroup group = groupSize > 1 ? DisplayGroup.find(level, worldPosition) : DisplayGroup.single(worldPosition);
        Map<DustElement, List<CrystalDisplayBlockEntity>> byElement = new LinkedHashMap<>();
        for (BlockPos at : group.members()) {
            if (level.getBlockEntity(at) instanceof CrystalDisplayBlockEntity display && display.element != null) {
                byElement.computeIfAbsent(display.element, key -> new ArrayList<>()).add(display);
            }
        }
        if (byElement.isEmpty()) return;
        AABB area = group.area();
        List<LivingEntity> creatures = level.getEntitiesOfClass(LivingEntity.class, area, entity -> entity.isAlive() && !entity.isSpectator());
        Set<CrystalDisplayBlockEntity> used = new HashSet<>();
        for (Map.Entry<DustElement, List<CrystalDisplayBlockEntity>> entry : byElement.entrySet()) {
            give(level, group, entry.getKey(), entry.getValue(), creatures, now, used);
        }
        for (CrystalDisplayBlockEntity display : used) display.sync();
        if (!used.isEmpty()) refresh(level, worldPosition);
    }

    // One element of the group: its effect on whatever in the area is due it, while its crystals have the dust
    private void give(ServerLevel level, DisplayGroup group, DustElement element, List<CrystalDisplayBlockEntity> crystals,
                      List<LivingEntity> creatures, long now, Set<CrystalDisplayBlockEntity> used)
    {
        int count = crystals.size();
        int cost = count;
        int duration = BASE_DURATION + EXTRA_DURATION * (count - 1);
        float potency = BASE_POTENCY + (count - 1);
        Spell own = null;
        for (CrystalDisplayBlockEntity display : crystals) {
            if (display.spell != null) {
                own = display.spell;
                break;
            }
        }
        DisplayEffects.Mode mode = own != null ? DisplayEffects.Mode.SELF : DisplayEffects.mode(element);
        if (mode == DisplayEffects.Mode.INERT) return;
        Spell spell = own != null ? own : new Spell(element,
                (mode == DisplayEffects.Mode.SELF ? SpellRegistries.SELF : SpellRegistries.TOUCH).getId(), Map.of(), List.of());
        SpellStats stats = own != null ? own.stats(1.0F) : SpellStats.defaults();
        stats = stats.withDuration(Math.max(stats.duration(), duration)).withPotency(Math.max(stats.potency(), potency));
        Vec3 origin = group.centre().add(0, 1, 0);
        SpellCast cast = null;
        if (mode == DisplayEffects.Mode.BLOCK) {
            Long next = nextBlock.get(element);
            if ((next != null && now < next) || pool(crystals) < cost) return;
            BlockPos target = randomBlock(level, group);
            if (target == null) return;
            cast = new SpellCast(level, null, spell, stats, Vec3.atCenterOf(target).add(0, 1, 0), new Vec3(0, -1, 0));
            cast.effect().onBlock(cast, target, Direction.UP);
            take(crystals, cost, used);
            nextBlock.put(element, now + duration);
            sparkle(level, element, Vec3.atCenterOf(target).add(0, 0.6, 0));
            return;
        }
        for (LivingEntity creature : creatures) {
            boolean hostile = creature instanceof Enemy;
            if ((mode == DisplayEffects.Mode.SELF) == hostile) continue;
            Long until = appliedUntil.getOrDefault(creature.getUUID(), Map.of()).get(element);
            if (until != null && now < until - REFRESH_TICKS) continue;
            if (pool(crystals) < cost) return;
            if (cast == null) cast = new SpellCast(level, null, spell, stats, origin, new Vec3(0, -1, 0));
            if (mode == DisplayEffects.Mode.SELF) cast.effect().onSelf(cast, creature);
            else cast.effect().onEntity(cast, creature);
            take(crystals, cost, used);
            appliedUntil.computeIfAbsent(creature.getUUID(), key -> new HashMap<>()).put(element, now + duration);
            sparkle(level, element, creature.position().add(0, creature.getBbHeight() / 2, 0));
        }
    }

    private static int pool(List<CrystalDisplayBlockEntity> crystals)
    {
        int total = 0;
        for (CrystalDisplayBlockEntity display : crystals) total += display.charge;
        return total;
    }

    // Uses up dust from the fullest crystals first, so they empty together
    private static void take(List<CrystalDisplayBlockEntity> crystals, int amount, Set<CrystalDisplayBlockEntity> used)
    {
        for (int i = 0; i < amount; i++) {
            CrystalDisplayBlockEntity fullest = null;
            for (CrystalDisplayBlockEntity display : crystals) if (display.charge > 0 && (fullest == null || display.charge > fullest.charge)) fullest = display;
            if (fullest == null) return;
            fullest.charge--;
            fullest.setChanged();
            used.add(fullest);
        }
    }

    // A block in the area to work (not air, a display, or anything holding things)
    private static @Nullable BlockPos randomBlock(ServerLevel level, DisplayGroup group)
    {
        AABB area = group.area();
        for (int tries = 0; tries < 16; tries++) {
            BlockPos at = BlockPos.containing(area.minX + level.getRandom().nextDouble() * area.getXsize(),
                    area.minY + level.getRandom().nextDouble() * area.getYsize(), area.minZ + level.getRandom().nextDouble() * area.getZsize());
            BlockState state = level.getBlockState(at);
            if (state.isAir() || state.getBlock() instanceof CrystalDisplayBlock || level.getBlockEntity(at) != null) continue;
            return at;
        }
        return null;
    }

    private static void sparkle(ServerLevel level, DustElement element, Vec3 at)
    {
        level.sendParticles(new DustParticleOptions(element.color(), 1.0F), at.x, at.y, at.z, 6, 0.3, 0.4, 0.3, 0);
    }

    // "Crystal Display: Fire, 12/16 | spell set" (for the player looking)
    public Component status(Player player)
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": ");
        if (element == null) return line.append(Component.translatable("dust.display.empty"));
        line.append(ElementNames.forViewer(element, player)).append(" " + charge + "/" + MAX_CHARGE);
        if (spell != null) line.append(" | ").append(Component.translatable("dust.display.spell"));
        else if (DisplayEffects.mode(element) == DisplayEffects.Mode.INERT) line.append(" | ").append(Component.translatable("dust.display.inert"));
        if (groupSize > 1) line.append(" | ").append(Component.translatable("dust.display.group", groupSize, groupSize));
        return line;
    }

    // ---- tubes: its own element in, nothing out ----

    @Override
    public int insertDust(DustElement dust, int amount, boolean simulate)
    {
        if (element == null || !element.equals(dust) || amount <= 0) return 0;
        int added = Math.min(amount, MAX_CHARGE - charge);
        if (added <= 0 || simulate) return Math.max(0, added);
        boolean wasSpent = charge <= 0;
        charge += added;
        sync();
        if (wasSpent && level != null) refresh(level, worldPosition);
        return added;
    }

    @Override
    public int dustAmount(DustElement dust)
    {
        return 0;
    }

    @Override
    public int extractDust(DustElement dust, int amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean prefersDust(DustElement dust)
    {
        return element != null && element.equals(dust);
    }

    // ---- dropped when broken ----

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        // Just dropped: the block is going, so its group is worked out again by the block (affectNeighborsAfterRemoval)
        if (level != null && element != null) {
            Block.popResource(level, pos, crystalStack());
            element = null;
            charge = 0;
            spell = null;
        }
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.putInt("charge", charge);
        if (spell != null) output.store("spell", Spell.CODEC, spell);
        if (master != null) output.store("master", BlockPos.CODEC, master);
        output.putInt("group_size", groupSize);
        output.putInt("group_color", groupColor);
        output.putBoolean("group_spent", groupSpent);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        element = input.read("element", DustElements.CODEC).orElse(null);
        charge = input.getIntOr("charge", 0);
        spell = input.read("spell", Spell.CODEC).orElse(null);
        master = input.read("master", BlockPos.CODEC).orElse(null);
        groupSize = input.getIntOr("group_size", 1);
        groupColor = input.getIntOr("group_color", 0xFFFFFF);
        groupSpent = input.getBooleanOr("group_spent", true);
    }
}
