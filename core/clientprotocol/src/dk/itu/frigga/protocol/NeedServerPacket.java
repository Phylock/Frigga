package dk.itu.frigga.protocol;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-28
 */
class NeedServerPacket
{
    public final static int LENGTH = 20;
    private final static byte[] HEADER = { 0x6e, 0x65, 0x65, 0x64, 0x20, 0x66, 0x72, 0x69, 0x67, 0x67, 0x61  };
    private final UUID serverId;
    private final byte[] data = new byte[LENGTH];

    public NeedServerPacket()
    {
        serverId = null;

        prepare();
    }

    public NeedServerPacket(final UUID serverId)
    {
        assert(serverId != null) : "ServerId can not be null.";

        this.serverId = serverId;

        prepare();
    }

    public NeedServerPacket(final DatagramPacket packet) throws NotCorrectPacketException
    {
        if (!validate(packet)) throw new NotCorrectPacketException();

        System.arraycopy(packet.getData(), 0, data, 0, Math.min(packet.getLength(), data.length));

        if (data[HEADER.length] == 0x01)
        {
            serverId = new UUID(byteArrayToLong(data, HEADER.length + 1), byteArrayToLong(data, HEADER.length + 9));
        }
        else
        {
            serverId = null;
        }
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

    public boolean requiresSpecificServer()
    {
        return serverId != null;
    }

    public boolean matchServer(final UUID serverId)
    {
        assert(serverId != null) : "ServerId can not be null";

        return this.serverId == null || this.serverId.equals(serverId);
    }

    private void prepare()
    {
        int idx;

        for (idx = 0; idx < HEADER.length; idx++)
        {
            data[idx] = HEADER[idx];
        }

        if (serverId != null)
        {
            data[idx++] = 0x01;

            byte[] msb = longToByteArray(serverId.getMostSignificantBits());
            byte[] lsb = longToByteArray(serverId.getLeastSignificantBits());

            for (byte b : msb)
            {
                data[idx++] = b;
            }

            for (byte b : lsb)
            {
                data[idx++] = b;
            }
        }
    }

    public static long byteArrayToLong(final byte[] data, final int offset)
    {
        assert(data != null) : "null object can not be converted to long";
        assert(data.length >= offset + 8) : "byte array not large enough to hold long";

        return (long)(0xff & data[0]) << 56 | (long)(0xff & data[1]) << 48 |
               (long)(0xff & data[2]) << 40 | (long)(0xff & data[3]) << 32 |
               (long)(0xff & data[4]) << 24 | (long)(0xff & data[5]) << 16 |
               (long)(0xff & data[6]) << 8  | (long)(0xff & data[7]);
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

    public DatagramPacket build(final InetAddress address, final int port)
    {
        DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setAddress(address);
        packet.setPort(port);
        return packet;
    }
}
