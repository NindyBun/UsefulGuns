package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public enum PouchTypes {
    LEATHER(1, 9, ModItems.LEATHER_POUCH),
    IRON(2, 9, ModItems.IRON_POUCH),
    GOLD(3, 9, ModItems.GOLD_POUCH),
    DIAMOND(4, 9, ModItems.DIAMOND_POUCH),
    OBSIDIAN(5, 9, ModItems.OBSIDIAN_POUCH),
    NETHERITE(7, 9, ModItems.NETHERITE_POUCH),
    NETHERSTAR(9, 9, ModItems.NETHERSTAR_POUCH),
    OMEGA(9, 15, ModItems.OMEGA_POUCH),
    ;

    public final int slots;
    public final int rowLength, columnLength;
    public final int slotX = 8, slotY = 18;
    public final int textureW, textureH;
    public final int playerX, playerY;
    public final int hotbarX, hotbarY;
    public final ResourceLocation texture;
    public final RegistryObject<Item> pouchItem;


    PouchTypes(int columnLength, int rowLength, RegistryObject<Item> pouch){
        this.slots = columnLength*rowLength;
        this.columnLength = columnLength;
        this.rowLength = rowLength;
        int addHeightPixels = 18*(columnLength-1);
        int addWidthPixels = 18*(rowLength);
        this.textureW = 14+addWidthPixels;
        this.textureH = 132+addHeightPixels;
        this.playerX = this.textureW/2-80;
        this.playerY = 50+addHeightPixels;
        this.hotbarX = this.playerX;
        this.hotbarY = 108+addHeightPixels;
        this.texture = new ResourceLocation(UsefulGuns.MOD_ID, "textures/gui/bagx"+this.slots+".png");
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
