package se.smrt.core.remote.compact;


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VlqInteger {
	private static final long LOWER_7BITS_MASK = 0x7FL;
	private static final long LOWER_6BITS_MASK = 0x3FL;
	private static final long MASK_BIT8 = 0x80L;
	private static final long MASK_BIT7 = 0x40L;

	public static void writeLong(OutputStream out, long value) throws IOException {
        boolean negative = value < 0;
        value = Math.abs(value);

        if (value <= LOWER_6BITS_MASK) {
            out.write((int) (value | (negative ? MASK_BIT7 : 0L)));
        } else {
            out.write((int) ((value & LOWER_6BITS_MASK) | (negative ? MASK_BIT7 : 0) | MASK_BIT8));
            value >>= 6;
            while (value > LOWER_7BITS_MASK) {
                out.write((int) ((value & LOWER_7BITS_MASK) | MASK_BIT8));
                value >>= 7;
            }
            out.write((int) value);
        }
    }

	public static long readLong(InputStream in) throws IOException {
        int b = readFromStream(in);
        boolean negative = (b & MASK_BIT7) != 0;
        long value = b & LOWER_6BITS_MASK;
        int shift = 0;
        if ((b & MASK_BIT8) != 0) {
            shift += 6;
            b = readFromStream(in);
        } else {
            return negative ? -value : value;
        }

        while ((b & MASK_BIT8) != 0) {
            value |= (b & LOWER_7BITS_MASK) << shift;
            shift += 7;
            b = readFromStream(in);
        }
        value |= b << shift;

        return negative ? -value : value;
	}

	private static int readFromStream(InputStream in) throws IOException {
		int value = in.read();
		if (value == -1) {
			throw new EOFException();
		}
		return value;
	}
}

