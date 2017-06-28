package com.mowmaster.dust.world.structures;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TestFile
{
    private ArrayList<Integer> blockCords;
    private Object blockType;
    //private File filename;
    //File filename = new File(getClass().getResource("").getPath());

    public  TestFile()
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
        int structure = rn.nextInt(file.listFiles().length - 1);

        File filename = new File(file.listFiles()[structure].toString());

        System.out.println(file.listFiles()[structure].getPath());
        System.out.println(file);


        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(filename));

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);

            System.out.println(reader);// Line x Comumn y
            System.out.println(jsonParser);//Dont Care
            System.out.println(jsonObject);//Prints a line of json code


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("jklfklfjbkhfkjhfkjhfjhfbjfbjfbfkjhglfhglfhjhfgfjhgfkjhgfjhfhjvjfkfhgfjhfjhfhjfhjfjkfhjhfjhfvjhvfjhfvhfhjffjkfhvbfkjhfjhfkfhgfhgfhfhfghfgflkjhfljkfjfhbfkljbfk");
        return Minecraft.getMinecraft().mcDataDir;


    }

    /*
    public void readFile(File file)
    {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(file));
    }
     */



    /*
    this.filename = new File(".");
        this.blockCords = Lists.newArrayList();
        System.out.println(filename);
        System.out.println("jkcnkljfbkhfgbkjhjufljfhlfkjhflhflkjfhkljfklfl;kfjf;lkjf;lkfj;lfjl;kfjlrhgukygdkfgkjhgfifhlkjfhklfjgfkjffkjlhfkjfdfffhbfkghfkfgjhgfkjgfkhfk");
     */
}
