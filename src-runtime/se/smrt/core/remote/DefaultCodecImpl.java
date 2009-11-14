package se.smrt.core.remote;

import java.io.*;

public class DefaultCodecImpl implements DefaultReadCodec, DefaultWriteCodec {
	@Override
	public void writeByte(OutputStream output, byte value) throws IOException {
		output.write(value);
	}

	@Override
	public byte readByte(InputStream in) throws IOException {
		return (byte) in.read();
	}

	@Override
	public int readUnsignedByte(InputStream in) throws IOException {
		return (256 + readByte(in)) & 255;
	}

	@Override
	public void writeShort(OutputStream out, short v) throws IOException {
		out.write((v >>> 8) & 0xFF);
		out.write((v >>> 0) & 0xFF);
	}

	@Override
	public short readShort(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0) {
			throw new EOFException();
		}
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	@Override
	public int readUnsignedShort(InputStream in) throws IOException {
		return (1 << 16 + readShort(in)) & 0xFFFF;
	}
	
	@Override
	public void writeInt(OutputStream out, int v) throws IOException {
		out.write((v >>> 24) & 0xFF);
		out.write((v >>> 16) & 0xFF);
		out.write((v >>> 8) & 0xFF);
		out.write((v >>> 0) & 0xFF);
	}

	@Override
	public int readInt(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0) {
			throw new EOFException();
		}
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	@Override
	public void writeLong(OutputStream out, long v) throws IOException {
		out.write((int) (v >>> 56));
		out.write((int) (v >>> 48));
		out.write((int) (v >>> 40));
		out.write((int) (v >>> 32));
		out.write((int) (v >>> 24));
		out.write((int) (v >>> 16));
		out.write((int) (v >>> 8));
		out.write((int) (v >>> 0));
	}

	@Override
	public long readLong(InputStream in) throws IOException {
		return (((long) in.read() << 56) +
				((long) (in.read() & 255) << 48) +
				((long) (in.read() & 255) << 40) +
				((long) (in.read() & 255) << 32) +
				((long) (in.read() & 255) << 24) +
				((in.read() & 255) << 16) +
				((in.read() & 255) << 8) +
				((in.read() & 255) << 0));
	}

	@Override
	public void writeString(OutputStream out, String v) throws IOException {
		// Default implementation, feel free to change in sub class
		writeStringAsUTF16(out, v);
	}

	@Override
	public String readString(InputStream in) throws IOException {
		// Default implementation, feel free to change in sub class
		return readStringAsUTF16(in);
	}

	@Override
	public void writeStringAsAscii(OutputStream out, String v) throws IOException {
		int len = v.length();
		writeInt(out, len);
		for (int i = 0; i < len; i++) {
			writeByte(out, (byte) v.charAt(i));
		}
	}

	@Override
	public String readStringAsAscii(InputStream in) throws IOException {
		int len = readInt(in);
		char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = (char) readUnsignedByte(in);
		}
		return new String(chars);
	}

	@Override
	public void writeStringAsUTF8(OutputStream output, String str) throws IOException {
		int len = str.length();
		writeInt(output, len);

		int i = 0;
		for (; i < len; i++) {
			char c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F))) {
				break;
			}
			writeByte(output, (byte) c);
		}

		for (; i < len; i++) {
			char c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				writeByte(output, (byte) c);
			} else if (c > 0x07FF) {
				writeByte(output, (byte) (0xE0 | ((c >> 12) & 0x0F)));
				writeByte(output, (byte) (0x80 | ((c >> 6) & 0x3F)));
				writeByte(output, (byte) (0x80 | ((c >> 0) & 0x3F)));
			} else {
				writeByte(output, (byte) (0xC0 | ((c >> 6) & 0x1F)));
				writeByte(output, (byte) (0x80 | ((c >> 0) & 0x3F)));
			}
		}
	}

	@Override
	public String readStringAsUTF8(InputStream in) throws IOException {
		int len = readInt(in);
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int b = readUnsignedByte(in);
			switch (b >> 4) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					/* 0xxxxxxx*/
					builder.append((char) b);
					break;
				case 12:
				case 13:
					/* 110x xxxx   10xx xxxx*/
					int b2 = readUnsignedByte(in);
					builder.append((char) (((b & 0x1F) << 6) | (b2 & 0x3F)));
					break;
				case 14:
					/* 1110 xxxx  10xx xxxx  10xx xxxx */
					b2 = readUnsignedByte(in);
					int b3 = readUnsignedByte(in);
					builder.append((char) (((b & 0x0F) << 12) | ((b2 & 0x3F) << 6) | ((b3 & 0x3F) << 0)));
					break;
				default:
					/* 10xx xxxx,  1111 xxxx */
					throw new UTFDataFormatException(
							"malformed input after: [" + builder.toString() + "] " + b + ", " + (b >> 4));
			}
		}

		return builder.toString();
	}

	@Override
	public void writeStringAsUTF16(OutputStream out, String v) throws IOException {
		int len = v.length();
		writeInt(out, len);
		for (int i = 0; i < len; i++) {
			writeChar(out, v.charAt(i));
		}
	}

	@Override
	public String readStringAsUTF16(InputStream in) throws IOException {
		int len = readInt(in);
		char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = readChar(in);
		}
		return new String(chars);
	}

	@Override
	public void writeBoolean(OutputStream out, boolean v) throws IOException {
		out.write(v ? 1 : 0);
	}

	@Override
	public boolean readBoolean(InputStream in) throws IOException {
		return in.read() == 1;
	}

	@Override
	public void writeFloat(OutputStream out, float v) throws IOException {
		writeInt(out, Float.floatToIntBits(v));
	}

	@Override
	public float readFloat(InputStream in) throws IOException {
		return Float.intBitsToFloat(readInt(in));
	}

	@Override
	public void writeDouble(OutputStream out, double v) throws IOException {
		writeLong(out, Double.doubleToLongBits(v));
	}

	@Override
	public double readDouble(InputStream in) throws IOException {
		return Double.longBitsToDouble(readLong(in));
	}

	@Override
	public void writeChar(OutputStream out, char v) throws IOException {
		out.write((v >>> 8) & 0xFF);
		out.write((v >>> 0) & 0xFF);
	}

	@Override
	public char readChar(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0) {
			throw new EOFException();
		}
		return (char) ((ch1 << 8) + (ch2 << 0));
	}
}
