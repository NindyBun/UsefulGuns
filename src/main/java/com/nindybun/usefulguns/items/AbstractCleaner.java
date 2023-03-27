package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractCleaner extends Item {
    public AbstractCleaner(int durability) {
        super(ModItems.ITEM_GROUP.defaultDurability(durability));
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return p_77636_1_.getItem() == ModItems.ULTIMATE_CLEANER.get();
    }

    @Override
    public void onUsingTick(ItemStack cleaner, LivingEntity livingEntity, int tick) {
        if (livingEntity instanceof Player){
            Player player = (Player) livingEntity;
            ItemStack gun = player.getOffhandItem();
            int used = getUseDuration(cleaner) - tick;
            if (used > 0 && !player.level.isClientSide && gun.getOrCreateTag().getInt(AbstractGun.DIRTINESS) != 0){
                if (cleaner.getItem() != ModItems.ULTIMATE_CLEANER.get()) cleaner.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                gun.getOrCreateTag().putInt(AbstractGun.DIRTINESS, gun.getOrCreateTag().getInt(AbstractGun.DIRTINESS)-1);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack cleaner = player.getItemInHand(hand);
        ItemStack gun = player.getOffhandItem();
        if (!(gun.getItem() instanceof AbstractGun))
            return InteractionResultHolder.fail(cleaner);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(cleaner);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        if (p_77624_1_.getItem() == ModItems.ULTIMATE_CLEANER.get()) p_77624_3_.add(Component.translatable("tooltip." + UsefulGuns.MOD_ID + ".unbreakable").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 7200;
    }
}
