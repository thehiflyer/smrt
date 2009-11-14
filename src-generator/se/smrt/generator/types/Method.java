package se.smrt.generator.types;

import se.smrt.generator.types.Type;
import se.smrt.generator.types.Parameter;

import java.util.List;
import java.io.IOException;

public class Method {
	private final Type returnType;
	private final String methodName;
	private final List<Parameter> parameters;

	public Method(String returnType, String methodName, List<Parameter> parameters) {
		this.returnType = new Type(returnType);
		this.methodName = methodName;
		this.parameters = parameters;
	}

	public String getName() {
		return methodName;
	}

	@Override
	public String toString() {
		return returnType + " " + methodName + "(" + getParameterList() + ")";
	}

	public String getParameterList() {
		StringBuilder builder = new StringBuilder();
		boolean notFirst = false;
		for (Parameter parameter : parameters) {
			if (notFirst) {
				builder.append(", ");
			}
			builder.append("final ").append(parameter.getType().getFullNameWithGenerics()).append(" ").append(parameter.getName());
			notFirst = true;
		}
		return builder.toString();
	}

	public String getParameterListWithoutTypes() {
		StringBuilder builder = new StringBuilder();
		boolean notFirst = false;
		for (Parameter parameter : parameters) {
			if (notFirst) {
				builder.append(", ");
			}
			builder.append(parameter.getName());
			notFirst = true;
		}
		return builder.toString();
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public Type getReturnType() {
		return returnType;
	}
}
