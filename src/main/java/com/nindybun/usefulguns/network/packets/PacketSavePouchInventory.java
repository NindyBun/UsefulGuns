package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSavePouchInventory {
    public PacketSavePouchInventory(){
    }

    public static void encode(PacketSavePouchInventory msg, FriendlyByteBuf buffer){
    }

    public static PacketSavePouchInventory decode(FriendlyByteBuf buffer){
        return new PacketSavePouchInventory();
    }

    public static class Handler {
        public static void handle(PacketSavePouchInventory msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack pouch = player.getMainHandItem().getItem() instanceof AbstractPouch ? player.getMainHandItem() : player.getOffhandItem();
                if (!(pouch.getItem() instanceof AbstractPouch))
                    return;
                pouch.getOrCreateTag().put(UtilMethods.INVENTORY_TAG, UtilMethods.serializeItemTagList(UtilMethods.collectAllBullets(pouch)));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
