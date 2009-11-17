package se.smrt.generator.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;

import se.smrt.core.SmrtProtocol;
import se.smrt.generator.types.Method;
import se.smrt.generator.types.Type;

public class ProtocolData {
	private final Map<Type, ProtocolData> protocols;
	private final String protocolName;

	private final List<Method> methods;
	private final Type type;

	public ProtocolData(String className, String protocolName, ArrayList<Method> methods, Map<Type, ProtocolData> protocols) {
		this.protocolName = protocolName;
		this.protocols = protocols;
		this.type = new Type(className);
		this.methods = methods;
	}

	public Type getType() {
		return type;
	}

	public List<ProtocolData> getSubProtocols() {
		List<ProtocolData> list = new ArrayList<ProtocolData>();
		for (Method method : methods) {
			ProtocolData protocolData = protocols.get(method.getReturnType());
			if (protocolData != null) {
				list.add(protocolData);
			}
		}
		return list;
	}

	public ProtocolData getSubProtocol(Type t) {
		return protocols.get(t);
	}

	public List<Method> getMethods() {
		return methods;
	}

	@Override
	public String toString() {
		return type.toString();
	}

    public String getName() {
        return protocolName;
    }

	public boolean isRootProtocol() {
		return !protocolName.equals("");
	}
}
