package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.conversions.SpellConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Optional;

// Rust's effects:
//   Self    Corrosive Aura: attacks on you are corroded, 15% weaker a level (up to 60%). Only attacks, damage something
//           dealt (a mob, a player, an arrow or a spell); falls, fire and the like aren't touched.
//   Block   oxidises the block a stage: copper -> exposed -> weathered -> oxidized, for every copper block (stairs,
//           doors, bulbs, grates...). Waxed copper is safe. Anything else goes through Rust's conversion table, so a
//           datapack can rust other metals.
//   Entity  Corroding: its armour, tools and weapons wear twice as fast, and once more a level
//           (mixin/ItemStackCorrodingMixin)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class RustParts
{
    public static final float AURA_PER_LEVEL = 0.15F;
    public static final float AURA_MAX = 0.6F;

    private RustParts() {}

    public static void corrosiveAura(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, DustEffectsRegistry.CORROSIVE_AURA);
    }

    public static void oxidise(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        // WeatheringCopper.getNext: the next stage of any copper block (empty for the last stage, waxed copper and
        // everything else). withPropertiesOf keeps its facing, half, open state and so on.
        Optional<Block> next = WeatheringCopper.getNext(state.getBlock());
        if (next.isEmpty()) {
            SpellConversions.convert(cast, pos, face);
            return;
        }
        level.setBlockAndUpdate(pos, next.get().withPropertiesOf(state));
        level.sendParticles(ParticleTypes.SCRAPE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15, 0.5, 0.5, 0.5, 0.5);
        level.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.BLOCKS, 0.8F, 0.6F);
    }

    public static void corrode(SpellCast cast, net.minecraft.world.entity.Entity target)
    {
        if (target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(DustEffectsRegistry.CORRODING, cast.stats().duration(), cast.stats().amplifier()), cast.caster());
        }
    }

    // How much more an item wears while its holder is Corroding: twice, and once more a level (1 if not corroding)
    public static int wearMultiplier(LivingEntity owner)
    {
        MobEffectInstance corroding = owner.getEffect(DustEffectsRegistry.CORRODING);
        return corroding == null ? 1 : 2 + corroding.getAmplifier();
    }

    // Corrosive Aura: an attack (damage with something behind it) is weakened
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        MobEffectInstance aura = event.getEntity().getEffect(DustEffectsRegistry.CORROSIVE_AURA);
        if (aura == null || event.getSource().getEntity() == null) return;
        float cut = Math.min(AURA_MAX, AURA_PER_LEVEL * (aura.getAmplifier() + 1));
        event.setAmount(event.getAmount() * (1 - cut));
        if (event.getEntity().level() instanceof ServerLevel level) {
            LivingEntity hit = event.getEntity();
            level.sendParticles(ParticleTypes.WAX_OFF, hit.getX(), hit.getY() + hit.getBbHeight() / 2, hit.getZ(), 4, 0.3, 0.4, 0.3, 0.2);
        }
    }
}
