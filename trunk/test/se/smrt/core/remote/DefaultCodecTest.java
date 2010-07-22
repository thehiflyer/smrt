package se.smrt.core.remote;

public class DefaultCodecTest extends CodecTest {
	@Override
	protected DefaultCodecImpl getCodec() {
		return new DefaultCodecImpl();
	}
}
