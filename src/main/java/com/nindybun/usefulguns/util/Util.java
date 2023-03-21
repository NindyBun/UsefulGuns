package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.items.AbstractPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static BlockRayTraceResult getLookingAt(World world, PlayerEntity player, Vector3d start, Vector3d look, RayTraceContext.FluidMode rayTraceFluid, double range) {
        Vector3d end = new Vector3d(start.x + look.x * range, start.y + look.y * range, start.z + look.z * range);
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, rayTraceFluid, player);
        return world.clip(context);
    }

    public static Vector3i getDim(Direction direction){
        //Z Face mining by default
        int xRange = 1;
        int yRange = 1;
        int zRange = 0;
        //X Face Mining
        if (Math.abs(direction.getNormal().getX()) == 1){
            zRange = 1;
            xRange = 0;
        }else if (Math.abs(direction.getNormal().getY()) == 1){
            yRange = 0;
            zRange = 1;
        }
        return new Vector3i(xRange, yRange, zRange);
    }

    private static boolean isEmpty(ItemStack stack){
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(stack).getOptional();
        List<ItemStack> list = new ArrayList<>();
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i< handler.getSlots(); i++){
                if (handler.getStackInSlot(i) != ItemStack.EMPTY)
                    list.add(handler.getStackInSlot(i));
            }
        }
        return list.isEmpty();
    }

    public static ItemStack locateAndGetPouch(PlayerEntity player){
        NonNullList<ItemStack> inventory = player.inventory.items;
        ItemStack pouch = ItemStack.EMPTY;
        if (inventory.size() != 0)
            for (ItemStack stack : inventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof AbstractPouch){
                    if (!Util.isEmpty(stack)) {
                        pouch = stack;
                        break;
                    }else
                        continue;
                }
            }
        return pouch;
    }

}
