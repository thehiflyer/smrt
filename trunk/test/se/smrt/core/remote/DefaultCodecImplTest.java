package se.smrt.core.remote;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class DefaultCodecImplTest {

	private static final int[] intList = new int[] {
			0, -1, 2^31, -2^31, 1234, -1234
	};

	@Test
	public void testCodecInt() throws IOException {
		DefaultCodecImpl codec = new DefaultCodecImpl();

		for (int i : intList) {
			PipedOutputStream out = new PipedOutputStream();
			InputStream in = new PipedInputStream(out);
			codec.writeInt(out, i);

			assertEquals(i, codec.readInt(in));
		}
	}

	@Test
	public void testCodecString() throws IOException {
		DefaultCodecImpl codec = new DefaultCodecImpl();

		PipedOutputStream out = new PipedOutputStream();
		InputStream in = new PipedInputStream(out);
		codec.writeString(out, "Hello world");

		assertEquals("Hello world", codec.readString(in));
	}


	@Test
	public void testCodecString2() throws IOException {
		DefaultCodecImpl codec = new DefaultCodecImpl();

		PipedOutputStream out = new PipedOutputStream();
		InputStream in = new PipedInputStream(out);
		codec.writeString(out, "Hello world ÅÄÖ");

		assertEquals("Hello world ÅÄÖ", codec.readString(in));
	}

	@Test
	public void testCodecStringUTF() throws IOException {
		DefaultCodecImpl codec = new DefaultCodecImpl();

		PipedOutputStream out = new PipedOutputStream();
		InputStream in = new PipedInputStream(out);
		codec.writeStringAsUTF8(out, "Hello world ÅÄÖ αβψ");

		assertEquals("Hello world ÅÄÖ αβψ", codec.readStringAsUTF8(in));
	}

}
