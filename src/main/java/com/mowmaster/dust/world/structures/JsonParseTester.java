package com.mowmaster.dust.world.structures;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.Block.getBlockFromName;


public class JsonParseTester
{
    //private File filename;
    //File filename = new File(getClass().getResource("").getPath());

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

        File filename = new File(file.listFiles()[0].toString());

        System.out.println(filename);



        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);
            String block = jsonObject.get("block").getAsString();
            JsonPrimitive getX = jsonObject.getAsJsonPrimitive("x");
            JsonPrimitive getY = jsonObject.getAsJsonPrimitive("y");
            JsonPrimitive getZ = jsonObject.getAsJsonPrimitive("z");

            int x = getX.getAsInt();
            int y = getY.getAsInt();
            int z = getZ.getAsInt();
            Block block2 = getBlockFromName(block);
            IBlockState state = block2.getDefaultState();

            System.out.println(x);
            System.out.println(y);
            System.out.println(z);
            System.out.println(state);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("jklfklfjbkhfkjhfkjhfjhfbjfbjfbfkjhglfhglfhjhfgfjhgfkjhgfjhfhjvjfkfhgfjhfjhfhjfhjfjkfhjhfjhfvjhvfjhfvhfhjffjkfhvbfkjhfjhfkfhgfhgfhfhfghfgflkjhfljkfjfhbfkljbfk");
        return Minecraft.getMinecraft().mcDataDir;
    }
}
