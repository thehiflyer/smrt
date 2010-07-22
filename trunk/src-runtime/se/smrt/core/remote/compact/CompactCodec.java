package se.smrt.core.remote.compact;

import se.smrt.core.remote.DefaultCodecImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompactCodec extends DefaultCodecImpl {
	@Override
	public short readShort(InputStream input) throws IOException {
		return (short) VlqInteger.readLong(input);
	}

	@Override
	public int readInt(InputStream input) throws IOException {
		return (int) VlqInteger.readLong(input);
	}

	@Override
	public long readLong(InputStream input) throws IOException {
		return VlqInteger.readLong(input);
	}

	@Override
	public void writeShort(OutputStream output, short value) throws IOException {
		VlqInteger.writeLong(output, value);
	}

	@Override
	public void writeInt(OutputStream output, int value) throws IOException {
		VlqInteger.writeLong(output, value);
	}

	@Override
	public void writeLong(OutputStream output, long value) throws IOException {
		VlqInteger.writeLong(output, value);
	}

		@Override
	public void writeString(OutputStream out, String v) throws IOException {
		writeStringAsUTF8(out, v);
	}

	@Override
	public String readString(InputStream in) throws IOException {
		return readStringAsUTF8(in);
	}
}
