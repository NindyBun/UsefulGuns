package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModSounds;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import javax.swing.text.JTextComponent;

public class Lang extends LanguageProvider {
    public Lang(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.usefulguns", "Useful Guns");

        add("key.usefulguns.radialmenu_key", "Bullet Radial Menu");
        add("key.categories.usefulguns", "Useful Guns");
        add("subtitles.usefulguns.pistol.shoot", "Pistol Fired");
        add("subtitles.usefulguns.pistol.dry", "Pistol Dry Fired");

        add(ModItems.LEATHER_POUCH.get(), "Leather Ammo Pouch");
        add(ModItems.IRON_POUCH.get(), "Iron Ammo Pouch");
        add(ModItems.GOLD_POUCH.get(), "Gold Ammo Pouch");
        add(ModItems.DIAMOND_POUCH.get(), "Diamond Ammo Pouch");
        add(ModItems.OBSIDIAN_POUCH.get(), "Obsidian Ammo Pouch");
        add(ModItems.NETHERITE_POUCH.get(), "Netherite Ammo Pouch");
        add(ModItems.NETHERSTAR_POUCH.get(), "Netherstar Ammo Pouch");

        add(ModItems.IRON_GUN.get(), "Iron Pistol");
        add(ModItems.GOLD_GUN.get(), "Gold Pistol");
        add(ModItems.DIAMOND_SNIPER.get(), "Diamond Sniper");

        add(ModItems.BULLET_CASING.get(), "Bullet Casing");
        add(ModItems.FLINT_BULLET.get(), "Flint Bullet");
        add(ModItems.TIPPED_BULLET.get(), "Tipped Bullet");
        add(ModItems.IRON_BULLET.get(), "Iron Bullet");
        add(ModItems.GLASS_BULLET.get(), "Glass Bullet");

        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.empty", "Uncraftable Tipped Bullet");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.night_vision", "Bullet of Night Vision");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.invisibility", "Bullet of Invisibility");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.leaping", "Bullet of Leaping");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.fire_resistance", "Bullet of Fire Resistance");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.swiftness", "Bullet of Swiftness");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.slowness", "Bullet of Slowness");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.water_breathing", "Bullet of Water Breathing");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.healing", "Bullet of Healing");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.harming", "Bullet of Harming");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.poison", "Bullet of Poison");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.regeneration", "Bullet of Regeneration");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.strength", "Bullet of Strength");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.weakness", "Bullet of Weakness");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.levitation", "Bullet of Levitation");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.luck", "Bullet of Luck");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.turtle_master", "Bullet of the Turtle Master");
        add(ModItems.TIPPED_BULLET.get().getDescriptionId()+".effect.slow_falling", "Bullet of Slow Falling");

        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.empty", "Uncraftable Splash Bullet");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.water", "Splash Water Bullet");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.mundane", "Mundane Splash Bullet");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.thick", "Thick Splash Bullet");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.awkward", "Awkward Splash Bullet");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.night_vision", "Splash Bullet of Night Vision");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.invisibility", "Splash Bullet of Invisibility");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.leaping", "Splash Bullet of Leaping");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.fire_resistance", "Splash Bullet of Fire Resistance");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.swiftness", "Splash Bullet of Swiftness");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.slowness", "Splash Bullet of Slowness");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.water_breathing", "Splash Bullet of Water Breathing");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.healing", "Splash Bullet of Healing");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.harming", "Splash Bullet of Harming");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.poison", "Splash Bullet of Poison");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.regeneration", "Splash Bullet of Regeneration");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.strength", "Splash Bullet of Strength");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.weakness", "Splash Bullet of Weakness");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.levitation", "Splash Bullet of Levitation");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.luck", "Splash Bullet of Luck");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.turtle_master", "Splash Bullet of the Turtle Master");
        add(ModItems.SPLASH_BULLET.get().getDescriptionId()+".effect.slow_falling", "Splash Bullet of Slow Falling");

        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.empty", "Uncraftable Lingering Bullet");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.water", "Lingering Water Bullet");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.mundane", "Mundane Lingering Bullet");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.thick", "Thick Lingering Bullet");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.awkward", "Awkward Lingering Bullet");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.night_vision", "Lingering Bullet of Night Vision");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.invisibility", "Lingering Bullet of Invisibility");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.leaping", "Lingering Bullet of Leaping");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.fire_resistance", "Lingering Bullet of Fire Resistance");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.swiftness", "Lingering Bullet of Swiftness");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.slowness", "Lingering Bullet of Slowness");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.water_breathing", "Lingering Bullet of Water Breathing");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.healing", "Lingering Bullet of Healing");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.harming", "Lingering Bullet of Harming");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.poison", "Lingering Bullet of Poison");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.regeneration", "Lingering Bullet of Regeneration");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.strength", "Lingering Bullet of Strength");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.weakness", "Lingering Bullet of Weakness");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.levitation", "Lingering Bullet of Levitation");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.luck", "Lingering Bullet of Luck");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.turtle_master", "Lingering Bullet of the Turtle Master");
        add(ModItems.LINGERING_BULLET.get().getDescriptionId()+".effect.slow_falling", "Lingering Bullet of Slow Falling");

        add("tooltip.usefulguns.shift", "\u00A77Hold \u00A7fSHIFT\u00A77 for stats\u00A7r");
        add("tooltip.usefulguns.selected_bullet", "\u00A73Selected bullet: ");
        add("tooltip.usefulguns.bullet.damage", "\u00A72Damage: \u00A7f+%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.flat", "\u00A72Damage: \u00A7f+%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.mult", "\u00A72Damage: \u00A7fx%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.both", "\u00A72Damage: \u00A7fx%s +%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.flat.modified", "\u00A7dDamage: \u00A7f+%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.mult.modified", "\u00A7dDamage: \u00A7fx%s\u00A7r");
        add("tooltip.usefulguns.gun.damage.both.modified", "\u00A7dDamage: \u00A7fx%s +%s\u00A7r");
        add("tooltip.usefulguns.gun.firerate", "\u00A72Fire delay: \u00A7f%d\u00A77 RPM\u00A7r");
        add("tooltip.usefulguns.gun.firerate.modified", "\u00A7dFire delay: \u00A7f%d\u00A77 RPM\u00A7r");
        add("tooltip.usefulguns.gun.ignore_invulnerability", "Projectiles ignore invulnerability time");
        
    }
}
