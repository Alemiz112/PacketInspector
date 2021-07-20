package eu.mizerak.alemiz.minecraft.packetinspector.protocol;

public enum PacketDirection {
    CLIENT_BOUND,
    SERVER_BOUND;

    public PacketDirection reverse() {
        if (this == CLIENT_BOUND) {
            return SERVER_BOUND;
        }
        return CLIENT_BOUND;
    }
}
