package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static int getMaterial(BoreKit.Kit kit, ItemStack stack){
        int value = -1;
        switch (kit){
            case WOOD:
                if (ItemTags.PLANKS.contains(stack.getItem()))
                    value = 0;
                break;
            case STONE:
                if (Tags.Items.COBBLESTONE.contains(stack.getItem()))
                    value = 1;
                break;
            case IRON:
                if (Tags.Items.INGOTS_IRON.contains(stack.getItem()))
                    value = 2;
                break;
            case GOLD:
                if (Tags.Items.INGOTS_GOLD.contains(stack.getItem()))
                    value = 3;
                break;
            case DIAMOND:
                if (Tags.Items.GEMS_DIAMOND.contains(stack.getItem()))
                    value = 4;
                break;
            case NETHERITE:
                if (Tags.Items.INGOTS_NETHERITE.contains(stack.getItem()))
                    value = 5;
                break;
        }
        return value;
    }

    public static Item createBore(BoreKit.Kit kit){
        Item boreKit = ItemStack.EMPTY.getItem();
        switch (kit){
            case WOOD:
                boreKit = ModItems.WOOD_MINING_BULLET.get();
                break;
            case STONE:
                boreKit = ModItems.STONE_MINING_BULLET.get();
                break;
            case IRON:
                boreKit = ModItems.IRON_MINING_BULLET.get();
                break;
            case GOLD:
                boreKit = ModItems.GOLD_MINING_BULLET.get();
                break;
            case DIAMOND:
                boreKit = ModItems.DIAMOND_MINING_BULLET.get();
                break;
            case NETHERITE:
                boreKit = ModItems.NETHERITE_MINING_BULLET.get();
                break;
        }
        return boreKit;
    }

    public static Item createKit(BoreKit.Kit kit){
        Item boreKit = ItemStack.EMPTY.getItem();
        switch (kit){
            case WOOD:
                boreKit = ModItems.WOOD_BORE_KIT.get();
                break;
            case STONE:
                boreKit = ModItems.STONE_BORE_KIT.get();
                break;
            case IRON:
                boreKit = ModItems.IRON_BORE_KIT.get();
                break;
            case GOLD:
                boreKit = ModItems.GOLD_BORE_KIT.get();
                break;
            case DIAMOND:
                boreKit = ModItems.DIAMOND_BORE_KIT.get();
                break;
            case NETHERITE:
                boreKit = ModItems.NETHERITE_BORE_KIT.get();
                break;
        }
        return boreKit;
    }

    public static Tags.IOptionalNamedTag<Item> createTag(BoreKit.Kit kit){
        Tags.IOptionalNamedTag<Item> tag = null;
        switch (kit){
            case STONE:
                tag = Tags.Items.COBBLESTONE;
                break;
            case IRON:
                tag = Tags.Items.INGOTS_IRON;
                break;
            case GOLD:
                tag = Tags.Items.INGOTS_GOLD;
                break;
            case DIAMOND:
                tag = Tags.Items.GEMS_DIAMOND;
                break;
            case NETHERITE:
                tag = Tags.Items.INGOTS_NETHERITE;
                break;
        }
        return tag;
    }

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
