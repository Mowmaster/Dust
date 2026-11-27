package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.Features.Spells.pylons.PylonBlock;
import com.mowmaster.dust.Features.Spells.Client.PylonTint;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.List;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import com.mowmaster.dust.Features.AncientLanguage.Client.GlyphStoneTint;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.Machines.Client.AlchemicalDustTint;
import com.mowmaster.dust.Features.Machines.Client.CompoundTint;
import com.mowmaster.dust.Features.Machines.Client.SourceBlockTint;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;

import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

import java.util.Map;
import java.util.Optional;

public class DustProviderModel extends ModelProvider {
    public DustProviderModel(PackOutput output) {
        super(output, DustReferences.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_FIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_ICE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.JAR_LABEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_EARTH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.ANCIENT_INK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.ANCIENT_TABLET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.TRANSCRIPTION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.GRIMOIRE.get(), ModelTemplates.FLAT_ITEM);
        // The Fused Crystal: two layers (models/item/fused_crystal.json), one in each of its fusion's elements' colours
        itemModels.itemModelOutput.accept(DustItemRegistry.FUSED_CRYSTAL.get(), ItemModelUtils.tintedModel(
                Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/fused_crystal"),
                new com.mowmaster.dust.Features.Spells.Client.FusedCrystalTint(false), new com.mowmaster.dust.Features.Spells.Client.FusedCrystalTint(true)));
        // The Akashic: its hand-made 3D book (models/item/akashic.json)
        itemModels.itemModelOutput.accept(DustItemRegistry.AKASHIC.get(), ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/akashic")));
        // Modifier cards: their models (models/item/cards/card_<name>.json, flat cards)
        DustItemRegistry.CARDS.values().forEach(card -> itemModels.itemModelOutput.accept(card.get(), ItemModelUtils.plainModel(
                Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/cards/" + BuiltInRegistries.ITEM.getKey(card.get()).getPath()))));
        itemModels.generateFlatItem(DustItemRegistry.PARCHMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.RESEARCH_NOTES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SHARED_RESEARCH_GUIDE.get(), ModelTemplates.FLAT_ITEM);

        createGlyphStone(blockModels);

        // Dust composition
        itemModels.generateFlatItem(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.get(), ModelTemplates.FLAT_ITEM);
        createDustFire(blockModels);
        createElementTester(blockModels);
        // Spell Caster: vanilla's dispenser model (furnace sides and top, its own front: spell_caster_front and
        // spell_caster_front_vertical), turned by FACING
        blockModels.createDispenserBlock(DustBlockRegistry.SPELL_CASTER.get());
        createElementItems(itemModels);
        createElementCrystals(blockModels, itemModels);
        // The Crystal Display: one model (models/block/crystal/crystal_display.json) lit or not, for block and item
        Identifier display = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/crystal/crystal_display");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.CRYSTAL_DISPLAY.get(), BlockModelGenerators.plainVariant(display)));
        blockModels.itemModelOutput.accept(DustBlockRegistry.CRYSTAL_DISPLAY.asItem(), ItemModelUtils.plainModel(display));
        createResearchCrusher(blockModels, itemModels);
        createResearchSeparation(blockModels, itemModels);
        createResearchEvaporation(blockModels);
        createResearchDistillation(blockModels, itemModels);
        createElementStations(blockModels, itemModels);
        createDustStorage(blockModels);
        createDustPile(blockModels);
        createLamps(blockModels);
        createApparatus(blockModels);
        createSpellScroll(itemModels);
        createPylons(blockModels);
        createCounterPylons(blockModels);
        // The Rune modifier's glyph: one model per letter (models/block/spell/rune/rune_<a-z>.json), by its glyph
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.RUNE.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.Spells.blocks.RuneBlock.GLYPH).generate(glyph -> BlockModelGenerators.plainVariant(
                        Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/spell/rune/rune_" + (char) ('a' + glyph))))));
        // Blocks spells leave behind: their models are in models/block/spell (no items)
        for (Block block : List.of(DustBlockRegistry.POISON_POOL.get(), DustBlockRegistry.BLIGHT.get(), DustBlockRegistry.CORRUPTED.get(),
                DustBlockRegistry.SPELL_LIGHT.get(), DustBlockRegistry.VEILED.get(), DustBlockRegistry.PHASED.get(), DustBlockRegistry.WARDED_GLASS.get())) {
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(
                    Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/spell/" + BuiltInRegistries.BLOCK.getKey(block).getPath()))));
        }

        itemModels.generateFlatItem(DustItemRegistry.DUST_RED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_GREEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_BLUE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_WHITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_BLACK.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_INERT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_RED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_GREEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_BLUE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_WHITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_BLACK.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_RED.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_GREEN.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_BLUE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_WHITE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_BLACK.get());

        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.get());

        blockModels.createTrivialCube(DustBlockRegistry.INERT_CRYSTAL_ORE.get());
        blockModels.createTrivialCube(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get());
        // The primal crystal ores (block_fire_crystal_ore...): plain cubes of their own textures
        DustBlockRegistry.PRIMAL_ORES.values().forEach(ore -> blockModels.createTrivialCube(ore.get()));
        DustBlockRegistry.PRIMAL_ORES_DEEPSLATE.values().forEach(ore -> blockModels.createTrivialCube(ore.get()));

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(DustItemRegistry.CRYSTAL_SPEAR.get());
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_MATTOCK.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Armor icons with one model per vanilla trim material. Deprecated in 26.3 but still works;
        // Map.of() = no trim palette replacements.
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS, false, Map.of());

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HORSE_ARMOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        blockModels.createPlantWithDefaultItem(
                DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get(),
                DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.get(),
                BlockModelGenerators.PlantType.TINTED
        );
        // The other elemental flowers: vanilla's flower models (a cross, and the potted cross) of block/<name>, the item
        // flat from the same texture
        for (com.mowmaster.dust.Features.Flora.ElementalFlower flower : com.mowmaster.dust.Features.Flora.ElementalFlower.values()) {
            if (flower == com.mowmaster.dust.Features.Flora.ElementalFlower.WINDWHEEL) continue;
            blockModels.createPlantWithDefaultItem(com.mowmaster.dust.Features.Flora.ElementalFlowers.FLOWERS.get(flower).get(),
                    com.mowmaster.dust.Features.Flora.ElementalFlowers.POTS.get(flower).get(), BlockModelGenerators.PlantType.NOT_TINTED);
        }
        createElementalWoods(blockModels, itemModels);
        createElementalArmor(itemModels);
        createArchonItems(itemModels);
        createSanctumBlocks(blockModels, itemModels);

    }

    // The elemental armor sets' items (Features/Armor): one icon per kind of piece (item/armor/<piece>_cloth and _trim),
    // the cloth tinted the set's cloth colour and the trim its trim colour. (Worn, they're 3D: Client/ElementalArmorModels.)
    private void createElementalArmor(ItemModelGenerators itemModels)
    {
        java.util.function.Function<String, Identifier> id = path -> Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
        Map<net.minecraft.world.item.equipment.ArmorType, Identifier> icons = new java.util.EnumMap<>(net.minecraft.world.item.equipment.ArmorType.class);
        for (net.minecraft.world.item.equipment.ArmorType type : com.mowmaster.dust.Features.Armor.ElementalArmor.PIECES) {
            String piece = type.getSerializedName();
            icons.put(type, ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/armor/" + piece), TextureMapping.layered(
                    new Material(id.apply("item/armor/" + piece + "_cloth")), new Material(id.apply("item/armor/" + piece + "_trim"))), itemModels.modelOutput));
        }
        for (com.mowmaster.dust.Features.Armor.ElementalArmorSet set : com.mowmaster.dust.Features.Armor.ElementalArmorSet.values()) {
            for (net.minecraft.world.item.equipment.ArmorType type : com.mowmaster.dust.Features.Armor.ElementalArmor.PIECES) {
                itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Armor.ElementalArmor.item(set, type), ItemModelUtils.tintedModel(icons.get(type),
                        new Constant(0xFF000000 | set.clothColor), new Constant(0xFF000000 | set.trimColor)));
            }
        }
    }

    // The Archons' items (Features/Bosses), from greyscale layers in textures/item/boss tinted per set:
    //   spawn eggs   spawn_egg_base (the set's cloth colour) and spawn_egg_spots (its trim colour)
    //   loot bags    loot_bag (cloth) and loot_bag_tie (trim)
    //   sigils       sigil_frame (as it is: tinted white) and sigil_gem (its element's colour)
    // and the three legendary trinkets, each its own texture (item/<name>).
    private void createArchonItems(ItemModelGenerators itemModels)
    {
        java.util.function.Function<String, Identifier> id = path -> Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
        Identifier egg = ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/boss/spawn_egg"), TextureMapping.layered(
                new Material(id.apply("item/boss/spawn_egg_base")), new Material(id.apply("item/boss/spawn_egg_spots"))), itemModels.modelOutput);
        Identifier bag = ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/boss/loot_bag"), TextureMapping.layered(
                new Material(id.apply("item/boss/loot_bag")), new Material(id.apply("item/boss/loot_bag_tie"))), itemModels.modelOutput);
        Identifier sigil = ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/boss/sigil"), TextureMapping.layered(
                new Material(id.apply("item/boss/sigil_frame")), new Material(id.apply("item/boss/sigil_gem"))), itemModels.modelOutput);
        for (com.mowmaster.dust.Features.Armor.ElementalArmorSet set : com.mowmaster.dust.Features.Armor.ElementalArmorSet.values()) {
            Constant cloth = new Constant(0xFF000000 | set.clothColor), trim = new Constant(0xFF000000 | set.trimColor);
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Bosses.Archons.EGGS.get(set).get(), ItemModelUtils.tintedModel(egg, cloth, trim));
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Bosses.Archons.BAGS.get(set).get(), ItemModelUtils.tintedModel(bag, cloth, trim));
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Bosses.Archons.SIGILS.get(set).get(), ItemModelUtils.tintedModel(sigil,
                    new Constant(-1), new Constant(0xFF000000 | gemColor(set.element().color()))));
        }
        for (var trinket : com.mowmaster.dust.Features.Bosses.Archons.LEGENDARY) itemModels.generateFlatItem(trinket.get(), ModelTemplates.FLAT_ITEM);
    }

    // The sanctums' blocks (Features/Sanctums), their models written by hand in models/block/sanctum:
    //   the Arcane Spawner   a cage with runes (tint index 0: its element's colour); its item tinted arcane violet
    //   the Arcane Vault     a cage with a keyhole in front (north in the model, turned by FACING) and runes (tint index
    //                        0: its element's colour, dim while nobody near can open it); its item tinted arcane violet
    //   the Arcane Portal    a sheet along x or z, by its axis (tint index 0)
    //   the Sanctum Keystone (its item: the block)
    // and the Vault Keys: the gold key (item/vault_key, as it is) with its gem (item/vault_key_gem) in its element's colour
    private void createSanctumBlocks(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        java.util.function.Function<String, Identifier> id = path -> Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
        Identifier spawner = id.apply("block/sanctum/arcane_spawner");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_SPAWNER.get(),
                BlockModelGenerators.plainVariant(spawner)));
        itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_SPAWNER_ITEM.get(), ItemModelUtils.tintedModel(spawner, new Constant(0xFFB070FF)));
        Identifier vault = id.apply("block/sanctum/arcane_vault");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_VAULT.get(),
                BlockModelGenerators.plainVariant(vault)).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_VAULT_ITEM.get(), ItemModelUtils.tintedModel(vault, new Constant(0xFFB070FF)));
        Identifier key = ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/vault_key"), TextureMapping.layered(
                new Material(id.apply("item/vault_key")), new Material(id.apply("item/vault_key_gem"))), itemModels.modelOutput);
        for (com.mowmaster.dust.Features.Armor.ElementalArmorSet set : com.mowmaster.dust.Features.Armor.ElementalArmorSet.values()) {
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Sanctums.Sanctums.KEYS.get(set).get(), ItemModelUtils.tintedModel(key,
                    new Constant(-1), new Constant(0xFF000000 | gemColor(set.element().color()))));
        }
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_PORTAL.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.Sanctums.ArcanePortalBlock.AXIS).generate(axis -> BlockModelGenerators.plainVariant(
                        id.apply(axis == Direction.Axis.Z ? "block/sanctum/arcane_portal_ew" : "block/sanctum/arcane_portal_ns")))));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Sanctums.Sanctums.SANCTUM_KEYSTONE.get(),
                BlockModelGenerators.plainVariant(id.apply("block/sanctum/sanctum_keystone"))));
        itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Sanctums.Sanctums.SANCTUM_KEYSTONE_ITEM.get(),
                ItemModelUtils.plainModel(id.apply("block/sanctum/sanctum_keystone")));
        // The labyrinth's seal (tint index 0) and runes, unlit or lit (models/block/labyrinth)
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Labyrinth.LabyrinthBlocks.SEAL.get(),
                BlockModelGenerators.plainVariant(id.apply("block/labyrinth/labyrinth_seal"))));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Labyrinth.LabyrinthBlocks.RUNE.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.Labyrinth.LabyrinthRuneBlock.LIT).generate(lit -> BlockModelGenerators.plainVariant(
                        id.apply(lit ? "block/labyrinth/labyrinth_rune_lit" : "block/labyrinth/labyrinth_rune")))));
    }

    // An element's colour for a gem: dark colours lifted so the gem still reads (Chaos: deep violet)
    private static int gemColor(int rgb)
    {
        int red = (rgb >> 16) & 0xFF, green = (rgb >> 8) & 0xFF, blue = rgb & 0xFF;
        int max = Math.max(red, Math.max(green, blue));
        if (max == 0) return 0x5A3A7A;
        if (max >= 0xC0) return rgb;
        return (red * 0xE0 / max) << 16 | (green * 0xE0 / max) << 8 | (blue * 0xE0 / max);
    }

    private static final ModelTemplate VERTICAL_SLAB = dustTemplate("template_vertical_slab", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);

    // The elemental woods (Features/Trees), each from its own textures (block/<wood>_log, _log_top, stripped_<wood>_log,
    // _log_top, <wood>_planks, <wood>_leaves, <wood>_sapling; entity/chest/<wood>):
    //   logs, wood, planks, stairs, slabs, fences, gates, buttons, pressure plates: vanilla's models, as for oak
    //   vertical slabs   models/block/template_vertical_slab.json (the half against the north side) turned to its side,
    //                    or the planks when double
    //   chests           a particle-only block (the chest is drawn by Client/ElementalChestRenderer); the item is vanilla's
    //                    chest item model with the wood's chest texture
    //   leaves           vanilla's leaves model (no tint: the texture is coloured); saplings a cross, potted too
    //   fruit pods       models/block/fruit/fruit_{hanging,side}_stage<age>.json (hand-made), turned to the trunk they
    //                    hang from; tinted by Client/ElementCrystalTint. No item.
    //   fruit            two layers, item/fruit/<shape> tinted the element's colour and <shape>_stem as it is
    //   charcoal         flat items (item/<colour>_charcoal)
    private void createElementalWoods(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        java.util.function.Function<String, Identifier> id = path -> Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
        Map<String, Identifier> fruitItems = new java.util.HashMap<>();
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            com.mowmaster.dust.Features.Trees.ElementalWood wood = set.type();
            blockModels.woodProvider(set.log().get()).logWithHorizontal(set.log().get()).wood(set.wood().get());
            blockModels.woodProvider(set.strippedLog().get()).logWithHorizontal(set.strippedLog().get()).wood(set.strippedWood().get());
            Block planks = set.planks().get();
            blockModels.family(planks).generateFor(DustWoodFamilies.of(set));
            // Vertical slab
            Material planksTexture = TextureMapping.getBlockTexture(planks);
            Identifier half = VERTICAL_SLAB.create(set.verticalSlab().get(), new TextureMapping().put(TextureSlot.BOTTOM, planksTexture)
                    .put(TextureSlot.TOP, planksTexture).put(TextureSlot.SIDE, planksTexture), blockModels.modelOutput);
            MultiVariant halfVariant = BlockModelGenerators.plainVariant(half);
            MultiVariant full = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(planks));
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(set.verticalSlab().get()).with(
                    PropertyDispatch.initial(com.mowmaster.dust.Features.Trees.VerticalSlabBlock.FACING, com.mowmaster.dust.Features.Trees.VerticalSlabBlock.DOUBLE)
                            .generate((facing, isDouble) -> isDouble ? full : switch (facing) {
                                case EAST -> halfVariant.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK);
                                case SOUTH -> halfVariant.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK);
                                case WEST -> halfVariant.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK);
                                default -> halfVariant;
                            })));
            blockModels.registerSimpleItemModel(set.verticalSlab().get(), half);
            // Chest, leaves, sapling
            blockModels.createChest(set.chest().get(), planks, id.apply(wood.id), false);
            blockModels.createTrivialBlock(set.leaves().get(), net.minecraft.client.data.models.model.TexturedModel.LEAVES);
            blockModels.createPlantWithDefaultItem(set.sapling().get(), set.pottedSapling().get(), BlockModelGenerators.PlantType.NOT_TINTED);
            // Fruit pod: hanging (facing up) or on a trunk's side, by age
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(set.fruitPod().get()).with(
                    PropertyDispatch.initial(com.mowmaster.dust.Features.Trees.ElementalFruitBlock.FACING, com.mowmaster.dust.Features.Trees.ElementalFruitBlock.AGE)
                            .generate((facing, age) -> {
                                if (facing == Direction.UP) return BlockModelGenerators.plainVariant(id.apply("block/fruit/fruit_hanging_stage" + age));
                                MultiVariant side = BlockModelGenerators.plainVariant(id.apply("block/fruit/fruit_side_stage" + age));
                                return switch (facing) {
                                    case EAST -> side.with(BlockModelGenerators.Y_ROT_90);
                                    case SOUTH -> side.with(BlockModelGenerators.Y_ROT_180);
                                    case WEST -> side.with(BlockModelGenerators.Y_ROT_270);
                                    default -> side;
                                };
                            })));
            // Fruit item: its shape's two layers, the fruit in its element's colour
            String shape = wood.fruitShape();
            Identifier fruitModel = fruitItems.computeIfAbsent(shape, key -> ModelTemplates.TWO_LAYERED_ITEM.create(id.apply("item/fruit/" + key),
                    TextureMapping.layered(new Material(id.apply("item/fruit/" + key)), new Material(id.apply("item/fruit/" + key + "_stem"))), itemModels.modelOutput));
            itemModels.itemModelOutput.accept(set.fruit().get(), ItemModelUtils.tintedModel(fruitModel, new Constant(0xFF000000 | wood.element().color()), new Constant(-1)));
        }
        com.mowmaster.dust.Features.Trees.ElementalWoods.CHARCOAL.values().forEach(charcoal -> itemModels.generateFlatItem(charcoal.get(), ModelTemplates.FLAT_ITEM));
        // The Fruit Medley: models/item/fruit_medley.json (hand-made: the bowl, then three groups of fruit pieces), each
        // group in the colour of one kind of fruit in it (Client/FruitMedleyTint)
        itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Trees.ElementalWoods.MEDLEY.get(), ItemModelUtils.tintedModel(id.apply("item/fruit_medley"),
                new Constant(-1), new com.mowmaster.dust.Features.Trees.Client.FruitMedleyTint(0),
                new com.mowmaster.dust.Features.Trees.Client.FruitMedleyTint(1), new com.mowmaster.dust.Features.Trees.Client.FruitMedleyTint(2)));
    }

    // The dust fire: shaped like soul fire (a floor fire plus four sides, each picking one of a few models at random),
    // but from Dust's own templates (models/block/template_dust_fire_*.json, hand-written), which are vanilla's with
    // "tintindex": 0 on every face so MowLib's tint source colours them. Textures: block/dust_fire_0 and _1 (greyscale).
    private static final ModelTemplate DUST_FIRE_FLOOR = dustTemplate("template_dust_fire_floor", TextureSlot.FIRE);
    private static final ModelTemplate DUST_FIRE_SIDE = dustTemplate("template_dust_fire_side", TextureSlot.FIRE);
    private static final ModelTemplate DUST_FIRE_SIDE_ALT = dustTemplate("template_dust_fire_side_alt", TextureSlot.FIRE);

    private static ModelTemplate dustTemplate(String name, TextureSlot... slots)
    {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/" + name)), Optional.empty(), slots);
    }

    private void createDustFire(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.DUST_FIRE.get();
        // TextureMapping.fire0 / fire1: the block's texture with "_0" / "_1" on the end
        TextureMapping fire0 = TextureMapping.fire0(block);
        TextureMapping fire1 = TextureMapping.fire1(block);
        MultiVariant floor = BlockModelGenerators.variants(
                BlockModelGenerators.plainModel(DUST_FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor1"), fire1, blockModels.modelOutput)));
        MultiVariant sides = BlockModelGenerators.variants(
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side1"), fire1, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt1"), fire1, blockModels.modelOutput)));
        // A multipart blockstate: every part with no condition is always drawn
        blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(floor)
                .with(sides)
                .with(sides.with(BlockModelGenerators.Y_ROT_90))
                .with(sides.with(BlockModelGenerators.Y_ROT_180))
                .with(sides.with(BlockModelGenerators.Y_ROT_270)));
    }

    // Every element's crystal and dust (not the five primals, which have their own textures): one shared flat model
    // per kind, from a greyscale texture (item/element_crystal, item/element_dust), tinted with the element's colour.
    // tintedModel + Constant: an item model whose layer 0 is always drawn in that colour (0xAARRGGBB).
    private void createElementItems(ItemModelGenerators itemModels)
    {
        Identifier crystalModel = ModelTemplates.FLAT_ITEM.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_crystal"),
                TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_crystal"))), itemModels.modelOutput);
        Identifier dustModel = ModelTemplates.FLAT_ITEM.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_dust"),
                TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_dust"))), itemModels.modelOutput);
        for (DustElement element : DustElements.all()) {
            if (element.isPrimal()) continue;
            int color = 0xFF000000 | element.color();
            itemModels.itemModelOutput.accept(DustItemRegistry.CRYSTALS.get(element).get(), ItemModelUtils.tintedModel(crystalModel, new Constant(color)));
            itemModels.itemModelOutput.accept(DustItemRegistry.DUSTS.get(element).get(), ItemModelUtils.tintedModel(dustModel, new Constant(color)));
        }
    }

    // Every element's crystal blocks (Features/Crystals/ElementCrystals):
    //   crystals    models/block/crystal/crystal_small / _medium / _large.json, shared by every element and tinted with its
    //               colour (tint index 1: Client/ElementCrystalTint in the world, a Constant on the items), turned to the
    //               face they grow from as vanilla's amethyst buds are (ROTATIONS_COLUMN_WITH_FACING). Their items show the
    //               same 3D crystal (models/item/crystal/crystal_<size>.json: the block model, scaled for each display).
    //   blocks      a cube of the element's own texture (block/crystal/<element>_crystal_block); the budding crystal is the
    //               same texture with one hole in the middle (block/crystal/budding_<element>_crystal). Not tinted.
    private void createElementCrystals(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        java.util.function.Function<String, Identifier> id = path -> Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
        for (DustElement element : DustElements.all()) {
            int color = 0xFF000000 | element.color();
            String name = element.id().getPath();
            for (com.mowmaster.dust.Features.Crystals.CrystalSize size : com.mowmaster.dust.Features.Crystals.CrystalSize.values()) {
                blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(com.mowmaster.dust.Features.Crystals.ElementCrystals.node(element, size),
                                BlockModelGenerators.plainVariant(id.apply("block/crystal/crystal_" + size.getSerializedName())))
                        .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
            }
            Identifier itemSmall = id.apply("item/crystal/crystal_small"), itemMedium = id.apply("item/crystal/crystal_medium"),
                    itemLarge = id.apply("item/crystal/crystal_large");
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.SMALL_ITEMS.get(element).get(),
                    ItemModelUtils.tintedModel(itemSmall, new Constant(-1), new Constant(color)));
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.MEDIUM_ITEMS.get(element).get(),
                    ItemModelUtils.tintedModel(itemMedium, new Constant(-1), new Constant(color)));
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.LARGE_ITEMS.get(element).get(),
                    ItemModelUtils.tintedModel(itemLarge, new Constant(-1), new Constant(color)));
            Block crystalBlock = com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCKS.get(element).get();
            Block budding = com.mowmaster.dust.Features.Crystals.ElementCrystals.BUDDING.get(element).get();
            Identifier blockModel = ModelTemplates.CUBE_ALL.create(crystalBlock,
                    TextureMapping.cube(new Material(id.apply("block/crystal/" + name + "_crystal_block"))), blockModels.modelOutput);
            Identifier buddingModel = ModelTemplates.CUBE_ALL.create(budding,
                    TextureMapping.cube(new Material(id.apply("block/crystal/budding_" + name + "_crystal"))), blockModels.modelOutput);
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(crystalBlock, BlockModelGenerators.plainVariant(blockModel)));
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(budding, BlockModelGenerators.plainVariant(buddingModel)));
            itemModels.itemModelOutput.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCK_ITEMS.get(element).get(),
                    ItemModelUtils.plainModel(blockModel));
        }
    }

    // Dust storage (Features/DustStorage):
    //   the Dust Jar: one model for block and item. The block's lid is tinted by Client/DustJarTint (tint index 1); the
    //     item's by "dust:dust_jar" (Client/DustJarItemTint), with index 0 left white.
    //   the Dust Grinder: the block shows the base (dust_grinder_base.json); the top turns in Client/DustGrinderRenderer
    //     (dust_grinder_top.json). The item shows the whole thing (dust_grinder.json). One model whether powered or not.
    // The lamps: vanilla's lantern models (standing, and hanging by its chain) with each lamp's own texture
    // (block/growth_lamp), and a flat item (item/growth_lamp), just as BlockModelGenerators.createLantern makes them
    private void createLamps(BlockModelGenerators blockModels)
    {
        blockModels.createLantern(DustBlockRegistry.GROWTH_LAMP.get());
        blockModels.createLantern(DustBlockRegistry.LOVELY_LAMP.get());
    }

    private void createDustStorage(BlockModelGenerators blockModels)
    {
        Identifier jar = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/dust_jar");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_JAR.get(), BlockModelGenerators.plainVariant(jar)));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_JAR.asItem(), ItemModelUtils.tintedModel(jar,
                new Constant(-1), new com.mowmaster.dust.Features.DustStorage.Client.DustJarItemTint()));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_GRINDER.get(),
                BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/dust_grinder_base"))));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_GRINDER.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/dust_grinder")));
    }

    // The Dust Pile: one hand-written model per height, like snow layers (models/block/dust_pile/dust_pile_height2 ...
    // _height16.json: "tintindex": 0 on every face, tinted with the mix of its elements by Client/DustPileTint)
    private void createDustPile(BlockModelGenerators blockModels)
    {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_PILE.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.DustStorage.DustPileBlock.LAYERS).generate(layers -> BlockModelGenerators.plainVariant(
                        Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/dust_pile/dust_pile_height" + layers * 2)))));
    }

    // The Research Crusher: the block shows its frame and funnel (models/block/station/research_crusher_body.json); its
    // wheels turn in Client/ResearchCrusherRenderer. Turned by FACING (the model's tray faces north). The item shows the
    // whole thing (research_crusher.json, wheels and all).
    // Crushed blocks: a greyscale pebbles texture tinted with their block's colour (Client/SourceBlockTint).
    private void createResearchCrusher(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_CRUSHER.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher_body")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher")));

        Identifier crushedModel = ModelTemplates.FLAT_ITEM.create(DustItemRegistry.CRUSHED_BLOCK.get(), TextureMapping.layer0(DustItemRegistry.CRUSHED_BLOCK.get()), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.CRUSHED_BLOCK.get(), ItemModelUtils.tintedModel(crushedModel, new SourceBlockTint()));
    }

    // The Alchemical Separation Table: research_separation.json for both the block and the item (the glyph that rises
    // off the scroll while separating is drawn by Client/ResearchSeparationRenderer). Turned by FACING (the outputs
    // face the player).
    // Alchemical compounds: paper (layer 0, as it is) with a dust pile (layer 1) tinted the compound's colour.
    private void createResearchSeparation(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_SEPARATION.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_separation")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_separation")));

        Identifier compoundModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_COMPOUND.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_paper")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_dust"))),
                itemModels.modelOutput);
        // One tint per layer: the paper as it is (constant white), the pile in the compound's colour
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_COMPOUND.get(),
                ItemModelUtils.tintedModel(compoundModel, new Constant(-1), new CompoundTint()));
        itemModels.generateFlatItem(DustItemRegistry.ALCHEMICAL_SOOT.get(), ModelTemplates.FLAT_ITEM);
    }

    // The Evaporation Table: the block shows everything but the spoon (research_evaporation_body.json), which stirs in
    // Client/ResearchEvaporationRenderer. Turned by FACING (the output faces the player). The item shows the whole thing.
    private void createResearchEvaporation(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.RESEARCH_EVAPORATION.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_evaporation_body")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_evaporation")));
    }

    // The Distillation Table: nothing in the model moves (the glyph, liquid and items are drawn by
    // Client/ResearchDistillationRenderer), so the block and the item both use the whole model. Turned by FACING.
    // Its compounds: a gas in a bottle (vanilla's potion: the liquid overlay, layer 0, tinted the compound's colour; the
    // bottle, layer 1, as it is) and a liquid in a bowl (the bowl as it is, layer 0; its contents tinted, layer 1).
    private void createResearchDistillation(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_DISTILLATION.get();
        Identifier model = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_distalation");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));

        Identifier gasModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_GAS.get(),
                TextureMapping.layered(new Material(Identifier.withDefaultNamespace("item/potion_overlay")),
                        new Material(Identifier.withDefaultNamespace("item/potion"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_GAS.get(),
                ItemModelUtils.tintedModel(gasModel, new CompoundTint(), new Constant(-1)));

        Identifier liquidModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_LIQUID.get(),
                TextureMapping.layered(new Material(Identifier.withDefaultNamespace("item/bowl")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_bowl_contents"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_LIQUID.get(),
                ItemModelUtils.tintedModel(liquidModel, new Constant(-1), new CompoundTint()));
    }

    // The Transformation and Combination Tables: nothing in the model moves (the glyphs and items are drawn by their
    // renderers), so block and item use the whole model. The Mixing Table's block leaves out the spoon
    // (research_mixing_body.json), which stirs in Client/ResearchMixingRenderer; its item shows the whole thing.
    // Alchemical Dust: the heap (layer 0) tinted the element's colour, its sparkles (layer 1) as they are.
    private void createElementStations(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        station(blockModels, DustBlockRegistry.RESEARCH_TRANSFORMATION.get(), "research_transformational", "research_transformational");
        station(blockModels, DustBlockRegistry.RESEARCH_COMBINATION.get(), "research_combination", "research_combination");
        // The Element Crafter: as a station, but the item shows its dye colour on the slabs and posts (tint indices 0 and 1;
        // "minecraft:dye" reads the item's dyed_color, -1 = white undyed)
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.ELEMENT_CRAFTER.get(),
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_crafter")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(DustBlockRegistry.ELEMENT_CRAFTER.asItem(),
                ItemModelUtils.tintedModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_crafter"),
                        new net.minecraft.client.color.item.Dye(-1), new net.minecraft.client.color.item.Dye(-1)));
        // The Element Replicator: the same way (tint indices 0 the slabs, rails and sigils, 1 the posts); its glyphs and
        // items are drawn by Client/ElementReplicatorRenderer
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.ELEMENT_REPLICATOR.get(),
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_replicator")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(DustBlockRegistry.ELEMENT_REPLICATOR.asItem(),
                ItemModelUtils.tintedModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_replicator"),
                        new net.minecraft.client.color.item.Dye(-1), new net.minecraft.client.color.item.Dye(-1)));
        station(blockModels, DustBlockRegistry.RESEARCH_MIXING.get(), "research_mixing_body", "research_mixing");

        Identifier dustModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_DUST.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_dust")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_dust_sparkle"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_DUST.get(),
                ItemModelUtils.tintedModel(dustModel, new AlchemicalDustTint(), new Constant(-1)));
    }

    // A Spell Scroll: the parchment (layer 0) as it is, its seal (layer 1) in the spell's element colour
    private void createSpellScroll(ItemModelGenerators itemModels)
    {
        Identifier model = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.SPELL_SCROLL.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/spell_scroll")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/spell_scroll_seal"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.SPELL_SCROLL.get(),
                ItemModelUtils.tintedModel(model, new Constant(-1), new com.mowmaster.dust.Features.Spells.Client.SpellTint()));
    }

    // The spell pylons (models/block/pylon):
    //   Spell Pylon     spell_pylon, turned by facing (its connection arm points north in the model)
    //   Shape Pylon     shape_pylon / _1 / _2 / _3 by links (PylonLinks.shapeModel), turned by parent
    //   the others      blank_pylon_<links>_level_<nodes> (PylonLinks.blankModel), turned by parent; all four kinds
    //                   share the models and differ by tint (Client/PylonTint). A spell break: pylon_break_broken,
    //                   or pylon_break_connected while powered.
    // The pylon models have their parent's arm to the south, so parent=south needs no turn, west a quarter turn...
    // Items: the plain models, the blank pylons' with the body in their kind's colour.
    private void createPylons(BlockModelGenerators blockModels)
    {
        Block spellPylon = DustBlockRegistry.SPELL_PYLON.get();
        Identifier spellModel = pylonModel("spell_pylon");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(spellPylon, BlockModelGenerators.plainVariant(spellModel))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(spellPylon.asItem(), ItemModelUtils.plainModel(spellModel));

        Block shapePylon = DustBlockRegistry.SHAPE_PYLON.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(shapePylon).with(
                PropertyDispatch.initial(PylonBlock.PARENT, PylonBlock.LINKS, PylonBlock.NODES, PylonBlock.BREAK).generate((parent, links, nodes, spellBreak) ->
                        BlockModelGenerators.plainVariant(pylonModel("shape_pylon" + links.shapeModel())).with(parentTurn(parent)))));
        blockModels.itemModelOutput.accept(shapePylon.asItem(), ItemModelUtils.plainModel(pylonModel("shape_pylon")));

        for (Block block : List.of(DustBlockRegistry.NEUTRAL_PYLON.get(), DustBlockRegistry.THRIFTY_PYLON.get(),
                DustBlockRegistry.GIANT_PYLON.get(), DustBlockRegistry.DAMAGE_PYLON.get(), DustBlockRegistry.EXPERIENCED_PYLON.get())) {
            // A spell break has its own two models (unpowered: broken, powered: connected) whatever its links and nodes
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatch.initial(PylonBlock.PARENT, PylonBlock.LINKS, PylonBlock.NODES, PylonBlock.BREAK).generate((parent, links, nodes, spellBreak) ->
                            BlockModelGenerators.plainVariant(pylonModel(switch (spellBreak) {
                                case BROKEN -> "pylon_break_broken";
                                case CONNECTED -> "pylon_break_connected";
                                // A Fusion / Spell Stack pylon (pylon_fusionstack_base, _1 ... _7, by links like the blank ones)
                                case FUSION -> "pylon_fusionstack_" + links.blankModel();
                                case NONE -> "blank_pylon_" + links.blankModel() + "_level_" + nodes;
                            })).with(parentTurn(parent)))));
            // Tint 0 (the body) in the kind's colour; the rest have no tint source, so they stay plain
            blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(pylonModel("blank_pylon_base_level_3"),
                    ItemModelUtils.constantTint(PylonTint.kindColor(((PylonBlock) block).type()))));
        }
    }

    // The Blessed and Corrupted Pylons: one model each, turned by facing (the angel / devil faces north in the model),
    // whatever the count (the nodes are tints: Client/CounterPylonTint). Items: the model with its count at 0.
    private void createCounterPylons(BlockModelGenerators blockModels)
    {
        for (Block block : List.of(DustBlockRegistry.PYLON_BLESSED.get(), DustBlockRegistry.PYLON_CORRUPTED.get())) {
            boolean blessed = ((com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock) block).blessed();
            Identifier model = pylonModel(blessed ? "pylon_blessed" : "pylon_corrupted");
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatch.initial(com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock.FACING, com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock.LEVEL)
                            .generate((facing, level) -> BlockModelGenerators.plainVariant(model).with(switch (facing) {
                                case EAST -> BlockModelGenerators.Y_ROT_90;
                                case SOUTH -> BlockModelGenerators.Y_ROT_180;
                                case WEST -> BlockModelGenerators.Y_ROT_270;
                                default -> BlockModelGenerators.NOP;
                            }))));
            List<net.minecraft.client.color.item.ItemTintSource> tints = new java.util.ArrayList<>();
            for (int i = 0; i <= com.mowmaster.dust.Features.Spells.Client.CounterPylonTint.MAX_INDEX; i++) {
                tints.add(ItemModelUtils.constantTint(com.mowmaster.dust.Features.Spells.Client.CounterPylonTint.colorOf(blessed, i, 0)));
            }
            blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(model, tints.toArray(new net.minecraft.client.color.item.ItemTintSource[0])));
        }
    }

    private static Identifier pylonModel(String name)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/pylon/" + name);
    }

    // The turn for a pylon whose parent is on this side (its model's back arm points south)
    private static VariantMutator parentTurn(Direction parent)
    {
        return switch (parent) {
            case WEST -> BlockModelGenerators.Y_ROT_90;
            case NORTH -> BlockModelGenerators.Y_ROT_180;
            case EAST -> BlockModelGenerators.Y_ROT_270;
            default -> BlockModelGenerators.NOP;
        };
    }

    // A station turned by FACING (the models face north), with its block and item models
    // The alchemical apparatus (Features/Apparatus): one hand-made model each (models/block/apparatus/<name>.json, made
    // with the scratch generator: copper, brass and wrought iron on a blackstone plinth), turned to face the way it was
    // placed; the item is the same model. Their moving parts are standalone models drawn by Client/ApparatusRenderer.
    // The Glass Dust Tube: a multipart blockstate (every part whose condition holds is drawn): the glass middle always,
    // and an arm (dust_tube_arm.json, made pointing north) turned toward each side joined to a tube, a longer one
    // (dust_tube_reach.json) toward each side joined to a jar or apparatus; the item is
    // a straight run (dust_tube_item.json).
    private void createApparatus(BlockModelGenerators blockModels)
    {
        for (Block block : List.of(DustBlockRegistry.CRUSHING_MILL.get(), DustBlockRegistry.ALCHEMICAL_SEPARATOR.get(), DustBlockRegistry.EVAPORATING_KETTLE.get(),
                DustBlockRegistry.STILL_TOWER.get(), DustBlockRegistry.STILL_TOP.get(), DustBlockRegistry.TRANSMUTER.get(), DustBlockRegistry.CONJOINING_CRUCIBLE.get(), DustBlockRegistry.MIXING_VAT.get(),
                DustBlockRegistry.DISSOLVING_RETORT.get())) {
            Identifier model = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/" + BuiltInRegistries.BLOCK.getKey(block).getPath());
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
                    .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
            blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
        }
        // The Still Pot: a multipart blockstate, the pot turned by facing, and a brass port toward each hopper feeding it
        // (still_pot_port.json made pointing north, turned to its side; still_pot_port_up.json for a hopper above)
        Block pot = DustBlockRegistry.STILL_POT.get();
        Identifier potModel = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/still_pot");
        MultiVariant potBody = BlockModelGenerators.plainVariant(potModel);
        MultiVariant port = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/still_pot_port"));
        MultiVariant portUp = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/still_pot_port_up"));
        Map<Direction, VariantMutator> faced = Map.of(
                Direction.NORTH, BlockModelGenerators.NOP, Direction.EAST, BlockModelGenerators.Y_ROT_90,
                Direction.SOUTH, BlockModelGenerators.Y_ROT_180, Direction.WEST, BlockModelGenerators.Y_ROT_270);
        MultiPartGenerator potParts = MultiPartGenerator.multiPart(pot);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            potParts.with(BlockModelGenerators.condition(com.mowmaster.dust.Features.Machines.StationBlock.FACING, facing), potBody.with(faced.get(facing)));
            potParts.with(BlockModelGenerators.condition(com.mowmaster.dust.Features.Apparatus.still.StillPotBlock.hopperProperty(facing), true), port.with(faced.get(facing)));
        }
        potParts.with(BlockModelGenerators.condition(com.mowmaster.dust.Features.Apparatus.still.StillPotBlock.HOPPER_UP, true), portUp);
        blockModels.blockStateOutput.accept(potParts);
        blockModels.itemModelOutput.accept(pot.asItem(), ItemModelUtils.plainModel(potModel));
        // The Dust Chute (made facing south: its back on the north edge), plain or filtered (dust_chute_filtered.json: a
        // brass plaque for the element symbol, drawn by Client/DustChuteRenderer), turned by facing
        Identifier chute = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_chute");
        Identifier chuteFiltered = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_chute_filtered");
        Map<Direction, VariantMutator> fromSouth = Map.of(
                Direction.SOUTH, BlockModelGenerators.NOP, Direction.WEST, BlockModelGenerators.Y_ROT_90,
                Direction.NORTH, BlockModelGenerators.Y_ROT_180, Direction.EAST, BlockModelGenerators.Y_ROT_270);
        // Sat back INSET pixels to meet the block it's fixed to: dust_chute(_filtered)_inset<n>.json, the same moved back
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_CHUTE.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.Apparatus.still.DustChuteBlock.FACING, com.mowmaster.dust.Features.Apparatus.still.DustChuteBlock.FILTERED,
                                com.mowmaster.dust.Features.Apparatus.still.DustChuteBlock.INSET)
                        .generate((facing, filtered, inset) -> BlockModelGenerators.plainVariant(inset == 0 ? (filtered ? chuteFiltered : chute)
                                : Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_chute" + (filtered ? "_filtered" : "") + "_inset" + inset))
                                .with(fromSouth.get(facing)))));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_CHUTE.asItem(), ItemModelUtils.plainModel(chute));
        // The Dust Separator and its extension: the block draws the base (the drum turns, drawn by
        // separator/Client/DrumRendering), turned by facing; the item is the whole thing (dust_seperator.json)
        for (Block block : List.of(DustBlockRegistry.DUST_SEPARATOR.get(), DustBlockRegistry.DUST_SEPARATOR_EXTENSION.get())) {
            String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                    BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/" + name + "_base")))
                    .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
            blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/" + name)));
        }
        // The Dust Mixer (made with its front to the south): the block draws its frame (the bowl, grate and paddles move:
        // mixer/Client/DustMixerRenderer); the item is the whole thing
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_MIXER.get()).with(
                PropertyDispatch.initial(com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlock.FACING).generate(facing ->
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_base")).with(fromSouth.get(facing)))));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_MIXER.asItem(), ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer")));
        // The Wet Dust Mixer and the Dust Centrifuge: the block draws the part that stays still (dust_mixer_wet_base.json,
        // dust_centrifuge_base.json: the moving parts are drawn by their renderers), turned by facing; the item is the
        // whole thing. The Dust Cyclone (dust_cyclone_top.json) turned by facing; its hopper the same every way round.
        for (String name : List.of("dust_mixer_wet", "dust_centrifuge")) {
            Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(DustReferences.MODID, name));
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                    BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/" + name + "_base")))
                    .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
            blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/" + name)));
        }
        Identifier cyclone = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_cyclone_top");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_CYCLONE.get(), BlockModelGenerators.plainVariant(cyclone))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_CYCLONE.asItem(), ItemModelUtils.plainModel(cyclone));
        Identifier cycloneHopper = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_cyclone_hopper");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_CYCLONE_HOPPER.get(), BlockModelGenerators.plainVariant(cycloneHopper)));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_CYCLONE_HOPPER.asItem(), ItemModelUtils.plainModel(cycloneHopper));
        // The Dust Collector: one model, the same every way round
        Identifier collector = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_collector");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.DUST_COLLECTOR.get(), BlockModelGenerators.plainVariant(collector)));
        blockModels.itemModelOutput.accept(DustBlockRegistry.DUST_COLLECTOR.asItem(), ItemModelUtils.plainModel(collector));
        // The Still's Burnable Firebox: one model lit or not (LIT gives the light and flames), turned by facing
        Identifier firebox = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/firebox_burnable");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DustBlockRegistry.STILL_FIREBOX.get(), BlockModelGenerators.plainVariant(firebox))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(DustBlockRegistry.STILL_FIREBOX.get().asItem(), ItemModelUtils.plainModel(firebox));
        Block tube = DustBlockRegistry.DUST_TUBE.get();
        MultiVariant core = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_core"));
        MultiVariant arm = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_arm"));
        // A longer arm toward a block that takes tubes (it reaches into that block's space to meet its body)
        MultiVariant reach = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_reach"));
        // A shorter one for blocks whose body sits nearer their edge (the Still Tower: TubeJoin.NEAR)
        MultiVariant reachNear = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_reach_near"));
        MultiPartGenerator parts = MultiPartGenerator.multiPart(tube).with(core);
        Map<Direction, VariantMutator> turns = Map.of(
                Direction.NORTH, BlockModelGenerators.NOP, Direction.EAST, BlockModelGenerators.Y_ROT_90, Direction.SOUTH, BlockModelGenerators.Y_ROT_180,
                Direction.WEST, BlockModelGenerators.Y_ROT_270, Direction.UP, BlockModelGenerators.X_ROT_270, Direction.DOWN, BlockModelGenerators.X_ROT_90);
        // Filled (dust running through): a tinted core of flowing dust inside the glass, in the middle and down each arm
        // (models/block/apparatus/dust_tube_fill_*.json, hand-written; texture block/apparatus/tube_dust, animated)
        var filled = com.mowmaster.dust.Features.DustStorage.DustTubeBlock.FILLED;
        MultiVariant fillCore = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_fill_core"));
        MultiVariant fillArm = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_fill_arm"));
        MultiVariant fillReach = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_fill_reach"));
        MultiVariant fillReachNear = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_fill_reach_near"));
        parts.with(BlockModelGenerators.condition(filled, true), fillCore);
        // A pulling end (redstone on it): the reach arm, with a red band by its collar (dust_tube_redstone.json)
        MultiVariant redstone = BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_redstone"));
        for (Direction side : Direction.values()) {
            var property = com.mowmaster.dust.Features.DustStorage.DustTubeBlock.CONNECTIONS.get(side);
            parts.with(BlockModelGenerators.condition(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.TUBE), arm.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.BLOCK, com.mowmaster.dust.Features.DustStorage.TubeJoin.PULL), reach.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.PULL), redstone.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.NEAR), reachNear.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition().term(filled, true).term(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.TUBE), fillArm.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition().term(filled, true).term(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.BLOCK, com.mowmaster.dust.Features.DustStorage.TubeJoin.PULL), fillReach.with(turns.get(side)));
            parts.with(BlockModelGenerators.condition().term(filled, true).term(property, com.mowmaster.dust.Features.DustStorage.TubeJoin.NEAR), fillReachNear.with(turns.get(side)));
        }
        blockModels.blockStateOutput.accept(parts);
        blockModels.itemModelOutput.accept(tube.asItem(), ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_tube_item")));
    }

    private void station(BlockModelGenerators blockModels, Block block, String blockModel, String itemModel)
    {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/" + blockModel)))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/" + itemModel)));
    }

    // The Element Tester: one hand-made model per number of crystals (models/block/station/element_tester_<n>.json),
    // turned to face the way it was placed (ROTATION_HORIZONTAL_FACING: the models face north). The item shows the bare
    // workstation.
    private static final String[] TESTER_MODELS = {"default", "one", "two", "three", "four", "five"};

    private void createElementTester(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.ELEMENT_TESTER.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(ElementTesterBlock.CRYSTALS).generate(count -> BlockModelGenerators.plainVariant(testerModel(count))))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(testerModel(0)));
    }

    private static Identifier testerModel(int crystals)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_tester_" + TESTER_MODELS[crystals]);
    }

    // The glyph stone's own model shape (models/block/glyph_stone_template.json, hand-written): a stone cube plus the
    // glyph as an overlay on the front with tint index 0, so Client/GlyphStoneTint can colour it per player.
    private static final TextureSlot GLYPH_SLOT = TextureSlot.create("glyph");
    private static final ModelTemplate GLYPH_STONE_TEMPLATE = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone_template")), Optional.empty(),
            TextureSlot.FRONT, TextureSlot.SIDE, TextureSlot.TOP, GLYPH_SLOT);

    // One model per glyph, picked by the GLYPH property and turned by FACING. The item shows the 'a' glyph, tinted
    // with the carved colour (item models need their own tint, the block tint isn't used for items).
    private void createGlyphStone(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.GLYPH_STONE.get();
        Material front = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_front"));
        Material side = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_side"));
        Material top = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_top"));
        Identifier[] models = new Identifier[AncientLanguage.GLYPH_COUNT];
        for (int i = 0; i < models.length; i++) {
            String letter = String.valueOf((char) ('a' + i));
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.FRONT, front)
                    .put(TextureSlot.SIDE, side)
                    .put(TextureSlot.TOP, top)
                    .put(GLYPH_SLOT, new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_" + letter)));
            models[i] = GLYPH_STONE_TEMPLATE.createWithSuffix(block, "_" + letter, textures, blockModels.modelOutput);
        }
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(GlyphStoneBlock.GLYPH).generate(glyph -> BlockModelGenerators.plainVariant(models[glyph])))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(models[0], new Constant(GlyphStoneTint.CARVED)));
    }
}