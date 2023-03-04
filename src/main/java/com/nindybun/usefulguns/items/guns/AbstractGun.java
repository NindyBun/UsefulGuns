package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.modRegistries.ModSounds;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.block.SoundType;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AbstractGun extends Item {
    private final int bonusDamage;
    private final double damageMultiplier;
    private final int fireDelay;
    private double projectileSpeed = 3;
    private final int enchantability;
    private final boolean ignoreInvulnerability = false;
    private Supplier<SoundEvent> fireSound = ModSounds.PISTOL::get;
    private Supplier<SoundEvent> drySound = ModSounds.DRY_FIRED::get;

    public AbstractGun(int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(ModItems.ITEM_GROUP.stacksTo(1));
        this.bonusDamage = bonusDamage;
        this.damageMultiplier = damageMultiplier;
        this.fireDelay = fireDelay;
        this.enchantability = enchantability;
    }

    public AbstractGun projectileSpeed(int projectileSpeed){
        this.projectileSpeed = projectileSpeed;
        return this;
    }

    public AbstractGun fireSound(Supplier<SoundEvent> fireSound){
        this.fireSound = fireSound;
        return this;
    }

    public boolean shoot(LazyOptional<IItemHandler> optional, ItemStack ammo, World world, PlayerEntity player, ItemStack gun){
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            for (int i = 0; i < handler.getSlots(); i++){
                ItemStack stack = handler.getStackInSlot(i).copy().split(1);
                if (ammo.equals(stack, false)) {
                    AbstractBullet abstractBullet = (AbstractBullet) (ammo.getItem() instanceof AbstractBullet ? ammo.getItem() : ModItems.FLINT_BULLET);
                    BulletEntity bulletEntity = abstractBullet.createProjectile(world, ammo, player);
                    bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun), 0);
                    bulletEntity.setDamage(bulletEntity.getDamage()+this.damageMultiplier*this.bonusDamage);
                    world.addFreshEntity(bulletEntity);
                    handler.extractItem(i, 1, false);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack gun = playerEntity.getItemInHand(hand);
        ItemStack pouch = Util.locateAndGetPouch(playerEntity);
        if (pouch == null)
            return ActionResult.fail(gun);
        if (!world.isClientSide){
            ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
            LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
            if (shoot(optional, bulletInfo, world, playerEntity, gun))
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.fireSound.get(), SoundCategory.PLAYERS, 0.8f, world.getRandom().nextFloat() * 0.4F + 0.8F);
            else
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), this.drySound.get(), SoundCategory.PLAYERS, 0.8f, world.getRandom().nextFloat() * 0.4F + 0.8F);
        }
         playerEntity.getCooldowns().addCooldown(this, getFireDelay(gun));
        return ActionResult.consume(gun);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean held) {
        //An annoying hack to update the pouch so that the radial menu can see among other classes
        super.inventoryTick(stack, world, entity, slot, held);
        PlayerEntity player = null;
        if (entity instanceof PlayerEntity) player = (PlayerEntity) entity;
        if (!held || player == null)
            return;
        ItemStack pouch = Util.locateAndGetPouch(player);
        if (pouch == null)
            return;
        AbstractPouch.getData(pouch);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        ItemStack bulletInfo = ItemStack.of(stack.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() != Items.AIR) tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".selected_bullet").append(new StringTextComponent(bulletInfo.getHoverName().getString()).withStyle(TextFormatting.WHITE)));

        if (Screen.hasShiftDown()){
            double damageMultiplier = getDamageMultipier(stack);
            double damageBonus = getBonusDamage(stack);

            if (damageMultiplier != 1) {
                if (damageBonus != 0) tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.both" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageMultiplier), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageBonus)));
                else tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.mult" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageMultiplier)));
            }
            else if (damageBonus != 0) tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".gun.damage.flat" + (isDamageModified(stack) ? ".modified" : ""), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageBonus)));


            int fireDelay = getFireDelay(stack);
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID + ".gun.firerate" + (isFireDelayModified(stack) ? ".modified" : ""), (60*20)/fireDelay));

            if (ignoresInvulnerability(stack))
                tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID + ".gun.ignores_invulnerability").withStyle(TextFormatting.GRAY));

        }else{
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID + ".shift"));
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
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
