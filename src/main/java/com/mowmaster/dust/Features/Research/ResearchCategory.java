package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// The book's five tabs, and which registry entries go in each (in registry order):
//   BLOCKS   every block that has an item, except plants
//   PLANTS   blocks in the block tag #dust:research/plants (flowers, saplings, crops, leaves... see DustTagProviderBlock)
//   ANIMALS  entities with a spawn egg that aren't monsters or bosses
//   MOBS     entities with a spawn egg in the MONSTER spawn category
//   BOSSES   entities in the NeoForge tag #c:bosses (the Ender Dragon, the Wither...)
// The lists depend on tags, so they're worked out on first use and thrown away when tags reload (clearCache).
public enum ResearchCategory
{
    BLOCKS("blocks", false, () -> new ItemStack(Items.GRASS_BLOCK)),
    PLANTS("plants", false, () -> new ItemStack(Items.POPPY)),
    ANIMALS("animals", true, () -> new ItemStack(Items.COW_SPAWN_EGG)),
    MOBS("mobs", true, () -> new ItemStack(Items.ZOMBIE_HEAD)),
    BOSSES("bosses", true, () -> new ItemStack(Items.DRAGON_HEAD));

    private final String name;
    private final boolean entityCategory;
    private final Supplier<ItemStack> tabIcon;
    private @Nullable List<ResearchEntry> cache;

    ResearchCategory(String name, boolean entityCategory, Supplier<ItemStack> tabIcon)
    {
        this.name = name;
        this.entityCategory = entityCategory;
        this.tabIcon = tabIcon;
    }

    public boolean isEntityCategory()
    {
        return entityCategory;
    }

    public ItemStack tabIcon()
    {
        return tabIcon.get();
    }

    public Component title()
    {
        return Component.translatable("research.dust.tab." + name);
    }

    public List<ResearchEntry> entries()
    {
        if (cache == null) {
            List<ResearchEntry> list = new ArrayList<>();
            if (entityCategory) {
                for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                    if (forEntity(type) == this) list.add(new ResearchEntry(this, BuiltInRegistries.ENTITY_TYPE.getKey(type)));
                }
            } else {
                for (Block block : BuiltInRegistries.BLOCK) {
                    if (forBlock(block) == this) list.add(new ResearchEntry(this, BuiltInRegistries.BLOCK.getKey(block)));
                }
            }
            cache = List.copyOf(list);
        }
        return cache;
    }

    // Which tab a block belongs in, or null if it can't be researched (no item: air, technical blocks)
    public static @Nullable ResearchCategory forBlock(Block block)
    {
        if (block.asItem() == Items.AIR) return null;
        return block.defaultBlockState().is(DustTags.Blocks.RESEARCH_PLANTS) ? PLANTS : BLOCKS;
    }

    // Which tab an entity type belongs in, or null if it can't be researched (no spawn egg: players, items, arrows...)
    public static @Nullable ResearchCategory forEntity(EntityType<?> type)
    {
        if (type.builtInRegistryHolder().is(Tags.EntityTypes.BOSSES)) return BOSSES;
        if (SpawnEggItem.byId(type).isEmpty()) return null;
        return type.getCategory() == MobCategory.MONSTER ? MOBS : ANIMALS;
    }

    public static @Nullable ResearchEntry entryFor(Block block)
    {
        ResearchCategory category = forBlock(block);
        return category == null ? null : new ResearchEntry(category, BuiltInRegistries.BLOCK.getKey(block));
    }

    public static @Nullable ResearchEntry entryFor(EntityType<?> type)
    {
        ResearchCategory category = forEntity(type);
        return category == null ? null : new ResearchEntry(category, BuiltInRegistries.ENTITY_TYPE.getKey(type));
    }

    // Called when tags (re)load: plants and bosses are tag-based
    public static void clearCache()
    {
        for (ResearchCategory category : values()) category.cache = null;
    }
}
