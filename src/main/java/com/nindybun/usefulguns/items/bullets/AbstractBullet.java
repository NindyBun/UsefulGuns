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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractBullet extends Item{
    private int damage;

    public AbstractBullet(int damage) {
        super(ModItems.ITEM_GROUP.stacksTo(64));
        this.damage = damage;
    }

    public BulletEntity createProjectile(World world, ItemStack stack, LivingEntity shooter){
        BulletEntity entity = new BulletEntity(world, shooter);
        entity.setBullet(stack);
        entity.setDamage(damage);
        return entity;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslationTextComponent("tooltip."+ UsefulGuns.MOD_ID +".bullet.damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.damage)));
    }
}
