package org.apache.mina.core.buffer;

public class IoBuffer {
	public boolean prefixedDataAvailable(int i, int messageMaxSize) {
		return false;
	}

	public int getInt() {
		return 0;
	}

	public void get(byte[] data) {
		
	}

	public static IoBuffer allocate(int i) {
		return null;
	}

	public void putInt(int length) {
	}

	public void put(byte[] encoded) {
	}

	public void flip() {
	}
}
