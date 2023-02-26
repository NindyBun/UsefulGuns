package com.nindybun.usefulguns.entities;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import com.nindybun.usefulguns.modRegistries.ModItems;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BulletEntity extends AbstractArrowEntity {
    private double damage = 1;
    private boolean ignoreInvulnerability = false;
    private int pierceLevel = 0;
    private int ticksSinceFired;
    private static final DataParameter<Integer> ID_EFFECT_COLOR = EntityDataManager.defineId(BulletEntity.class, DataSerializers.INT);
    private Potion potion = Potions.EMPTY;
    private final Set<EffectInstance> effects = Sets.newHashSet();
    private boolean fixedColor;
    private static final double STOP_TRESHOLD = 0.01;
    private IntOpenHashSet piercingIgnoreEntityIds;
    private List<Entity> piercedAndKilledEntities;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity livingEntity) {
        super(ModEntities.BULLET.get(), livingEntity, world);
    }

    public void setDamage(double damage){
        this.damage = damage;
    }

    public double getDamage(){
        return this.damage;
    }

    public void setEffectsFromItem(ItemStack bullet){
        if (bullet.getItem() == ModItems.TIPPED_BULLET.get()) {
            this.potion = PotionUtils.getPotion(bullet);
            Collection<EffectInstance> collection = PotionUtils.getCustomEffects(bullet);
            if (!collection.isEmpty()) {
                for(EffectInstance effectinstance : collection) {
                    this.effects.add(new EffectInstance(effectinstance));
                }
            }

            int i = getCustomColor(bullet);
            if (i == -1) {
                this.updateColor();
            } else {
                this.setFixedColor(i);
            }
        } else if (bullet.getItem() instanceof BucketItem) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.set(ID_EFFECT_COLOR, -1);
        }
    }

    public static int getCustomColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99) ? compoundnbt.getInt("CustomPotionColor") : -1;
    }

    private void updateColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.entityData.set(ID_EFFECT_COLOR, -1);
        } else {
            this.entityData.set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 0) {
            int i = this.getColor();
            if (i != -1) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;

                for(int j = 0; j < 20; ++j) {
                    this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                }
            }
        } else {
            super.handleEntityEvent(p_70103_1_);
        }

    }

    public void addEffect(EffectInstance p_184558_1_) {
        this.effects.add(p_184558_1_);
        this.getEntityData().set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
    }

    private void makeParticle(int p_184556_1_) {
        int i = this.getColor();
        if (i != -1 && p_184556_1_ > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for(int j = 0; j < p_184556_1_; ++j) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }

        }
    }
    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    private void setFixedColor(int p_191507_1_) {
        this.fixedColor = true;
        this.entityData.set(ID_EFFECT_COLOR, p_191507_1_);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void setPierceLevel(byte pierceLevel) {
        super.setPierceLevel(pierceLevel);
        this.pierceLevel = Byte.toUnsignedInt(pierceLevel);
    }

    @Override
    public byte getPierceLevel() {
        return (byte) this.pierceLevel;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityHit) {
        super.onHitEntity(entityHit);
        Entity owner = this.getOwner();
        Entity entity = entityHit.getEntity();

        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        int lastHurt = entity.invulnerableTime;
        if (ignoreInvulnerability) entity.invulnerableTime = 0;

        DamageSource damagesource;
        if (owner == null) {
            damagesource = (new IndirectEntityDamageSource("arrow", this, this)).setProjectile();
        } else {
            damagesource = (new IndirectEntityDamageSource("arrow", this, owner)).setProjectile();
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(entity);
            }
        }

        boolean damaged = entity.hurt(damagesource, (float)getDamage());
        if (damaged) {
            if (entity.getType() == EntityType.ENDERMAN)
                return;

            if (entity instanceof LivingEntity){
                LivingEntity livingentity = (LivingEntity)entity;
                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingentity);
                }
                this.doPostHurtEffects(livingentity);
                if (owner != null && livingentity != owner && livingentity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)owner).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)owner;
                    if (this.piercedAndKilledEntities != null) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
                    }
                }
                if (this.getPierceLevel() <= 0) {
                    this.remove();
                }
            }
        }else if (!damaged && ignoreInvulnerability){
            entity.invulnerableTime = lastHurt;
            this.remove();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.3f;
    }

    @Override
    public void tick() {
        ticksSinceFired++;
        if (ticksSinceFired > 100 || this.getDeltaMovement().lengthSqr() < STOP_TRESHOLD) {
            this.remove();
        }
        super.tick();

        if (this.level.isClientSide){
            this.makeParticle(2);
        }else if (!this.effects.isEmpty()){
            this.level.broadcastEntityEvent(this, (byte)0);
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.set(ID_EFFECT_COLOR, -1);
        }

        Vector3d vec3 = this.getDeltaMovement();
            this.inGroundTime = 0;
            Vector3d vector3d2 = this.position();
            Vector3d vector3d3 = vector3d2.add(vec3);
            RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                vector3d3 = raytraceresult.getLocation();
            }

            while(!this.removed) {
                EntityRayTraceResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                    Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
                    Entity owner = this.getOwner();
                    if (entity instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canHarmPlayer((PlayerEntity)entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.hasImpulse = true;
                }

                if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            float f2 = 0.99f;
        if (this.isInWater()){
            this.level.addParticle(ParticleTypes.BUBBLE, d7, d2, d3 , d5, d6, d1);
            f2 = getWaterInertia();
        }
        else
            this.level.addParticle(ParticleTypes.FLAME, d7, d2, d3 , d5, d6, d1);
        this.setDeltaMovement(vec3.scale((double)f2));
        this.setPos(d5, d1, d2);
        this.checkInsideBlocks();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_EFFECT_COLOR, -1);
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
    protected void doPostHurtEffects(LivingEntity p_184548_1_) {
        super.doPostHurtEffects(p_184548_1_);

        for(EffectInstance effectinstance : this.potion.getEffects()) {
            p_184548_1_.addEffect(new EffectInstance(effectinstance.getEffect(), Math.max(effectinstance.getDuration() / 8, 1), effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
        }

        if (!this.effects.isEmpty()) {
            for(EffectInstance effectinstance1 : this.effects) {
                p_184548_1_.addEffect(effectinstance1);
            }
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("tsf", ticksSinceFired);
        nbt.putDouble("damage", damage);
        nbt.putInt("pierceLevel", pierceLevel);
        nbt.putBoolean("ignoreInv", ignoreInvulnerability);
        
        if (this.potion != Potions.EMPTY && this.potion != null) {
            nbt.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (this.fixedColor) {
            nbt.putInt("Color", this.getColor());
        }

        if (!this.effects.isEmpty()) {
            ListNBT listnbt = new ListNBT();
            for(EffectInstance effectinstance : this.effects) {
                listnbt.add(effectinstance.save(new CompoundNBT()));
            }
            nbt.put("CustomPotionEffects", listnbt);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        ticksSinceFired = nbt.getInt("tsf");
        damage = nbt.getDouble("damage");
        ignoreInvulnerability = nbt.getBoolean("ignoreInv");
        pierceLevel = nbt.getInt("pierceLevel");

        if (nbt.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(nbt);
        }

        for(EffectInstance effectinstance : PotionUtils.getCustomEffects(nbt)) {
            this.addEffect(effectinstance);
        }

        if (nbt.contains("Color", 99)) {
            this.setFixedColor(nbt.getInt("Color"));
        } else {
            this.updateColor();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new NetworkHooks().getEntitySpawningPacket(this);
    }
}
