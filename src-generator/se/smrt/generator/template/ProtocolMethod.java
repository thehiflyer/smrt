package se.smrt.generator.template;

import se.smrt.generator.types.Method;
import se.smrt.generator.parser.ProtocolData;

public class ProtocolMethod {
	private final String chain;
	private final Method method;
	private final ProtocolData protocol;

	public ProtocolMethod(String chain, Method method, ProtocolData protocol) {
		this.chain = chain;
		this.method = method;
		this.protocol = protocol;
	}

	public String getChain() {
		return chain;
	}

	public Method getMethod() {
		return method;
	}

	public ProtocolData getProtocol() {
		return protocol;
	}
}
