package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
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

public class AbstractGun extends Item {
    private final int bonusDamage;
    private final double damageMultiplier;
    private final int fireDelay;
    private final double projectileSpeed = 3;
    private final int enchantability;
    private final boolean ignoreInvulnerability = false;
    private final SoundEvent fireSound = SoundEvents.BLAZE_SHOOT;

    public AbstractGun(int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(ModItems.ITEM_GROUP.stacksTo(1));
        this.bonusDamage = bonusDamage;
        this.damageMultiplier = damageMultiplier;
        this.fireDelay = fireDelay;
        this.enchantability = enchantability;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack gun = playerEntity.getItemInHand(hand);
        ItemStack pouch = Util.locateAndGetPouch(playerEntity);
        if (pouch == null)
            return ActionResult.fail(gun);
        ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info")).split(1);
        if (!world.isClientSide){
           LazyOptional<IItemHandler> optional = pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if (optional.isPresent()) {
                IItemHandler handler = optional.resolve().get();
                for (int i = 0; i < handler.getSlots(); i++){
                    ItemStack stack = handler.getStackInSlot(i).copy().split(1);
                    if (bulletInfo.equals(stack, false)) {
                        handler.getStackInSlot(i).shrink(1);
                        break;
                    }
                }
            }
        }
        playerEntity.getCooldowns().addCooldown(this, getFireDelay(gun));
        return ActionResult.consume(gun);
    }

    private void shoot(World world, PlayerEntity player, ItemStack gun, ItemStack ammmo){
        AbstractBullet abstractBullet = (AbstractBullet) (ammmo.getItem() instanceof AbstractBullet ? ammmo.getItem() : ModItems.FLINT_BULLET);
        BulletEntity bulletEntity = abstractBullet.createProjectile(world, ammmo, player);
        bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun), 0);
        bulletEntity.setDamage(bulletEntity.getDamage());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean held) {
        //An annoying hack to update the pouch so that the radial menu can see
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
        if (bulletInfo != ItemStack.EMPTY) tooltip.add(new StringTextComponent("Selected Bullet: ").append(bulletInfo.getHoverName()).withStyle(TextFormatting.AQUA));

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
