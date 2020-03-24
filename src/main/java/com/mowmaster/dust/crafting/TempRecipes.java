package com.mowmaster.dust.crafting;

import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.mowmaster.dust.blocks.BlockTrap.WATERLOGGED;


@Mod.EventBusSubscriber
public class TempRecipes
{
    @SubscribeEvent()
    public static void ExplosionCrafting(ExplosionEvent event)
    {

    }

}