package com.mowmaster.dust.world.structures;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.Block.getBlockFromName;
import static net.minecraft.block.BlockSand.VARIANT;


public class JsonParseTester extends WorldGenerator
{
    //private File filename;
    //File filename = new File(getClass().getResource("").getPath());

    static File filename;
    static int xcord;
    static int ycord;
    static int zcord;
    static String stringBlock;
    static IBlockState returnState;

    static JsonReader reader;
    static JsonParser jsonParser;
    static JsonObject jsonObject;

    public JsonParseTester()
    {
    }

    public static File runFileGetter()
    {
        ArrayList<String> fileFolder = new ArrayList<>();
        fileFolder.add("small");
        fileFolder.add("medium");
        fileFolder.add("large");

        String size = fileFolder.get(0);
        File file = new File("config" + "/" + Reference.MODID + "/" + "structures" + "/" + size + "/");

        Random rn = new Random();
        //int structure = rn.nextInt(file.listFiles().length - 1);

        filename = new File(file.listFiles()[0].toString());

        System.out.println(filename);

        return Minecraft.getMinecraft().mcDataDir;
    }

    public static void getInfoFromJson()
    {
        try {
            reader = new JsonReader(new FileReader(filename));
            jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(reader);
            stringBlock = jsonObject.get("block").getAsString();
            JsonArray array = jsonObject.getAsJsonArray("cords");
            String string = array.toString();
            String[] cords = string.replace("[","").replace("]","").split(",");
            xcord = Integer.parseInt(cords[0]);
            ycord = Integer.parseInt(cords[1]);
            zcord = Integer.parseInt(cords[2]);





/*
            Block block = getBlockFromName(stringBlock);
            BlockStateContainer blockStateContainer = block.getBlockState();
            returnState = blockStateContainer.getBaseState();

            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stringBlock));
            System.out.println(block);
            BlockStateContainer blockStateContainer = block.getBlockState();
            IBlockState returnState = blockStateContainer.getBaseState();
*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printInfo()
    {
        System.out.println(reader);//returns null
        System.out.println(jsonParser);//returns null
        System.out.println(jsonObject);//returns null
        System.out.println("jklfklfjbkhfkjhfkjhfjhfbjfbjfbfkjhglfhglfhjhfgfjhgfkjhgfjhfhjvjfkfhgfjhfjhfhjfhjfjkfhjhfjhfvjhvfjhfvhfhjffjkfhvbfkjhfjhfkfhgfhgfhfhfghfgflkjhfljkfjfhbfkljbfk");
    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        worldIn.setBlockState(pos.add(xcord,ycord,zcord),Blocks.DIAMOND_BLOCK.getDefaultState());


        return true;
    }
}
