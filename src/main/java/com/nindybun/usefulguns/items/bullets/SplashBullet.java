package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class SplashBullet extends AbstractBullet{
    private int damage;
    public SplashBullet(int damage) {
        super(damage);
        this.damage = damage;
    }

    @Override
    public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter, ItemStack gun) {
        BulletEntity entity = super.createProjectile(world, stack, shooter, gun);
        entity.setEffectsFromItem(stack);
        return entity;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            for(Potion potion : ForgeRegistries.POTIONS) {
                if (potion != Potions.EMPTY) {
                    p_150895_2_.add(PotionUtils.setPotion(new ItemStack(this), potion));
                }
            }
        }
        super.fillItemCategory(p_150895_1_, p_150895_2_);
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return super.isFoil(p_77636_1_) || !PotionUtils.getMobEffects(p_77636_1_).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        PotionUtils.addPotionTooltip(p_77624_1_, tooltip, 0.25F);
        tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.damage)));

    }

    public String getDescriptionId(ItemStack p_77667_1_) {
        return PotionUtils.getPotion(p_77667_1_).getName(this.getDescriptionId() + ".effect.");
    }
}
