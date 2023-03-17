package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PouchHandler extends ItemStackHandler {
    public PouchHandler(int size){
        super(size);
    }

    @Override
    protected void onContentsChanged(int slots) {
        PouchManager.get().setDirty();
    }

    public void upgrade(int slots){
        if (slots <= this.stacks.size())
            return;
        NonNullList<ItemStack> oldStacks = this.stacks;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < oldStacks.size(); i++){
            this.stacks.set(i, oldStacks.get(i));
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= this.getSlots())
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        if (stack.getItem() instanceof AbstractBullet)
            return true;
        return false;
    }
}
