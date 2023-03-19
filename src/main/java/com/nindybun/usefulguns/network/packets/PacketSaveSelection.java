package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketSaveSelection {
    private ItemStack itemStack;

    public PacketSaveSelection(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public static void encode(PacketSaveSelection msg, PacketBuffer buffer){
        buffer.writeItemStack(msg.itemStack, false);
    }

    public static PacketSaveSelection decode(PacketBuffer buffer){
        return new PacketSaveSelection(buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketSaveSelection msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack tool = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
                if (!(tool.getItem() instanceof AbstractGun))
                    return;
                //tool.getOrCreateTag().put("Bullet_Info", msg.itemStack.getOrCreateTag().copy());
                ItemStack stack = msg.itemStack;
                tool.getOrCreateTag().put("Bullet_Info",stack.serializeNBT());
                //AbstractGun.setBullet(tool, msg.itemStack);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
