package com.nindybun.usefulguns.items.guns;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.items.bullets.AbstractBullet;
import com.nindybun.usefulguns.items.bullets.ShotgunBullet;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
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
                    bulletEntity.setDamage((float) ((bulletEntity.getDamage()/shotgunBullet.getAmount())*this.damageMultiplier+this.bonusDamage));
                    bulletEntity.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, (float) getProjectileSpeed(gun),shotgunBullet.getSpread() == 0 ? 0 : 5 + (int)(Math.random() * ((shotgunBullet.getSpread() - 5) + 1)));
                    world.addFreshEntity(bulletEntity);
                }
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void addExtraTooltip(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID + ".shotgun.shoot"));
    }
}
