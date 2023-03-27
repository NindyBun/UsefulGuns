package com.nindybun.usefulguns.gui;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.modRegistries.ModContainers;
import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PouchContainer extends AbstractContainerMenu {
    public final PouchHandler handler;
    private final PouchTypes type;
    private final UUID uuid;

    public static PouchContainer fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data){
        UUID uuidIn = data.readUUID();
        PouchTypes type = PouchTypes.values()[data.readInt()];
        return new PouchContainer(windowId, playerInventory, uuidIn, type, new PouchHandler(type.slots));
    }

    public PouchContainer(int windowId, Inventory playerInventory, UUID uuidIn, PouchTypes type, PouchHandler handler){
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
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    @Nonnull
    public void clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (clickTypeIn == ClickType.SWAP)
            return;
        if (slot >= 0) getSlot(slot).container.setChanged();
        super.clicked(slot, dragType, clickTypeIn, player);
    }


    private void addPlayerSlots(Inventory playerInventory) {
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
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            int bagslotcount = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < playerIn.getInventory().items.size()) { //Inserts into bag
                if (!this.moveItemStackTo(itemstack1, playerIn.getInventory().items.size(), bagslotcount, false)){
                    if (checkInsert(itemstack1)) {
                    }else if (!playerIn.level.isClientSide){
                        playerIn.sendSystemMessage(Component.translatable("tooltip." + UsefulGuns.MOD_ID + ".pouch.container.max", PouchHandler.maxTypes));
                    }
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, playerIn.getInventory().items.size(), false)) { //Inserts into player
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }
}
