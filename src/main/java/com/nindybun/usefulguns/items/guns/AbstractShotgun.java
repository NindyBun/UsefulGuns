package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.ShotgunBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.Map;

public class AbstractShotgun extends AbstractGun{
    private double damageMultiplier;
    private int bonusDamage;
    public AbstractShotgun(int bonusDamage, double damageMultiplier, int fireDelay, int enchantability) {
        super(bonusDamage, damageMultiplier, fireDelay, enchantability);
        this.bonusDamage = bonusDamage;
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int shoot(IItemHandler handler, ItemStack ammo, World world, PlayerEntity player, ItemStack gun){
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack stack = handler.getStackInSlot(i).copy().split(1);
            if (ammo.equals(stack, false)) {
                ShotgunBullet shotgunBullet = (ShotgunBullet) (ammo.getItem() instanceof ShotgunBullet ? ammo.getItem() : ModItems.BUCKSHOT_BULLET);
                for (int j = 0; j < shotgunBullet.getAmount(); j++){
                    BulletEntity bulletEntity = shotgunBullet.createProjectile(world, ammo, player);
                    bulletEntity.setDamage((bulletEntity.getDamage()*this.damageMultiplier+this.bonusDamage));
                    bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun), shotgunBullet.getSpread());
                    world.addFreshEntity(bulletEntity);
                }
                return i;
            }
        }
        return -1;
    }

}
