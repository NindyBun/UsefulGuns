package com.nindybun.usefulguns.gui;

import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.modRegistries.ModContainers;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PouchContainer extends Container {
    public final PouchHandler handler;
    private final PouchTypes type;
    private final UUID uuid;

    public static PouchContainer fromNetwork(int windowId, PlayerInventory playerInventory, PacketBuffer data){
        UUID uuidIn = data.readUUID();
        PouchTypes type = PouchTypes.values()[data.readInt()];
        return new PouchContainer(windowId, playerInventory, uuidIn, type, new PouchHandler(type.slots));
    }

    public PouchContainer(int windowId, PlayerInventory playerInventory, UUID uuidIn, PouchTypes type, PouchHandler handler){
        super(ModContainers.POUCH_CONTAINER.get(), windowId);

        this.uuid = uuidIn;
        this.handler = handler;
        this.type = type;

        addPlayerSlots(playerInventory);
        addPouchSlots();
    }

    public PouchTypes getPouchType(){
        return this.type;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

/*    @Override
    @Nonnull
    public ItemStack clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull PlayerEntity player) {
        if (clickTypeIn == ClickType.SWAP)
            return ItemStack.EMPTY;
        if (slot >= 0) getSlot(slot).container.setChanged();
        return super.clicked(slot, dragType, clickTypeIn, player);
    }*/


    private void addPlayerSlots(PlayerInventory playerInventory) {
        //Hotbar
        for (int col = 0; col < 9; col++) {
            int x = type.hotbarX + col * 18;
            int y = type.hotbarY;
            this.addSlot(new Slot(playerInventory, col, x, y));
        }

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = type.playerX + col * 18;
                int y = type.playerY + row * 18;
                int index = (col + row * 9) + 9;
                this.addSlot(new Slot(playerInventory, index, x, y));
            }
        }
    }

    private void addPouchSlots() {
        if (this.handler == null) return;
        int slot_index = 0;

        for (int row = 0; row < type.columnLength; row++) {
            for (int col = 0; col < type.rowLength; col++) {
                int x = type.slotX + col * 18;
                int y = type.slotY + row * 18;
                this.addSlot(new SlotItemHandler(this.handler, slot_index, x, y));
                slot_index++;
                if (slot_index >= this.type.slots)
                    break;
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            int bagslotcount = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < playerIn.inventory.items.size()) {
                if (!this.moveItemStackTo(itemstack1, playerIn.inventory.items.size(), bagslotcount, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, 0, playerIn.inventory.items.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }
}
