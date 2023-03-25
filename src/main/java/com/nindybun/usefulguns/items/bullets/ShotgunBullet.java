package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.entities.BulletEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ShotgunBullet extends AbstractBullet{
    private int amount;
    private float spread;
    public ShotgunBullet(int damage, int amount) {
        super(damage);
        this.amount = amount;
        this.spread = amount <= 1 ? 0: amount*2;
    }

    public int getAmount(){
        return this.amount;
    }

    public float getSpread(){
        return this.spread;
    }

    @Override
    public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter, ItemStack gun) {
        BulletEntity entity = super.createProjectile(world, stack, shooter, gun);
        entity.setIgnoreInvulnerability(amount > 1 ? true : false);
        return entity;
    }
}
