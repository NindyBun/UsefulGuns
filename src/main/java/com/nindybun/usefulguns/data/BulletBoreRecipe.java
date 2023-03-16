package com.nindybun.usefulguns.data;

import com.google.common.collect.Lists;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class BulletBoreRecipe extends SpecialRecipe {
    public BulletBoreRecipe(ResourceLocation p_i48169_1_) {
        super(p_i48169_1_);
    }

    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
        List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < p_77569_1_.getContainerSize(); ++i) {
            ItemStack itemstack1 = p_77569_1_.getItem(i);
            if (!itemstack1.isEmpty()) {
                list.add(itemstack1);
                if (list.size() > 1) {
                    ItemStack itemstack0 = list.get(0);
                    if (
                            (itemstack0.getItem() == itemstack1.getItem())
                            || (itemstack0.getItem() instanceof BoreKit && itemstack1.getItem() != ModItems.BULLET_CASING.get())
                            || (itemstack1.getItem() instanceof BoreKit && itemstack0.getItem() != ModItems.BULLET_CASING.get())
                    )
                        return false;
                }
            }
        }

        return list.size() == 2;
    }

    @Override
    public ItemStack assemble(CraftingInventory p_77572_1_) {
        List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < p_77572_1_.getContainerSize(); ++i) {
            ItemStack itemstack1 = p_77572_1_.getItem(i);
            if (!itemstack1.isEmpty()) {
                list.add(itemstack1);
                if (list.size() > 1) {
                    ItemStack itemstack0 = list.get(0);
                    if (
                            (itemstack0.getItem() == itemstack1.getItem())
                            || (itemstack0.getItem() instanceof BoreKit && itemstack1.getItem() != ModItems.BULLET_CASING.get())
                            || (itemstack1.getItem() instanceof BoreKit && itemstack0.getItem() != ModItems.BULLET_CASING.get())
                    )
                        return ItemStack.EMPTY;
                }
            }
        }

        if (list.size() == 2) {
            ItemStack itemstack0 = list.get(0);
            ItemStack itemstack1 = list.get(1);
            if (itemstack0.getItem() instanceof BoreKit && itemstack1.getItem() == ModItems.BULLET_CASING.get()){
                return createBore(itemstack0);
            }else if (itemstack1.getItem() instanceof BoreKit && itemstack0.getItem() == ModItems.BULLET_CASING.get()){
                return createBore(itemstack1);
            }
        }

        return ItemStack.EMPTY;
    }

    private ItemStack createBore(ItemStack boreKit){
        BoreKit kit = (BoreKit)boreKit.getItem();
        ItemStack bore = ItemStack.EMPTY;
        switch (kit.getKit()){
            case WOOD:
                bore = new ItemStack(ModItems.WOOD_MINING_BULLET.get());
                break;
            case STONE:
                bore = new ItemStack(ModItems.STONE_MINING_BULLET.get());
                break;
            case IRON:
                bore = new ItemStack(ModItems.IRON_MINING_BULLET.get());
                break;
            case GOLD:
                bore = new ItemStack(ModItems.GOLD_MINING_BULLET.get());
                break;
            case DIAMOND:
                bore = new ItemStack(ModItems.DIAMOND_MINING_BULLET.get());
                break;
            case NETHERITE:
                bore = new ItemStack(ModItems.NETHERITE_MINING_BULLET.get());
                break;
        }
        return bore;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.BULLET_BORE_RECIPE.get();
    }
}
