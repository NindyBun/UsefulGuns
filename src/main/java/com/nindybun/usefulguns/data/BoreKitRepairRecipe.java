package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class BoreKitRepairRecipe extends SpecialRecipe {
    public BoreKitRepairRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    @Override
    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
            ItemStack itemStack2 = p_77569_1_.getItem(i);
            if (!itemStack2.isEmpty()) {
                list.add(itemStack2);
                if (list.size() > 2) {
                    ItemStack itemStack0 = list.get(0);
                    ItemStack itemStack1 = list.get(1);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (
                                (itemStack0.getItem() instanceof BoreKit && itemStack0.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack1) != -1 && ((BoreKit)itemStack0.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack1) && Util.getMaterial(kit, itemStack1) == Util.getMaterial(kit, itemStack2))
                             || (itemStack1.getItem() instanceof BoreKit && itemStack1.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack2) != -1 && ((BoreKit)itemStack1.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack2) && Util.getMaterial(kit, itemStack2) == Util.getMaterial(kit, itemStack0))
                             || (itemStack2.getItem() instanceof BoreKit && itemStack2.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack0) != -1 && ((BoreKit)itemStack2.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack0) && Util.getMaterial(kit, itemStack0) == Util.getMaterial(kit, itemStack1))
                        ){
                            found = true;
                        }
                    }
                    if (!found)
                        return false;
                }
            }
        }

        return list.size() == 3;
    }

    @Override
    public ItemStack assemble(CraftingInventory p_77569_1_) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
            ItemStack itemStack2 = p_77569_1_.getItem(i);
            if (!itemStack2.isEmpty()) {
                list.add(itemStack2);
                if (list.size() > 2) {
                    ItemStack itemStack0 = list.get(0);
                    ItemStack itemStack1 = list.get(1);
                    boolean found = false;
                    for (BoreKit.Kit kit : BoreKit.Kit.values()){
                        if (
                                (itemStack0.getItem() instanceof BoreKit && itemStack0.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack1) != -1 && ((BoreKit)itemStack0.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack1) && Util.getMaterial(kit, itemStack1) == Util.getMaterial(kit, itemStack2))
                             || (itemStack1.getItem() instanceof BoreKit && itemStack1.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack2) != -1 && ((BoreKit)itemStack1.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack2) && Util.getMaterial(kit, itemStack2) == Util.getMaterial(kit, itemStack0))
                             || (itemStack2.getItem() instanceof BoreKit && itemStack2.getOrCreateTag().getInt(BoreKit.USES) == 0 && Util.getMaterial(kit, itemStack0) != -1 && ((BoreKit)itemStack2.getItem()).getKit().ordinal() == Util.getMaterial(kit, itemStack0) && Util.getMaterial(kit, itemStack0) == Util.getMaterial(kit, itemStack1))
                        ){
                            found = true;
                        }
                    }
                    if (!found)
                        return ItemStack.EMPTY;
                }
            }
        }

        if (list.size() == 3) {
            ItemStack itemStack0 = list.get(0);
            ItemStack itemStack1 = list.get(1);
            ItemStack itemStack2 = list.get(2);
            if (itemStack0.getItem() instanceof BoreKit)
                return craft(itemStack0, itemStack1);
            else if (itemStack1.getItem() instanceof BoreKit)
                return craft(itemStack1, itemStack2);
            else if (itemStack2.getItem() instanceof BoreKit)
                return craft(itemStack2, itemStack0);
        }
        return ItemStack.EMPTY;
    }

    private ItemStack craft(ItemStack stack, ItemStack material){
        ItemStack copy = stack.copy();
        BoreKit borekit = ((BoreKit) copy.getItem());

        for (BoreKit.Kit kit : BoreKit.Kit.values()){
            if (borekit.getKit() == kit && Util.getMaterial(kit, material) == kit.ordinal()) {
                copy.getOrCreateTag().putInt(BoreKit.USES, borekit.getKit().getUses());
                break;
            }
        }

        return copy;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.BOREKIT_REPAIR_RECIPE.get();
    }
}
