package se.smrt.core.remote;

import java.io.IOException;
import java.io.OutputStream;

public interface DefaultWriteCodec {
	void writeByte(OutputStream output, byte value) throws IOException;

	void writeShort(OutputStream output, short value) throws IOException;

	void writeInt(OutputStream output, int value) throws IOException;

	void writeLong(OutputStream output, long value) throws IOException;

	void writeBoolean(OutputStream output, boolean value) throws IOException;

	void writeFloat(OutputStream output, float value) throws IOException;

	void writeDouble(OutputStream output, double value) throws IOException;

	void writeChar(OutputStream output, char value) throws IOException;

	void writeString(OutputStream output, String value) throws IOException;

	void writeStringAsAscii(OutputStream output, String value) throws IOException;

	void writeStringAsUTF8(OutputStream output, String value) throws IOException;

	void writeStringAsUTF16(OutputStream output, String value) throws IOException;


}