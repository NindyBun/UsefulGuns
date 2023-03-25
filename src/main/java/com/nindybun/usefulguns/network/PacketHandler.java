package com.nindybun.usefulguns.network;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.network.packets.PacketSaveSelection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "2";
    private static int index = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(UsefulGuns.MOD_ID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register(){
        int id = 0;
        INSTANCE.registerMessage(id++, PacketSaveSelection.class, PacketSaveSelection::encode, PacketSaveSelection::decode, PacketSaveSelection.Handler::handle);

    }

    public static void send(Object msg, Supplier playerEntity){
        INSTANCE.send(PacketDistributor.PLAYER.with(playerEntity), msg);
    }

    public static void sendTo(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer))
            INSTANCE.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object msg){
        INSTANCE.sendToServer(msg);
    }
}
