package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class BoreKit extends Item {
    private final Kit kit;
    public BoreKit(Kit kit) {
        super(
                kit.isFireResistant()
                        ? ModItems.ITEM_GROUP.durability(kit.getDurability()).setNoRepair().fireResistant()
                        : ModItems.ITEM_GROUP.durability(kit.getDurability()).setNoRepair());
        this.kit = kit;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        copy.setDamageValue(copy.getDamageValue()+1);
        return copy.getDamageValue() == copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getDamageValue() != 0;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public enum Kit{
        WOOD(1600),
        STONE(1600),
        IRON(1600),
        GOLD(1600),
        DIAMOND(1600),
        NETHERITE(1600, true)
        ;

        private int durability;
        private boolean isFireResistant;

        Kit(int durability, boolean isFireResistant){
            this.durability = durability;
            this.isFireResistant = isFireResistant;
        }

        Kit(int durability){
            this.durability = durability;
            this.isFireResistant = false;
        }

        public int getDurability(){
            return this.durability;
        }

        public boolean isFireResistant(){
            return this.isFireResistant;
        }

    }
}
