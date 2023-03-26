package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToServerOpenRadial {
    public PacketToServerOpenRadial(){

    }

    public static void encode(PacketToServerOpenRadial msg, FriendlyByteBuf buffer){

    }

    public static PacketToServerOpenRadial decode(FriendlyByteBuf buffer){
        return new PacketToServerOpenRadial();
    }

    public static class Handler {
        public static void handle(PacketToServerOpenRadial msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack pouch = UtilMethods.locateAndGetPouch(player);
                if (pouch.isEmpty())
                    return;
                ItemStack gun = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
                if (!(gun.getItem() instanceof AbstractGun))
                    return;
                pouch.getOrCreateTag().put(UtilMethods.INVENTORY_TAG, UtilMethods.serializeItemTagList(UtilMethods.collectAllBullets(pouch)));
                PacketHandler.sendTo(new PacketToClientOpenRadial(pouch, gun), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
