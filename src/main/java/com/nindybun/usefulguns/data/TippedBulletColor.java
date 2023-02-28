package com.nindybun.usefulguns.data;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

public class TippedBulletColor implements IItemColor {
    @Override
    public int getColor(ItemStack stack, int index_) {
        return index_ == 0 ? PotionUtils.getColor(stack) : -1;
    }
}
