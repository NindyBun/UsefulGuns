package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.events.ClientStuff;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketClientOpenRadialMenu {
    private ItemStack gun, pouch;


    public PacketClientOpenRadialMenu(ItemStack gun, ItemStack pouch){
        this.gun = gun;
        this.pouch = pouch;
    }

    public static void encode(PacketClientOpenRadialMenu msg, PacketBuffer buffer){
        buffer.writeItemStack(msg.gun, false);
        buffer.writeItemStack(msg.pouch, false);
    }

    public static PacketClientOpenRadialMenu decode(PacketBuffer buffer){
        return new PacketClientOpenRadialMenu(buffer.readItem(), buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketClientOpenRadialMenu msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
                    ClientStuff.openradialMenu(msg.gun, msg.pouch);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
