package se.smrt.core.remote.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ByteArrayDecoder extends CumulativeProtocolDecoder {
	private static final int MESSAGE_MAX_SIZE = 100 * 1024; //100kb data

	@Override
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.prefixedDataAvailable(4, MESSAGE_MAX_SIZE)) {
			int len = in.getInt();
			byte[] data = new byte[len];
			in.get(data);
			out.write(data);
			return true;
		} else {
			return false;
		}
	}
}