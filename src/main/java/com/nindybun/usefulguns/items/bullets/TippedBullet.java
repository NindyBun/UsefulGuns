package com.nindybun.usefulguns.items.bullets;


import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class TippedBullet extends AbstractBullet{
    public TippedBullet(int damage) {
        super(damage, 0);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
    }

    @Override
    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            for(Potion potion : ForgeRegistries.POTION_TYPES) {
                if (!potion.getEffects().isEmpty()) {
                    p_150895_2_.add(PotionUtils.setPotion(new ItemStack(this), potion));
                }
            }
        }
        super.fillItemCategory(p_150895_1_, p_150895_2_);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        PotionUtils.addPotionTooltip(p_77624_1_, p_77624_3_, 0.125F);
    }

    public String getDescriptionId(ItemStack p_77667_1_) {
        return PotionUtils.getPotion(p_77667_1_).getName(this.getDescriptionId() + ".effect.");
    }
}
