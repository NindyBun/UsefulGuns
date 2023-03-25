package com.nindybun.usefulguns.data;


import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

public class TippedBulletColor implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int index_) {
        return index_ == 0 ? PotionUtils.getColor(stack) : -1;
    }
}
