package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class BoreKitRecipe extends SpecialRecipe {
    public BoreKitRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    @Override
    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
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

                    if (    stack1.getItem() == Items.FLINT && Tags.Items.STONE.contains(stack3.getItem())
                            && Tags.Items.STONE.contains(stack4.getItem()) && Tags.Items.STONE.contains(stack5.getItem())) {
                    }else
                        return false;

                    ItemStack stack0 = list.get(0);
                    ItemStack stack2 = list.get(2);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (Util.getMaterial(kit, stack0) == Util.getMaterial(kit, stack2) && Util.getMaterial(kit, stack0) == kit.ordinal())
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
    public ItemStack assemble(CraftingInventory p_77569_1_) {
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

                    if (    stack1.getItem() == Items.FLINT && Tags.Items.STONE.contains(stack3.getItem())
                            && Tags.Items.STONE.contains(stack4.getItem()) && Tags.Items.STONE.contains(stack5.getItem())) {
                    }else
                        return ItemStack.EMPTY;

                    ItemStack stack0 = list.get(0);
                    ItemStack stack2 = list.get(2);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (Util.getMaterial(kit, stack0) == Util.getMaterial(kit, stack2) && Util.getMaterial(kit, stack0) == kit.ordinal())
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
                if (/*Util.getMaterial(kit, stack0) == Util.getMaterial(kit, stack2) && */Util.getMaterial(kit, stack0) == kit.ordinal()){
                    ItemStack borekit = new ItemStack(Util.createKit(kit));
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.BOREKIT_RECIPE.get();
    }
}
