package dk.itu.frigga.protocol;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.UUID;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-28
 */
class HasServerPacket
{
    public final static int LENGTH = 512;
    private static final byte[] HEADER = { 0x68, 0x61, 0x73, 0x20, 0x66, 0x72, 0x69, 0x67, 0x67, 0x61 };
    private final byte[] data = new byte[LENGTH];

    public HasServerPacket(final DatagramPacket packet) throws NotCorrectPacketException
    {
        if (!validate(packet)) throw new NotCorrectPacketException();

        System.arraycopy(packet.getData(), packet.getLength(), data, 0, Math.min(packet.getLength(), data.length));
    }

    public HasServerPacket(final String host, final int port, final UUID serverId, final String description) throws UnsupportedEncodingException
    {
        System.arraycopy(HEADER, 0, data, 0, HEADER.length);
        byte[] h = host.getBytes("UTF-8");
        System.arraycopy(h, 0, data, 10, Math.min(h.length, 255));
        data[265] = (byte)((port >>> 8) & 0xff);
        data[266] = (byte)(port & 0xff);
        System.arraycopy(longToByteArray(serverId.getMostSignificantBits()), 0, data, 267, 8);
        System.arraycopy(longToByteArray(serverId.getLeastSignificantBits()), 0, data, 275, 8);
        byte[] str = description.getBytes("UTF-8");
        System.arraycopy(str, 0, data, 283, Math.min(str.length, 229));
    }

    public static boolean validate(final DatagramPacket packet)
    {
        assert(packet != null) : "Packet can not be null.";

        if (packet.getLength() < LENGTH) return false;

        final byte[] d = packet.getData();
        for (int i = 0; i < HEADER.length; i++)
        {
            if (d[i] != HEADER[i]) return false;
        }

        return true;
    }

    public UUID getServerId()
    {
        return new UUID(byteArrayToLong(data, 267), byteArrayToLong(data, 275));
    }

    public String getServerDescription() throws UnsupportedEncodingException
    {
        return new String(data, 283, 229, "UTF-8");
    }

    public int getServerPort()
    {
        return (data[265] & 0xff) << 8 | (data[266] & 0xff);
    }

    public String getServerHost() throws UnsupportedEncodingException
    {
        return new String(data, 10, 255, "UTF-8");
    }

    public static long byteArrayToLong(final byte[] data, final int offset)
    {
        assert(data != null) : "null object can not be converted to long";
        assert(data.length >= offset + 8) : "byte array not large enough to hold long";

        return (long)(data[offset] & 0xff) << 56 | (long)(data[offset + 1] & 0xff) << 48 |
               (long)(data[offset + 2] & 0xff) << 40 | (long)(data[offset + 3] & 0xff) << 32 |
               (long)(data[offset + 4] & 0xff) << 24 | (long)(data[offset + 5] & 0xff) << 16 |
               (long)(data[offset + 6] & 0xff) << 8  | (long)(data[offset + 7] & 0xff);
    }

    private static byte[] longToByteArray(final long data)
    {
        byte[] bytes = new byte[8];

        for (int i = 0; i < 8; i++)
        {
            bytes[i] = (byte)(data >>> (7 - i) & 0xFFl);
        }

        return bytes;
    }

    public DatagramPacket build(final int port)
    {
        final DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setPort(port);
        return packet;
    }
}
