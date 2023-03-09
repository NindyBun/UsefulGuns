package com.nindybun.usefulguns.entities;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import com.nindybun.usefulguns.modRegistries.ModItems;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class BulletEntity extends ProjectileEntity {
    private double damage = 1;
    private boolean ignoreInvulnerability = false;
    private int pierceLevel = 0;
    private boolean isShrapnel = false;
    private ItemStack bullet = ItemStack.EMPTY;
    private int ticksSinceFired;
    private static final DataParameter<Integer> ID_EFFECT_COLOR = EntityDataManager.defineId(BulletEntity.class, DataSerializers.INT);
    private static final DataParameter<Byte> PIERCE_LEVEL = EntityDataManager.defineId(BulletEntity.class, DataSerializers.BYTE);
    private Potion potion = Potions.EMPTY;
    private final Set<EffectInstance> effects = Sets.newHashSet();
    private boolean fixedColor;
    private static final double STOP_TRESHOLD = 0.01;
    private IntOpenHashSet piercingIgnoreEntityIds;
    private List<Entity> piercedAndKilledEntities;
    private final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity livingEntity) {
        super(ModEntities.BULLET.get(), world);
        this.setOwner(livingEntity);
    }

    public void setDamage(double damage){
        this.damage = damage;
    }
    public double getDamage() {
        return this.damage;
    }
    
    public void setBullet(ItemStack bullet){
        this.bullet = bullet;
    }

    public void setShrapnel(boolean shrapnel){
        this.isShrapnel = shrapnel;
    }

    public void setIgnoreInvulnerability(boolean ignoreInvulnerability){
        this.ignoreInvulnerability = ignoreInvulnerability;
    }

    public void setPierceLevel(int pierceLevel){
        this.pierceLevel = pierceLevel;
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
        } else if (bullet.getItem() instanceof AbstractBullet) {
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

    @Override
    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
        super.move(p_213315_1_, p_213315_2_);
    }

    @Override
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

    private void applyWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                double d0 = this.distanceToSqr(livingentity);
                if (d0 < 16.0D && livingentity.isSensitiveToWater()) {
                    livingentity.hurt(DamageSource.indirectMagic(livingentity, this.getOwner()), 1.0F);
                }
            }
        }

    }
    
    private void applySplash(Potion potion, @Nullable Entity p_213888_2_, Vector3d pos) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = pos.distanceToSqr(new Vector3d(livingentity.getX(), livingentity.getY(), livingentity.getZ()));
                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        if (livingentity == p_213888_2_) {
                            d1 = 1.0D;
                        }
                        for(EffectInstance effectinstance : potion.getEffects()) {
                            Effect effect = effectinstance.getEffect();
                            if (effect.isInstantenous()) {
                                effect.applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), d1);
                            } else {
                                int i = (int)(d1 * (double)Math.max(effectinstance.getDuration() / 4, 1));
                                if (i > 20) {
                                    livingentity.addEffect(new EffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(Potion potion, Vector3d pos) {
        AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, pos.x, pos.y, pos.z);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloudentity.setOwner((LivingEntity)entity);
        }
        areaeffectcloudentity.setRadius(3.0F);
        areaeffectcloudentity.setRadiusOnUse(-0.5F);
        areaeffectcloudentity.setWaitTime(10);
        areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());
        areaeffectcloudentity.setFixedColor(PotionUtils.getColor(potion));
        for (EffectInstance effectInstance : potion.getEffects()){
            areaeffectcloudentity.addEffect(new EffectInstance(effectInstance.getEffect(), Math.max(effectInstance.getDuration() / 16, 1)));
        }
        this.level.addFreshEntity(areaeffectcloudentity);
    }

    private Item isLingeringOrSplash() {
        if (this.bullet.getItem() == ModItems.LINGERING_BULLET.get())
            return Items.LINGERING_POTION;
        else if (this.bullet.getItem() == ModItems.SPLASH_BULLET.get())
            return Items.SPLASH_POTION;
        return null;
    }

    private void dowseFire(BlockPos p_184542_1_) {
        BlockState blockstate = this.level.getBlockState(p_184542_1_);
        if (blockstate.is(BlockTags.FIRE)) {
            this.level.removeBlock(p_184542_1_, false);
        } else if (CampfireBlock.isLitCampfire(blockstate)) {
            this.level.levelEvent((PlayerEntity)null, 1009, p_184542_1_, 0);
            CampfireBlock.dowse(this.level, p_184542_1_, blockstate);
            this.level.setBlockAndUpdate(p_184542_1_, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }

    }

    @Override
    protected void onHitBlock(BlockRayTraceResult rayTrace) {
        super.onHitBlock(rayTrace);
        if (!this.level.isClientSide){
            if (this.isLingeringOrSplash() != null){
                ItemStack itemstack = this.bullet;
                Potion potion = PotionUtils.getPotion(itemstack);
                List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
                boolean flag = potion == Potions.WATER && list.isEmpty();
                Direction direction = rayTrace.getDirection();
                BlockPos blockpos = rayTrace.getBlockPos();
                BlockPos blockpos1 = blockpos.relative(direction);
                if (flag) {
                    this.dowseFire(blockpos1);
                    this.dowseFire(blockpos1.relative(direction.getOpposite()));
                    for(Direction direction1 : Direction.Plane.HORIZONTAL) {
                        this.dowseFire(blockpos1.relative(direction1));
                    }
                }
                this.remove();
            }else if (!this.isShrapnel){
                BlockPos blockPos = rayTrace.getBlockPos();
                BlockState blockState = this.level.getBlockState(blockPos);
                SoundType soundType = blockState.getSoundType(this.level, blockPos, null);
                Minecraft.getInstance().level.playLocalSound(blockPos, soundType.getBreakSound(), SoundCategory.NEUTRAL,  0.8F, random.nextFloat() * 0.1F + 0.9F, false);
                IParticleData particleData = ParticleTypes.CRIT;
                Vector3d vector3d = rayTrace.getLocation();
                for(int i1 = 0; i1 < 8; ++i1) {
                    Minecraft.getInstance().level.addParticle(particleData, vector3d.x, vector3d.y, vector3d.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
                }
                this.remove();
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult rayTrace) {
        super.onHit(rayTrace);
        if (this.isShrapnel){
            if (!this.level.isClientSide) {
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
                Entity hitEntity = rayTrace.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) rayTrace).getEntity() : null;
                Entity owner = this.getOwner();
                if (!list.isEmpty()) {
                    for (LivingEntity livingentity : list) {
                        double d0 = rayTrace.getLocation().distanceToSqr(new Vector3d(livingentity.getX(), livingentity.getY(), livingentity.getZ()));
                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                            if (livingentity == hitEntity) {
                                d1 = 1.0D;
                            }
                            DamageSource damagesource;
                            if (owner == null) {
                                damagesource = (new IndirectEntityDamageSource("arrow", this, this)).setProjectile();
                            } else {
                                damagesource = (new IndirectEntityDamageSource("arrow", this, owner)).setProjectile();
                                if (owner instanceof LivingEntity) {
                                    ((LivingEntity) owner).setLastHurtMob(livingentity);
                                }
                            }
                            livingentity.hurt(damagesource, (float) (d1 * this.damage));
                        }
                    }
                }
                BlockPos blockPos = new BlockPos(rayTrace.getLocation());
                Minecraft.getInstance().level.playLocalSound(blockPos, SoundEvents.SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 0.8F, random.nextFloat() * 0.1F + 0.9F, false);
                IParticleData particleData = ParticleTypes.CRIT;
                Vector3d vector3d = rayTrace.getLocation();
                for (int i1 = 0; i1 < 8; ++i1) {
                    Minecraft.getInstance().level.addParticle(particleData, vector3d.x, vector3d.y, vector3d.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
                }
                this.remove();
            }
        }else if (this.isLingeringOrSplash() != null){
            if (!this.level.isClientSide){
                ItemStack itemstack = this.bullet;
                Potion potion = PotionUtils.getPotion(itemstack);
                List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
                boolean flag = potion == Potions.WATER && list.isEmpty();
                if (flag)
                    this.applyWater();
                else if (!list.isEmpty())
                    if (this.isLingeringOrSplash() == Items.LINGERING_POTION)
                        this.makeAreaOfEffectCloud(potion, rayTrace.getLocation());
                    else
                        this.applySplash(potion, rayTrace.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult)rayTrace).getEntity() : null, rayTrace.getLocation());

                int i = potion.hasInstantEffects() ? 2007 : 2002;
                this.level.levelEvent(i, new BlockPos(rayTrace.getLocation()), PotionUtils.getColor(itemstack));
                this.remove();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityHit) {
        Entity owner = this.getOwner();
        Entity entity = entityHit.getEntity();

        if (this.isShrapnel)
            return;

        if (this.pierceLevel > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.pierceLevel + 1) {
                this.remove();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        int lastHurt = entity.invulnerableTime;
        if (this.ignoreInvulnerability) entity.invulnerableTime = 0;

        DamageSource damagesource;
        if (owner == null) {
            damagesource = (new IndirectEntityDamageSource("arrow", this, this)).setProjectile();
        } else {
            damagesource = (new IndirectEntityDamageSource("arrow", this, owner)).setProjectile();
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(entity);
            }
        }

        boolean damaged = entity.hurt(damagesource, (float)this.damage);
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
                if (this.pierceLevel <= 0) {
                    this.remove();
                }
            }
        }else if (!damaged && ignoreInvulnerability){
            entity.invulnerableTime = lastHurt;
            this.remove();
        }
    }

    private float getWaterInertia() {
        return 0.75f;
    }

    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.getEntityHitResult(this.level, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected boolean canHitEntity(Entity p_230298_1_) {
        return super.canHitEntity(p_230298_1_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_230298_1_.getId()));
    }

    public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRot(p_180426_7_, p_180426_8_);
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

            if (entityraytraceresult == null || this.pierceLevel <= 0) {
                break;
            }

            raytraceresult = null;
        }

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        float f2 = 0.99f;
        if (this.isInWater()){
            for (int i = 0; i < 4; i++) this.level.addParticle(ParticleTypes.BUBBLE, d7 * 0.25D, d2 * 0.25D, d3 * 0.25D, d5, d6, d1);
            f2 = getWaterInertia();
        }
        else
            for (int i = 0; i < 4; i++) this.level.addParticle(ParticleTypes.FLAME, d7 * 0.25D, d2 * 0.25D, d3 * 0.25D, d5, d6, d1);
        this.setDeltaMovement(vec3.scale(f2));
        this.setPos(d5, d1, d2);
        this.checkInsideBlocks();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ID_EFFECT_COLOR, -1);
        this.entityData.define(PIERCE_LEVEL, (byte)0);

    }

    protected void doPostHurtEffects(LivingEntity entity) {
        for(EffectInstance effectinstance : this.potion.getEffects()) {
            entity.addEffect(new EffectInstance(effectinstance.getEffect(), Math.max(effectinstance.getDuration() / 8, 1), effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
        }

        if (!this.effects.isEmpty()) {
            for(EffectInstance effectinstance1 : this.effects) {
                entity.addEffect(effectinstance1);
            }
        }

    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("tsf", this.ticksSinceFired);
        nbt.putDouble("damage", this.damage);
        nbt.putInt("pierceLevel", this.pierceLevel);
        nbt.putBoolean("ignoreInv", this.ignoreInvulnerability);
        
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
        this.ticksSinceFired = nbt.getInt("tsf");
        this.damage = nbt.getDouble("damage");
        this.ignoreInvulnerability = nbt.getBoolean("ignoreInv");
        this.pierceLevel = nbt.getInt("pierceLevel");

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
