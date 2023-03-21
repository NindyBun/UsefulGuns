package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.AbstractCleaner;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.items.bullets.*;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.items.guns.AbstractMachineGun;
import com.nindybun.usefulguns.items.guns.AbstractShotgun;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final Item.Properties ITEM_GROUP = (new Item.Properties()).tab(UsefulGuns.itemGroup);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UsefulGuns.MOD_ID);

    public static final RegistryObject<Item> TIPPED_BULLET = ITEMS.register("tipped_bullet", () -> new TippedBullet(5));
    public static final RegistryObject<Item> SPLASH_BULLET = ITEMS.register("splash_bullet", () -> new SplashBullet(1));
    public static final RegistryObject<Item> LINGERING_BULLET = ITEMS.register("lingering_bullet", () -> new LingeringBullet(1));

    public static final RegistryObject<Item> DRAGONS_FIREBALL_BULLET = ITEMS.register("dragonsfireball_bullet", () -> new ShotgunBullet(5, 1));
    public static final RegistryObject<Item> DRAGONS_BREATH_BULLET = ITEMS.register("dragonsbreath_bullet", () -> new ShotgunBullet(8, 5));
    public static final RegistryObject<Item> BUCKSHOT_BULLET = ITEMS.register("buckshot_bullet", () -> new ShotgunBullet(8, 5));
    public static final RegistryObject<Item> BIRDSHOT_BULLET = ITEMS.register("birdshot_bullet", () -> new ShotgunBullet(8, 15));
    public static final RegistryObject<Item> SLUG_BULLET = ITEMS.register("slug_bullet", () -> new ShotgunBullet(12, 1));

    public static final RegistryObject<Item> LIGHT_ITEM = ITEMS.register("light", () -> new BlockItem(ModBlocks.LIGHT.get(), ITEM_GROUP.stacksTo(64)));

    public static final RegistryObject<Item> NETHERITE_MINING_BULLET = ITEMS.register("netherite_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(4));
    public static final RegistryObject<Item> DIAMOND_MINING_BULLET = ITEMS.register("diamond_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(3));
    public static final RegistryObject<Item> GOLD_MINING_BULLET = ITEMS.register("gold_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(0));
    public static final RegistryObject<Item> IRON_MINING_BULLET = ITEMS.register("iron_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(2));
    public static final RegistryObject<Item> STONE_MINING_BULLET = ITEMS.register("stone_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(1));
    public static final RegistryObject<Item> WOOD_MINING_BULLET = ITEMS.register("wood_mining_bullet", () -> new MiningBullet(1).setHarvestLevel(0));

    public static final RegistryObject<Item> TORCH_BULLET = ITEMS.register("torch_bullet", () -> new AbstractBullet(1));
    public static final RegistryObject<Item> ENDER_BULLET = ITEMS.register("ender_bullet", () -> new AbstractBullet(0));
    public static final RegistryObject<Item> EXPLOSIVE_BULLET = ITEMS.register("explosive_bullet", () -> new AbstractBullet(1));
    public static final RegistryObject<Item> HOLLOW_POINT_BULLET = ITEMS.register("hollow_point_bullet", () -> new AbstractBullet(8));
    public static final RegistryObject<Item> ARMOR_PIERCING_BULLET = ITEMS.register("armor_piercing_bullet", () -> new AbstractBullet(7).setPierceLevel(1));
    public static final RegistryObject<Item> GLASS_BULLET = ITEMS.register("glass_bullet", () -> new AbstractBullet(5));
    public static final RegistryObject<Item> DIAMOND_BULLET = ITEMS.register("diamond_bullet", () -> new AbstractBullet(7));
    public static final RegistryObject<Item> IRON_BULLET = ITEMS.register("iron_bullet", () -> new AbstractBullet(6));
    public static final RegistryObject<Item> FLINT_BULLET = ITEMS.register("flint_bullet", () -> new AbstractBullet(5));
    public static final RegistryObject<Item> BULLET_CASING = ITEMS.register("bullet_casing", () -> new Item(ITEM_GROUP.stacksTo(64)));

    public static final RegistryObject<Item> LEATHER_POUCH = ITEMS.register("leather_pouch", () -> new AbstractPouch("leather_pouch", PouchTypes.LEATHER));
    public static final RegistryObject<Item> IRON_POUCH = ITEMS.register("iron_pouch", () -> new AbstractPouch("iron_pouch", PouchTypes.IRON));
    public static final RegistryObject<Item> GOLD_POUCH = ITEMS.register("gold_pouch", () -> new AbstractPouch("gold_pouch", PouchTypes.GOLD));
    public static final RegistryObject<Item> DIAMOND_POUCH = ITEMS.register("diamond_pouch", () -> new AbstractPouch("diamond_pouch", PouchTypes.DIAMOND));
    public static final RegistryObject<Item> OBSIDIAN_POUCH = ITEMS.register("obsidian_pouch", () -> new AbstractPouch("obsidian_pouch", PouchTypes.OBSIDIAN));
    public static final RegistryObject<Item> NETHERITE_POUCH = ITEMS.register("netherite_pouch", () -> new AbstractPouch("netherite_pouch", PouchTypes.NETHERITE));
    public static final RegistryObject<Item> NETHERSTAR_POUCH = ITEMS.register("netherstar_pouch", () -> new AbstractPouch("netherstar_pouch", PouchTypes.NETHERSTAR));
    public static final RegistryObject<Item> OMEGA_POUCH = ITEMS.register("omega_pouch", () -> new AbstractPouch("omegar_pouch", PouchTypes.OMEGA));

    public static final RegistryObject<Item> IRON_GUN = ITEMS.register("iron_pistol", () -> new AbstractGun(250, 0, 1, 16, 14)
            .setType(AbstractGun.Type.GUN));
    public static final RegistryObject<Item> GOLD_GUN = ITEMS.register("gold_pistol", () -> new AbstractGun(56, 0, 0.75, 10, 22)
            .setType(AbstractGun.Type.GUN));
    public static final RegistryObject<Item> DIAMOND_SNIPER = ITEMS.register("diamond_sniper", () -> new AbstractGun(1561, 0, 1.6, 24, 10)
            .projectileSpeed(4).fireSound(() -> ModSounds.SNIPER.get()).setType(AbstractGun.Type.RIFLE));
    public static final RegistryObject<Item> DIAMOND_SHOTGUN = ITEMS.register("diamond_shotgun", () -> new AbstractShotgun(1561, 0, 0.85, 16, 10)
            .fireSound(() -> ModSounds.SHOTGUN.get()).setType(AbstractGun.Type.SHOTGUN));
    public static final RegistryObject<Item> DIAMOND_MINIGUN = ITEMS.register("diamond_minigun", () -> new AbstractMachineGun(1561, 0, 1, 4, 4)
            .fireSound(() -> ModSounds.GUNNER.get()).setType(AbstractGun.Type.GUNNER));

    public static final RegistryObject<Item> NETHERITE_BORE_KIT = ITEMS.register("netherite_bore_kit", () -> new BoreKit(BoreKit.Kit.NETHERITE));
    public static final RegistryObject<Item> DIAMOND_BORE_KIT = ITEMS.register("diamond_bore_kit", () -> new BoreKit(BoreKit.Kit.DIAMOND));
    public static final RegistryObject<Item> GOLD_BORE_KIT = ITEMS.register("gold_bore_kit", () -> new BoreKit(BoreKit.Kit.GOLD));
    public static final RegistryObject<Item> IRON_BORE_KIT = ITEMS.register("iron_bore_kit", () -> new BoreKit(BoreKit.Kit.IRON));
    public static final RegistryObject<Item> STONE_BORE_KIT = ITEMS.register("stone_bore_kit", () -> new BoreKit(BoreKit.Kit.STONE));
    public static final RegistryObject<Item> WOOD_BORE_KIT = ITEMS.register("wood_bore_kit", () -> new BoreKit(BoreKit.Kit.WOOD));

    public static final RegistryObject<Item> CLEANER = ITEMS.register("cleaner", () -> new AbstractCleaner(256));
    public static final RegistryObject<Item> BETTER_CLEANER = ITEMS.register("better_cleaner", () -> new AbstractCleaner(768));
    public static final RegistryObject<Item> BEST_CLEANER = ITEMS.register("best_cleaner", () -> new AbstractCleaner(1280));
    public static final RegistryObject<Item> ULTIMATE_CLEANER = ITEMS.register("ultimate_cleaner", () -> new AbstractCleaner(-1));






}
