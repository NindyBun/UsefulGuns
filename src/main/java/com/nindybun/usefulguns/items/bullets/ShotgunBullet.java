package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;


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
    public BulletEntity createProjectile(World world, ItemStack stack, LivingEntity shooter) {
        BulletEntity entity = super.createProjectile(world, stack, shooter);
        entity.setIgnoreInvulnerability(amount > 1 ? true : false);
        return entity;
    }
}
