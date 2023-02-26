package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.TippedBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final Item.Properties ITEM_GROUP = new Item.Properties().tab(UsefulGuns.itemGroup);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UsefulGuns.MOD_ID);

    public static final RegistryObject<Item> TIPPED_BULLET = ITEMS.register("tipped_bullet", () -> new TippedBullet(6));

    public static final RegistryObject<Item> FLINT_BULLET = ITEMS.register("flint_bullet", () -> new AbstractBullet(5));

    public static final RegistryObject<Item> IRON_GUN = ITEMS.register("iron_pistol", () -> new AbstractGun(0, 1, 16, 14));

    public static final RegistryObject<Item> LEATHER_POUCH = ITEMS.register("leather_pouch", () -> new AbstractPouch("leather_pouch", PouchTypes.LEATHER));
    public static final RegistryObject<Item> IRON_POUCH = ITEMS.register("iron_pouch", () -> new AbstractPouch("iron_pouch", PouchTypes.IRON));
    public static final RegistryObject<Item> GOLD_POUCH = ITEMS.register("gold_pouch", () -> new AbstractPouch("gold_pouch", PouchTypes.GOLD));
    public static final RegistryObject<Item> DIAMOND_POUCH = ITEMS.register("diamond_pouch", () -> new AbstractPouch("diamond_pouch", PouchTypes.DIAMOND));
    public static final RegistryObject<Item> OBSIDIAN_POUCH = ITEMS.register("obsidian_pouch", () -> new AbstractPouch("obsidian_pouch", PouchTypes.OBSIDIAN));
    public static final RegistryObject<Item> NETHERITE_POUCH = ITEMS.register("netherite_pouch", () -> new AbstractPouch("netherite_pouch", PouchTypes.NETHERITE));
    public static final RegistryObject<Item> NETHERSTAR_POUCH = ITEMS.register("netherstar_pouch", () -> new AbstractPouch("netherstar_pouch", PouchTypes.NETHERSTAR));
}
