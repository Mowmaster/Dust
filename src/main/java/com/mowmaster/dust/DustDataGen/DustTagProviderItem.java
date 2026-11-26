package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

// 26.x tag appenders take registry keys (ResourceKey<Item>), not the Item objects. A DeferredItem's key() gives that
// key without needing the item to exist yet. A block's item has the same id as the block, so blockItem() builds it.
public class DustTagProviderItem extends ItemTagsProvider {
    public DustTagProviderItem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
    }

    private static ResourceKey<Item> blockItem(DeferredBlock<? extends Block> block) {
        return ResourceKey.create(Registries.ITEM, block.getId());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Dyeable in a crafting grid with MowLib's dyes (ColorableItemDyeRecipe): the crafter and replicator keep the
        // colour when placed
        tag(com.mowmaster.mowlib.recipes.ColorableItemDyeRecipe.DYEABLE_IN_CRAFTING).add(blockItem(DustBlockRegistry.ELEMENT_CRAFTER))
                .add(blockItem(DustBlockRegistry.ELEMENT_REPLICATOR));

        // Spell tools: the crystal weapons cast on a hit, the crystal tools on striking a block (SpellTools)
        tag(DustTags.Items.SPELL_WEAPONS).add(DustItemRegistry.CRYSTAL_SWORD.key()).add(DustItemRegistry.CRYSTAL_SPEAR.key());
        tag(DustTags.Items.SPELL_TOOLS).add(DustItemRegistry.CRYSTAL_PICKAXE.key()).add(DustItemRegistry.CRYSTAL_AXE.key())
                .add(DustItemRegistry.CRYSTAL_SHOVEL.key()).add(DustItemRegistry.CRYSTAL_HOE.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key()).add(DustItemRegistry.CRYSTAL_HAMMER.key());

        // Every element's dust (the five primals' and the rest: DustItemRegistry.DUSTS)
        var dusts = tag(DustTags.Items.MAGICAL_DUST_ITEMS);
        DustItemRegistry.DUSTS.values().forEach(item -> dusts.add(item.key()));

        // Every element's crystal (DustItemRegistry.CRYSTALS)
        var crystals = tag(DustTags.Items.MAGICAL_CRYSTAL_ITEMS);
        DustItemRegistry.CRYSTALS.values().forEach(item -> crystals.add(item.key()));
        tag(DustTags.Items.CRYSTALS_OR_AMETHYST).addTag(DustTags.Items.MAGICAL_CRYSTAL_ITEMS).add(net.minecraft.world.item.Items.AMETHYST_SHARD.builtInRegistryHolder().key());

        tag(DustTags.Items.CRYSTAL_REPAIRABLES)
                .add(DustItemRegistry.CRYSTAL_RED.key())
                .add(DustItemRegistry.CRYSTAL_GREEN.key())
                .add(DustItemRegistry.CRYSTAL_BLUE.key())
                .add(DustItemRegistry.CRYSTAL_WHITE.key())
                .add(DustItemRegistry.CRYSTAL_BLACK.key());

        tag(DustTags.Items.CRYSTAL_BLOCK_REPAIRABLES)
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK));

        //This is required for specific enchants to work
        tag(ItemTags.SWORDS)
                .add(DustItemRegistry.CRYSTAL_SWORD.key());
        tag(ItemTags.PICKAXES)
                .add(DustItemRegistry.CRYSTAL_PICKAXE.key())
                .add(DustItemRegistry.CRYSTAL_HAMMER.key());
        tag(ItemTags.AXES)
                .add(DustItemRegistry.CRYSTAL_AXE.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.SHOVELS)
                .add(DustItemRegistry.CRYSTAL_SHOVEL.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.HOES)
                .add(DustItemRegistry.CRYSTAL_HOE.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.SPEARS)
                .add(DustItemRegistry.CRYSTAL_SPEAR.key());

        tag(ItemTags.HEAD_ARMOR)
                .add(DustItemRegistry.CRYSTAL_HELMET.key());
        tag(ItemTags.CHEST_ARMOR)
                .add(DustItemRegistry.CRYSTAL_CHESTPLATE.key());
        tag(ItemTags.LEG_ARMOR)
                .add(DustItemRegistry.CRYSTAL_LEGGINGS.key());
        tag(ItemTags.FOOT_ARMOR)
                .add(DustItemRegistry.CRYSTAL_BOOTS.key());

        // The elemental woods (Features/Trees): as vanilla's woods' items, except that their logs aren't in
        // logs_that_burn (that's vanilla's charcoal recipe: each wood smelts into its own charcoal instead, from its
        // dust:<wood>_logs tag). The fruit are fruit; the charcoals light torches and campfires (coals).
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            var logs = tag(DustTags.Items.logs(set.type()));
            set.logs().forEach(log -> logs.add(blockItem(log)));
            tag(ItemTags.LOGS).addTag(DustTags.Items.logs(set.type()));
            tag(ItemTags.PLANKS).add(blockItem(set.planks()));
            tag(ItemTags.WOODEN_STAIRS).add(blockItem(set.stairs()));
            tag(ItemTags.WOODEN_SLABS).add(blockItem(set.slab()));
            tag(ItemTags.WOODEN_FENCES).add(blockItem(set.fence()));
            tag(net.neoforged.neoforge.common.Tags.BlockItems.FENCES_WOODEN.item()).add(blockItem(set.fence()));
            tag(ItemTags.FENCE_GATES).add(blockItem(set.fenceGate()));
            tag(net.neoforged.neoforge.common.Tags.BlockItems.FENCE_GATES_WOODEN.item()).add(blockItem(set.fenceGate()));
            tag(ItemTags.WOODEN_BUTTONS).add(blockItem(set.button()));
            tag(ItemTags.WOODEN_PRESSURE_PLATES).add(blockItem(set.pressurePlate()));
            tag(net.neoforged.neoforge.common.Tags.BlockItems.CHESTS_WOODEN.item()).add(blockItem(set.chest()));
            tag(ItemTags.LEAVES).add(blockItem(set.leaves()));
            tag(ItemTags.SAPLINGS).add(blockItem(set.sapling()));
            tag(net.neoforged.neoforge.common.Tags.Items.FOODS_FRUIT).add(set.fruit().key());
        }
        com.mowmaster.dust.Features.Trees.ElementalWoods.CHARCOAL.values().forEach(charcoal -> tag(ItemTags.COALS).add(charcoal.key()));

        // The elemental armor sets (Features/Armor): armour of their kind (for enchanting), repaired with their element's
        // crystal blocks; Rimefrost keeps its wearer from freezing
        for (com.mowmaster.dust.Features.Armor.ElementalArmorSet set : com.mowmaster.dust.Features.Armor.ElementalArmorSet.values()) {
            tag(DustTags.Items.armorRepair(set)).add(com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCK_ITEMS.get(set.element()).key());
            var pieces = com.mowmaster.dust.Features.Armor.ElementalArmor.ITEMS.get(set);
            tag(ItemTags.HEAD_ARMOR).add(pieces.get(net.minecraft.world.item.equipment.ArmorType.HELMET).key());
            tag(ItemTags.CHEST_ARMOR).add(pieces.get(net.minecraft.world.item.equipment.ArmorType.CHESTPLATE).key());
            tag(ItemTags.LEG_ARMOR).add(pieces.get(net.minecraft.world.item.equipment.ArmorType.LEGGINGS).key());
            tag(ItemTags.FOOT_ARMOR).add(pieces.get(net.minecraft.world.item.equipment.ArmorType.BOOTS).key());
            if (set == com.mowmaster.dust.Features.Armor.ElementalArmorSet.RIMEFROST) pieces.values().forEach(piece -> tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(piece.key()));
        }
    }
}
