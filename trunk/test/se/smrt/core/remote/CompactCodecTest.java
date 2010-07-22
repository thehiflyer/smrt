package se.smrt.core.remote;

import se.smrt.core.remote.compact.CompactCodec;

public class CompactCodecTest extends CodecTest {
	@Override
	protected DefaultCodecImpl getCodec() {
		return new CompactCodec();
	}
}
