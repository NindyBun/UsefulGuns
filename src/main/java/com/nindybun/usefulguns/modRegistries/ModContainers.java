package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.gui.PouchContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<MenuType<PouchContainer>> POUCH_CONTAINER = CONTAINERS.register("pouch_container", () -> IForgeMenuType.create(PouchContainer::fromNetwork));
}
