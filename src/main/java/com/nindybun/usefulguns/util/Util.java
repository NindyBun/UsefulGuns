package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static ItemStack locateAndGetPouch(PlayerEntity player){
        NonNullList<ItemStack> inventory = player.inventory.items;
        ItemStack pouch = ItemStack.EMPTY;
        if (inventory.size() != 0)
            for (ItemStack stack : inventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof AbstractPouch){
                    pouch = stack;
                    break;
                }
            }
        return pouch;
    }

    public static boolean checkIfPouchEmpty(IItemHandler handler){
        List<ItemStack> inventory = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++){
            if (handler.getStackInSlot(i).copy() != ItemStack.EMPTY)
                inventory.add(handler.getStackInSlot(i));
        }
        return inventory.size() == 0 ? true : false;
    }

    public static List<ItemStack> getItemsInPouch(PouchData data){
        List<ItemStack> inventory = new ArrayList<>();
       PouchHandler handler = (PouchHandler) data.getHandler();
        if (!checkIfPouchEmpty(handler))
            for (int i = 0; i < handler.getSlots(); i++){
                inventory.add(handler.getStackInSlot(i));
            }
        return inventory;
    }

    public static List<ItemStack> getItemsInPouch(IItemHandler handler){
        List<ItemStack> inventory = new ArrayList<>();
        if (!checkIfPouchEmpty(handler))
            for (int i = 0; i < handler.getSlots(); i++){
                inventory.add(handler.getStackInSlot(i));
            }
        return inventory;
    }




}
