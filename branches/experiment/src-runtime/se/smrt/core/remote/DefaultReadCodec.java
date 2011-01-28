package se.smrt.core.remote;

import java.io.IOException;
import java.io.InputStream;

public interface DefaultReadCodec {
	byte readByte(InputStream input) throws IOException;
	int readUnsignedByte(InputStream input) throws IOException;

	short readShort(InputStream input) throws IOException;

	short readShortNormal(InputStream input) throws IOException;

	short readShortVlq(InputStream input) throws IOException;

	int readUnsignedShort(InputStream input) throws IOException;

	int readInt(InputStream input) throws IOException;

	int readIntNormal(InputStream input) throws IOException;

	int readIntVlq(InputStream input) throws IOException;

	long readLong(InputStream input) throws IOException;

	long readLongNormal(InputStream input) throws IOException;
	
	long readLongVlq(InputStream input) throws IOException;

	boolean readBoolean(InputStream input) throws IOException;

	float readFloat(InputStream input) throws IOException;

	double readDouble(InputStream input) throws IOException;

	char readChar(InputStream input) throws IOException;

	String readString(InputStream input) throws IOException;

	String readStringAsAscii(InputStream input) throws IOException;

	String readStringAsUTF8(InputStream input) throws IOException;

	String readStringAsUTF16(InputStream input) throws IOException;


}
