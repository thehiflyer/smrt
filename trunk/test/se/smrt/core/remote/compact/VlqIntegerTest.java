package se.smrt.core.remote.compact;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class VlqIntegerTest {
	private ByteArrayInputStream in;
	private ByteArrayOutputStream out;

	@Before
	public void setup() throws IOException {
		out = new ByteArrayOutputStream(100);
	}

	@Test
	public void testReadWriteZero() throws IOException {
		testReadWrite(0);
	}

	@Test
	public void testReadWrite42() throws IOException {
		testReadWrite(42);
	}

	@Test
	public void testReadWrite128() throws IOException {
		testReadWrite(0x00000080);
	}

	@Test
	public void testReadWrite8192() throws IOException {
		testReadWrite(0x00002000);
	}

	@Test
	public void testReadWrite16383() throws IOException {
		testReadWrite(0x00003FFF);
	}

	@Test
	public void testReadWrite16384() throws IOException {
		testReadWrite(0x00004000);
	}

	@Test
	public void testReadWrite2097151() throws IOException {
		testReadWrite(0x001FFFFF);
	}

	@Test
	public void testReadWrite2147483647() throws IOException {
		testReadWrite(0x7FFFFFFF);
	}


	private void testReadWrite(int value) throws IOException {
		VlqInteger.writeInt(out, value);
		byte[] data = out.toByteArray();
		System.out.print("[");
		for (byte b : data) {
			System.out.print(String.format("%x, ", b));
		}
		System.out.print("]");
		in = new ByteArrayInputStream(data);
		assertEquals(value, VlqInteger.readInt(in));
	}

}
