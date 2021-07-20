package eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.ServerChatPacket;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.PlayerChatPacket;

public interface PacketHandler {

    default boolean handlePlayerChat(PlayerChatPacket packet) {
        return false;
    }

    default boolean handleServerChat(ServerChatPacket packet) {
        return false;
    }
}
