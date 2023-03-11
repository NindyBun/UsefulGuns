package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.Util;
import com.sun.jna.platform.win32.NTSecApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractMachineGun extends AbstractGun{
    public AbstractMachineGun(int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(bonusDamage, damageMultiplier, fireDelay, enchantability);
    }

    @Override
    public int shoot(IItemHandler handler, ItemStack ammo, World world, PlayerEntity player, ItemStack gun) {
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack stack = handler.getStackInSlot(i).copy().split(1);
            if (ammo.equals(stack, false)) {
                AbstractBullet abstractBullet = (AbstractBullet) (ammo.getItem() instanceof AbstractBullet ? ammo.getItem() : ModItems.FLINT_BULLET);
                BulletEntity bulletEntity = abstractBullet.createProjectile(world, ammo, player);
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
    public void onUseTick(World world, LivingEntity livingEntity, ItemStack gun, int ticks) {
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity playerEntity = (PlayerEntity) livingEntity;
            int used = getUseDuration(gun) - ticks;
            if (used > 0 && used % getFireDelay(gun) == 0 && !playerEntity.level.isClientSide) {
                ItemStack pouch = Util.locateAndGetPouch(playerEntity);
                ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
                LazyOptional<IItemHandler> optional = AbstractPouch.getData(pouch).getOptional();
                if (optional.isPresent()) {
                    IItemHandler handler = optional.resolve().get();
                    int shot = shoot(handler, bulletInfo, world, playerEntity, gun);
                    if (shot != -1){
                        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), fireSound.get(), SoundCategory.PLAYERS, 0.8f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                        handler.extractItem(shot, 1, false);
                    } else{
                        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), drySound.get(), SoundCategory.PLAYERS, 0.8f, world.getRandom().nextFloat() * 0.4F + 0.8F);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack gun = playerEntity.getItemInHand(hand);
        ItemStack pouch = Util.locateAndGetPouch(playerEntity);
        if (pouch == null)
            return ActionResult.fail(gun);
        ItemStack bulletInfo = ItemStack.of(gun.getOrCreateTag().getCompound("Bullet_Info"));
        if (bulletInfo.getItem() == Items.AIR) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), drySound.get(), SoundCategory.PLAYERS, 0.8f, world.getRandom().nextFloat() * 0.4F + 0.8F);
            return ActionResult.fail(gun);
        }
        playerEntity.startUsingItem(hand);
        return ActionResult.consume(gun);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    protected void addExtraTooltip(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID + ".machinegun.shoot"));
    }
}
