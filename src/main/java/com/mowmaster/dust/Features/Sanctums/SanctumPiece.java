package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustRegistries.DustFeatureRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// One sanctum: a structure template (SanctumTemplates) placed turned some way, its stand-in blocks swapped for its
// element's (SanctumProcessor; structure blocks aren't placed). Minecraft builds it a chunk at a time: the template's
// blocks in that chunk (TemplateStructurePiece), its data markers emptied (handleDataMarker), and then, in the same
// chunk, the ground layer shored up where the ground dips, empty chests and barrels filled with the element's loot,
// and the keystone given its spawners, its vaults, its portal and the return point. Saved as the template's name, where it is, its
// turn and its element, so every chunk builds the same sanctum.
public class SanctumPiece extends TemplateStructurePiece
{
    // How far down the ground layer is shored up
    private static final int FOUNDATION = 8;

    private final ElementalArmorSet set;

    public SanctumPiece(StructureTemplateManager templates, Identifier template, BlockPos position, Rotation rotation, ElementalArmorSet set)
    {
        super(DustFeatureRegistry.SANCTUM_PIECE.get(), 0, templates, template, template.toString(), settings(rotation, set), position);
        this.set = set;
    }

    public SanctumPiece(StructurePieceSerializationContext context, CompoundTag tag)
    {
        super(DustFeatureRegistry.SANCTUM_PIECE.get(), tag, context.structureTemplateManager(), template -> settings(rotation(tag), set(tag)));
        this.set = set(tag);
        TemplateScan.fixLegacy(makeTemplateLocation(), template);
    }

    // Turned; structure blocks left out; stand-ins swapped; blocks placed where water was aren't waterlogged (only what
    // the template has)
    private static StructurePlaceSettings settings(Rotation rotation, ElementalArmorSet set)
    {
        return new StructurePlaceSettings().setRotation(rotation).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new SanctumProcessor(set));
    }

    private static Rotation rotation(CompoundTag tag)
    {
        String name = tag.getStringOr("rotation", "");
        for (Rotation rotation : Rotation.values()) if (rotation.name().equals(name)) return rotation;
        return Rotation.NONE;
    }

    private static ElementalArmorSet set(CompoundTag tag)
    {
        String id = tag.getStringOr("set", "");
        for (ElementalArmorSet set : ElementalArmorSet.values()) if (set.id.equals(id)) return set;
        return ElementalArmorSet.PYREBORN;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
    {
        super.addAdditionalSaveData(context, tag);
        tag.putString("rotation", placeSettings.getRotation().name());
        tag.putString("set", set.id);
    }

    // Data markers only tell the code where things are (SanctumTemplates); where one stood is emptied
    @Override
    protected void handleDataMarker(String marker, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBox)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Build.FLAGS);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random, BoundingBox chunkBox,
                            ChunkPos chunkPos, BlockPos pivot)
    {
        TemplateScan.fixLegacy(makeTemplateLocation(), template);
        super.postProcess(level, structures, generator, random, chunkBox, chunkPos, pivot);
        SanctumTemplates.Layout layout = SanctumTemplates.layout(makeTemplateLocation(), template);
        shoreUp(level, layout, chunkBox, random);
        fillContainers(level, layout, chunkBox, random);
        setUpKeystone(level, layout, chunkBox);
    }

    // A position in the template, in the world
    private BlockPos world(BlockPos local)
    {
        return StructureTemplate.calculateRelativePosition(placeSettings, local).offset(templatePosition);
    }

    // Under the ground layer, wherever the ground dips, walls down to the ground (up to FOUNDATION blocks), so nothing
    // hangs over a hollow; never into the template's own blocks or rooms
    private void shoreUp(WorldGenLevel level, SanctumTemplates.Layout layout, BoundingBox chunkBox, RandomSource random)
    {
        SanctumPalette palette = SanctumPalette.of(set);
        for (BlockPos local : layout.ground()) {
            BlockPos top = world(local);
            if (top.getX() < chunkBox.minX() || top.getX() > chunkBox.maxX() || top.getZ() < chunkBox.minZ() || top.getZ() > chunkBox.maxZ()) continue;
            for (int down = 1; down <= FOUNDATION; down++) {
                if (layout.filled().contains(local.below(down))) break;
                BlockPos below = top.below(down);
                if (!chunkBox.isInside(below)) break;
                BlockState there = level.getBlockState(below);
                if (!there.canBeReplaced() && there.getFluidState().isEmpty()) break;
                level.setBlock(below, palette.wall(random), Build.FLAGS);
            }
        }
    }

    // Chests and barrels the template left empty (no items, no loot table) get the element's loot
    private void fillContainers(WorldGenLevel level, SanctumTemplates.Layout layout, BoundingBox chunkBox, RandomSource random)
    {
        for (BlockPos local : layout.containers()) {
            BlockPos pos = world(local);
            if (!chunkBox.isInside(pos)) continue;
            BlockEntity entity = level.getBlockEntity(pos);
            if (!(entity instanceof ChestBlockEntity) && !(entity instanceof BarrelBlockEntity)) continue;
            RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity) entity;
            if (container.getLootTable() == null && container.isEmpty()) loot(container, random);
        }
    }

    // The element's dust and crystals, its tree's fruit (and maybe a sapling), now and then a loot bag
    private void loot(Container container, RandomSource random)
    {
        int size = container.getContainerSize();
        DustElement element = set.element();
        container.setItem(random.nextInt(size), new ItemStack(DustItemRegistry.DUSTS.get(element).get(), 8 + random.nextInt(17)));
        container.setItem(random.nextInt(size), new ItemStack(DustItemRegistry.CRYSTALS.get(element).get(), 2 + random.nextInt(4)));
        for (ElementalWood type : ElementalWood.values()) {
            if (!type.element().equals(element)) continue;
            WoodSet wood = ElementalWoods.set(type);
            container.setItem(random.nextInt(size), new ItemStack(wood.fruit().get(), 2 + random.nextInt(3)));
            if (random.nextBoolean()) container.setItem(random.nextInt(size), new ItemStack(wood.sapling().get()));
        }
        if (random.nextInt(10) == 0) container.setItem(random.nextInt(size), LootBags.bag(set, 1));
    }

    // The keystone, in the chunk that holds it: where its spawners and vaults are, its portal's opening (the corner
    // nearest the start of the axis it runs along: the keystone fills it towards east or south) and the return point,
    // facing away
    private void setUpKeystone(WorldGenLevel level, SanctumTemplates.Layout layout, BoundingBox chunkBox)
    {
        SanctumTemplates.Portal portal = layout.portal();
        if (portal == null) return;
        BlockPos at = world(portal.keystone());
        if (!chunkBox.isInside(at) || !(level.getBlockEntity(at) instanceof SanctumKeystoneBlockEntity keystone)) return;
        List<BlockPos> spawners = new ArrayList<>(), vaults = new ArrayList<>();
        for (BlockPos local : layout.spawners()) spawners.add(world(local));
        for (BlockPos local : layout.vaults()) vaults.add(world(local));
        Rotation rotation = placeSettings.getRotation();
        Direction.Axis axis = rotation.rotate(Direction.fromAxisAndDirection(portal.axis(), Direction.AxisDirection.POSITIVE)).getAxis();
        BlockPos first = world(portal.first()), second = world(portal.second());
        BlockPos corner = first.get(axis) <= second.get(axis) ? first : second;
        Direction front = rotation.rotate(portal.front());
        keystone.setup(set, spawners, vaults, corner, axis, Vec3.atBottomCenterOf(world(portal.returnPos())), front.toYRot());
    }
}
