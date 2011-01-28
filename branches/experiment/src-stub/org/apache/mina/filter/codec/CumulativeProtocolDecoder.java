package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

public abstract class CumulativeProtocolDecoder {
	public abstract boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception;
}
