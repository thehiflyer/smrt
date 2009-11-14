package se.smrt.generator;

import se.smrt.generator.types.Type;

import java.util.Set;
import java.util.HashSet;

public class BuiltinTypes {
	private final Set<Type> builtins;

	public BuiltinTypes() {
		builtins = new HashSet<Type>();

		builtins.add(new Type("byte"));
		builtins.add(new Type("short"));
		builtins.add(new Type("int"));
		builtins.add(new Type("long"));

		builtins.add(new Type("float"));
		builtins.add(new Type("double"))
				;
		builtins.add(new Type("char"));

		builtins.add(new Type("boolean"));

		builtins.add(new Type("java.lang.String"));
	}

	public boolean isBuiltIn(Type t) {
		return builtins.contains(t);
	}
}
