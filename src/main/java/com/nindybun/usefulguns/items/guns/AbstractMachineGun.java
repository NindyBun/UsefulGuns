package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractMachineGun extends AbstractGun{
    private final int dirtiness;
    public AbstractMachineGun(int dirtiness, int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(dirtiness, bonusDamage, damageMultiplier, fireDelay, enchantability);
        this.dirtiness = dirtiness;
    }

    @Override
    public int shoot(IItemHandler handler, ItemStack ammo, Level world, Player player, ItemStack gun) {
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack stack = handler.getStackInSlot(i).copy().split(1);
            if (ammo.equals(stack, false)) {
                AbstractBullet abstractBullet = (AbstractBullet) (ammo.getItem() instanceof AbstractBullet ? ammo.getItem() : ModItems.FLINT_BULLET);
                BulletEntity bulletEntity = abstractBullet.createProjectile(world, ammo, player, gun);
                bulletEntity.setDamage((float) (bulletEntity.getDamage()*this.damageMultiplier+this.bonusDamage));
                bulletEntity.setIgnoreInvulnerability(true);
                bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun), 0);
                world.addFreshEntity(bulletEntity);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onUsingTick(ItemStack gun, LivingEntity livingEntity, int ticks) {
        if (livingEntity instanceof Player){
            Player playerEntity = (Player) livingEntity;
            Level world = playerEntity.level;
            int used = getUseDuration(gun) - ticks;
            if (used > 0 && used % getFireDelay(gun) == 0 && !playerEntity.level.isClientSide) {
                ItemStack pouch = UtilMethods.locateAndGetPouch(playerEntity);
                ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound(UtilMethods.BULLET_INFO_TAG));
                LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
                if (optional.isPresent()) {
                    IItemHandler handler = optional.resolve().get();
                    int shot = shoot(handler, bulletInfo, world, playerEntity, gun);
                    if (shot != -1){
                        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), fireSound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                        handler.extractItem(shot, 1, false);
                        UtilMethods.updatePouchAfterShooting(pouch, bulletInfo);
                        gun.getOrCreateTag().putInt(DIRTINESS, gun.getOrCreateTag().getInt(DIRTINESS)+1);
                    } else{
                        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), drySound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        ItemStack gun = playerEntity.getItemInHand(hand);
        ItemStack pouch = UtilMethods.locateAndGetPouch(playerEntity);
        if (gun.getOrCreateTag().getInt(DIRTINESS) == this.dirtiness && !playerEntity.level.isClientSide){
            playerEntity.sendMessage(new TextComponent("Gun's dirty! Go clean it! ;-;"), Util.NIL_UUID);
            return InteractionResultHolder.fail(gun);
        }
        ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() == Items.AIR || pouch.isEmpty()) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), drySound.get(), SoundSource.PLAYERS, 1.0f, world.getRandom().nextFloat() * 0.4F + 0.8F);
            return InteractionResultHolder.fail(gun);
        }
        playerEntity.startUsingItem(hand);
        return InteractionResultHolder.consume(gun);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    protected void addExtraTooltip(ItemStack stack, @Nullable Level world, List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID + ".machinegun.shoot"));
    }
}
