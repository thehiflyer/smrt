package se.smrt.generator.parser;

import se.smrt.generator.parser.ProtocolData;
import se.smrt.generator.types.Method;
import se.smrt.generator.types.Parameter;
import se.smrt.generator.types.Type;

import java.io.IOException;
import java.util.*;

public class MessageClass {
	private final ProtocolData bundle;
	private final List<Method> methods;
	private final Type type;

	public MessageClass(String className, ProtocolData bundle, List<Method> methods) {
		this.type = new Type(className);
		this.bundle = bundle;
		this.methods = methods;
	}

	public void addMethod(Method method) {
		methods.add(method);
	}

	public Type getType() {
		return type;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public Collection<String> findTypes() {
		Set<String> ret = new HashSet<String>();
		for (Method method : methods) {
			for (Parameter parameter : method.getParameters()) {
				Type type = parameter.getType();
				findType(type, ret);
			}
		}
		return ret;
	}

	private void findType(Type type, Set<String> types) {
		String packageName = type.getPackage();
		if (!packageName.equals("java.lang") && !packageName.equals("")) {
			types.add(type.getFullNameWithGenerics());
		}
		for (Type type1 : type.getGenericParameters()) {
			findType(type1, types);
		}
	}

	private Type lookupType(Set<Type> types, Type type) {
		for (Type type1 : types) {
			if (type1.equals(type)) {
				return type1;
			}
		}
		return null;
	}
}
