package com.mowmaster.dust.Features.FocusedBooks;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.numeric.Count;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.BiConsumer;

public class DataGenCustomItemModels extends ItemModelGenerators
{
    public DataGenCustomItemModels(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    public void generateFocusedBookModels(Item item)
    {
        ItemModel.Unbaked focusedbookBase = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked anchor = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_anchor"));
        ItemModel.Unbaked animal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_animal"));
        ItemModel.Unbaked armor = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_armor"));
        ItemModel.Unbaked bow = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_bow"));
        ItemModel.Unbaked breakable = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_breakable"));
        ItemModel.Unbaked chest = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_chest"));
        ItemModel.Unbaked crossbow = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_crossbow"));
        ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_default"));
        ItemModel.Unbaked digger = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_digger"));
        ItemModel.Unbaked feet = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_feet"));
        ItemModel.Unbaked fish = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_fish"));
        ItemModel.Unbaked gun = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_gun"));
        ItemModel.Unbaked hammer = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hammer"));
        ItemModel.Unbaked helm = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_helm"));
        ItemModel.Unbaked hoe = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hoe"));
        ItemModel.Unbaked knife = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_knife"));
        ItemModel.Unbaked legs = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_legs"));
        ItemModel.Unbaked pedestal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_pedestal"));
        ItemModel.Unbaked trident = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_trident"));
        ItemModel.Unbaked weapon = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_weapon"));
        ItemModel.Unbaked wearable = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_wearable"));
        this.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        ItemModelUtils.hasComponent(DustComponentDataRegistry.FOCUSEDBOOK_BOOKCOVER_TYPE.get()),
                        ItemModelUtils.rangeSelect(new Count(false),
                                List.of(
                                        ItemModelUtils.override(anchor, 1),
                                        ItemModelUtils.override(animal, 2),
                                        ItemModelUtils.override(armor, 3),
                                        ItemModelUtils.override(bow, 4),
                                        ItemModelUtils.override(breakable, 5),
                                        ItemModelUtils.override(chest, 6),
                                        ItemModelUtils.override(crossbow, 7),
                                        ItemModelUtils.override(base, 8),
                                        ItemModelUtils.override(digger, 9),
                                        ItemModelUtils.override(feet, 10),
                                        ItemModelUtils.override(fish, 11),
                                        ItemModelUtils.override(gun, 12),
                                        ItemModelUtils.override(hammer, 13),
                                        ItemModelUtils.override(helm, 14),
                                        ItemModelUtils.override(hoe, 15),
                                        ItemModelUtils.override(knife, 16),
                                        ItemModelUtils.override(legs, 17),
                                        ItemModelUtils.override(pedestal, 18),
                                        ItemModelUtils.override(trident, 19),
                                        ItemModelUtils.override(weapon, 20),
                                        ItemModelUtils.override(wearable, 21))), focusedbookBase));
    }



}