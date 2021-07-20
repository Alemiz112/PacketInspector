package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketHelper;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data.TextComponent;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

import static eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection.CLIENT_BOUND;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerChatPacket extends MinecraftPacket {

    private String message;

    @Override
    public void encode(ByteBuf buffer, PacketDirection direction) {
        PacketHelper.writeString(buffer, this.message);
    }

    @Override
    public void decode(ByteBuf buffer, PacketDirection direction) {
        this.message = PacketHelper.readString(buffer);
    }

    @Override
    public boolean handle(PacketHandler handler, PacketDirection direction) {
        return handler.handlePlayerChat(this);
    }
}
