package eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.PlayerChatPacket;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.ServerChatPacket;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data.TextComponent;

public class TestPacketHandler implements PacketHandler {

    @Override
    public boolean handlePlayerChat(PlayerChatPacket packet) {
        if (packet.getMessage().charAt(0) != '/') {
            packet.setMessage(packet.getMessage() + " Arrived");
        }
        // Do not drop packet
        return false;
    }

    @Override
    public boolean handleServerChat(ServerChatPacket packet) {
        TextComponent component = packet.getTextComponent();
        this.appendComponent(component, " Sent");
        return false;
    }

    private void appendComponent(TextComponent component, String string) {
        if (component.getText() != null && !component.getText().isEmpty()) {
            component.setText(component.getText() + string);
        }

        if (component.getExtra() != null && !component.getExtra().isEmpty()) {
            for (TextComponent child : component.getExtra()) {
                this.appendComponent(child, string);
            }
        }
    }
}
