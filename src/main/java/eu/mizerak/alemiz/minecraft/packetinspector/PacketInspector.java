package eu.mizerak.alemiz.minecraft.packetinspector;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.ProtocolCodec;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.TestPacketHandler;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.ServerChatPacket;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.PlayerChatPacket;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.pipeline.PacketInspectorInterfaceDecoder;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.pipeline.PacketInspectorInterfaceEncoder;
import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class PacketInspector extends JavaPlugin implements Listener {

    private static PacketInspector instance;

    private final ProtocolCodec protocolCodec = new ProtocolCodec();
    private PacketHandler handler;

    @Override
    public void onEnable() {
        if (instance == null) {
            instance = this;
        }

        this.registerDefaultPackets();
        this.setHandler(new TestPacketHandler());

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void registerDefaultPackets() {
        this.protocolCodec.registerClientBound(14, ServerChatPacket.class, ServerChatPacket::new);
        this.protocolCodec.registerServerBound(3, PlayerChatPacket.class, PlayerChatPacket::new);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Channel channel = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addBefore("decoder", PacketInspectorInterfaceDecoder.NAME, new PacketInspectorInterfaceDecoder(this.protocolCodec, this.handler));
        channel.pipeline().addBefore("encoder", PacketInspectorInterfaceEncoder.NAME, new PacketInspectorInterfaceEncoder(this.protocolCodec, this.handler));
    }

    public static PacketInspector getInstance() {
        return instance;
    }

    public static Logger logger() {
        if (instance == null) {
            throw new NullPointerException("PacketInspector was not initialized!");
        }
        return instance.getLogger();
    }

    public void setHandler(PacketHandler handler) {
        this.handler = handler;
    }

    public PacketHandler getHandler() {
        return this.handler;
    }

    public ProtocolCodec getProtocolCodec() {
        return this.protocolCodec;
    }
}
