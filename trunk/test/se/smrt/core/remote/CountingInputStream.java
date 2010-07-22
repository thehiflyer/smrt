package se.smrt.core.remote;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class CountingInputStream extends InputStream {
	private final InputStream inputStream;
	private final AtomicInteger count = new AtomicInteger(0);

	public CountingInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public int read() throws IOException {
		count.incrementAndGet();
		return inputStream.read();
	}

	public int getBytesRead() {
		return count.get();
	}
}
