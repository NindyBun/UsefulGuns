package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoreBulletRecipe extends SpecialRecipe {
    public BoreBulletRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    @Override
    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
            ItemStack itemStack1 = p_77569_1_.getItem(i);
            if (!itemStack1.isEmpty()) {
                list.add(itemStack1);
                if (list.size() > 1) {
                    ItemStack itemStack0 = list.get(0);
                    if (       (itemStack0.getItem() instanceof BoreKit && itemStack0.getOrCreateTag().getInt(BoreKit.USES) != 0 && itemStack1.getItem() == ModItems.BULLET_CASING.get())
                            || (itemStack1.getItem() instanceof BoreKit && itemStack1.getOrCreateTag().getInt(BoreKit.USES) != 0 && itemStack0.getItem() == ModItems.BULLET_CASING.get())) {
                    }else
                        return false;
                }
            }
        }

        return list.size() == 2;
    }

    @Override
    public ItemStack assemble(CraftingInventory p_77569_1_) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < p_77569_1_.getContainerSize(); i++) {
            ItemStack itemStack1 = p_77569_1_.getItem(i);
            if (!itemStack1.isEmpty()) {
                list.add(itemStack1);
                if (list.size() > 1) {
                    ItemStack itemStack0 = list.get(0);
                    if (       (itemStack0.getItem() instanceof BoreKit && itemStack0.getOrCreateTag().getInt(BoreKit.USES) != 0 && itemStack1.getItem() == ModItems.BULLET_CASING.get())
                            || (itemStack1.getItem() instanceof BoreKit && itemStack1.getOrCreateTag().getInt(BoreKit.USES) != 0 && itemStack0.getItem() == ModItems.BULLET_CASING.get())) {
                    }else
                        return ItemStack.EMPTY;
                }
            }
        }

        if (list.size() == 2) {
            ItemStack itemStack0 = list.get(0);
            ItemStack itemStack1 = list.get(1);
            if (itemStack0.getItem() instanceof BoreKit)
                return craft(itemStack0);
            else if (itemStack1.getItem() instanceof BoreKit)
                return craft(itemStack1);
        }
        return ItemStack.EMPTY;
    }

    private ItemStack craft(ItemStack stack){
        ItemStack bore = new ItemStack(Util.createBore(((BoreKit)stack.getItem()).getKit()));
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.containsKey(Enchantments.BLOCK_FORTUNE))
            bore.enchant(Enchantments.BLOCK_FORTUNE, enchantments.get(Enchantments.BLOCK_FORTUNE));
        else if (enchantments.containsKey(Enchantments.SILK_TOUCH))
            bore.enchant(Enchantments.SILK_TOUCH, enchantments.get(Enchantments.SILK_TOUCH));
        return bore;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.BORE_BULLET_RECIPE.get();
    }
}
