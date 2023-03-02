package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UsefulGuns.MOD_ID);

    public static final RegistryObject<SoundEvent> PISTOL = SOUNDS.register("pistol.shoot", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "pistol.shoot")));
    public static final RegistryObject<SoundEvent> DRY_FIRED = SOUNDS.register("pistol.dry", () -> new SoundEvent(new ResourceLocation(UsefulGuns.MOD_ID, "pistol.dry")));


}
