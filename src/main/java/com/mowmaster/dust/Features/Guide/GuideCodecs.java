package com.mowmaster.dust.Features.Guide;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;

// Small codecs for guide scene files (GuideScene), written to be easy to type by hand:
//   BLOCK_STATE   "minecraft:oak_stairs[facing=east,half=top]" (as in commands), or vanilla's
//                 {"Name": "minecraft:oak_stairs", "Properties": {"facing": "east"}}
//   COLOR         "#RRGGBB" or "#AARRGGBB" (no alpha: opaque), or a plain number
// A Codec turns a value to and from JSON (or NBT); Codec.either tries the first form, then the second.
public final class GuideCodecs
{
    private GuideCodecs() {}

    private static DataResult<BlockState> parseState(String text)
    {
        try {
            // BuiltInRegistries.BLOCK is also a HolderLookup (what the command parser looks block ids up in)
            return DataResult.success(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, text, false).blockState());
        } catch (CommandSyntaxException e) {
            return DataResult.error(() -> "Bad block state '" + text + "': " + e.getMessage());
        }
    }

    public static final Codec<BlockState> BLOCK_STATE = Codec.either(Codec.STRING.comapFlatMap(GuideCodecs::parseState, BlockStateParser::serialize), BlockState.CODEC)
            .xmap(either -> either.map(state -> state, state -> state), Either::left);

    private static DataResult<Integer> parseColor(String text)
    {
        String hex = text.startsWith("#") ? text.substring(1) : text;
        try {
            long value = Long.parseLong(hex, 16);
            return DataResult.success((int) (hex.length() <= 6 ? 0xFF000000L | value : value));
        } catch (NumberFormatException e) {
            return DataResult.error(() -> "Bad colour '" + text + "' (use #RRGGBB or #AARRGGBB)");
        }
    }

    public static final Codec<Integer> COLOR = Codec.either(Codec.STRING.comapFlatMap(GuideCodecs::parseColor, color -> String.format("#%08X", color)), Codec.INT)
            .xmap(either -> either.map(color -> color, color -> color), Either::left);
}
