package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.events.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToClientOpenRadial {
    private ItemStack pouch, gun;
    public PacketToClientOpenRadial(ItemStack pouch, ItemStack gun){
        this.pouch = pouch;
        this.gun = gun;
    }

    public static void encode(PacketToClientOpenRadial msg, FriendlyByteBuf buffer){
        buffer.writeItemStack(msg.pouch, false);
        buffer.writeItemStack(msg.gun, false);
    }

    public static PacketToClientOpenRadial decode(FriendlyByteBuf buffer){
        return new PacketToClientOpenRadial(buffer.readItem(), buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketToClientOpenRadial msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ClientEvents.openMenu(msg.pouch, msg.gun);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
