package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class BoreKitRecipe extends CustomRecipe {
    public BoreKitRecipe(ResourceLocation p_i48169_1_, CraftingBookCategory category) {
        super(p_i48169_1_, category);
    }

    @Override
    public boolean matches(CraftingContainer p_77569_1_, Level p_77569_2_) {
        List<ItemStack> list = new ArrayList<>();
        if (p_77569_1_.getWidth() == 3 && p_77569_1_.getHeight() >= 2){
            for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
                ItemStack stack = p_77569_1_.getItem(i);
                list.add(stack);
                if (list.get(0).isEmpty())
                    list.remove(0);
                else if (list.size() > 6 && list.get(6).isEmpty())
                    list.remove(6);
                if (list.size() > 5){
                    ItemStack stack1 = list.get(1);
                    ItemStack stack3 = list.get(3);
                    ItemStack stack4 = list.get(4);
                    ItemStack stack5 = list.get(5);

                    if (    stack1.getItem() == Items.FLINT && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack3.getItem())
                            && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack4.getItem()) && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack5.getItem())) {
                    }else
                        return false;

                    ItemStack stack0 = list.get(0);
                    ItemStack stack2 = list.get(2);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (UtilMethods.getMaterial(kit, stack0) == UtilMethods.getMaterial(kit, stack2) && UtilMethods.getMaterial(kit, stack0) == kit.ordinal())
                            found = true;
                    }
                    if (!found)
                        return false;
                }
            }
        }

        return list.size() == 6;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_77569_1_) {
        List<ItemStack> list = new ArrayList<>();
        if (p_77569_1_.getWidth() == 3 && p_77569_1_.getHeight() >= 2){
            for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
                ItemStack stack = p_77569_1_.getItem(i);
                list.add(stack);
                if (list.get(0).isEmpty())
                    list.remove(0);
                else if (list.size() > 6 && list.get(6).isEmpty())
                    list.remove(6);
                if (list.size() > 5){
                    ItemStack stack1 = list.get(1);
                    ItemStack stack3 = list.get(3);
                    ItemStack stack4 = list.get(4);
                    ItemStack stack5 = list.get(5);

                    if (    stack1.getItem() == Items.FLINT && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack3.getItem())
                            && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack4.getItem()) && UtilMethods.doesItemContainInItemTags(Tags.Items.STONE, stack5.getItem())) {
                    }else
                        return ItemStack.EMPTY;

                    ItemStack stack0 = list.get(0);
                    ItemStack stack2 = list.get(2);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (UtilMethods.getMaterial(kit, stack0) == UtilMethods.getMaterial(kit, stack2) && UtilMethods.getMaterial(kit, stack0) == kit.ordinal())
                            found = true;
                    }
                    if (!found)
                        return ItemStack.EMPTY;
                }
            }
        }

        if (list.size() == 6) {
            /*ItemStack stack1 = list.get(1);
            ItemStack stack3 = list.get(3);
            ItemStack stack4 = list.get(4);
            ItemStack stack5 = list.get(5);

            if (stack1.getItem() != Items.FLINT && Tags.Items.STONE.contains(stack3.getItem()) && Tags.Items.STONE.contains(stack4.getItem()) && Tags.Items.STONE.contains(stack5.getItem()))
                return ItemStack.EMPTY;
*/
            ItemStack stack0 = list.get(0);
            ItemStack stack2 = list.get(2);

            for (BoreKit.Kit kit : BoreKit.Kit.values()){
                if (/*Util.getMaterial(kit, stack0) == Util.getMaterial(kit, stack2) && */UtilMethods.getMaterial(kit, stack0) == kit.ordinal()){
                    ItemStack borekit = new ItemStack(UtilMethods.createKit(kit));
                    borekit.getOrCreateTag().putInt(BoreKit.USES, kit.getUses());
                    return borekit;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ == 3 && p_194133_2_ >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BOREKIT_RECIPE.get();
    }
}
