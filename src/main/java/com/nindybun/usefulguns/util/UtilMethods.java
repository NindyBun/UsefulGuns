package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;

public class UtilMethods {

    public static boolean doesItemContainInItemTags(TagKey<Item> tagKey, Item item){
        return ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList().contains(item);
    }

    public static int getMaterial(BoreKit.Kit kit, ItemStack stack){
        int value = -1;
        switch (kit){
            case WOOD:
                if (doesItemContainInItemTags(ItemTags.PLANKS, stack.getItem()))
                    value = 0;
                break;
            case STONE:
                if (doesItemContainInItemTags(Tags.Items.COBBLESTONE, stack.getItem()))
                    value = 1;
                break;
            case IRON:
                if (doesItemContainInItemTags(Tags.Items.INGOTS_IRON, stack.getItem()))
                    value = 2;
                break;
            case GOLD:
                if (doesItemContainInItemTags(Tags.Items.INGOTS_GOLD, stack.getItem()))
                    value = 3;
                break;
            case DIAMOND:
                if (doesItemContainInItemTags(Tags.Items.GEMS_DIAMOND, stack.getItem()))
                    value = 4;
                break;
            case NETHERITE:
                if (doesItemContainInItemTags(Tags.Items.INGOTS_NETHERITE, stack.getItem()))
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

    public static TagKey<Item> createTag(BoreKit.Kit kit){
        TagKey<Item> tag = null;
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

    public static BlockHitResult getLookingAt(Level world, Player player, Vec3 start, Vec3 look, ClipContext.Fluid rayTraceFluid, double range) {
        Vec3 end = new Vec3(start.x + look.x * range, start.y + look.y * range, start.z + look.z * range);
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, rayTraceFluid, player);
        return world.clip(context);
    }

    public static Vec3i getDim(Direction direction){
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
        return new Vec3i(xRange, yRange, zRange);
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

    public static ItemStack locateAndGetPouch(Player player){
        NonNullList<ItemStack> inventory = player.getInventory().items;
        ItemStack pouch = ItemStack.EMPTY;
        if (inventory.size() != 0)
            for (ItemStack stack : inventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof AbstractPouch){
                    if (!UtilMethods.isEmpty(stack)) {
                        pouch = stack;
                        break;
                    }else
                        continue;
                }
            }
        return pouch;
    }

}
