package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.items.AbstractCleaner;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PouchHandler extends ItemStackHandler {
    public static final int maxTypes = 18;
    public PouchHandler(int size){
        super(size);
    }

    @Override
    protected void onContentsChanged(int slots) {
        PouchManager.get().setDirty();
        //PacketHandler.sendToServer(new PacketSyncPouchData());
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

    public boolean isEqual(ItemStack stack1, ItemStack stack2){
        return stack1.copy().split(1).equals(stack2.copy().split(1), false);
    }

    private boolean contains(ItemStack stack, List<ItemStack> list){
        for (ItemStack itemStack : list){
            if (this.isEqual(itemStack, stack))
                return true;
        }
        return false;
    }

    private List<ItemStack> getItems(){
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < getSlots(); i++){
            ItemStack stack = getStackInSlot(i);
            if (stack != ItemStack.EMPTY && !contains(stack, list) && stack.getItem() instanceof AbstractBullet)
                list.add(stack);
        }

        return list;
    }

    public boolean canSwap(int slot, ItemStack stack, List<ItemStack> list){
        if (list.size() == maxTypes){
            ItemStack toSwapWith = getStackInSlot(slot);
            if (!isEqual(stack, toSwapWith) && !toSwapWith.isEmpty())
                return true;
        }
        return false;
    }

    public boolean checkInsert(int slot, ItemStack stack){
        List<ItemStack> list = getItems();
        if (       (list.size() <= this.maxTypes && contains(stack, list))
                || (list.size() < this.maxTypes && !contains(stack, list))
                || canSwap(slot, stack, list))
            return true;
        return false;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= this.getSlots())
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        if (stack.getItem() instanceof AbstractBullet) {
            if (this.checkInsert(slot, stack))
                return true;
        }else if (stack.getItem() == ModItems.BULLET_CASING.get() || stack.getItem() instanceof BoreKit || stack.getItem() instanceof AbstractCleaner)
            return true;

        return false;
    }
}
