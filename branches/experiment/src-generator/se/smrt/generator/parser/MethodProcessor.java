package se.smrt.generator.parser;

import se.smrt.generator.types.Method;
import se.smrt.generator.types.Parameter;
import se.smrt.generator.types.Type;

import javax.lang.model.element.*;
import java.io.IOException;
import java.util.ArrayList;

public class MethodProcessor implements ElementVisitor<Void, ArrayList<Method>> {
	@Override
	public Void visit(Element element, ArrayList<Method> methods) {
		return null;
	}

	@Override
	public Void visit(Element element) {
		return null;
	}

	@Override
	public Void visitPackage(PackageElement packageElement, ArrayList<Method> methods) {
		return null;
	}

	@Override
	public Void visitType(TypeElement typeElement, ArrayList<Method> methods) {
		return null;
	}

	@Override
	public Void visitVariable(VariableElement variableElement, ArrayList<Method> methods) {
		return null;
	}

	@Override
	public Void visitExecutable(ExecutableElement e, ArrayList<Method> methods) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		for (VariableElement param : e.getParameters()) {
			parameters.add(new Parameter(new Type(param.asType().toString()), param.getSimpleName().toString()));
		}
		methods.add(new Method(e.getReturnType().toString(), e.getSimpleName().toString(), parameters));
		return null;
	}

	@Override
	public Void visitTypeParameter(TypeParameterElement typeParameterElement, ArrayList<Method> methods) {
		return null;
	}

	@Override
	public Void visitUnknown(Element element, ArrayList<Method> methods) {
		return null;
	}
}
