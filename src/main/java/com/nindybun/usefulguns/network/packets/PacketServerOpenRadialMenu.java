package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketServerOpenRadialMenu {
    public PacketServerOpenRadialMenu(){
    }

    public static void encode(PacketServerOpenRadialMenu msg, PacketBuffer buffer){
    }

    public static PacketServerOpenRadialMenu decode(PacketBuffer buffer){
        return new PacketServerOpenRadialMenu();
    }

    public static class Handler {
        public static void handle(PacketServerOpenRadialMenu msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack gun = !(player.getMainHandItem().getItem() instanceof AbstractGun) ? player.getOffhandItem(): player.getMainHandItem();
                if (!(gun.getItem() instanceof AbstractGun))
                    return;

                ItemStack pouch = Util.locateAndGetPouch(player);

                if (pouch == null)
                    return;

                /*DistExecutor.unsafeCallWhenOn(Dist.CLIENT, ()->()->{
                    Minecraft.getInstance().setScreen(new BulletRadialMenu(gun, pouch));
                    return null;
                });*/
                PacketHandler.sendTo(new PacketClientOpenRadialMenu(gun, pouch), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
