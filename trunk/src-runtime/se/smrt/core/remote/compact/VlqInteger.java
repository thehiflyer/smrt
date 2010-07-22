package se.smrt.core.remote.compact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VlqInteger {
	private static final long LOWER_BITS_MASK = 0x7FL;
	private static final int MSB_MASK = 0x80;

	public static void writeInt(OutputStream out, int value) throws IOException {
		writeLong(out, value);
	}

	public static void writeLong(OutputStream out, long value) throws IOException {
        boolean negative = value < 0;
        value = Math.abs(value);

        if (value <= 0x3FL) {
            out.write((int) (value | (negative ? 0x40L : 0L)));
        } else {
            out.write((int) ((value & 0x3FL) | (negative ? 0x40L : 0) | 0x80L));
            value >>= 6;
            while (value > 0x7FL) {
                out.write((int) ((value & 0x7FL) | 0x80L));
                value >>= 7;
            }
            out.write((int) value);
        }
    }

	public static int readInt(InputStream in) throws IOException {
		return (int) readLong(in);
	}

	public static long readLong(InputStream in) throws IOException {
        int b = in.read();
        boolean negative = (b & 0x40L) != 0;
        long value = b & 0x3FL;
        int shift = 0;
        if ((b & 0x80L) != 0) {
            shift += 6;
            b = in.read();
        } else {
            return negative ? -value : value;
        }

        while ((b & 0x80L) != 0) {
            value |= (b & 0x7FL) << shift;
            shift += 7;
            b = in.read();
        }
        value |= b << shift;

        return negative ? -value : value;
	}
}

