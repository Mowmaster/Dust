package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Creatures (bosses = false): animals and hostile mobs, every entity type with a spawn egg that isn't a boss.
// Bosses (bosses = true): entity types in NeoForge's #c:bosses (the Ender Dragon, the Wither...).
// Observed by looking at one for a few seconds; shown on their page as the mob itself.
public class CreatureResearchTab extends ResearchTab
{
    private final boolean bosses;

    public CreatureResearchTab(boolean bosses)
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, bosses ? "bosses" : "creatures"), bosses ? 400 : 300);
        this.bosses = bosses;
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(bosses ? Items.DRAGON_HEAD : Items.ZOMBIE_HEAD);
    }

    private boolean covers(EntityType<?> type)
    {
        boolean boss = type.builtInRegistryHolder().is(Tags.EntityTypes.BOSSES);
        if (bosses) return boss;
        return !boss && SpawnEggItem.byId(type).isPresent();
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (covers(type)) list.add(BuiltInRegistries.ENTITY_TYPE.getKey(type));
        }
        return list;
    }

    @Override
    public @Nullable Identifier entryForEntity(Entity entity)
    {
        return entity instanceof LivingEntity && covers(entity.getType()) ? BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()) : null;
    }

    // Mobs take a while to watch
    @Override
    public int observeTicks()
    {
        return 60;
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        return SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.getValue(entry)).map(ItemStack::new).orElse(new ItemStack(Items.BARRIER));
    }

    @Override
    public Component name(Identifier entry)
    {
        return BuiltInRegistries.ENTITY_TYPE.getValue(entry).getDescription();
    }

    @Override
    public @Nullable EntityType<?> displayEntity(Identifier entry)
    {
        return BuiltInRegistries.ENTITY_TYPE.getValue(entry);
    }
}
