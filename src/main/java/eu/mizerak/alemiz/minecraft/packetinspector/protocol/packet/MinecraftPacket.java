package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import io.netty.buffer.ByteBuf;

public abstract class MinecraftPacket {

    public abstract void encode(ByteBuf buffer, PacketDirection direction);
    public abstract void decode(ByteBuf buffer, PacketDirection direction);

    public abstract boolean handle(PacketHandler handler, PacketDirection direction);

}
