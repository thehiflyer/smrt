package se.smrt.core.remote.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ByteArrayEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput out) throws Exception {
		byte[] encoded = (byte[]) o;
		IoBuffer buffer = IoBuffer.allocate(encoded.length + 4); //the buffer to hold the output
		buffer.putInt(encoded.length);
		buffer.put(encoded);
		buffer.flip();
		out.write(buffer);
	}
}