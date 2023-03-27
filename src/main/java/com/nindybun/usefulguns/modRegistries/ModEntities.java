package com.nindybun.usefulguns.modRegistries;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UsefulGuns.MOD_ID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITY.register("bullet",
            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.3125f, 0.3125f).clientTrackingRange(4).setUpdateInterval(20).setShouldReceiveVelocityUpdates(true).build(UsefulGuns.MOD_ID+":bullet"));
}
