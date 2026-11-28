package com.mowmaster.dust.Integration.jei;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.Research.RecipeRecord;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// JEI's page for Element Crafter recipes (26.3 only, with JEI installed; DustJeiPlugin). A recipe here is one of the
// research book's RecipeRecords (RecipeCatalog, sent to every client): the 3x3 grid on the left, an arrow, what it
// makes, and down the right the dust it takes from the crafter's jars (each element named as the research book names
// it: its icon and name once discovered, else in the ancient script).
public class ElementCrafterCategory implements IRecipeCategory<RecipeRecord>
{
    public static final IRecipeType<RecipeRecord> TYPE = IRecipeType.create(DustReferences.MODID, "element_crafting", RecipeRecord.class);
    // The same page for Dust's own crafting table recipes (dust:dust_crafting: the research tables and the rest), which
    // JEI's crafting page doesn't know: made at a crafting table, no jar dust
    public static final IRecipeType<RecipeRecord> DUST_CRAFTING = IRecipeType.create(DustReferences.MODID, "dust_crafting", RecipeRecord.class);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 56;
    private static final int OUTPUT_X = 90;
    private static final int COSTS_X = 116;
    private static final int TEXT = 0xFF404040;

    private final IRecipeType<RecipeRecord> type;
    private final Component title;
    private final IDrawable icon;
    private final IDrawable arrow;

    // The Element Crafter's page
    public static ElementCrafterCategory elementCrafter(IGuiHelper guiHelper)
    {
        return new ElementCrafterCategory(guiHelper, TYPE, DustBlockRegistry.ELEMENT_CRAFTER.get(), Component.translatable("block.dust.element_crafter"));
    }

    // Dust's crafting table recipes' page
    public static ElementCrafterCategory dustCrafting(IGuiHelper guiHelper)
    {
        return new ElementCrafterCategory(guiHelper, DUST_CRAFTING, net.minecraft.world.level.block.Blocks.CRAFTING_TABLE,
                Component.translatable("jei.dust.dust_crafting"));
    }

    private ElementCrafterCategory(IGuiHelper guiHelper, IRecipeType<RecipeRecord> type, net.minecraft.world.level.ItemLike station, Component title)
    {
        this.type = type;
        this.title = title;
        this.icon = guiHelper.createDrawableItemLike(station);
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeRecord> getRecipeType()
    {
        return type;
    }

    @Override
    public Component getTitle()
    {
        return title;
    }

    @Override
    public int getWidth()
    {
        return WIDTH;
    }

    @Override
    public int getHeight()
    {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeRecord recipe, IFocusGroup focuses)
    {
        int width = Math.max(1, recipe.width());
        List<Identifier> slots = recipe.slots();
        // Every grid square gets a slot (an empty one is just its background), so the grid reads as the crafter's
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                var slot = builder.addInputSlot(column * 18 + 1, row * 18 + 1).setStandardSlotBackground();
                if (column >= width) continue;
                int index = row * width + column;
                if (index < slots.size()) {
                    ItemStack stack = stackOf(slots.get(index));
                    if (!stack.isEmpty()) slot.add(stack);
                }
            }
        }
        builder.addOutputSlot(OUTPUT_X, 19).setOutputSlotBackground().add(stackOf(recipe.output()));
    }

    @Override
    public void draw(RecipeRecord recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY)
    {
        arrow.draw(graphics, 60, 19);
        Font font = Minecraft.getInstance().font;
        int y = 28 - recipe.elements().size() * 6;
        for (RecipeRecord.Cost cost : recipe.elements()) {
            Component line = Component.empty().append(ElementNames.forClient(cost.element())).append(" " + cost.amount());
            graphics.text(font, line, COSTS_X, y, TEXT, false);
            y += 12;
        }
    }

    private static ItemStack stackOf(Identifier id)
    {
        if (id.equals(RecipeRecord.EMPTY)) return ItemStack.EMPTY;
        return new ItemStack(BuiltInRegistries.ITEM.getValue(id));
    }
}
