package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PouchHandler extends ItemStackHandler {
    public static final int maxTypes = 81;
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

    private boolean contains(ItemStack stack, List<ItemStack> list){
        for (ItemStack itemStack : list){
            if (itemStack.copy().split(1).equals(stack.copy().split(1), false))
                return true;
        }
        return false;
    }

    private List<ItemStack> getItems(){
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < getSlots(); i++){
            ItemStack stack = getStackInSlot(i);
            if (stack != ItemStack.EMPTY && !contains(stack, list))
                list.add(stack);
        }

        return list;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= this.getSlots())
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        if (stack.getItem() instanceof AbstractBullet) {
            List<ItemStack> list = getItems();
            if ((list.size() <= this.maxTypes && contains(stack, list)) || (list.size() < this.maxTypes && !contains(stack, list)))
                return true;
        }
        return false;
    }
}
