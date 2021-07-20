package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketHelper;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data.TextComponent;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServerChatPacket extends MinecraftPacket {

    public enum ChatType {
        CHAT,
        SYSTEM,
        GAME_INFO
    }

    private TextComponent textComponent;
    private ChatType chatType;
    private UUID senderUuid;

    @Override
    public void encode(ByteBuf buffer, PacketDirection direction) {
        PacketHelper.writeString(buffer, this.textComponent.serialize());
        buffer.writeByte(this.chatType.ordinal());
        PacketHelper.writeUUID(buffer, this.senderUuid);
    }

    @Override
    public void decode(ByteBuf buffer, PacketDirection direction) {
        this.textComponent = TextComponent.deserialize(PacketHelper.readString(buffer));
        this.chatType = ChatType.values()[buffer.readByte()];
        this.senderUuid = PacketHelper.readUUID(buffer);
    }

    @Override
    public boolean handle(PacketHandler handler, PacketDirection direction) {
        return handler.handleServerChat(this);
    }
}
