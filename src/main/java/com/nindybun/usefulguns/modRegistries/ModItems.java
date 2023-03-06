package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.LingeringBullet;
import com.nindybun.usefulguns.items.bullets.SplashBullet;
import com.nindybun.usefulguns.items.bullets.TippedBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.sun.org.apache.xalan.internal.res.XSLTErrorResources_zh_TW;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

public class ModItems {
    public static final Item.Properties ITEM_GROUP = new Item.Properties().tab(UsefulGuns.itemGroup);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UsefulGuns.MOD_ID);

    public static final RegistryObject<Item> TIPPED_BULLET = ITEMS.register("tipped_bullet", () -> new TippedBullet(5, 0, false));
    public static final RegistryObject<Item> SPLASH_BULLET = ITEMS.register("splash_bullet", () -> new SplashBullet(1, 0, false));
    public static final RegistryObject<Item> LINGERING_BULLET = ITEMS.register("lingering_bullet", () -> new LingeringBullet(1, 0, false));

    public static final RegistryObject<Item> GLASS_BULLET = ITEMS.register("glass_bullet", () -> new AbstractBullet(5, 0, true));
    public static final RegistryObject<Item> IRON_BULLET = ITEMS.register("iron_bullet", () -> new AbstractBullet(6, 0, false));
    public static final RegistryObject<Item> FLINT_BULLET = ITEMS.register("flint_bullet", () -> new AbstractBullet(5, 0, false));
    public static final RegistryObject<Item> BULLET_CASING = ITEMS.register("bullet_casing", () -> new Item(ITEM_GROUP.stacksTo(64)));

    public static final RegistryObject<Item> IRON_GUN = ITEMS.register("iron_pistol", () -> new AbstractGun(0, 1, 16, 14));
    public static final RegistryObject<Item> GOLD_GUN = ITEMS.register("gold_pistol", () -> new AbstractGun(0, 0.85, 10, 22));
    public static final RegistryObject<Item> DIAMOND_SNIPER = ITEMS.register("diamond_sniper", () -> new AbstractGun(0, 1.6, 24, 10)
            .projectileSpeed(4).fireSound(() -> ModSounds.SNIPER.get()));

    public static final RegistryObject<Item> LEATHER_POUCH = ITEMS.register("leather_pouch", () -> new AbstractPouch("leather_pouch", PouchTypes.LEATHER));
    public static final RegistryObject<Item> IRON_POUCH = ITEMS.register("iron_pouch", () -> new AbstractPouch("iron_pouch", PouchTypes.IRON));
    public static final RegistryObject<Item> GOLD_POUCH = ITEMS.register("gold_pouch", () -> new AbstractPouch("gold_pouch", PouchTypes.GOLD));
    public static final RegistryObject<Item> DIAMOND_POUCH = ITEMS.register("diamond_pouch", () -> new AbstractPouch("diamond_pouch", PouchTypes.DIAMOND));
    public static final RegistryObject<Item> OBSIDIAN_POUCH = ITEMS.register("obsidian_pouch", () -> new AbstractPouch("obsidian_pouch", PouchTypes.OBSIDIAN));
    public static final RegistryObject<Item> NETHERITE_POUCH = ITEMS.register("netherite_pouch", () -> new AbstractPouch("netherite_pouch", PouchTypes.NETHERITE));
    public static final RegistryObject<Item> NETHERSTAR_POUCH = ITEMS.register("netherstar_pouch", () -> new AbstractPouch("netherstar_pouch", PouchTypes.NETHERSTAR));
}
