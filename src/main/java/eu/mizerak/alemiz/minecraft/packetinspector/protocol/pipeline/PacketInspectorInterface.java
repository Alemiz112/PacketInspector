package eu.mizerak.alemiz.minecraft.packetinspector.protocol.pipeline;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketHelper;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.ProtocolCodec;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import io.netty.buffer.ByteBuf;

public interface PacketInspectorInterface {

    ProtocolCodec getCodec();
    PacketHandler getHandler();
    PacketDirection getDirection();

    default boolean accept(ByteBuf buffer) {
        if (!buffer.isReadable()) {
            return false;
        }

        int index = buffer.readerIndex();
        try {
            int packetId = PacketHelper.readInt(buffer);
            return this.getCodec().containsPacket(packetId, this.getDirection());
        } finally {
            buffer.readerIndex(index);
        }
    }
}
