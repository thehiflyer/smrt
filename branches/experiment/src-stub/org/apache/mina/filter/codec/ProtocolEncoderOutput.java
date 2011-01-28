package org.apache.mina.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;

public interface ProtocolEncoderOutput {
	void write(Object o);
}
