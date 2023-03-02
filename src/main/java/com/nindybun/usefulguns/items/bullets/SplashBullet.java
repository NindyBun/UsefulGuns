package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class SplashBullet extends AbstractBullet{
    private int damage;
    public SplashBullet(int damage, int pierceLevel, boolean isShrapnel) {
        super(damage, pierceLevel, isShrapnel);
        this.damage = damage;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }

    @Override
    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            for(Potion potion : ForgeRegistries.POTION_TYPES) {
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
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        PotionUtils.addPotionTooltip(p_77624_1_, tooltip, 0.25F);
        tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.damage)));

    }

    public String getDescriptionId(ItemStack p_77667_1_) {
        return PotionUtils.getPotion(p_77667_1_).getName(this.getDescriptionId() + ".effect.");
    }
}
