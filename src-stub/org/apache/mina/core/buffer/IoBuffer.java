package org.apache.mina.core.buffer;

public abstract class IoBuffer {
	public abstract boolean prefixedDataAvailable(int i, int messageMaxSize);
	public abstract int getInt();
	public abstract IoBuffer get(byte[] data);

	public static IoBuffer allocate(int i) {
		return null;
	}

	public abstract IoBuffer putInt(int length);

	public abstract IoBuffer put(byte[] encoded);

	public abstract IoBuffer flip();
}
