package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, UsefulGuns.MOD_ID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITY.register("bullet",
            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, EntityClassification.MISC)
                    .sized(0.3125f, 0.3125f).clientTrackingRange(4).setUpdateInterval(20).setShouldReceiveVelocityUpdates(true).build(UsefulGuns.MOD_ID+":bullet"));
}
