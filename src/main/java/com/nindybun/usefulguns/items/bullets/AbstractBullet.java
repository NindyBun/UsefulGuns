package com.nindybun.usefulguns.items.bullets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.entities.BulletEntity;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractBullet extends Item {
    private int damage;
    private int pierceLevel = 0;

    public AbstractBullet(int damage) {
        super(ModItems.ITEM_GROUP.stacksTo(64));
        this.damage = damage;
    }

    public AbstractBullet setPierceLevel(int level){
        this.pierceLevel = level;
        return this;
    }

    public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter, ItemStack gun){
        BulletEntity entity = new BulletEntity(world, shooter);
        entity.setBullet(stack);
        entity.setDamage(damage);
        entity.setPierce(pierceLevel);
        return entity;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        if (stack.getItem() == ModItems.ARMOR_PIERCING_BULLET.get())
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.armor_piercing").withStyle(ChatFormatting.YELLOW));
        if (stack.getItem() == ModItems.HOLLOW_POINT_BULLET.get())
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.hollow_point").withStyle(ChatFormatting.YELLOW));
        if (stack.getItem() == ModItems.DRAGONS_BREATH_BULLET.get())
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.dragonsbreath").withStyle(ChatFormatting.YELLOW));
        if (stack.getItem() == ModItems.DRAGONS_FIREBALL_BULLET.get())
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.dragonsfireball").withStyle(ChatFormatting.YELLOW));
        if (stack.getItem() == ModItems.GLASS_BULLET.get())
            tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.glass").withStyle(ChatFormatting.YELLOW));


        tooltip.add(new TranslatableComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.damage)));
    }
}
