package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

public class CumulativeProtocolDecoder {
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		return false;
	}	
}
