package com.nindybun.usefulguns.util;

import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.items.bullets.ShotgunBullet;
import com.nindybun.usefulguns.items.guns.AbstractShotgun;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilMethods {
    public static final String BULLET_INFO_TAG = "Bullet_Info";
    private static final String ITEMSTACK_TAG = "ItemStack";
    private static final String AMOUNT_TAG = "Amount";
    public static final String INVENTORY_TAG = "ClientInventory";

    public static List<ItemStack> subtractAndReturnBulletList(List<ItemStack> itemStackList, ItemStack ammo){
        for (int i = 0; i < itemStackList.size(); i++){
            ItemStack stack = itemStackList.get(i);
            if (isEqual(ammo, stack))
                stack.setCount(stack.getCount()-1);
            if (stack.getCount() > 0)
                itemStackList.set(i, stack);
            else
                itemStackList.remove(i);
        }
        return itemStackList;
    }

    public static void updatePouchAfterShooting(ItemStack pouch, ItemStack ammo){
        List<ItemStack> itemStackList = deserializeItemTagList(pouch.getOrCreateTag().getList(INVENTORY_TAG, Tag.TAG_COMPOUND));
        pouch.getOrCreateTag().put(INVENTORY_TAG, serializeItemTagList(subtractAndReturnBulletList(itemStackList, ammo)));
    }


    public static Tag serializeItemTagList(Map<List<ItemStack>, List<Integer>> listMap){
        List<ItemStack> itemStackList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        ListTag listTag = new ListTag();
        for (Map.Entry<List<ItemStack>, List<Integer>> entry : listMap.entrySet()) {
            itemStackList = entry.getKey();
            amountList = entry.getValue();
        }
        for (int i = 0; i < itemStackList.size(); i++){
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put(ITEMSTACK_TAG, itemStackList.get(i).serializeNBT());
            compoundTag.putInt(AMOUNT_TAG, amountList.get(i));
            listTag.add(compoundTag);
        }
        return listTag;
    }

    public static Tag serializeItemTagList(List<ItemStack> itemStackList){
        ListTag listTag = new ListTag();
        for (int i = 0; i < itemStackList.size(); i++){
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put(ITEMSTACK_TAG, itemStackList.get(i).copy().split(1).serializeNBT());
            compoundTag.putInt(AMOUNT_TAG, itemStackList.get(i).getCount());
            listTag.add(compoundTag);
        }
        return listTag;
    }

    public static List<ItemStack> deserializeItemTagList(ListTag tag){
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < tag.size(); i++){
            CompoundTag compoundTag = tag.getCompound(i);
            ItemStack itemStack = ItemStack.of(compoundTag.getCompound(ITEMSTACK_TAG));
            itemStack.setCount(compoundTag.getInt(AMOUNT_TAG));
            itemStackList.add(itemStack);
        }
        return itemStackList;
    }

    public static boolean doesItemContainInItemTags(TagKey<Item> tagKey, Item item){
        return ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList().contains(item);
    }

    public static boolean isValidForGun(ItemStack gun, ItemStack ammo){
        if (ammo.getItem() instanceof AbstractBullet){
            if (ammo.getItem() instanceof MiningBullet)
                return true;
            if (gun.getItem() instanceof AbstractShotgun && ammo.getItem() instanceof ShotgunBullet)
                return true;
            if (!(gun.getItem() instanceof AbstractShotgun) && !(ammo.getItem() instanceof ShotgunBullet))
                return true;
        }
        return false;
    }

    /*public static Map<List<ItemStack>, List<Integer>> collectBullets(ItemStack gun, ItemStack pouch){
        Map<List<ItemStack>, List<Integer>> listMap = new HashMap<>();
        List<ItemStack> itemStackList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack ammo = handler.getStackInSlot(i);
                if (ammo != ItemStack.EMPTY && isValidForGun(gun, ammo)){
                    ItemStack copy = ammo.copy().split(1);
                    int amount = ammo.getCount();
                    if (!doesContainInList(itemStackList, ammo)){
                        itemStackList.add(copy);
                        amountList.add(amount);
                    }else{
                        for (int j = 0; j < i; j++){
                            if (itemStackList.get(j).equals(copy, false)){
                                amountList.set(j, amountList.get(j)+amount);
                                break;
                            }
                        }
                    }
                }
            }
        }
        listMap.put(itemStackList, amountList);
        return listMap;
    }*/

    public static Map<List<ItemStack>, List<Integer>> collectAllBullets(ItemStack pouch){
        Map<List<ItemStack>, List<Integer>> listMap = new HashMap<>();
        List<ItemStack> itemStackList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack ammo = handler.getStackInSlot(i);
                if (ammo != ItemStack.EMPTY && ammo.getItem() instanceof AbstractBullet){
                    ItemStack copy = ammo.copy().split(1);
                    int amount = ammo.getCount();
                    if (!doesContainInList(itemStackList, ammo)){
                        itemStackList.add(copy);
                        amountList.add(amount);
                    }else{
                        for (int j = 0; j < i; j++){
                            if (itemStackList.get(j).equals(copy, false)){
                                amountList.set(j, amountList.get(j)+amount);
                                break;
                            }
                        }
                    }
                }
            }
        }
        listMap.put(itemStackList, amountList);
        return listMap;
    }

    public static ItemStack getItemInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.copy().split(1).equals(itemStack.copy().split(1), false))
                return stack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean doesContainInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.copy().split(1).equals(itemStack.copy().split(1), false))
                return true;
        }
        return false;
    }

    public static boolean isEqual(ItemStack ammo, ItemStack itemStack){
        if (ammo.copy().split(1).equals(itemStack.copy().split(1), false))
            return true;
        return false;
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
        List<ItemStack> list = deserializeItemTagList(stack.getOrCreateTag().getList(INVENTORY_TAG, Tag.TAG_COMPOUND)).stream().filter(itemStack -> itemStack.getItem() instanceof AbstractBullet).collect(Collectors.toList());
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
