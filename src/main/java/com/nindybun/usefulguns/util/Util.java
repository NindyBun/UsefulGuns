package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static BlockRayTraceResult getLookingAt(World world, PlayerEntity player, RayTraceContext.FluidMode rayTraceFluid, double range) {
        Vector3d look = player.getLookAngle();
        Vector3d start = new Vector3d(player.getX(), player.getEyeY()-(double)0.1f, player.getZ());
        Vector3d end = new Vector3d(start.x + look.x * range, start.y + look.y * range, start.z + look.z * range);
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, rayTraceFluid, player);
        return world.clip(context);
    }

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
