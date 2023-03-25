package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UsefulGuns.MOD_ID);

    public static final RegistryObject<SoundEvent> GUNNER = SOUNDS.register("gunner.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "gunner.shoot")));
    public static final RegistryObject<SoundEvent> SHOTGUN = SOUNDS.register("shotgun.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "shotgun.shoot")));
    public static final RegistryObject<SoundEvent> RIFLE = SOUNDS.register("rifle.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "rifle.shoot")));
    public static final RegistryObject<SoundEvent> SNIPER = SOUNDS.register("sniper.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "sniper.shoot")));
    public static final RegistryObject<SoundEvent> PISTOL = SOUNDS.register("pistol.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "pistol.shoot")));

    public static final RegistryObject<SoundEvent> DRY_FIRED = SOUNDS.register("pistol.dry", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "pistol.dry")));

}
