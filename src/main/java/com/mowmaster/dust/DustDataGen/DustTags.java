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
        public static final TagKey<Item> MAGICAL_CRYSTAL_ITEMS = createTag("magical_crystals");
        public static final TagKey<Item> CRYSTAL_REPAIRABLES = createTag("crystal_repairables");
        public static final TagKey<Item> CRYSTAL_BLOCK_REPAIRABLES = createTag("crystal_block_repairables");

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

        public static TagKey<Block> createTag(String name)
        {
            return BlockTags.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, name));
        }
    }
}
