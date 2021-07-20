package eu.mizerak.alemiz.minecraft.packetinspector.protocol;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.MinecraftPacket;

public interface PacketFactory {

    MinecraftPacket newInstance();
}
