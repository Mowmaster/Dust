package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.*;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockNaturalSpawns extends BlockBasic
{
    public static Block darksoil;
    public BlockNaturalSpawns(String unloc, String registryName)
    {
        super(Material.GROUND);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(20);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setTickRandomly(true);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random,int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    private static void trySpawnMob(WorldServer world, BlockPos pos, EntityLiving mob) {
        if (mob == null) return;

        boolean shouldCenter = world.rand.nextBoolean();
        float x = pos.getX() + (shouldCenter ? 0.5F : world.rand.nextFloat());
        float y = pos.getY() + 1;
        float z = pos.getZ() + (shouldCenter ? 0.5F : world.rand.nextFloat());
        mob.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);

        if (!ForgeEventFactory.doSpecialSpawn(mob, world, x, y, z))
            mob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), null);

        if (!mob.isNotColliding() || !mob.getCanSpawnHere()) {
            mob.setDead();
            return;
        }

        if (spawnMobs(mob)) {
            mob.playLivingSound();
        }
    }

    public static boolean spawnMobs(Entity mob) {
        mob.forceSpawn = true;
        if (mob instanceof EntityLivingBase) {
            mob.getEntityData().setInteger("SinisterEarth", 60);
            EntityLivingBase living = (EntityLivingBase) mob;
            if (living instanceof EntityZombie) {
                IAttributeInstance attributeInstanceByName = living.getAttributeMap().getAttributeInstanceByName("zombie.spawnReinforcements");
                if (attributeInstanceByName != null) {
                    attributeInstanceByName.setBaseValue(0);
                }
            }
        }
        if (!mob.world.spawnEntity(mob)) {
            return false;
        }

        if (mob.isBeingRidden()) {
            mob.getPassengers().forEach(BlockNaturalSpawns::spawnMobs);
        }
        return true;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

        for (int i = 0; i < 5; i++) {
            worldIn.spawnParticle(
                    EnumParticleTypes.PORTAL,
                    pos.getX() + rand.nextDouble(),
                    pos.getY() + 1.01,
                    pos.getZ() + rand.nextDouble(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
        if (worldIn.isRemote) return;
        tickBlock(worldIn, pos, random);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn == null || worldIn.isRemote) return;
        tickBlock(worldIn, pos, rand);
    }

    protected void tickBlock(World worldIn, BlockPos pos, Random rand) {
        WorldServer world = (WorldServer) worldIn;

        int light = world.getLightFromNeighbors(pos.up());

        if (light >= 7) {
                return;
        }
        AxisAlignedBB bb = new AxisAlignedBB(pos).grow(-7, 4, 7);
        int creaturesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bb, input -> input != null && input.isCreatureType(EnumCreatureType.MONSTER, false)).size();

        if (creaturesNearby < 16)
            trySpawnMob(world, pos);
    }

    protected void trySpawnMob(WorldServer world, BlockPos pos) {
        EnumCreatureType type = EnumCreatureType.MONSTER;
        Biome.SpawnListEntry entry = world.getSpawnListEntryForTypeAt(type, pos);

        if (entry == null || !world.canCreatureTypeSpawnHere(type, entry, pos))
            return;

        EntityLiving mob;

        try {
            mob = entry.entityClass.getConstructor(World.class).newInstance(world);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        trySpawnMob(world, pos, mob);
    }

    public static class EventHandler {


        @SubscribeEvent
        public void spawnInWorld(EntityJoinWorldEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof EntityLiving) {
                NBTTagCompound nbt = entity.getEntityData();
                if (nbt.hasKey("SinisterEarth", Constants.NBT.TAG_INT)) {
                    int sinisterSoil = nbt.getInteger("SinisterEarth");
                    if (sinisterSoil <= 0) {
                        entity.setDead();
                        event.setCanceled(true);
                    } else {
                        EntityLiving living = (EntityLiving) entity;
                        living.tasks.addTask(0, new CustomAISinisterEarth(living, sinisterSoil));
                    }
                }
            }
        }
    }

    public static class CustomAISinisterEarth extends EntityAIBase {
        final EntityLiving living;
        int sinisterSoil;

        public CustomAISinisterEarth(EntityLiving living, int sinisterSoil) {
            this.living = living;
            this.sinisterSoil = sinisterSoil;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return true;
        }

        @Override
        public void updateTask() {
            if ((living.world.getTotalWorldTime() % 20) != 0) {
                return;
            }

            if (sinisterSoil < 0) return;

            if (sinisterSoil == 0) {
                for (int k = 0; k < 20; ++k) {
                    Random rand = living.world.rand;
                    double d2 = rand.nextGaussian() * 0.02D;
                    double d0 = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;

                    living.world.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            living.posX + (double) (rand.nextFloat() * living.width * 2.0F) - (double) living.width,
                            living.posY + (double) (rand.nextFloat() * living.height),
                            living.posZ + (double) (rand.nextFloat() * living.width * 2.0F) - (double) living.width,
                            d2, d0, d1);
                }

                living.setDead();
            } else {
                sinisterSoil--;
                living.getEntityData().setInteger("SinisterEarth", sinisterSoil);
            }
        }
    }


    public static void Init()
    {
        darksoil = new BlockNaturalSpawns("darksoil","darksoil");
    }

    public static void Register()
    {
        registerBlock(darksoil);
    }

    public static void RegisterRender()
    {
        registerRender(darksoil);
    }
}
