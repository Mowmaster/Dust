package com.mowmaster.dust.effects;


import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

public class EffectGetter
{
    private static final EffectGetter EFFECT_BASE = new EffectGetter();
    private final Map<Integer, EffectHashMap> effectList = Maps.<Integer, EffectHashMap>newHashMap();
    public static EffectGetter instance()
    {
        return EFFECT_BASE;
    }
    private boolean immersiveE=false;
    private boolean astralSorc=false;
    private boolean bloodMagic=false;
    private boolean botania=false;
    private boolean champions=false;
    private boolean naturalPledge=false;
    private boolean naturesAura=false;
    private boolean quark=false;
    private boolean railCraft=false;
    private boolean rustic=false;
    private boolean thaumcraft=false;
    private boolean twilightForest=false;
    private boolean wizardry=false;


    private EffectGetter()
    {
        if(Loader.isModLoaded("immersiveengineering")) {immersiveE = true;}
        if(Loader.isModLoaded("astralsorcery")) {astralSorc = true;}
        if(Loader.isModLoaded("bloodmagic")) {bloodMagic = true;}
        if(Loader.isModLoaded("botania")) {botania = true;}
        if(Loader.isModLoaded("champions")) {champions = true;}
        if(Loader.isModLoaded("naturalpledge")) {naturalPledge = true;}
        if(Loader.isModLoaded("naturesaura")) {naturesAura = true;}
        if(Loader.isModLoaded("quark")) {quark = true;}
        if(Loader.isModLoaded("railcraft")) {railCraft = true;}
        if(Loader.isModLoaded("rustic")) {rustic = true;}
        if(Loader.isModLoaded("thaumcraft")) {thaumcraft = true;}
        if(Loader.isModLoaded("twilightforest")) {twilightForest = true;}
        if(Loader.isModLoaded("wizardry")) {wizardry = true;}
      //this.addSpellEffect(xRRRBBBYYY, new PotionEffect(MobEffects.STRENGTH));x=1 for good effect x=2 for bad effect otherwise its 0-100% of the mix is which of the three colors
        this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red
        this.addSpellEffect(1099001000, new PotionEffect(MobEffects.INSTANT_HEALTH));//99% red 1% blue
        this.addSpellEffect(1090000010, new PotionEffect(MobEffects.HEALTH_BOOST));
        this.addSpellEffect(1075000025, new PotionEffect(MobEffects.JUMP_BOOST));//red-orange
        this.addSpellEffect(1075025000, new PotionEffect(PotionRegistry.POTION_MAGNETISM));//Red - Purple
        this.addSpellEffect(1060000040, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1050000050, new PotionEffect(MobEffects.HASTE));//orange 50/50
        this.addSpellEffect(1000000100, new PotionEffect(MobEffects.SATURATION));//100% yellow
        this.addSpellEffect(1010000090, new PotionEffect(MobEffects.ABSORPTION));//100% yellow
        this.addSpellEffect(1025000075, new PotionEffect(MobEffects.NIGHT_VISION));//yellow-orange
        this.addSpellEffect(1000025075, new PotionEffect(MobEffects.INVISIBILITY));//green 50/50
        this.addSpellEffect(1040000060, new PotionEffect(PotionRegistry.POTION_HARVESTER));
        this.addSpellEffect(1000040060, new PotionEffect(PotionRegistry.POTION_GROWER));
        this.addSpellEffect(1000050050, new PotionEffect(MobEffects.REGENERATION));//green 50/50
        this.addSpellEffect(1000100000, new PotionEffect(MobEffects.WATER_BREATHING));//100% Blue
        this.addSpellEffect(1000090010, new PotionEffect(MobEffects.SPEED));
        this.addSpellEffect(1005095000, new PotionEffect(MobEffects.LUCK));
        this.addSpellEffect(1000075025, new PotionEffect(PotionRegistry.POTION_QUICKNESS));//Blue - Green
        this.addSpellEffect(1025075000, new PotionEffect(MobEffects.FIRE_RESISTANCE));//Blue - Purple
        this.addSpellEffect(1000070030, new PotionEffect(PotionRegistry.POTION_STEPASSIST));
        this.addSpellEffect(1000060040, new PotionEffect(PotionRegistry.POTION_PLANTER));
        this.addSpellEffect(1040060000, new PotionEffect(PotionRegistry.POTION_WATERQUICKNESS));
        this.addSpellEffect(1050050000, new PotionEffect(MobEffects.RESISTANCE));//purple 50/50



        this.addSpellEffect(1033033033, new PotionEffect(PotionRegistry.POTION_FLIGHT));//3 way split
        this.addSpellEffect(1025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));



