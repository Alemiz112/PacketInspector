package eu.mizerak.alemiz.minecraft.packetinspector.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketHelper {

    public static int readInt(ByteBuf buffer) {
        return (int) decodeVarInt(buffer);
    }

    public static void writeInt(ByteBuf buffer, int value) {
        encodeVarInt(buffer, value & 0xFFFFFFFFL);
    }

    public static long readLong(ByteBuf buffer) {
        return decodeVarInt(buffer);
    }

    public static void writeLong(ByteBuf buffer, long value) {
        encodeVarInt(buffer, value);
    }

    public static byte[] readByteArray(ByteBuf buffer) {
        int length = readInt(buffer);
        if (!buffer.isReadable(length)) {
            throw new IllegalArgumentException("Not enough bytes to read byteArray, " + buffer.readableBytes() + " bytes left");
        }

        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    public static void writeByteArray(ByteBuf buffer, byte[] bytes) {
        assert bytes != null;
        writeInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer) {
        return new String(readByteArray(buffer), StandardCharsets.UTF_8);
    }

    public static void writeString(ByteBuf buffer, String string) {
        assert string != null;
        writeByteArray(buffer, string.getBytes(StandardCharsets.UTF_8));
    }

    public static UUID readUUID(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    public static void writeUUID(ByteBuf buffer, UUID uuid) {
        assert uuid != null;
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    private static long decodeVarInt(ByteBuf buffer) {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            final byte b = buffer.readByte();
            result |= (b & 0x7FL) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
        }
        throw new ArithmeticException("VarInt was too large");
    }

    private static void encodeVarInt(ByteBuf buffer, long value) {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                buffer.writeByte((int) value);
                return;
            } else {
                buffer.writeByte((byte) (((int) value & 0x7F) | 0x80));
                value >>>= 7;
            }
        }
    }
}
