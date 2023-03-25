package com.nindybun.usefulguns.network.packets;

import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.items.AbstractPouch;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncToClientPouchData {
    private CompoundTag tag;
    private UUID uuid;
    public PacketSyncToClientPouchData(UUID uuid, CompoundTag tag){
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(PacketSyncToClientPouchData msg, FriendlyByteBuf buffer){
        buffer.writeNbt(msg.tag);
        buffer.writeUUID(msg.uuid);
    }

    public static PacketSyncToClientPouchData decode(FriendlyByteBuf buffer){
        return new PacketSyncToClientPouchData(buffer.readUUID(), buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketSyncToClientPouchData msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    LazyOptional<IItemHandler> optional = PouchManager.get().getCapability(msg.uuid);
                    optional.ifPresent(hander ->{
                        ((PouchHandler)hander).deserializeNBT(msg.tag);
                    });
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
