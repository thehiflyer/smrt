package se.smrt.core.remote;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;

public abstract class CodecTest {

	private static final double EPSILON = 1e-20;

	private static final int[] intList = new int[]{
			  0, -1, 2 ^ 31, -2 ^ 31, 1234, -1234
	};


	private static final float[] floatList = new float[]{
			  0.0f, -1.0f, 0.23152521f, 1.15215e14f, -0.1532153242342341f, 1.236263213262e35f
	};
	private DefaultCodecImpl codec;
	private static PipedOutputStream out;
	private static CountingInputStream in;


	@BeforeClass
	public static void setupStreams() throws IOException {
		out = new PipedOutputStream();
		in = new CountingInputStream(new PipedInputStream(out));
	}

	@Before
	public void setup() throws IOException {
		codec = getCodec();

	}

	protected abstract DefaultCodecImpl getCodec();

	@Test
	public void testCodecInt() throws IOException {
		//System.out.println(String.format("%02x", 0));

		for (int i : intList) {
			codec.writeInt(out, i);
			assertEquals(i, codec.readInt(in));
		}
	}

	@Test
	public void testCodecFloat() throws IOException {
		//System.out.println(String.format("%02x", 0));

		for (float f : floatList) {
			codec.writeFloat(out, f);
			assertEquals(f, codec.readFloat(in), EPSILON);
		}
	}

	@Test
	public void testCodecString() throws IOException {
		codec.writeString(out, "Hello world");
		assertEquals("Hello world", codec.readString(in));
	}


	@Test
	public void testCodecString2() throws IOException {
		codec.writeString(out, "Hello world ÅÄÖ");
		assertEquals("Hello world ÅÄÖ", codec.readString(in));
	}

	@Test
	public void testCodecStringUTF() throws IOException {
		codec.writeStringAsUTF8(out, "Hello world ÅÄÖ αβψ");
		assertEquals("Hello world ÅÄÖ αβψ", codec.readStringAsUTF8(in));
	}

	@AfterClass
	public static void outputStats() {
		System.out.println("Read " + in.getBytesRead() + " bytes in total");
	}
}
