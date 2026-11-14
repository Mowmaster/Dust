package com.mowmaster.dust.Features.Crafting;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Machines.IStationSlots;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.ghost.GhostItem;
import com.mowmaster.mowlib.storage.StorageInteractions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// The Element Crafter: a 3x3 grid of items on top and four jars of element dust round it.
//   grid       each cell holds a stack (right-click a cell to put items in, left-click a filled one to take them out)
//   jars       each holds up to 1000 dust of one element. The first dust put in sets the jar's element (its lid takes
//              that colour); it keeps it when empty, so only that element goes back in, until it's cleared (sneak +
//              right-click the empty jar with an empty hand)
//   result     whenever the grid changes, the matching element crafting recipe's result is worked out here (the
//              server has the recipes, clients don't) and sent to clients as a ghost item over the grid, with what it
//              takes from the jars underneath (red if the jars are short)
//   crafting   left-click the crafter (not a filled cell): crafts once; sneaking, as many as make a stack
// What's saved is also what clients get (CBSyncedBlockEntity): the jars for the lid colours, the grid and the ghost.
public class ElementCrafterBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    // One jar: its element (null: none set yet / cleared) and how much dust is in it
    private final @Nullable DustElement[] jarElements = new DustElement[ElementCrafterLayout.JARS];
    private final int[] jarAmounts = new int[ElementCrafterLayout.JARS];
    private final ItemStack[] grid = new ItemStack[ElementCrafterLayout.CELLS];
    // The result shown over the grid: none, or one ghost
    private List<GhostItem> ghosts = List.of();

    public ElementCrafterBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.ELEMENT_CRAFTER.get(), pos, state);
        Arrays.fill(grid, ItemStack.EMPTY);
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        List<StationSlot> slots = new ArrayList<>();
        for (int i = 0; i < ElementCrafterLayout.CELLS; i++) {
            final int cell = i;
            slots.add(StationSlot.ofStack(ElementCrafterLayout.cell(cell), stack -> true, 64,
                    () -> grid[cell], stack -> grid[cell] = stack, this::gridChanged));
        }
        // The jars take their element's dust; they give nothing back by left-click (that crafts), so contents is empty
        for (int i = 0; i < ElementCrafterLayout.JARS; i++) {
            final int jar = i;
            slots.add(new StationSlot(ElementCrafterLayout.jar(jar), stack -> canFill(jar, stack),
                    (stack, player) -> fill(jar, stack), () -> ItemStack.EMPTY, max -> ItemStack.EMPTY));
        }
        return slots;
    }

    public ItemStack getCell(int cell)
    {
        return grid[cell];
    }

    public List<GhostItem> getGhosts()
    {
        return ghosts;
    }

    public boolean hasResult()
    {
        return !ghosts.isEmpty();
    }

    // ---- jars ----

    public @Nullable DustElement jarElement(int jar)
    {
        return jarElements[jar];
    }

    public int jarAmount(int jar)
    {
        return jarAmounts[jar];
    }

    // Element dust, of the jar's element (or any element if it has none set), while it has room
    public boolean canFill(int jar, ItemStack stack)
    {
        DustElement element = CrystalElements.ofDust(stack);
        return element != null && (jarElements[jar] == null || jarElements[jar] == element) && jarAmounts[jar] < ElementCrafterLayout.JAR_CAPACITY;
    }

    // Puts as much of the stack in as fits (each dust item is one dust); returns how many went in
    public int fill(int jar, ItemStack stack)
    {
        if (!canFill(jar, stack)) return 0;
        int moved = Math.min(stack.getCount(), ElementCrafterLayout.JAR_CAPACITY - jarAmounts[jar]);
        jarElements[jar] = CrystalElements.ofDust(stack);
        jarAmounts[jar] += moved;
        jarsChanged();
        return moved;
    }

    // Clears an empty jar's element (its lid goes back to plain); false if it has dust in it or no element anyway
    public boolean clearJar(int jar)
    {
        if (jarAmounts[jar] > 0 || jarElements[jar] == null) return false;
        jarElements[jar] = null;
        jarsChanged();
        return true;
    }

    // How much of this element all the jars hold together (two jars can share an element: both count)
    public int dustOf(DustElement element)
    {
        int total = 0;
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            if (jarElements[jar] == element) total += jarAmounts[jar];
        }
        return total;
    }

    private boolean hasCosts(ElementCraftingRecipe recipe)
    {
        for (ElementCraftingRecipe.ElementCost cost : recipe.elements()) {
            if (dustOf(cost.element()) < cost.amount()) return false;
        }
        return true;
    }

    // Takes the recipe's dust from the jars (the jars keep their element when they run dry)
    private void takeCosts(ElementCraftingRecipe recipe)
    {
        for (ElementCraftingRecipe.ElementCost cost : recipe.elements()) {
            int left = cost.amount();
            for (int jar = 0; jar < ElementCrafterLayout.JARS && left > 0; jar++) {
                if (jarElements[jar] != cost.element()) continue;
                int taken = Math.min(left, jarAmounts[jar]);
                jarAmounts[jar] -= taken;
                left -= taken;
            }
        }
    }

    // ---- the recipe and its result ----

    private CraftingInput input()
    {
        return CraftingInput.of(3, 3, Arrays.asList(grid));
    }

    private Optional<RecipeHolder<ElementCraftingRecipe>> findRecipe(ServerLevel level)
    {
        // recipeAccess(): the server's RecipeManager. getRecipeFor asks every recipe of this type whether it matches.
        return level.recipeAccess().getRecipeFor(DustRecipeRegistry.ELEMENT_CRAFTING_TYPE.get(), input(), level);
    }

    // Works the result out again and sends it to clients if it changed (server only)
    private void updateResult()
    {
        if (!(level instanceof ServerLevel serverLevel)) return;
        List<GhostItem> now = findRecipe(serverLevel).map(holder -> List.of(ghostFor(holder.value()))).orElse(List.of());
        if (!GhostItem.same(ghosts, now)) ghosts = now;
    }

    // The result over the grid: its name (with the count when more than one) and, below, the dust it uses
    private GhostItem ghostFor(ElementCraftingRecipe recipe)
    {
        ItemStack result = recipe.assemble(input());
        Component label = result.getCount() > 1
                ? Component.literal(result.getCount() + " x ").append(result.getHoverName())
                : result.getHoverName();
        Optional<Component> below = Optional.empty();
        if (!recipe.elements().isEmpty()) {
            MutableComponent costs = Component.empty();
            for (int i = 0; i < recipe.elements().size(); i++) {
                ElementCraftingRecipe.ElementCost cost = recipe.elements().get(i);
                if (i > 0) costs.append(Component.literal("  "));
                boolean enough = dustOf(cost.element()) >= cost.amount();
                costs.append(Component.literal(cost.amount() + " ").append(cost.element().name())
                        .withStyle(enough ? ChatFormatting.WHITE : ChatFormatting.RED));
            }
            below = Optional.of(costs);
        }
        return new GhostItem(Optional.of(worldPosition), result, label, below);
    }

    // Left-click: crafts once, or (all) as many times as make one stack of the result. The result goes to the player.
    // Every craft uses one of each grid item (what's left of an item, like a bucket, stays in its cell or goes to the
    // player) and the recipe's dust from the jars. False (with a message) if nothing was crafted.
    public boolean craft(ServerLevel level, Player player, boolean all)
    {
        Optional<RecipeHolder<ElementCraftingRecipe>> found = findRecipe(level);
        if (found.isEmpty()) return false;
        ElementCraftingRecipe recipe = found.get().value();
        if (!recipe.allows(player)) {
            player.sendOverlayMessage(Component.translatable("dust.element_crafter.needs_research"));
            return false;
        }
        if (!hasCosts(recipe)) {
            player.sendOverlayMessage(Component.translatable("dust.element_crafter.needs_dust"));
            return false;
        }

        ItemStack made = ItemStack.EMPTY;
        do {
            ItemStack result = recipe.assemble(input());
            if (!made.isEmpty() && (!ItemStack.isSameItemSameComponents(made, result) || made.getCount() + result.getCount() > made.getMaxStackSize())) break;
            useGridItems(player);
            takeCosts(recipe);
            if (made.isEmpty()) made = result;
            else made.grow(result.getCount());
        // Sneaking: again while the same recipe still matches and the jars still have its dust
        } while (all && recipe.matches(input(), level) && hasCosts(recipe));

        StorageInteractions.giveToPlayer(player, made);
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.8F, 1.2F);
        jarsChanged();
        gridChanged();
        return true;
    }

    private void useGridItems(Player player)
    {
        for (int cell = 0; cell < ElementCrafterLayout.CELLS; cell++) {
            ItemStack stack = grid[cell];
            if (stack.isEmpty()) continue;
            // getCraftingRemainder: what an item leaves behind when crafted with (a bucket from a milk bucket), as a
            // template (an item, count and components) to make a stack from; null for most items
            ItemStackTemplate remainder = stack.getCraftingRemainder();
            stack.shrink(1);
            if (stack.isEmpty()) grid[cell] = ItemStack.EMPTY;
            if (remainder == null) continue;
            ItemStack left = remainder.create();
            if (grid[cell].isEmpty()) grid[cell] = left;
            else StorageInteractions.giveToPlayer(player, left);
        }
    }

    private void gridChanged()
    {
        updateResult();
        setChanged();
        sync();
    }

    private void jarsChanged()
    {
        updateResult(); // the red/white dust line under the ghost follows the jars
        setChanged();
        sync();
    }

    // ---- saving ----

    private record Jar(Optional<DustElement> element, int amount)
    {
        static final Codec<Jar> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group(
                DustElements.CODEC.optionalFieldOf("element").forGetter(Jar::element),
                Codec.INT.optionalFieldOf("amount", 0).forGetter(Jar::amount)
        ).apply(i, Jar::new));
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        List<Jar> jars = new ArrayList<>();
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) jars.add(new Jar(Optional.ofNullable(jarElements[jar]), jarAmounts[jar]));
        output.store("jars", Jar.CODEC.listOf(), jars);
        output.store("grid", ItemStack.OPTIONAL_CODEC.listOf(), Arrays.asList(grid));
        if (!ghosts.isEmpty()) output.store("ghosts", GhostItem.CODEC.listOf(), ghosts);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        List<Jar> jars = input.read("jars", Jar.CODEC.listOf()).orElse(List.of());
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            jarElements[jar] = jar < jars.size() ? jars.get(jar).element().orElse(null) : null;
            jarAmounts[jar] = jar < jars.size() ? jars.get(jar).amount() : 0;
        }
        List<ItemStack> cells = input.read("grid", ItemStack.OPTIONAL_CODEC.listOf()).orElse(List.of());
        for (int cell = 0; cell < ElementCrafterLayout.CELLS; cell++) grid[cell] = cell < cells.size() ? cells.get(cell) : ItemStack.EMPTY;
        ghosts = input.read("ghosts", GhostItem.CODEC.listOf()).orElse(List.of());
        // The lid colours come from here (Client/ElementCrafterTint): ask for the block to be drawn again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    // Recipes may have changed since this was saved (a datapack reload, an update): work the result out again
    @Override
    public void onLoad()
    {
        super.onLoad();
        updateResult();
    }

    // Broken or replaced: drop the grid items and the jars' dust
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        double x = pos.getX() + 0.5, y = pos.getY() + 0.8, z = pos.getZ() + 0.5;
        for (ItemStack stack : grid) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, x, y, z, stack);
        }
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            if (jarElements[jar] == null) continue;
            int left = jarAmounts[jar];
            while (left > 0) {
                ItemStack dust = CrystalElements.dustOf(jarElements[jar], Math.min(64, left));
                if (dust.isEmpty()) break;
                left -= dust.getCount();
                Containers.dropItemStack(level, x, y, z, dust);
            }
        }
    }
}
