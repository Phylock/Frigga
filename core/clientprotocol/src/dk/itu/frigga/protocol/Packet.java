package dk.itu.frigga.protocol;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by IntelliJ IDEA. User: Tommy Date: 18-04-11 Time: 21:49 To change this template use File | Settings | File
 * Templates.
 */
final class Packet
{
    /**
     * Magic number, this is the first 6 bytes of the packet, and are the ascii codes for 'frigga'.
     */
    private static final byte[] MAGIC = { 0x66, 0x72, 0x69, 0x67, 0x67, 0x61, 0x00, (byte)0xff };
    private static final byte[] START = { 0x01 };
    private static final byte[] END = { 0x04 };

    private final byte[] buffer;

    public Packet(final byte[] data)
    {
        buffer = data.clone();
    }

    public Packet(final InputStream inputStream) throws IOException, ProtocolException
    {
        evaluateStartByte(readBytesFromStream(inputStream, START.length));
        evaluateMagicNumber(readBytesFromStream(inputStream, MAGIC.length));

        final byte[] options = readBytesFromStream(inputStream, 1);
        final int size = byteArrayToInt(readBytesFromStream(inputStream, 4));

        if (isPackedSet(options[0]))
        {
            final GZIPInputStream packedStream = new GZIPInputStream(inputStream);
            buffer = readBytesFromStream(packedStream, size);
        }
        else
        {
            buffer = readBytesFromStream(inputStream, size);
        }

        evaluateEndByte(readBytesFromStream(inputStream, END.length));
    }

    public Packet(final Message message) throws ProtocolException, IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        message.writeToStream(bytes);
        buffer = bytes.toByteArray();
    }

    public void writeToStream(final OutputStream outputStream) throws IOException
    {
        writeToStream(outputStream, true);
    }

    public void writeToStream(final OutputStream outputStream, final boolean packed) throws IOException
    {
        outputStream.write(START);
        writeMagicNumber(outputStream);
        outputStream.write(buildOptionsBytes(packed));
        if (packed)
        {
            writeCompressedData(outputStream);
        }
        else
        {
            writeRawData(outputStream);
        }
        outputStream.write(END);
        outputStream.flush();
    }

    private void writeCompressedData(final OutputStream outputStream) throws IOException
    {
        final byte[] packed = createPackedBuffer();
        outputStream.write(intToByteArray(packed.length));
        outputStream.write(packed);
    }

    private void writeRawData(final OutputStream outputStream) throws IOException
    {
        outputStream.write(intToByteArray(buffer.length));
        outputStream.write(buffer);
    }

    public byte[] toByteArray()
    {
        return buffer;
    }

    public InputStream getInputStream()
    {
        return new ByteArrayInputStream(buffer);
    }

    private byte[] createPackedBuffer() throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream packedStream = new GZIPOutputStream(baos);

        packedStream.write(buffer);
        packedStream.finish();

        return baos.toByteArray();
    }

    private static byte[] intToByteArray(final int value)
    {
        return new byte[] { (byte)(value >>> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)(value) };
    }

    private static int byteArrayToInt(final byte[] bytes)
    {
        return (bytes[0] << 24) |
               ((bytes[1] & 0xFF) << 16) |
               ((bytes[2] & 0xFF) << 8) |
               (bytes[3] & 0xFF);
    }

    private static void evaluateMagicNumber(final byte[] magic) throws InvalidMagicNumberException
    {
        if (magic.length < MAGIC.length) throw new InvalidMagicNumberException();

        for (int i = 0; i < MAGIC.length; i++)
        {
            if (magic[i] != MAGIC[i]) throw new InvalidMagicNumberException();
        }
    }

    private static void evaluateStartByte(final byte[] start) throws MalformedPackageException
    {
        if (start.length < START.length) throw new MalformedPackageException();

        for (int i = 0; i < START.length; i++)
        {
            if (start[i] != START[i]) throw new MalformedPackageException();
        }
    }

    private static void evaluateEndByte(final byte[] end) throws MalformedPackageException
    {
        if (end.length < END.length) throw new MalformedPackageException();

        for (int i = 0; i < END.length; i++)
        {
            if (end[i] != END[i]) throw new MalformedPackageException();
        }
    }

    private static byte[] buildOptionsBytes(final boolean isPacked)
    {
        final byte[] options = new byte[1];

        if (isPacked)
        {
            options[0] |= 1;
        }

        return options;
    }

    private static boolean isPackedSet(final byte byteOptions)
    {
        return (byteOptions & 1) != 0;
    }

    private static void writeMagicNumber(final OutputStream outputStream) throws IOException
    {
        outputStream.write(MAGIC);
    }

    private byte[] readBytesFromStream(final InputStream inputStream, int count) throws IOException, ProtocolException
    {
        final byte[] bytes = new byte[count];

        int offset = 0;
        int length = count;

        while (length > 0)
        {
            final int read = inputStream.read(bytes, offset, length);
            offset += read;
            length -= read;

            if (read == -1)
            {
                throw new ProtocolException("Unexpected end of stream.");
            }
        }

        return bytes;
    }
}
