package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncPouchData {
    public PacketSyncPouchData(){

    }

    public static void encode(PacketSyncPouchData msg, FriendlyByteBuf buffer){

    }

    public static PacketSyncPouchData decode(FriendlyByteBuf buffer){
        return new PacketSyncPouchData();
    }

    public static class Handler {
        public static void handle(PacketSyncPouchData msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack pouch = UtilMethods.locateAndGetPouch(player);
                if (pouch.isEmpty())
                    return;

                PouchData data = AbstractPouch.getData(pouch);
                UUID uuid = data.getUuid();
                CompoundTag tag = new CompoundTag();
                data.getOptional().ifPresent(iItemHandler -> tag.put("ClientInv", ((PouchHandler)iItemHandler).serializeNBT()));

                if (!tag.isEmpty())
                    PacketHandler.sendTo(new PacketSyncToClientPouchData(uuid, tag), player);

            });
            ctx.get().setPacketHandled(true);
        }
    }
}
