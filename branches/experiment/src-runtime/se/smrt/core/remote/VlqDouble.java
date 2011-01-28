package se.smrt.core.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VlqDouble {
    public static void write(OutputStream out, double value) throws IOException {
        VlqInteger.write(out, Long.reverse(Double.doubleToLongBits(value)));
    }

	public static double read(InputStream in) throws IOException {
        return Double.longBitsToDouble(Long.reverse(VlqInteger.read(in)));
	}
}
