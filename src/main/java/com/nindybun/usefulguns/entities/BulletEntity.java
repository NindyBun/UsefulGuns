package com.nindybun.usefulguns.entities;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.modRegistries.ModBlocks;
import com.nindybun.usefulguns.modRegistries.ModEntities;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.UtilMethods;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class BulletEntity extends AbstractArrow {
    private float damage = 1f;
    private int pierceLevel = 0;
    private boolean ignoreInvulnerability = false;
    private ItemStack bullet = ItemStack.EMPTY;
    private int ticksSinceFired;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.INT);
    private Potion potion = Potions.EMPTY;
    private final Set<MobEffectInstance> effects = Sets.newHashSet();
    private boolean fixedColor;
    private static final double STOP_TRESHOLD = 0.01;
    private IntOpenHashSet piercingIgnoreEntityIds;
    private List<Entity> piercedAndKilledEntities;
    private final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;
    private UUID ownerUUID;
    private int ownerNetworkId;
    private boolean leftOwner;
    private Vec3 shotAngle = new Vec3(0, 0, 0);
    private Vec3 shotPos = new Vec3(0, 0, 0);
    private int miningArea = 0;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, Level world) {
        super(entityType, world);
    }

    public BulletEntity(Level world, LivingEntity livingEntity) {
        super(ModEntities.BULLET.get(), livingEntity, world);
        this.setOwner(livingEntity);
        Player player = (Player)livingEntity;
        this.shotAngle = player.getLookAngle();
        this.shotPos = new Vec3(player.getX(), player.getEyeY()-(double)0.1f, player.getZ());
    }

    @Override
    public void shoot(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        super.shoot(p_70186_1_, p_70186_3_, p_70186_5_, p_70186_7_, p_70186_8_);
    }

    public void setMiningArea(int area){
        this.miningArea = area;
    }

    public void setDamage(float damage){
        this.damage = damage;
    }
    public float getDamage() {
        return this.damage;
    }
    
    public void setBullet(ItemStack bullet){
        this.bullet = bullet;
    }

    public void setIgnoreInvulnerability(boolean ignoreInvulnerability){
        this.ignoreInvulnerability = ignoreInvulnerability;
    }

    public void setPierce(int level){
        this.pierceLevel = level;
    }

    public void setEffectsFromItem(ItemStack bullet){
        if (bullet.getItem() == ModItems.TIPPED_BULLET.get()) {
            this.potion = PotionUtils.getPotion(bullet);
            List<MobEffectInstance> collection = PotionUtils.getCustomEffects(bullet);
            if (!collection.isEmpty()) {
                for(MobEffectInstance effectinstance : collection) {
                    this.effects.add(new MobEffectInstance(effectinstance));
                }
            }

            int i = getCustomColor(bullet);
            if (i == -1) {
                this.updateColor();
            } else {
                this.setFixedColor(i);
            }
        } else if (bullet.getItem() != ModItems.TIPPED_BULLET.get()) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.set(ID_EFFECT_COLOR, -1);
        }
    }

    public static int getCustomColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
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

    public void addEffect(MobEffectInstance p_184558_1_) {
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

    private void applyWater() {
        AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
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

    private void applySplash(Potion potion, @Nullable Entity p_213888_2_, Vec3 pos) {
        AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = pos.distanceToSqr(new Vec3(livingentity.getX(), livingentity.getY(), livingentity.getZ()));
                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        if (livingentity == p_213888_2_) {
                            d1 = 1.0D;
                        }
                        for(MobEffectInstance effectinstance : potion.getEffects()) {
                            MobEffect effect = effectinstance.getEffect();
                            if (effect.isInstantenous()) {
                                effect.applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), d1);
                            } else {
                                int i = (int)(d1 * (double)Math.max(effectinstance.getDuration() / 4, 1));
                                if (i > 20) {
                                    livingentity.addEffect(new MobEffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(Potion potion, Vec3 pos) {
        AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, pos.x, pos.y, pos.z);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloudentity.setOwner((LivingEntity)entity);
        }
        areaeffectcloudentity.setRadius(3.0F);
        areaeffectcloudentity.setDuration(150);
        areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());
        areaeffectcloudentity.setFixedColor(PotionUtils.getColor(potion));
        for (MobEffectInstance effectInstance : potion.getEffects()){
            areaeffectcloudentity.addEffect(new MobEffectInstance(effectInstance.getEffect(), Math.max(effectInstance.getDuration() / 16, 1)));
        }
        this.level.addFreshEntity(areaeffectcloudentity);
    }

    private void makeDragonsBreathCloud(Vec3 pos) {
        AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, pos.x, pos.y, pos.z);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloudentity.setOwner((LivingEntity)entity);
        }
        areaeffectcloudentity.setParticle(ParticleTypes.DRAGON_BREATH);
        areaeffectcloudentity.setRadius(3.0F);
        areaeffectcloudentity.setDuration(150);
        areaeffectcloudentity.setRadiusPerTick((5.0F - areaeffectcloudentity.getRadius()) / (float)areaeffectcloudentity.getDuration());
        areaeffectcloudentity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
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
            this.level.levelEvent((Player)null, 1009, p_184542_1_, 0);
            CampfireBlock.dowse((Player)null, this.level, p_184542_1_, blockstate);
            this.level.setBlockAndUpdate(p_184542_1_, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult rayTrace) {
        super.onHitBlock(rayTrace);
        BlockPos blockPos = rayTrace.getBlockPos();
        BlockState blockState = this.level.getBlockState(blockPos);
        SoundType soundType = blockState.getSoundType(this.level, blockPos, null);
        this.setSoundEvent(soundType.getBreakSound());
        this.playSound(soundType.getBreakSound(), 1.0F, random.nextFloat() * 0.1F + 0.9F);
        Vec3 vector3d = rayTrace.getLocation();
        for (int i1 = 0; i1 < 8; ++i1) {
            this.level.addParticle(ParticleTypes.CRIT, vector3d.x, vector3d.y, vector3d.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
        }
        if (this.bullet.getItem() == ModItems.DRAGONS_BREATH_BULLET.get() && !this.level.isClientSide) {
            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                BlockPos blockpos = rayTrace.getBlockPos().relative(rayTrace.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
                }
            }
            this.discard();
        }else if (this.isLingeringOrSplash() != null && !this.level.isClientSide){
            ItemStack itemstack = this.bullet;
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
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
            this.discard();
        }else if (this.bullet.getItem() instanceof MiningBullet){
            int harvestLevel = ((MiningBullet)this.bullet.getItem()).getHarvestLevel();
            BlockPos pos = rayTrace.getBlockPos();
            Direction direction = rayTrace.getDirection();
            if (this.miningArea == 1) this.mineBlock(pos, harvestLevel);
            else if (this.miningArea == 3) {
                for (int i = 0; i < 3; i++) {
                    this.mineBlock(pos, harvestLevel);
                    pos = pos.relative(direction.getOpposite());
                }
            }else if (this.miningArea == 9){
                Vec3i dim = UtilMethods.getDim(direction);
                for (int xPos = pos.getX() - dim.getX(); xPos <= pos.getX() +dim.getX(); ++xPos) {
                    for (int yPos = pos.getY() - dim.getY(); yPos <= pos.getY() + dim.getY(); ++yPos) {
                        for (int zPos = pos.getZ() - dim.getZ(); zPos <= pos.getZ() + dim.getZ(); ++zPos) {
                            this.mineBlock(new BlockPos(xPos, yPos, zPos), harvestLevel);
                        }
                    }
                }
            }
            this.discard();
        }
        this.discard();
    }
    
    public boolean isHarvestable(int toolLevel, BlockState state){
        if (toolLevel < 3 && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (toolLevel < 2 && state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else if (toolLevel < 1 && state.is(BlockTags.NEEDS_STONE_TOOL)) {
            return false;
        }
        return true;
    }

    public void mineBlock(BlockPos pos, int harvestLevel){
        BlockState state = this.level.getBlockState(pos);
        float destroySpeed = state.getDestroySpeed(this.level, pos);
        if (destroySpeed != -1.0f) {
            PlayerEvent.HarvestCheck event = new PlayerEvent.HarvestCheck((Player) this.getOwner(), state, this.isHarvestable(harvestLevel, state));
            MinecraftForge.EVENT_BUS.post(event);
            if (event.canHarvest() && !this.level.isClientSide){
                FluidState fluidstate = this.level.getFluidState(pos);
                boolean flag = state.onDestroyedByPlayer(this.level, pos, (Player) this.getOwner(), false, fluidstate);
                if (flag) {
                    BlockEntity tileEntity = state.hasBlockEntity() ? this.level.getBlockEntity(pos) : null;
                    Block.dropResources(state, this.level, pos, tileEntity, this.getOwner(), this.bullet);
                }
            }
        }
    }

    public void toTeleport(Entity entity, Vec3 target){
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverplayerentity = (ServerPlayer)entity;
            if (serverplayerentity.connection.getConnection().isConnected() && serverplayerentity.level == this.level && !serverplayerentity.isSleeping() && serverplayerentity.isAlive()) {
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }
                entity.teleportTo(target.x, target.y, target.z);
                entity.fallDistance = 0.0F;
                entity.hurt(DamageSource.FALL, 0.5f);
            }else if (entity != null){
                entity.teleportTo(target.x, target.y, target.z);
                entity.fallDistance = 0.0F;
            }
        }
    }

    @Override
    protected void onHit(HitResult rayTrace) {
        super.onHit(rayTrace);
        if (this.bullet.getItem() == ModItems.GLASS_BULLET.get()){
            AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
            Entity hitEntity = rayTrace.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) rayTrace).getEntity() : null;
            Entity owner = this.getOwner();
            if (!list.isEmpty()) {
                for (LivingEntity livingentity : list) {
                    double d0 = rayTrace.getLocation().distanceToSqr(new Vec3(livingentity.getX(), livingentity.getY(), livingentity.getZ()));
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
            this.playSound(SoundEvents.SPLASH_POTION_BREAK, 1.0F, random.nextFloat() * 0.1F + 0.9F);
            this.discard();
        }else if (this.isLingeringOrSplash() != null){
            if (!this.level.isClientSide){
                ItemStack itemstack = this.bullet;
                Potion potion = PotionUtils.getPotion(itemstack);
                List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
                boolean flag = potion == Potions.WATER && list.isEmpty();
                if (flag)
                    this.applyWater();
                else if (!list.isEmpty())
                    if (this.isLingeringOrSplash() == Items.LINGERING_POTION)
                        this.makeAreaOfEffectCloud(potion, rayTrace.getLocation());
                    else
                        this.applySplash(potion, rayTrace.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)rayTrace).getEntity() : null, rayTrace.getLocation());

                int i = potion.hasInstantEffects() ? 2007 : 2002;
                this.level.levelEvent(i, new BlockPos(rayTrace.getLocation()), PotionUtils.getColor(itemstack));
                this.discard();
            }
        }else if (this.bullet.getItem() == ModItems.DRAGONS_FIREBALL_BULLET.get()){
            if (!this.level.isClientSide){
                this.makeDragonsBreathCloud(rayTrace.getLocation());
                this.level.levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.discard();
            }
        }else if (this.bullet.getItem() == ModItems.EXPLOSIVE_BULLET.get()){
            Vec3i direction = new Vec3i(0, 0, 0);
            if (rayTrace.getType() == HitResult.Type.BLOCK)
                direction = UtilMethods.getLookingAt(this.level, (Player)this.getOwner(), this.shotPos, this.shotAngle, ClipContext.Fluid.NONE, this.shotPos.distanceTo(rayTrace.getLocation()) * 1.2).getDirection().getNormal();
            if (!this.level.isClientSide){
                this.level.explode(this, rayTrace.getLocation().x+direction.getX(), rayTrace.getLocation().y+direction.getY(), rayTrace.getLocation().z+direction.getZ(), 2.5F, Explosion.BlockInteraction.BREAK);
                this.discard();
            }
        }else if (this.bullet.getItem() == ModItems.ENDER_BULLET.get()){
            for(int i = 0; i < 32; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
            }
            if (!this.level.isClientSide){
                BlockHitResult blockRay = UtilMethods.getLookingAt(this.level, (Player)this.getOwner(), this.shotPos, this.shotAngle, ClipContext.Fluid.NONE, this.shotPos.distanceTo(rayTrace.getLocation()) * 1.2);
                Vec3i direction = blockRay.getDirection().getNormal();
                Vec3 pos = rayTrace.getLocation();
                if (rayTrace.getType() == HitResult.Type.BLOCK) {
                    pos = pos.add(direction.getX()*0.35, direction.getY()*0.35, direction.getZ()*0.35);
                }
                this.toTeleport(this.getOwner(), pos);
                this.discard();
            }
        }else if (this.bullet.getItem() == ModItems.TORCH_BULLET.get()){
            if (rayTrace.getType() == HitResult.Type.ENTITY)
                ((EntityHitResult)rayTrace).getEntity().hurt(DamageSource.ON_FIRE, this.damage);
            else if (rayTrace.getType() == HitResult.Type.BLOCK){
                if (!this.level.isClientSide) {
                    BlockHitResult blockRay = UtilMethods.getLookingAt(this.level, (Player)this.getOwner(), this.shotPos, this.shotAngle, ClipContext.Fluid.NONE, this.shotPos.distanceTo(rayTrace.getLocation()) * 1.2);
                    this.placeBlock((Player) this.getOwner(), blockRay);
                }
            }
            this.discard();
        }
    }

    public void placeBlock(Player player, BlockHitResult blockRay){
        BlockPlaceContext blockItemUseContext = new BlockPlaceContext(player, null, ModItems.LIGHT_ITEM.get().getDefaultInstance(), blockRay);
        if (!blockItemUseContext.canPlace() || blockItemUseContext == null || !this.level.setBlock(blockItemUseContext.getClickedPos(), ModBlocks.LIGHT.get().defaultBlockState(), 11))
            return;
        else{
            BlockPos blockPos = blockItemUseContext.getClickedPos();
            Level world = blockItemUseContext.getLevel();
            Player playerentity = blockItemUseContext.getPlayer();
            BlockState blockstate1 = world.getBlockState(blockPos);
            Block block = blockstate1.getBlock();
            if (block == blockstate1.getBlock())
                block.setPlacedBy(world, blockPos, blockstate1, playerentity, ModItems.LIGHT_ITEM.get().getDefaultInstance());
            return;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHit) {
        //super.onHitEntity(entityHit);
        Entity owner = this.getOwner();
        Entity entity = entityHit.getEntity();

        if (this.bullet.getItem() == ModItems.GLASS_BULLET.get() || this.bullet.getItem() == ModItems.ENDER_BULLET.get()){
            entity.hurt(DamageSource.thrown(this, owner), 0.0f);
            return;
        }

        if (this.pierceLevel > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.pierceLevel + 1) {
                this.discard();
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

        if (this.bullet.getItem() == ModItems.ARMOR_PIERCING_BULLET.get())
            damagesource.bypassArmor();
        if (this.bullet.getItem() == ModItems.HOLLOW_POINT_BULLET.get()){
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;
                this.damage = CombatRules.getDamageAfterAbsorb(this.damage, (float)livingentity.getArmorValue(), (float)livingentity.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
            }
        }

        boolean damaged = entity.hurt(damagesource, this.damage);
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
                if (owner != null && livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayer) {
                    ServerPlayer serverplayerentity = (ServerPlayer)owner;
                    if (this.piercedAndKilledEntities != null) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
                    }
                }
                if (this.pierceLevel <= 0) {
                    this.discard();
                }
            }
        }else if (!damaged && ignoreInvulnerability){
            entity.invulnerableTime = lastHurt;
            this.discard();
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel p_241206_1_, ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != p_241206_1_.dimension()) {
            this.setOwner((Entity)null);
        }
        return super.changeDimension(p_241206_1_, teleporter);
    }

    @Override
    protected float getWaterInertia() {
        return 0.78f;
    }

    public void setOwner(@Nullable Entity p_212361_1_) {
        if (p_212361_1_ != null) {
            this.ownerUUID = p_212361_1_.getUUID();
            this.ownerNetworkId = p_212361_1_.getId();
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            return ((ServerLevel)this.level).getEntity(this.ownerUUID);
        } else {
            return this.ownerNetworkId != 0 ? this.level.getEntity(this.ownerNetworkId) : null;
        }
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for(Entity entity1 : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (p_234613_0_) -> {
                return !p_234613_0_.isSpectator() && p_234613_0_.isPickable();
            })) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void tick() {
        ticksSinceFired++;
        if (ticksSinceFired > 100 || this.getDeltaMovement().lengthSqr() < STOP_TRESHOLD) {
            this.discard();
        }
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }

        if (this.getOwner() instanceof Player && !this.getOwner().isAlive() && this.bullet.getItem() == ModItems.ENDER_BULLET.get())
            this.discard();

        super.tick();

        if (this.level.isClientSide){
            this.makeParticle(2);
        }else if (!this.effects.isEmpty()){
            this.level.broadcastEntityEvent(this, (byte)0);
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.set(ID_EFFECT_COLOR, -1);
        }

        /*Vec3 vec3 = this.getDeltaMovement();
        this.inGroundTime = 0;
        Vec3 vector3d2 = this.position();
        Vec3 vector3d3 = vector3d2.add(vec3);
        HitResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            vector3d3 = raytraceresult.getLocation();
        }

        while(!this.removed) {
            EntityHitResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)raytraceresult).getEntity();
                Entity owner = this.getOwner();
                if (entity instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player)entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.hasImpulse = true;
            }

            if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                break;
            }

            raytraceresult = null;
        }*/

        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        //float f2 = 0.99f;
        if (this.isInWater()){
            for (int i = 0; i < 2; i++) this.level.addParticle(ParticleTypes.BUBBLE, d7-(d5/5*i), d2-(d6/5*i)+0.15625f, d3-(d1/5*i), d5, d6, d1);
            //f2 = getWaterInertia();
        }
        else
            for (int i = 0; i < 2; i++) this.level.addParticle(ParticleTypes.FLAME, d7-(d5/5*i), d2-(d6/5*i)+0.15625f, d3-(d1/5*i), d5, d6, d1);
        /*this.setDeltaMovement(vec3.scale((double)f2));
        this.setPos(d5, d1, d2);
        this.checkInsideBlocks();*/
    }

    @Override
    protected boolean canHitEntity(Entity p_230298_1_) {
        return super.canHitEntity(p_230298_1_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_230298_1_.getId()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_EFFECT_COLOR, -1);
    }


    @Override
    public void move(MoverType p_213315_1_, Vec3 p_213315_2_) {
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
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        
        if (this.bullet.getItem() == ModItems.DRAGONS_BREATH_BULLET.get())
            entity.setSecondsOnFire(5);

        for(MobEffectInstance effectinstance : this.potion.getEffects()) {
            entity.addEffect(new MobEffectInstance(effectinstance.getEffect(), Math.max(effectinstance.getDuration() / 8, 1), effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
        }

        if (!this.effects.isEmpty()) {
            for(MobEffectInstance effectinstance1 : this.effects) {
                entity.addEffect(effectinstance1);
            }
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("tsf", this.ticksSinceFired);
        nbt.putFloat("damage", this.damage);
        nbt.putInt("pierceLevel", this.pierceLevel);
        nbt.putBoolean("ignoreInv", this.ignoreInvulnerability);
        nbt.putString("shotAngle", this.shotAngle.x+":"+this.shotAngle.y+":"+this.shotAngle.z);
        nbt.putString("shotPos", this.shotPos.x+":"+this.shotPos.y+":"+this.shotPos.z);
        nbt.putInt("miningArea", this.miningArea);
        nbt.put("bullet", this.bullet.serializeNBT());
        
        if (this.potion != Potions.EMPTY && this.potion != null) {
            nbt.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (this.fixedColor) {
            nbt.putInt("Color", this.getColor());
        }

        if (!this.effects.isEmpty()) {
            ListTag listnbt = new ListTag();
            for(MobEffectInstance effectinstance : this.effects) {
                listnbt.add(effectinstance.save(new CompoundTag()));
            }
            nbt.put("CustomPotionEffects", listnbt);
        }

        if (this.ownerUUID != null) {
            nbt.putUUID("Owner", this.ownerUUID);
        }

        if (this.leftOwner) {
            nbt.putBoolean("LeftOwner", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.ticksSinceFired = nbt.getInt("tsf");
        this.damage = nbt.getFloat("damage");
        this.ignoreInvulnerability = nbt.getBoolean("ignoreInv");
        this.pierceLevel = nbt.getInt("pierceLevel");
        this.shotAngle = this.getVectorFromString(nbt.getString("shotAngle"));
        this.shotPos = this.getVectorFromString(nbt.getString("shotPos"));
        this.miningArea = nbt.getInt("miningArea");
        this.bullet = ItemStack.of(nbt.getCompound("bullet"));

        if (nbt.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(nbt);
        }

        for(MobEffectInstance effectinstance : PotionUtils.getCustomEffects(nbt)) {
            this.addEffect(effectinstance);
        }

        if (nbt.contains("Color", 99)) {
            this.setFixedColor(nbt.getInt("Color"));
        } else {
            this.updateColor();
        }

        if (nbt.hasUUID("Owner")) {
            this.ownerUUID = nbt.getUUID("Owner");
        }

        this.leftOwner = nbt.getBoolean("LeftOwner");
    }

    public Vec3 getVectorFromString(String string){
        int first = string.indexOf(":");
        int last = string.lastIndexOf(":");
        double x = Double.parseDouble(string.substring(0, first));
        double y = Double.parseDouble(string.substring(first+1, last));
        double z = Double.parseDouble(string.substring(last));
       return new Vec3(x, y, z);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new NetworkHooks().getEntitySpawningPacket(this);
    }
}
