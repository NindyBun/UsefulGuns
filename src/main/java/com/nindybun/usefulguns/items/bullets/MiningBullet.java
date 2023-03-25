package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;

public class MiningBullet extends AbstractBullet{
    private int harvestLevel = 0;
    public MiningBullet(int damage) {
        super(damage);
    }

    public MiningBullet setHarvestLevel(int harvestLevel){
        this.harvestLevel = harvestLevel;
        return this;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)){
                ItemStack stack0 = new ItemStack(this);
                p_150895_2_.add(stack0);

                stack0 = new ItemStack(this);
                stack0.enchant(Enchantments.BLOCK_FORTUNE, 1);
                p_150895_2_.add(stack0);

                stack0 = new ItemStack(this);
                stack0.enchant(Enchantments.BLOCK_FORTUNE, 2);
                p_150895_2_.add(stack0);

                stack0 = new ItemStack(this);
                stack0.enchant(Enchantments.BLOCK_FORTUNE, 3);
                p_150895_2_.add(stack0);

                stack0 = new ItemStack(this);
                stack0.enchant(Enchantments.SILK_TOUCH, 1);
                p_150895_2_.add(stack0);
        }
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getHarvestLevel(){
        return this.harvestLevel;
    }

    @Override
    public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter, ItemStack gun) {
        BulletEntity entity = super.createProjectile(world, stack, shooter, gun);
        entity.setPierce(1);
        entity.setMiningArea(((AbstractGun)gun.getItem()).getType().getSize());
        return entity;
    }

    public static float getEnchantmentID(ItemStack stack, @Nullable Level world, @Nullable LivingEntity livingEntity, int entityid){
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        float id = 0.0f;
        if (enchantments.containsKey(Enchantments.BLOCK_FORTUNE))
            id = ((float)enchantments.get(Enchantments.BLOCK_FORTUNE))/10f;
        else if (enchantments.containsKey(Enchantments.SILK_TOUCH))
            id = 0.4f;
        return id;
    }
}
