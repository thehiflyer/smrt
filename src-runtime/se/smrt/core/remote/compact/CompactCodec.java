package se.smrt.core.remote.compact;

import se.smrt.core.remote.DefaultReadCodec;
import se.smrt.core.remote.DefaultWriteCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompactCodec implements DefaultReadCodec, DefaultWriteCodec {
	@Override
	public byte readByte(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public int readUnsignedByte(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public short readShort(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public int readUnsignedShort(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public int readInt(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public long readLong(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public boolean readBoolean(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public float readFloat(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public double readDouble(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public char readChar(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public String readString(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public String readStringAsAscii(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public String readStringAsUTF8(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public String readStringAsUTF16(InputStream input) throws IOException {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public void writeByte(OutputStream output, byte value) throws IOException {
	}

	@Override
	public void writeShort(OutputStream output, short value) throws IOException {
	}

	@Override
	public void writeInt(OutputStream output, int value) throws IOException {
	}

	@Override
	public void writeLong(OutputStream output, long value) throws IOException {
	}

	@Override
	public void writeBoolean(OutputStream output, boolean value) throws IOException {
	}

	@Override
	public void writeFloat(OutputStream output, float value) throws IOException {
	}

	@Override
	public void writeDouble(OutputStream output, double value) throws IOException {
	}

	@Override
	public void writeChar(OutputStream output, char value) throws IOException {
	}

	@Override
	public void writeString(OutputStream output, String value) throws IOException {
	}

	@Override
	public void writeStringAsAscii(OutputStream output, String value) throws IOException {
	}

	@Override
	public void writeStringAsUTF8(OutputStream output, String value) throws IOException {
	}

	@Override
	public void writeStringAsUTF16(OutputStream output, String value) throws IOException {
	}
}
