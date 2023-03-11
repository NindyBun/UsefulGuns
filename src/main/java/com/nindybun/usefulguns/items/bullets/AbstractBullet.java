package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.jws.soap.SOAPBinding;
import java.util.List;

public class AbstractBullet extends Item{
    private int damage, pierceLevel = 0;

    public AbstractBullet(int damage) {
        super(ModItems.ITEM_GROUP.stacksTo(64));
        this.damage = damage;
    }

    public AbstractBullet setPierceLevel(int level){
        this.pierceLevel = level;
        return this;
    }

    public BulletEntity createProjectile(World world, ItemStack stack, LivingEntity shooter){
        BulletEntity entity = new BulletEntity(world, shooter);
        entity.setBullet(stack);
        entity.setDamage(damage);
        entity.setPierceLevel(pierceLevel);
        return entity;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        if (stack.getItem() == ModItems.ARMOR_PIERCING_BULLET.get())
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.armor_piercing").withStyle(TextFormatting.YELLOW));
        if (stack.getItem() == ModItems.HOLLOW_POINT_BULLET.get())
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.hollow_point").withStyle(TextFormatting.YELLOW));
        if (stack.getItem() == ModItems.DRAGONS_BREATH_BULLET.get())
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.dragonsbreath").withStyle(TextFormatting.YELLOW));
        if (stack.getItem() == ModItems.DRAGONS_FIREBALL_BULLET.get())
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.dragonsfireball").withStyle(TextFormatting.YELLOW));
        if (stack.getItem() == ModItems.GLASS_BULLET.get())
            tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.glass").withStyle(TextFormatting.YELLOW));


        tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.damage)));
    }
}
