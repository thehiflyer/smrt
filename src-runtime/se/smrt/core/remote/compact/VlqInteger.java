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
		writeLongHelper(out, value, true);
	}

	private static void writeLongHelper(OutputStream out, long value, boolean isFirst) throws IOException {
		int smallPart = (int) (value & LOWER_BITS_MASK);
		if (smallPart < value) {
			writeLongHelper(out, value >>> 7, false);
		}
		if(isFirst) {
			out.write(smallPart);
		} else {
			out.write(smallPart | MSB_MASK);
		}
	}

	public static int readInt(InputStream in) throws IOException {
		return (int) readLong(in);
	}

	public static long readLong(InputStream in) throws IOException {
		return readLongHelper(in, 0);
	}

	public static long readLongHelper(InputStream in, long upperValue) throws IOException {
		long readValue = (long) in.read();
		long combinedValue = upperValue << 7 | readValue & LOWER_BITS_MASK;
		if ((readValue & MSB_MASK) != 0) {
			return readLongHelper(in, combinedValue);
		}
		return combinedValue;
	}
}

