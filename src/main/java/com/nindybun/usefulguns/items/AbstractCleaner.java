package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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
    public void onUseTick(World world, LivingEntity livingEntity, ItemStack cleaner, int tick) {
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) livingEntity;
            ItemStack gun = player.getOffhandItem();
            int used = getUseDuration(cleaner) - tick;
            if (used > 0 && !player.level.isClientSide && gun.getOrCreateTag().getInt(AbstractGun.DIRTYNESS) != 0){
                if (cleaner.getItem() != ModItems.ULTIMATE_CLEANER.get()) cleaner.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                gun.getOrCreateTag().putInt(AbstractGun.DIRTYNESS, gun.getOrCreateTag().getInt(AbstractGun.DIRTYNESS)-1);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack cleaner = player.getItemInHand(hand);
        ItemStack gun = player.getOffhandItem();
        if (!(gun.getItem() instanceof AbstractGun))
            return ActionResult.fail(cleaner);

        player.startUsingItem(hand);
        return ActionResult.consume(cleaner);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        if (p_77624_1_.getItem() == ModItems.ULTIMATE_CLEANER.get()) p_77624_3_.add(new TranslationTextComponent("tooltip." + UsefulGuns.MOD_ID + ".unbreakable").withStyle(TextFormatting.GOLD));
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 7200;
    }
}
