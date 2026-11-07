package com.mowmaster.dust.Features.Research;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;

// One thing that can be researched: a block (BLOCKS / PLANTS) or an entity type (ANIMALS / MOBS / BOSSES).
public record ResearchEntry(ResearchCategory category, Identifier id)
{
    public boolean isEntity()
    {
        return category.isEntityCategory();
    }

    public Block block()
    {
        return BuiltInRegistries.BLOCK.getValue(id);
    }

    public EntityType<?> entityType()
    {
        return BuiltInRegistries.ENTITY_TYPE.getValue(id);
    }

    // What the book shows for it: the block's item, or the mob's spawn egg
    public ItemStack icon()
    {
        if (isEntity()) {
            return SpawnEggItem.byId(entityType()).map(egg -> new ItemStack(egg)).orElse(ItemStack.EMPTY);
        }
        return new ItemStack(block().asItem());
    }

    public Component name()
    {
        return isEntity() ? entityType().getDescription() : block().getName();
    }

    // Translation key for this entry's built-in notes, e.g. research.dust.note.minecraft.zombie
    public String noteKey()
    {
        return "research.dust.note." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }
}
