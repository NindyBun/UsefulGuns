package com.nindybun.usefulguns.entities;


import com.google.common.collect.Sets;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Set;

public class BulletEntity extends AbstractArrowEntity {
    private double damage = 1;
    private boolean ignoreInvulnerability = false;
    private int pierceLevel = 0;
    private int ticksSinceFired;
    private static final DataParameter<Integer> ID_EFFECT_COLOR = EntityDataManager.defineId(ArrowEntity.class, DataSerializers.INT);
    private Potion potion = Potions.EMPTY;
    private final Set<EffectInstance> effects = Sets.newHashSet();
    private boolean fixedColor;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity livingEntity) {
        super(ModEntities.BULLET.get(), livingEntity, world);
    }

    public void setEffectsFromItem(ItemStack stack){

    }


    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        this.remove();
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        this.level.addParticle(ParticleTypes.FLAME, d7, d2, d3 , d5, d6, d1);
    }

    @Override
    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
        super.move(p_213315_1_, p_213315_2_);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return null;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("tsf", ticksSinceFired);
        nbt.putDouble("damage", damage);
        nbt.putInt("pierceLevel", pierceLevel);
        if (ignoreInvulnerability) nbt.putBoolean("ignoreInv", ignoreInvulnerability);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        ticksSinceFired = nbt.getInt("tsf");
        damage = nbt.getDouble("damage");
        ignoreInvulnerability = nbt.getBoolean("ignoreInv");
        pierceLevel = nbt.getInt("pierceLevel");
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new NetworkHooks().getEntitySpawningPacket(this);
    }
}
