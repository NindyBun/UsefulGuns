package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AbstractBullet extends Item{
    private int damage;
    private int pierceLevel;

    public AbstractBullet(int damage, int pierceLevel) {
        super(ModItems.ITEM_GROUP.stacksTo(64));
        this.damage = damage;
        this.pierceLevel = pierceLevel;
    }

    public BulletEntity createProjectile(World world, ItemStack stack, LivingEntity shooter){
        BulletEntity entity = new BulletEntity(world, shooter);
        entity.setDamage(damage);
        entity.setPierceLevel((byte)pierceLevel);
        entity.setEffectsFromItem(stack);
        return entity;
    }

}
