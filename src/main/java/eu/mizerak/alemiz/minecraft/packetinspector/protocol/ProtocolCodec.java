package eu.mizerak.alemiz.minecraft.packetinspector.protocol;

import eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.MinecraftPacket;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class ProtocolCodec {

    private final Int2ObjectMap<PacketFactory> clientBoundFactoryMap = new Int2ObjectOpenHashMap<>();
    private final Object2IntMap<Class<? extends MinecraftPacket>> clientBoundPacketMap = new Object2IntOpenHashMap<>();

    private final Int2ObjectMap<PacketFactory> serverBoundFactoryMap = new Int2ObjectOpenHashMap<>();
    private final Object2IntMap<Class<? extends MinecraftPacket>> serverBoundPacketMap = new Object2IntOpenHashMap<>();


    public <T extends MinecraftPacket> MinecraftPacket constructPacket(Class<T> packetClass, PacketDirection direction) {
        if (direction == PacketDirection.CLIENT_BOUND && this.clientBoundPacketMap.containsKey(packetClass)) {
            return this.constructPacket(this.clientBoundPacketMap.get(packetClass), direction);
        } else if (direction == PacketDirection.SERVER_BOUND && this.serverBoundPacketMap.containsKey(packetClass)) {
            return this.constructPacket(this.serverBoundPacketMap.get(packetClass), direction);
        }
        throw new IllegalStateException("Packet " + packetClass.getSimpleName() + " is not registered!");
    }

    public MinecraftPacket constructPacket(int packetId, PacketDirection direction) {
        PacketFactory factory;
        if (direction == PacketDirection.CLIENT_BOUND) {
            factory = this.clientBoundFactoryMap.get(packetId);
        } else {
            factory = this.serverBoundFactoryMap.get(packetId);
        }

        if (factory == null) {
            throw new IllegalStateException("Packet " + packetId + " is not registered!");
        }
        return factory.newInstance();
    }

    public <T extends MinecraftPacket> void registerClientBound(int packetId, Class<T> packetClass, PacketFactory packetFactory) {
        assert packetClass != null;
        assert packetFactory != null;
        if (packetId < 0) {
            throw new IllegalArgumentException("Packet ID can not be negative!");
        }

        if (this.clientBoundFactoryMap.containsKey(packetId) || this.clientBoundPacketMap.containsKey(packetId)) {
            throw new IllegalStateException("Packet " + packetClass.getSimpleName() + " is already registered!");
        }
        this.clientBoundFactoryMap.put(packetId, packetFactory);
        this.clientBoundPacketMap.put(packetClass, packetId);
    }

    public <T extends MinecraftPacket> void registerServerBound(int packetId, Class<T> packetClass, PacketFactory packetFactory) {
        assert packetClass != null;
        assert packetFactory != null;
        if (packetId < 0) {
            throw new IllegalArgumentException("Packet ID can not be negative!");
        }

        if (this.serverBoundFactoryMap.containsKey(packetId) || this.serverBoundPacketMap.containsKey(packetId)) {
            throw new IllegalStateException("Packet " + packetClass.getSimpleName() + " is already registered!");
        }
        this.serverBoundFactoryMap.put(packetId, packetFactory);
        this.serverBoundPacketMap.put(packetClass, packetId);
    }

    public <T extends MinecraftPacket> void deregisterClientBound(Class<T> packetClass) {
        assert packetClass != null;
        if (!this.clientBoundPacketMap.containsKey(packetClass)) {
            return;
        }
        int packetId = this.clientBoundPacketMap.removeInt(packetClass);
        this.clientBoundFactoryMap.remove(packetId);
    }

    public <T extends MinecraftPacket> void deregisterServerBound(Class<T> packetClass) {
        assert packetClass != null;
        if (!this.serverBoundPacketMap.containsKey(packetClass)) {
            return;
        }
        int packetId = this.serverBoundPacketMap.removeInt(packetClass);
        this.serverBoundFactoryMap.remove(packetId);
    }

    public boolean containsPacket(int packetId, PacketDirection direction) {
        if (direction == PacketDirection.CLIENT_BOUND) {
            return this.clientBoundFactoryMap.containsKey(packetId);
        }
        return this.serverBoundFactoryMap.containsKey(packetId);
    }
}
