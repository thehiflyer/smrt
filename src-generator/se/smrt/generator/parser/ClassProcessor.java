package se.smrt.generator.parser;

import se.smrt.core.SmrtProtocol;
import se.smrt.core.ProtocolVersion;
import se.smrt.generator.types.Method;
import se.smrt.generator.types.Type;

import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.Map;

public class ClassProcessor implements ElementVisitor<ProtocolData, Map<Type, ProtocolData>> {
	public ClassProcessor() {
	}

	@Override
	public ProtocolData visit(Element element, Map<Type, ProtocolData> protocols) {
		return null;
	}

	@Override
	public ProtocolData visit(Element element) {
		return null;
	}

	@Override
	public ProtocolData visitPackage(PackageElement packageElement, Map<Type, ProtocolData> protocols) {
		return null;
	}

	@Override
	public ProtocolData visitType(TypeElement e, Map<Type, ProtocolData> protocols) {
		String className = e.getQualifiedName().toString();
		String protocolName = e.getAnnotation(SmrtProtocol.class).value();
		ProtocolVersion annotation = e.getAnnotation(ProtocolVersion.class);
		String protocolVersion = annotation != null ? annotation.value() : "";

		ArrayList<Method> methods = new ArrayList<Method>();
		ProtocolData protocol = new ProtocolData(className, protocolName, protocolVersion, methods, protocols);

		for (Element element : e.getEnclosedElements())
		{

			element.accept(new MethodProcessor(), methods);
		}
		return protocol;
	}

	@Override
	public ProtocolData visitVariable(VariableElement variableElement, Map<Type, ProtocolData> protocols) {
		return null;
	}

	@Override
	public ProtocolData visitExecutable(ExecutableElement executableElement, Map<Type, ProtocolData> protocols) {
		return null;
	}

	@Override
	public ProtocolData visitTypeParameter(TypeParameterElement typeParameterElement, Map<Type, ProtocolData> protocols) {
		return null;
	}

	@Override
	public ProtocolData visitUnknown(Element element, Map<Type, ProtocolData> protocols) {
		return null;
	}
}
