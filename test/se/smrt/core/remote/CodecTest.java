package se.smrt.core.remote;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.UUID;

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

	@Test
	public void testLong1() throws IOException {
		long testValue = 0xB4D69DBB64D1A09FL;
		codec.writeLong(out, testValue);
		long value = codec.readLong(in);
		assertEquals(testValue, value);
	}

	@Test
	public void testLong2() throws IOException {
		long testValue = 5415968073690537825L;
		codec.writeLong(out, testValue);
		long value = codec.readLong(in);
		assertEquals(testValue, value);
	}

	@Test
	public void testUUID() throws IOException {
		UUID uuid = UUID.fromString("686e1b60-7944-4fed-b4d6-9dbb64d1a09f");

		long leastSignificantBits = uuid.getLeastSignificantBits();
		long mostSignificantBits = uuid.getMostSignificantBits();
		codec.writeLong(out, leastSignificantBits);
		codec.writeLong(out, mostSignificantBits);

		long lsb = codec.readLong(in);
		long msb = codec.readLong(in);
		UUID uuid2 = new UUID(msb, lsb);
		assertEquals(uuid, uuid2);
	}

	@AfterClass
	public static void outputStats() {
		System.out.println("Read " + in.getBytesRead() + " bytes in total");
	}
}
