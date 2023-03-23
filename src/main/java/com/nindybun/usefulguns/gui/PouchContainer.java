package com.nindybun.usefulguns.gui;

import com.nindybun.usefulguns.UsefulGuns;
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
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Override
    @Nonnull
    public ItemStack clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull PlayerEntity player) {
        if (clickTypeIn == ClickType.SWAP)
            return ItemStack.EMPTY;
        if (slot >= 0) getSlot(slot).container.setChanged();
        return super.clicked(slot, dragType, clickTypeIn, player);
    }


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

    private List<ItemStack> getList(){
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < slots.size(); i++){
            ItemStack stack = slots.get(i).getItem();
            if (stack != ItemStack.EMPTY && !contains(stack, list))
                list.add(stack);
        }

        return list;
    }

    public boolean checkInsert(ItemStack stack){
        List<ItemStack> list = getList();
        if (       (list.size() <= PouchHandler.maxTypes && contains(stack, list))
                || (list.size() < PouchHandler.maxTypes && !contains(stack, list))
                )
            return true;
        return false;
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
            if (index < playerIn.inventory.items.size()) { //Inserts into bag
                if (!this.moveItemStackTo(itemstack1, playerIn.inventory.items.size(), bagslotcount, false)){
                    if (checkInsert(itemstack1)) {
                    }else if (!playerIn.level.isClientSide){
                        playerIn.sendMessage(new StringTextComponent("Unique ammo types exceed "+ PouchHandler.maxTypes +"! Cannot insert anymore!"), Util.NIL_UUID);
                    }
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, playerIn.inventory.items.size(), false)) { //Inserts into player
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }
}
