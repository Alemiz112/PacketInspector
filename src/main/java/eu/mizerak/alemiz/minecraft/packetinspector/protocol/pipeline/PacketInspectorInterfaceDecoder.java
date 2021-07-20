package eu.mizerak.alemiz.minecraft.packetinspector.protocol.pipeline;

import eu.mizerak.alemiz.minecraft.packetinspector.PacketInspector;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketDirection;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.PacketHelper;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.handler.PacketHandler;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.ProtocolCodec;
import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketInspectorInterfaceDecoder extends SimpleChannelInboundHandler<ByteBuf> implements PacketInspectorInterface {
    public static final String NAME = "packet-inspector-decoder";

    private final ProtocolCodec codec;
    private final PacketHandler handler;

    public PacketInspectorInterfaceDecoder(ProtocolCodec codec, PacketHandler handler) {
        this.codec = codec;
        this.handler = handler;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && this.accept((ByteBuf) msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int packetId = PacketHelper.readInt(buffer);
        MinecraftPacket packet = this.codec.constructPacket(packetId, this.getDirection());

        try {
            packet.decode(buffer, this.getDirection());
            if (buffer.readableBytes() != 0) {
                PacketInspector.logger().info(packet.getClass().getSimpleName() + " was not fully decoded, " + buffer.readableBytes() + " bytes left");
            }

            boolean handled = this.handler != null && packet.handle(handler, this.getDirection());
            if (handled) {
                // Packet was handled internally, don't forward it
                return;
            }

            ByteBuf out = buffer.alloc().ioBuffer();
            PacketHelper.writeInt(out, packetId);
            packet.encode(out, this.getDirection());
            ctx.fireChannelRead(out);
        } catch (Throwable t) {
            PacketInspector.logger().warning("Error appeared while trying to inspect arriving packet!");
            t.printStackTrace();
        }
    }

    @Override
    public ProtocolCodec getCodec() {
        return this.codec;
    }

    @Override
    public PacketHandler getHandler() {
        return this.handler;
    }

    @Override
    public PacketDirection getDirection() {
        return PacketDirection.SERVER_BOUND;
    }
}
