package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.WorldWorkerManager;

import javax.annotation.Nullable;
import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    public void onCraftedBy(ItemStack stack, World p_77622_2_, PlayerEntity p_77622_3_) {
        stack.getOrCreateTag().putInt(USES, this.kit.getUses());
    }

    @Override
    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
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
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        tooltip.add(new TranslationTextComponent("tooltip."+UsefulGuns.MOD_ID+".kit_uses", stack.getOrCreateTag().getInt(USES), ((BoreKit)stack.getItem()).kit.getUses()));
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
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double max = ((BoreKit)stack.getItem()).kit.getUses();
        double current = stack.getOrCreateTag().getInt(USES);
        return 1d-current/max;
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
        WOOD(ItemTier.WOOD.getUses()),
        STONE(ItemTier.STONE.getUses()),
        IRON(ItemTier.IRON.getUses()),
        GOLD(ItemTier.GOLD.getUses()),
        DIAMOND(ItemTier.DIAMOND.getUses()),
        NETHERITE((int)(ItemTier.NETHERITE.getUses()*1.5), true)
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
