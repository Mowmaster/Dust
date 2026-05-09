package com.mowmaster.dust.Features.CrystalTools.Item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CrystalToolMattock extends Item {

    protected static final Map<Block, Block> STRIPPABLES;
    protected static final Map<Block, BlockState> FLATTENABLES;
    protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES;
    public CrystalToolMattock(ToolMaterial toolMaterial, float attackDamageBaseline, float attackSpeedBaseline, Properties properties) {
        super(properties.tool(toolMaterial, DustTags.Blocks.MATTOCK_MINEABLE, attackDamageBaseline, attackSpeedBaseline, 1f));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();

        if (playerHasBlockingItemUseIntent(context)) {
            return InteractionResult.PASS;
        }else {
            Optional<BlockState> optional = this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context);
            if (optional.isEmpty()) {
                BlockState blockstate = level.getBlockState(blockpos);
                if (context.getClickedFace() == Direction.DOWN) {
                    return InteractionResult.PASS;
                } else {
                    BlockState blockstate1 = FLATTENABLES.get(blockstate.getBlock());
                    BlockState blockstate2 = null;
                    if (blockstate1 != null && level.getBlockState(blockpos.above()).isAir()) {
                        level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                        blockstate2 = blockstate1;
                    } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                        if (!level.isClientSide()) {
                            level.levelEvent(null, 1009, blockpos, 0);
                        }

                        CampfireBlock.dowse(context.getPlayer(), level, blockpos, blockstate);
                        blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
                    }

                    if (blockstate2 != null) {
                        if (!level.isClientSide()) {
                            level.setBlock(blockpos, blockstate2, 11);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                            if (player != null) {
                                context.getItemInHand().hurtAndBreak(1, player, context.getHand());
                            }
                        }

                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.PASS;
                    }
                }
            } else {
                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                }

                level.setBlock(blockpos, optional.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, optional.get()));
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, context.getHand());
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    public InteractionResult useOnAxe(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (playerHasBlockingItemUseIntent(context)) {
            return InteractionResult.PASS;
        }
        else {
            Optional<BlockState> newBlock = this.evaluateNewBlockState(level, pos, player, level.getBlockState(pos), context);
            if (newBlock.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                ItemStack itemInHand = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemInHand);
                }

                level.setBlock(pos, (BlockState)newBlock.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, (BlockState)newBlock.get()));
                if (player != null) {
                    itemInHand.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    public InteractionResult useOnShovel(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Player player = context.getPlayer();
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }
        else {
            BlockState newState = blockState.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
            BlockState updatedState = null;
            if (newState != null && level.getBlockState(pos.above()).isAir()) {
                level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                updatedState = newState;
            } else if ((updatedState = blockState.getToolModifiedState(context, ItemAbilities.SHOVEL_DOUSE, false)) != null && !level.isClientSide()) {
                level.levelEvent((Entity)null, 1009, pos, 0);
            }

            if (updatedState != null) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, updatedState, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, updatedState));
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public InteractionResult useOnHoe(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = toolModifiedState == null ? null : Pair.of((Predicate)(ctx) -> true, changeIntoState(toolModifiedState));
        if (logicPair == null) {
            return InteractionResult.PASS;
        } else {
            Predicate<UseOnContext> predicate = (Predicate)logicPair.getFirst();
            Consumer<UseOnContext> action = (Consumer)logicPair.getSecond();
            if (predicate.test(context)) {
                Player player = context.getPlayer();
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    action.accept(context);
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    //Have offhand shield override stipping method basically
    private static boolean playerHasBlockingItemUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && !player.isSecondaryUseActive();
    }

    //determines what action the tool does basically
    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, @Nullable Player player, BlockState oldState, UseOnContext context) {
        Optional<BlockState> strippedBlock = Optional.ofNullable(oldState.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false));
        if (strippedBlock.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return strippedBlock;
        } else {
            Optional<BlockState> scrapedBlock = Optional.ofNullable(oldState.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false));
            if (scrapedBlock.isPresent()) {
                spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_SCRAPE, 3005);
                return scrapedBlock;
            } else {
                Optional<BlockState> waxoffBlock = Optional.ofNullable(oldState.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false));
                if (waxoffBlock.isPresent()) {
                    spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_WAX_OFF, 3004);
                    return waxoffBlock;
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private static void spawnSoundAndParticle(Level level, BlockPos pos, @Nullable Player player, BlockState oldState, SoundEvent soundEvent, int particle) {
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.levelEvent(player, particle, pos, 0);
        if (oldState.getBlock() instanceof ChestBlock && oldState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of(player, level.getBlockState(neighborPos)));
            level.levelEvent(player, particle, neighborPos, 0);
        }

    }

    public static @Nullable BlockState getAxeStrippingState(BlockState originalState) {
        Strippable strippable = (Strippable)originalState.getBlock().builtInRegistryHolder().getData(NeoForgeDataMaps.STRIPPABLES);
        if (strippable != null) {
            return strippable.strippedBlock().withPropertiesOf(originalState);
        } else {
            Block block = (Block)STRIPPABLES.get(originalState.getBlock());
            return block != null ? (BlockState)block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, (Direction.Axis)originalState.getValue(RotatedPillarBlock.AXIS)) : null;
        }
    }

    private Optional<BlockState> getStripped(BlockState state) {
        Strippable strippable = state.getBlock().builtInRegistryHolder().getData(NeoForgeDataMaps.STRIPPABLES);
        return strippable != null ? Optional.of(strippable.strippedBlock().withPropertiesOf(state)) : Optional.ofNullable((Block)STRIPPABLES.get(state.getBlock())).map((block) -> (BlockState)block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, (Direction.Axis)state.getValue(RotatedPillarBlock.AXIS)));
    }

    public static @Nullable BlockState getShovelPathingState(BlockState originalState) {
        return (BlockState)FLATTENABLES.get(originalState.getBlock());
    }

    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        /*
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility) ||
                ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility) ||
                ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility);
         */
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility) ||
                ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }

    public static Consumer<UseOnContext> changeIntoState(BlockState state) {
        return (context) -> {
            context.getLevel().setBlock(context.getClickedPos(), state, 11);
            context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), state));
        };
    }

    public static Consumer<UseOnContext> changeIntoStateAndDropItem(BlockState state, ItemLike item) {
        return (context) -> {
            context.getLevel().setBlock(context.getClickedPos(), state, 11);
            context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), state));
            Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(item));
        };
    }

    public static boolean onlyIfAirAbove(UseOnContext context) {
        return context.getClickedFace() != Direction.DOWN && context.getLevel().getBlockState(context.getClickedPos().above()).isAir();
    }

    //This is a backup list incase the NeoForgeDataMap doesnt exist for some reason
    static {
        STRIPPABLES = (new ImmutableMap.Builder()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_WOOD).put(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD).put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK).build();
    }
    static {
        FLATTENABLES = Maps.newHashMap((new ImmutableMap.Builder()).put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState()).build());
    }
    static {
        TILLABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT_PATH, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.DIRT.defaultBlockState())), Blocks.ROOTED_DIRT, Pair.of((Predicate)(context) -> true, changeIntoStateAndDropItem(Blocks.DIRT.defaultBlockState(), Items.HANGING_ROOTS))));
    }



}
