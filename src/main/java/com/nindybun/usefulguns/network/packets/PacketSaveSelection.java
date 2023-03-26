package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSaveSelection {
    private ItemStack itemStack;

    public PacketSaveSelection(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public static void encode(PacketSaveSelection msg, FriendlyByteBuf buffer){
        buffer.writeItemStack(msg.itemStack, false);
    }

    public static PacketSaveSelection decode(FriendlyByteBuf buffer){
        return new PacketSaveSelection(buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketSaveSelection msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack tool = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
                if (!(tool.getItem() instanceof AbstractGun))
                    return;
                ItemStack stack = msg.itemStack.copy().split(1);
                tool.getOrCreateTag().put(UtilMethods.BULLET_INFO_TAG, stack.serializeNBT());
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
