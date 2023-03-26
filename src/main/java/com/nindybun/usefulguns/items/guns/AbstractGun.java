package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModSounds;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AbstractGun extends Item {
    protected final int bonusDamage;
    protected final double damageMultiplier;
    private final int fireDelay;
    private double projectileSpeed = 3;
    private final int enchantability;
    private final int dirtyness;
    private Type type;
    private final boolean ignoreInvulnerability = false;
    protected Supplier<SoundEvent> fireSound = ModSounds.PISTOL::get;
    protected Supplier<SoundEvent> drySound = ModSounds.DRY_FIRED::get;
    public static final String DIRTINESS = "Durability";

    public enum Type{
        /*
          1 = 1x1 block
          3 = 1x3 block
          9 = 3x3 block
        */
        GUN(1),
        RIFLE(3),
        GUNNER(1),
        SHOTGUN(9),
        ;
        private final int size;
        Type(int size){
            this.size = size;
        }

        public int getSize(){
            return this.size;
        }
    }

    public AbstractGun(int dirtyness, int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(ModItems.ITEM_GROUP);
        this.dirtyness = dirtyness;
        this.bonusDamage = bonusDamage;
        this.damageMultiplier = damageMultiplier;
        this.fireDelay = fireDelay;
        this.enchantability = enchantability;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float curr = stack.getOrCreateTag().getInt(DIRTINESS);
        return Mth.hsvToRgb(Math.min(this.dirtyness, 1-curr/(float) this.dirtyness) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int curr = stack.getOrCreateTag().getInt(DIRTINESS);
        return Math.round((float)curr*13.0f/ (float)this.dirtyness);
    }

    public AbstractGun setType(Type type){
        this.type = type;
        return this;
    }
    public Type getType(){
        return this.type;
    }

    public AbstractGun projectileSpeed(int projectileSpeed){
        this.projectileSpeed = projectileSpeed;
        return this;
    }

    public AbstractGun fireSound(Supplier<SoundEvent> fireSound){
        this.fireSound = fireSound;
        return this;
    }

    public int shoot(IItemHandler handler, ItemStack ammo, Level world, Player player, ItemStack gun){
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack stack = handler.getStackInSlot(i).copy().split(1);
            if (ammo.equals(stack, false) && ammo.getItem() instanceof AbstractBullet) {
                AbstractBullet abstractBullet = (AbstractBullet) ammo.getItem();
                BulletEntity bulletEntity = abstractBullet.createProjectile(world, ammo, player, gun);
                bulletEntity.setDamage((float) (bulletEntity.getDamage()*this.damageMultiplier+this.bonusDamage));
                bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun), 0);
                world.addFreshEntity(bulletEntity);
                return i;
            }
        }
        return -1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        ItemStack gun = playerEntity.getItemInHand(hand);
        ItemStack pouch = UtilMethods.locateAndGetPouch(playerEntity);
        if (!world.isClientSide){
            if (gun.getOrCreateTag().getInt(DIRTINESS) == this.dirtyness && !playerEntity.level.isClientSide){
                playerEntity.sendMessage(new TranslatableComponent("tooltip."+UsefulGuns.MOD_ID+".gun.dirty"), net.minecraft.Util.NIL_UUID);
                return InteractionResultHolder.fail(gun);
            }
            ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound(UtilMethods.BULLET_INFO_TAG));
            if (bulletInfo.getItem() == Items.AIR || pouch.isEmpty()){
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.drySound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                return InteractionResultHolder.fail(gun);
            }
            LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
            if (optional.isPresent()) {
                IItemHandler handler = optional.resolve().get();
                int shot = shoot(handler, bulletInfo, world, playerEntity, gun);
                if (shot != -1){
                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.fireSound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    handler.extractItem(shot, 1, false);
                    UtilMethods.updatePouchAfterShooting(pouch, bulletInfo);
                    gun.getOrCreateTag().putInt(DIRTINESS, gun.getOrCreateTag().getInt(DIRTINESS)+1);
                } else
                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.drySound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
            }
        }
        playerEntity.getCooldowns().addCooldown(this, getFireDelay(gun));
        return InteractionResultHolder.consume(gun);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean held) {
        //An annoying hack to update the pouch so that the radial menu can see among other classes
        super.inventoryTick(stack, world, entity, slot, held);
        Player player = null;
        if (entity instanceof Player) player = (Player) entity;
        if (!held || player == null)
            return;
        if (!world.isClientSide){
            ItemStack pouch = UtilMethods.locateAndGetPouch(player);
            if (pouch == null)
                return;
            AbstractPouch.getData(pouch);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        ItemStack bulletInfo = ItemStack.of(stack.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() != Items.AIR) tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".selected_bullet")
                .append(new TranslatableComponent(bulletInfo.getHoverName().getString()).withStyle(ChatFormatting.WHITE))
                .append(new TranslatableComponent(bulletInfo.isEnchanted() ? "tooltip."+ UsefulGuns.MOD_ID +".bullet.enchanted" : "").withStyle(ChatFormatting.GOLD)));
        tooltip.add(new TranslatableComponent("tooltip."+UsefulGuns.MOD_ID+".dirtyness", (stack.getOrCreateTag().getInt(DIRTINESS)*100)/this.dirtyness).append(new TextComponent(" \u00A77%")));

        if (Screen.hasShiftDown()){
            double damageMultiplier = getDamageMultipier(stack);
            double damageBonus = getBonusDamage(stack);

            if (damageMultiplier != 1) {
                if (damageBonus != 0) tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.both" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageMultiplier), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageBonus)));
                else tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.mult" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageMultiplier)));
            }
            else if (damageBonus != 0) tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.flat" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageBonus)));


            int fireDelay = getFireDelay(stack);
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID + ".gun.firerate" + (isFireDelayModified(stack) ? ".modified" : ""), (60*20)/fireDelay));

            if (ignoresInvulnerability(stack))
                tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID + ".gun.ignores_invulnerability").withStyle(ChatFormatting.GRAY));

        }else{
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID + ".shift"));
        }
    }

    protected void addExtraTooltip(ItemStack stack, @Nullable Level world, List<Component> tooltip){

    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static int getBonusDamage(ItemStack stack){
        return ((AbstractGun)stack.getItem()).bonusDamage;
    }
    public static double getDamageMultipier(ItemStack stack){
        return ((AbstractGun)stack.getItem()).damageMultiplier;
    }
    public static int getFireDelay(ItemStack stack){
        return ((AbstractGun)stack.getItem()).fireDelay;
    }
    public static double getProjectileSpeed(ItemStack stack){
        return ((AbstractGun)stack.getItem()).projectileSpeed;
    }
    public static boolean isDamageModified(ItemStack stack){
        return false;
    }
    public static boolean isFireDelayModified(ItemStack stack){
        return false;
    }
    public static boolean ignoresInvulnerability(ItemStack stack){
        return ((AbstractGun)stack.getItem()).ignoreInvulnerability;
    }

}
