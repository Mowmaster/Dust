package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public class PotionMagnetism extends PotionBasic
{
    public PotionMagnetism(String potionName, String registryName, Boolean harmfulToUndead, int decimalPotionColor, int iconXCord, int iconYCord)
    {
        super(potionName,registryName,harmfulToUndead,decimalPotionColor,iconXCord,iconYCord);
    }

    public void effectOnPlayer(World world, EntityPlayer player, Boolean effectactive)
    {
        int amp=0;
        if(player.isPotionActive(PotionRegistry.POTION_MAGNETISM) && !player.isSneaking())
        {
            effectactive=true;
            amp = 1+player.getActivePotionEffect(PotionRegistry.POTION_MAGNETISM).getAmplifier();
        }
        else{effectactive=false;}

        if(effectactive==true)
        {
            float range = Math.multiplyExact(5,amp);
            float verticalRange = Math.multiplyExact(3,amp);
            float posX = Math.round(player.posX);
            float posY = (float) (player.posY - player.getEyeHeight());
            float posZ = Math.round(player.posZ);

            List<EntityItem> entities = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - range, posY - verticalRange, posZ - range, posX + range, posY + verticalRange, posZ + range));
            List<EntityXPOrb> xpOrbs = player.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - range, posY - verticalRange, posZ - range, posX + range, posY + verticalRange, posZ + range));

            for (EntityItem entity : entities) {
                if (entity != null && !world.isRemote && !entity.isDead) {
                    entity.onCollideWithPlayer(player);
                }
            }

            for (EntityXPOrb xpOrb : xpOrbs) {
                if (xpOrb != null && !world.isRemote) {
                    xpOrb.onCollideWithPlayer(player);
                }
            }
        }
    }

    public void effectOnPedestal(World world, TilePedestal pedestal)
    {

    }
}
