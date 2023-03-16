package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.items.guns.AbstractMachineGun;
import com.nindybun.usefulguns.items.guns.AbstractShotgun;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MiningBullet extends AbstractBullet{
    private int harvestLevel = 0;
    public MiningBullet(int damage) {
        super(damage);
    }

    public MiningBullet setHarvestLevel(int harvestLevel){
        this.harvestLevel = harvestLevel;
        return this;
    }

    public int getHarvestLevel(){
        return this.harvestLevel;
    }

    @Override
    public BulletEntity createProjectile(World world, ItemStack stack, LivingEntity shooter, ItemStack gun) {
        BulletEntity entity = super.createProjectile(world, stack, shooter, gun);
        entity.setPierce(1);
        entity.setMiningArea(((AbstractGun)gun.getItem()).getType().getSize());
        return entity;
    }
}