        this.addSpellEffect(2100000000, new PotionEffect(MobEffects.WEAKNESS));//100%red
        this.addSpellEffect(2099001000, new PotionEffect(MobEffects.INSTANT_DAMAGE));
        this.addSpellEffect(2050000050, new PotionEffect(MobEffects.MINING_FATIGUE));//orange 50/50
        this.addSpellEffect(2000000100, new PotionEffect(MobEffects.HUNGER));//100% yellow
        this.addSpellEffect(2025000075, new PotionEffect(MobEffects.BLINDNESS));//yellow-orange
        this.addSpellEffect(2000025075, new PotionEffect(MobEffects.GLOWING));
        this.addSpellEffect(2000040060, new PotionEffect(MobEffects.POISON));
        this.addSpellEffect(2000050050, new PotionEffect(MobEffects.WITHER));//green 50/50
        this.addSpellEffect(2000100000, new PotionEffect(PotionRegistry.POTION_DROWNING));//100% Blue
        this.addSpellEffect(2000090010, new PotionEffect(MobEffects.SLOWNESS));
        this.addSpellEffect(2005095000, new PotionEffect(MobEffects.UNLUCK));
        this.addSpellEffect(2033033033, new PotionEffect(MobEffects.LEVITATION));
        this.addSpellEffect(2025025050, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
        this.addSpellEffect(2025050025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
        this.addSpellEffect(2050025025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));


        if(immersiveE){this.addSpellEffect(2000075025, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:slippery")));//Blue - Green
            this.addSpellEffect(2025075000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flammable")));//Blue - Purple
            this.addSpellEffect(2040060000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:conductive")));
            this.addSpellEffect(2000070030, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:sticky")));
            this.addSpellEffect(2000060040, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:stunned")));
            this.addSpellEffect(2000055045, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flashed")));
        }
        if(astralSorc)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potionbleed")));//extra mob damage???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potioncheatdeath")));//one free chance to not die when taken enough damage to
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potiondropmodifier")));//more mob drops???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potionspellplague")));//
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potiontimefreeze")));//freezes time for target
        }
        if(bloodMagic)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:boost")));//movement buff
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:bounce")));//slime block like bouncing
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:cling")));//
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:constrict")));//
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:deafness")));//muffles all sounds
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:fire_fuse")));//causes player to ignite after effect runs out
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:flight")));//flight
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:planar_binding")));//disables the ability to teleport
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:plant_leach")));//plants consume blood to grow when walking on them
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:sacrificial_lamb")));//???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:soul_fray")));//prevents you from draining life to an alter
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:soul_snare")));//to get will from mobs

        }
        if(botania)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:allure")));//???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:bloodthirst")));//???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:clear")));//???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:emptiness")));//???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:featherfeet")));//probably slowfall or feather falling type
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:soulcross")));//???
        }
        if(champions)
        {
            this.addSpellEffect(2050050000, new PotionEffect(Potion.getPotionFromResourceLocation("champions:injured")));//take more damage (anti resist)
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("champions:jailed")));//cant move
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("champions:plague")));//wither+ ?
        }
        if(naturalPledge)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:drab")));//grey screen
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:everburn")));//take burn damage constantly
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:faithlessness")));//deity's disfavor???
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:featherweight")));//get blown around
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:immorality")));//take like no fall damage
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:overcharged")));//you are a lightning rod
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:rooted")));//cant move
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:trap_seer")));//true sight so see invisible???
        }
        if(naturesAura){this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("naturesaura:breathless")));}//take suffocation damage
        if(quark)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("quark:curse")));//spawns mobs around player
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("quark:danger_sight")));//???
        }
        if(railCraft){this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("railcraft:creosote")));}//???
        if(rustic)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:blazing_trail")));//leave a trail of fire
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:feather")));//slowfall in place
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:full")));//less damage from starving
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:ironskin")));//absorption hearts (no timer)
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:magic_resistance")));//magic/posion resist
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:shame")));//makes food particles on player
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:tipsy")));//really really bad nausea+blindness
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:wither_ward")));//less wither damage
        }
        if(thaumcraft)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:blurredvision")));//blurs vision
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:deathgaze")));//damages things you look at
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:fluxtaint")));//taint posion damage
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:infectiousvisexhaust")));//flux phage
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:sunscorned")));//super bright screen and burn in daylight
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:thaumarhia")));//thaumic shits
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:unnaturalhunger")));//have to eat brains or wait it out mega hunger
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:visexhaust")));//non spreadable flux phage
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:warpward")));//keeps warp effects away
        }
        if(twilightForest){this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("twilightforest:frosted")));}//super ice slow
        if(wizardry)
        {
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:crash")));//breaks things on falling impact?
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:grace")));//speed
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:low_gravity")));//able to jump super high
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:null_movement")));//cant move at all
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:nullify_gravity")));//hover
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:phase")));//phase through blocks
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:slippery")));//feels like walking on ice
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:steroid")));//says overdrive but idk?
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:suffocate")));//take drowning damage
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:time_slow")));//everything is slow jumps and all
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:vanish")));//full invis but mobs still see you
            this.addSpellEffect(2000000000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:zach_corruption")));//immunity???
        }

    }


    public void addSpellEffect(int colorIn, PotionEffect effectOut)
    {
        if(colorIn>=1000000000)
        {
            this.effectList.put(colorIn,new EffectHashMap(colorIn,effectOut));;
        }
        else return;
    }

    public boolean hasPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return true;
            }
        }

        return false;
    }

    public PotionEffect getPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return entry.getValue().getOutput();
            }
        }

        return null;
    }
}
