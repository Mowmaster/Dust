package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DustTags
{
    public static class Items {
        public static final TagKey<Item> MAGICAL_DUST_ITEMS = createTag("magical_dusts");
        // Spell tools (Features/Spells/SpellTools): weapons cast their Touch spell on what they hit, tools on the block
        // they strike
        public static final TagKey<Item> SPELL_WEAPONS = createTag("spell_weapons");
        public static final TagKey<Item> SPELL_TOOLS = createTag("spell_tools");
        public static final TagKey<Item> MAGICAL_CRYSTAL_ITEMS = createTag("magical_crystals");
        // Any element crystal, or an amethyst shard (the Element Tester's recipe)
        public static final TagKey<Item> CRYSTALS_OR_AMETHYST = createTag("crystals_or_amethyst");
        public static final TagKey<Item> CRYSTAL_REPAIRABLES = createTag("crystal_repairables");
        public static final TagKey<Item> CRYSTAL_BLOCK_REPAIRABLES = createTag("crystal_block_repairables");

        // An elemental wood's four logs (dust:<wood>_logs): its planks, chest and charcoal are made from any of them
        public static TagKey<Item> logs(com.mowmaster.dust.Features.Trees.ElementalWood wood)
        {
            return createTag(wood.id + "_logs");
        }

        public static TagKey<Item> createTag(String name)
        {
            return ItemTags.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> MAGICAL_CRYSTAL_BLOCKS = createTag("magical_crystal_blocks");
        public static final TagKey<Block> NEEDS_CRYSTAL_TOOL = createTag("needs_crystal_tool");
        public static final TagKey<Block> INCORRECT_FOR_CRYSTAL_TOOL = createTag("incorrect_for_crystal_tool");
        public static final TagKey<Block> PICKADZE_MINEABLE = createTag("mineable/pickadze");
        public static final TagKey<Block> MATTOCK_MINEABLE = createTag("mineable/mattock");
        // Research's "Found in" biomes (Research.notesBiome): blocks too common in world generation to be worth noting,
        // and crafted blocks that are worth noting anyway because they also generate (snow, packed ice)
        public static final TagKey<Block> NO_BIOME_NOTES = createTag("no_biome_notes");
        public static final TagKey<Block> BIOME_NOTES = createTag("biome_notes");
        // One Research family (Blocks tab) for every element crystal, crystal block and budding crystal
        public static final TagKey<Block> RESEARCH_FAMILY_CRYSTALS = createTag("research_family/element_crystals");

        // An elemental wood's four logs (dust:<wood>_logs), as vanilla's oak_logs
        public static TagKey<Block> logs(com.mowmaster.dust.Features.Trees.ElementalWood wood)
        {
            return createTag(wood.id + "_logs");
        }

        // One Research family (Blocks tab) for each elemental wood: all its blocks
        public static TagKey<Block> researchFamily(com.mowmaster.dust.Features.Trees.ElementalWood wood)
        {
            return createTag("research_family/" + wood.id);
        }

        public static TagKey<Block> createTag(String name)
        {
            return BlockTags.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, name));
        }
    }
}
