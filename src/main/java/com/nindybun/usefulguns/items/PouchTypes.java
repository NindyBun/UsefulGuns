package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public enum PouchTypes {
    LEATHER(9, ModItems.LEATHER_POUCH),
    IRON(18, ModItems.IRON_POUCH),
    GOLD(27, ModItems.GOLD_POUCH),
    DIAMOND(36, ModItems.DIAMOND_POUCH),
    OBSIDIAN(45, ModItems.OBSIDIAN_POUCH),
    NETHERITE(63, ModItems.NETHERITE_POUCH),
    NETHERSTAR(81, ModItems.NETHERSTAR_POUCH),
    ;

    public final int slots;
    public final int rowLength = 9, columnLength;
    public final int slotX = 8, slotY = 18;
    public final int textureW = 176, textureH;
    public final int playerX = 8, playerY;
    public final int hotbarX = 8, hotbarY;
    public final ResourceLocation texture;
    public final RegistryObject<Item> pouchItem;


    PouchTypes(int slots, RegistryObject<Item> pouch){
        this.slots = slots;
        this.columnLength = slots/rowLength;
        int addHeightPixels = 18*(this.columnLength-1);
        this.textureH = 132+addHeightPixels;
        this.playerY = 50+addHeightPixels;
        this.hotbarY = 108+addHeightPixels;
        this.texture = new ResourceLocation(UsefulGuns.MOD_ID, "textures/gui/bagx"+slots+".png");
        this.pouchItem = pouch;
    }

    public static Item getPouch(PouchTypes type){
        switch (type) {
            case LEATHER:
                return ModItems.LEATHER_POUCH.get();
            case IRON:
                return ModItems.IRON_POUCH.get();
            case GOLD:
                return ModItems.GOLD_POUCH.get();
            case DIAMOND:
                return ModItems.DIAMOND_POUCH.get();
            case OBSIDIAN:
                return ModItems.OBSIDIAN_POUCH.get();
            case NETHERITE:
                return ModItems.NETHERITE_POUCH.get();
            case NETHERSTAR:
                return ModItems.NETHERSTAR_POUCH.get();
            default:
                return ItemStack.EMPTY.getItem();
        }
    }


}
