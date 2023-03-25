package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BoreKit extends Item {
    private final Kit kit;
    public static final String USES = "Uses";
    public BoreKit(Kit kit) {
        super(
                kit.isFireResistant()
                        ? ModItems.ITEM_GROUP.fireResistant()
                        : ModItems.ITEM_GROUP);
        this.kit = kit;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level p_77622_2_, Player p_77622_3_) {
        stack.getOrCreateTag().putInt(USES, this.kit.getUses());
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)){
            ItemStack uses = new ItemStack(this);
            uses.getOrCreateTag().putInt(USES, this.kit.uses);
            p_150895_2_.add(uses);
        }
        super.fillItemCategory(p_150895_1_, p_150895_2_);
    }

    public Kit getKit(){
        return this.kit;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        tooltip.add(new TranslatableComponent("tooltip."+UsefulGuns.MOD_ID+".kit_uses", stack.getOrCreateTag().getInt(USES), ((BoreKit)stack.getItem()).kit.getUses()));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return stack.getOrCreateTag().getInt(USES) > 0;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        copy.getOrCreateTag().putInt(USES, copy.getOrCreateTag().getInt(USES)-1);
        return copy;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float max = ((BoreKit)stack.getItem()).kit.getUses();
        float current = stack.getOrCreateTag().getInt(USES);
        return Mth.hsvToRgb( Math.max(0.0f, current*13f/max) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float max = ((BoreKit)stack.getItem()).kit.getUses();
        float current = stack.getOrCreateTag().getInt(USES);
        return Math.round((current*13f)/max);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        return enchantments.containsKey(Enchantments.BLOCK_FORTUNE) || enchantments.containsKey(Enchantments.SILK_TOUCH);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_FORTUNE || enchantment == Enchantments.SILK_TOUCH;
    }

    public enum Kit{
        WOOD(Tiers.WOOD.getUses()),
        STONE(Tiers.STONE.getUses()),
        IRON(Tiers.IRON.getUses()),
        GOLD(Tiers.GOLD.getUses()),
        DIAMOND(Tiers.DIAMOND.getUses()),
        NETHERITE((int)(Tiers.NETHERITE.getUses()*1.5), true)
        ;

        private int uses;
        private boolean isFireResistant;

        Kit(int uses, boolean isFireResistant){
            this.uses = uses;
            this.isFireResistant = isFireResistant;
        }

        Kit(int uses){
            this(uses, false);
        }

        public int getUses(){
            return this.uses;
        }

        public boolean isFireResistant(){
            return this.isFireResistant;
        }

    }
}
