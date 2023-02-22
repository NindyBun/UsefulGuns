package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.gui.PouchContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UsefulGuns.MOD_ID);

    public static final RegistryObject<ContainerType<PouchContainer>> POUCH_CONTAINER = CONTAINERS.register("pouch_container", () -> IForgeContainerType.create(PouchContainer::fromNetwork));
}
