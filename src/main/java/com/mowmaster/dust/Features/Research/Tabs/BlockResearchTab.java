package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Blocks (plants included): every block with an item, plus fluids (water, lava: shown as their bucket).
// Observed by looking at the block, or at it dropped as an item.
//
// Blocks are listed by family: one icon per family, with every member on the family's page. A family is joined from:
// - vanilla's block families (BlockFamilies: each wood's planks/stairs/slab/fence/door..., stone and brick sets...)
// - colour variants: red_wool, white_wool... -> "wool"; red_stained_glass and glass -> "glass";
//   red_terracotta and terracotta -> "terracotta" (a colour name or "stained_" at the front is ignored)
// - copper ageing and wax: exposed_ / weathered_ / oxidized_ / waxed_ at the front is ignored
// - block tags named <any namespace>:research_family/<name>: every block in one such tag is one family
//   (how other mods group their blocks)
public class BlockResearchTab extends ResearchTab
{
    private static final List<String> IGNORED_PREFIXES = buildPrefixes();

    public BlockResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "blocks"), 100);
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(Items.GRASS_BLOCK);
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (researchable(block)) list.add(BuiltInRegistries.BLOCK.getKey(block));
        }
        return list;
    }

    private static boolean researchable(Block block)
    {
        return block instanceof LiquidBlock || block.asItem() != Items.AIR;
    }

    @Override
    public @Nullable Identifier entryForBlock(BlockState state)
    {
        return researchable(state.getBlock()) ? BuiltInRegistries.BLOCK.getKey(state.getBlock()) : null;
    }

    // A block's item (dropped in the world, or crafted) counts as the block
    @Override
    public @Nullable Identifier entryForItem(ItemStack stack)
    {
        return stack.getItem() instanceof BlockItem blockItem ? entryForBlock(blockItem.getBlock().defaultBlockState()) : null;
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        Block block = BuiltInRegistries.BLOCK.getValue(entry);
        if (block instanceof LiquidBlock) {
            // Fluids have no item of their own: show their bucket
            return new ItemStack(block.defaultBlockState().getFluidState().getType().getBucket());
        }
        return new ItemStack(block.asItem());
    }

    @Override
    public Component name(Identifier entry)
    {
        return BuiltInRegistries.BLOCK.getValue(entry).getName();
    }

    // ---- families ----

    @Override
    protected List<List<Identifier>> computeGroups(List<Identifier> entries)
    {
        // Union-find over the entries: find(x) is the representative of x's family; union joins two families
        Map<Identifier, Identifier> parent = new HashMap<>();
        for (Identifier entry : entries) parent.put(entry, entry);

        // Vanilla families
        BlockFamilies.getAllFamilies().forEach(family -> {
            Identifier base = BuiltInRegistries.BLOCK.getKey(family.getBaseBlock());
            for (Block variant : family.getVariants().values()) {
                union(parent, base, BuiltInRegistries.BLOCK.getKey(variant));
            }
        });

        // Same name once colours / copper ageing / wax are ignored
        Map<String, Identifier> byName = new HashMap<>();
        for (Identifier entry : entries) {
            String stripped = stripPrefixes(entry.getPath());
            // Vanilla names the base copper block copper_block but its aged forms exposed_copper etc.
            if (stripped.equals("copper")) stripped = "copper_block";
            String key = entry.getNamespace() + ":" + stripped;
            Identifier first = byName.putIfAbsent(key, entry);
            if (first != null) union(parent, first, entry);
        }

        // research_family/... block tags (any namespace)
        BuiltInRegistries.BLOCK.getTags().forEach(named -> {
            if (!named.key().location().getPath().startsWith("research_family/")) return;
            Identifier first = null;
            for (var holder : named) {
                Identifier id = BuiltInRegistries.BLOCK.getKey(holder.value());
                if (!parent.containsKey(id)) continue;
                if (first == null) first = id;
                else union(parent, first, id);
            }
        });

        // Collect each family's members in list order; families are listed where their first member would be
        Map<Identifier, List<Identifier>> families = new LinkedHashMap<>();
        for (Identifier entry : entries) {
            families.computeIfAbsent(find(parent, entry), root -> new ArrayList<>()).add(entry);
        }
        return new ArrayList<>(families.values());
    }

    private static String stripPrefixes(String path)
    {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String prefix : IGNORED_PREFIXES) {
                if (path.startsWith(prefix) && path.length() > prefix.length()) {
                    path = path.substring(prefix.length());
                    changed = true;
                }
            }
        }
        return path;
    }

    private static List<String> buildPrefixes()
    {
        List<String> prefixes = new ArrayList<>();
        // Longest first, so light_gray_ is removed before gray_ could half-match it
        for (DyeColor color : DyeColor.values()) prefixes.add(color.getSerializedName() + "_");
        prefixes.sort((a, b) -> b.length() - a.length());
        prefixes.addAll(List.of("stained_", "waxed_", "exposed_", "weathered_", "oxidized_"));
        return List.copyOf(prefixes);
    }

    private static Identifier find(Map<Identifier, Identifier> parent, Identifier x)
    {
        Identifier root = x;
        while (!parent.getOrDefault(root, root).equals(root)) root = parent.get(root);
        // Point everything on the way straight at the root, so later finds are quick
        while (!x.equals(root)) {
            Identifier next = parent.get(x);
            parent.put(x, root);
            x = next;
        }
        return root;
    }

    private static void union(Map<Identifier, Identifier> parent, Identifier a, Identifier b)
    {
        if (!parent.containsKey(a) || !parent.containsKey(b)) return;
        Identifier rootA = find(parent, a);
        Identifier rootB = find(parent, b);
        if (!rootA.equals(rootB)) parent.put(rootB, rootA);
    }
}
