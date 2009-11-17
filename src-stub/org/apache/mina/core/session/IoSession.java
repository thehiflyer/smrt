package org.apache.mina.core.session;

public interface IoSession {
	org.apache.mina.core.future.WriteFuture write(Object o);
}
