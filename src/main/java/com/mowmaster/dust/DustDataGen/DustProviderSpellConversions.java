package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.conversions.BlockConversion;
import com.mowmaster.dust.Features.Spells.conversions.BlockRef;
import com.mowmaster.dust.Features.Spells.conversions.ConversionTable;
import com.mowmaster.dust.Features.Spells.conversions.SpellConversions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Dust's spell conversion tables (data/dust/dust_spell_conversions/<element>.json; SpellConversions reads them). Packs
// add their own files, or replace one of these with a file of the same name. JsonCodecProvider: NeoForge's provider
// that writes each value through a codec as a json file.
public class DustProviderSpellConversions extends JsonCodecProvider<ConversionTable>
{
    public DustProviderSpellConversions(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, PackOutput.Target.DATA_PACK, SpellConversions.DIRECTORY, ConversionTable.CODEC, lookupProvider, DustReferences.MODID);
    }

    private static final String[] CORALS = {"tube", "brain", "bubble", "fire", "horn"};
    private static final String[] CORAL_PARTS = {"_coral_block", "_coral", "_coral_fan", "_coral_wall_fan"};

    @Override
    protected void gather()
    {
        // Stone: loose blocks harden
        List<BlockConversion> stone = new ArrayList<>(List.of(
                convert("gravel", "stone"), convert("sand", "sandstone"), convert("red_sand", "red_sandstone"), convert("mud", "packed_mud")));
        for (DyeColor color : DyeColor.values()) stone.add(convert(color.getName() + "_concrete_powder", color.getName() + "_concrete"));
        table(DustElements.STONE, stone);

        // Mud: dirt turns to mud
        table(DustElements.MUD, List.of(convert("dirt", "mud"), convert("coarse_dirt", "mud"), convert("grass_block", "mud"), convert("podzol", "mud"),
                convert("mycelium", "mud"), convert("rooted_dirt", "muddy_mangrove_roots")));

        // Fracture: blocks crumble a step
        table(DustElements.FRACTURE, List.of(convert("stone", "gravel"), convert("cobblestone", "gravel"), convert("deepslate", "cobbled_deepslate"),
                convert("cobbled_deepslate", "gravel"), convert("stone_bricks", "cracked_stone_bricks"),
                BlockConversion.dropping(b("gravel"), List.of(new BlockConversion.Drop(Items.FLINT, 1))),
                convert("sandstone", "sand"), convert("cut_sandstone", "sand"), convert("smooth_sandstone", "sand"), convert("chiseled_sandstone", "sand"),
                convert("red_sandstone", "red_sand"), convert("cut_red_sandstone", "red_sand"), convert("smooth_red_sandstone", "red_sand"),
                convert("chiseled_red_sandstone", "red_sand")));

        // Crystal: crystallises
        table(DustElements.CRYSTAL, List.of(convert("sand", "glass"), convert("red_sand", "glass"), convert("quartz_block", "tinted_glass"),
                convert("amethyst_cluster", "tinted_glass"), convert("budding_amethyst", "tinted_glass")));

        // Life: dead things live again
        List<BlockConversion> life = new ArrayList<>();
        List<BlockConversion> death = new ArrayList<>();
        for (String coral : CORALS) {
            for (String part : CORAL_PARTS) {
                life.add(convert("dead_" + coral + part, coral + part));
                death.add(convert(coral + part, "dead_" + coral + part));
            }
        }
        life.add(BlockConversion.convert(b("dead_bush"), BlockRef.of(BlockTags.SAPLINGS)));
        life.add(convert("coarse_dirt", "dirt"));
        table(DustElements.LIFE, life);

        // Death: living things die
        death.add(BlockConversion.convert(BlockRef.of(BlockTags.SAPLINGS), b("dead_bush")));
        death.add(convert("short_grass", "dead_bush"));
        death.add(convert("moss_block", "coarse_dirt"));
        death.add(convert("grass_block", "dirt"));
        table(DustElements.DEATH, death);

        // Healing: cracked and damaged blocks mend
        table(DustElements.HEALING, List.of(convert("cracked_stone_bricks", "stone_bricks"), convert("cracked_deepslate_bricks", "deepslate_bricks"),
                convert("cracked_deepslate_tiles", "deepslate_tiles"), convert("cracked_nether_bricks", "nether_bricks"),
                convert("cracked_polished_blackstone_bricks", "polished_blackstone_bricks"), convert("infested_cracked_stone_bricks", "infested_stone_bricks"),
                convert("damaged_anvil", "chipped_anvil"), convert("chipped_anvil", "anvil")));

        // Change: a block moves along its family: block, stairs, slab, wall / fence, pressure plate, button, and round
        List<BlockConversion> change = new ArrayList<>();
        BlockFamilies.getAllFamilies().forEach(family -> {
            List<BlockRef> cycle = new ArrayList<>();
            cycle.add(BlockRef.of(family.getBaseBlock()));
            for (BlockFamily.Variant variant : List.of(BlockFamily.Variant.STAIRS, BlockFamily.Variant.SLAB, BlockFamily.Variant.WALL,
                    BlockFamily.Variant.FENCE, BlockFamily.Variant.PRESSURE_PLATE, BlockFamily.Variant.BUTTON)) {
                Block block = family.get(variant);
                if (block != null) cycle.add(BlockRef.of(block));
            }
            if (cycle.size() >= 2) change.add(BlockConversion.cycleOf(cycle));
        });
        table(DustElements.CHANGE, change);

        // Motion: paths
        table(DustElements.MOTION, List.of(convert("dirt", "dirt_path"), convert("grass_block", "dirt_path"), convert("coarse_dirt", "dirt_path"),
                convert("podzol", "dirt_path"), convert("mycelium", "dirt_path"), convert("rooted_dirt", "dirt_path")));

        // Fungus: mycelium on dirt; nylium on netherrack (crimson by lava, warped by water)
        table(DustElements.FUNGUS, List.of(convert("dirt", "mycelium"), convert("grass_block", "mycelium"),
                convert("netherrack", "crimson_nylium").near(FluidTags.LAVA), convert("netherrack", "warped_nylium").near(FluidTags.WATER)));

        // Radiance: the block spreads onto what's around it
        table(DustElements.RADIANCE, List.of(BlockConversion.spreadOnto(b("grass_block"), List.of(b("dirt"))),
                BlockConversion.spreadOnto(b("podzol"), List.of(b("dirt"), b("grass_block"))),
                BlockConversion.spreadOnto(b("mycelium"), List.of(b("dirt"), b("grass_block"))),
                BlockConversion.spreadOnto(b("crimson_nylium"), List.of(b("netherrack"))),
                BlockConversion.spreadOnto(b("warped_nylium"), List.of(b("netherrack"))),
                BlockConversion.spreadOnto(b("moss_block"), List.of(b("stone"), b("deepslate"), b("dirt")))));

        // Gem: compressed into something finer, one time in ten; otherwise it crumbles away
        BlockRef air = b("air");
        table(DustElements.GEM, List.of(
                BlockConversion.dropping(b("coal_block"), List.of(new BlockConversion.Drop(Items.DIAMOND, 1))).chance(0.1F, air),
                convert("raw_iron_block", "iron_ore").deep(b("deepslate_iron_ore")).chance(0.1F, air),
                convert("raw_copper_block", "copper_ore").deep(b("deepslate_copper_ore")).chance(0.1F, air),
                convert("raw_gold_block", "gold_ore").deep(b("deepslate_gold_ore")).chance(0.1F, air)));

        // Ice: still water freezes, still lava sets to obsidian
        table(DustElements.ICE, List.of(convert("water", "ice").onlySources(), convert("lava", "obsidian").onlySources()));

        // Slime: sticky (pistons become sticky pistons; falling blocks are stuck in place by Features/Spells/blocks/Sticking)
        table(DustElements.SLIME, List.of(convert("piston", "sticky_piston")));

        // Seeds: plants go back to their seeds
        table(DustElements.SEEDS, List.of(seeds("wheat", Items.WHEAT_SEEDS), seeds("carrots", Items.CARROT), seeds("potatoes", Items.POTATO),
                seeds("beetroots", Items.BEETROOT_SEEDS), seeds("melon_stem", Items.MELON_SEEDS), seeds("attached_melon_stem", Items.MELON_SEEDS),
                seeds("pumpkin_stem", Items.PUMPKIN_SEEDS), seeds("attached_pumpkin_stem", Items.PUMPKIN_SEEDS),
                seeds("torchflower_crop", Items.TORCHFLOWER_SEEDS), seeds("torchflower", Items.TORCHFLOWER_SEEDS),
                seeds("pitcher_crop", Items.PITCHER_POD), seeds("pitcher_plant", Items.PITCHER_POD), seeds("sweet_berry_bush", Items.SWEET_BERRIES),
                seeds("cocoa", Items.COCOA_BEANS), seeds("nether_wart", Items.NETHER_WART), seeds("short_grass", Items.WHEAT_SEEDS),
                seeds("tall_grass", Items.WHEAT_SEEDS)));
    }

    private void table(DustElement element, List<BlockConversion> conversions)
    {
        unconditional(Identifier.fromNamespaceAndPath(DustReferences.MODID, element.id().getPath()), new ConversionTable(element, conversions));
    }

    private static BlockRef b(String vanillaId)
    {
        return BlockRef.of(BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(vanillaId)));
    }

    private static BlockConversion convert(String from, String to)
    {
        return BlockConversion.convert(b(from), b(to));
    }

    private static BlockConversion seeds(String plant, Item seed)
    {
        return BlockConversion.dropping(b(plant), List.of(new BlockConversion.Drop(seed, 1)));
    }
}
