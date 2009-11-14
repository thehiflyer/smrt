package se.smrt.generator;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import se.smrt.core.SmrtProtocol;
import se.smrt.generator.parser.ClassProcessor;
import se.smrt.generator.parser.ProtocolData;
import se.smrt.generator.template.ImportManager;
import se.smrt.generator.template.ProtocolMethod;
import se.smrt.generator.types.Method;
import se.smrt.generator.types.Parameter;
import se.smrt.generator.types.Type;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageCodeGenerator implements Processor {
	private final Map<Type, ProtocolData> protocols = new HashMap<Type, ProtocolData>();
	private Filer filer;
	private File templateDir;

	@Override
	public Set<String> getSupportedOptions() {
		return new HashSet<String>(Arrays.asList("templatedir"));
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(SmrtProtocol.class.getName());
		return hashSet;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		Map<String, String> options = processingEnv.getOptions();
		templateDir = new File(options.get("templatedir"));
		filer = processingEnv.getFiler();
	}

	@Override
	public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
		return new HashSet<Completion>();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			String annotationName = annotation.getQualifiedName().toString();

			if (annotationName.equals(SmrtProtocol.class.getCanonicalName())) {
				Set<? extends Element> annotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
				for (Element element : annotatedWith) {
					ProtocolData protocolData = element.accept(new ClassProcessor(), protocols);
					Type type = protocolData.getType();
					protocols.put(type, protocolData);
				}
			} else {
				throw new RuntimeException("Invalid annotation");
			}

		}
		if (roundEnv.processingOver()) {

			ArrayList<String> errors = new ArrayList<String>();
			postProcessData(errors);
			if (errors.size() > 0) {
				System.out.println("Found " + errors.size() + " errors when processing smrt annotations:");
				for (String error : errors) {
					System.out.println(error);
				}
				return true;
			}

			// Example output - just show what we've gatherered.
			dumpOutput();

			generateJava();
		}
		return true;
	}

	private void generateJava() {
		Collection<String> singletons = new ArrayList<String>();

		for (File file : templateDir.listFiles()) {
			String name = file.getName();
			if (name.equals("singletons")) {
				getTemplates(singletons, file, "");
			}
		}


		try {
			VelocityEngine engine = new VelocityEngine();
			engine.setProperty("resource.loader", "class");
			engine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
			engine.init();

			for (ProtocolData protocolData : protocols.values()) {
				if (protocolData.isRootProtocol()) {
					generateJavaFiles(engine, protocolData, singletons);
				}
			}
			// Generate output!

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateJavaFiles(VelocityEngine engine, ProtocolData protocolData, Collection<String> singletons) throws Exception {
		VelocityContext context = getContext(engine);
		context.put("protocol", protocolData);

		context.put("allmethods", getAllMethods(protocolData));
		context.put("allsubprotocols", getAllSubprotocols(protocolData));

		List<ProtocolData> subProtocols = getSubProtocols(protocolData);
		context.put("subprotocols", subProtocols);

		System.out.println(subProtocols);

		Set<String> typesString = new TreeSet<String>();
		findTypes(protocolData, typesString);

		Set<Type> types = new TreeSet<Type>();
		for (String s : typesString) {
			types.add(new Type(s));
		}

		context.put("types", types);

		for (String singleton : singletons) {
			generate(engine, protocolData, context, "singletons" + singleton, singleton.substring(1));
		}
	}

	private List<ProtocolMethod> getAllSubprotocols(ProtocolData protocolData) {
		return getAllSubprotocols(protocolData, "", new ArrayList<ProtocolMethod>());
	}

	private List<ProtocolMethod> getAllSubprotocols(ProtocolData protocolData, String chain, ArrayList<ProtocolMethod> protocolMethods) {
		for (Method method : protocolData.getMethods()) {
			ProtocolData subProtocol = protocols.get(method.getReturnType());
			if (method.getReturnType().getFullName().equals("void")) {
			} else {
				System.out.println("Adding protol " + method);
				protocolMethods.add(new ProtocolMethod(chain, method, subProtocol));
				getAllSubprotocols(subProtocol, chain + method.getName() + ".", protocolMethods);
			}
		}
		 return protocolMethods;
	}

	private List<ProtocolMethod> getAllMethods(ProtocolData protocolData) {
		return getAllMethods(protocolData, "", new ArrayList<ProtocolMethod>());
	}

	private List<ProtocolMethod> getAllMethods(ProtocolData protocolData, String chain, ArrayList<ProtocolMethod> protocolMethods) {
		for (Method method : protocolData.getMethods()) {
			ProtocolData subProtocol = protocols.get(method.getReturnType());
			if (method.getReturnType().getFullName().equals("void")) {
				protocolMethods.add(new ProtocolMethod(chain, method, subProtocol));
			} else {
				getAllMethods(subProtocol, chain + method.getName() + ".", protocolMethods);
			}
		}
		 return protocolMethods;
	}

	private List<ProtocolData> getSubProtocols(ProtocolData protocolData) {
		List<ProtocolData> subProtocols = new ArrayList<ProtocolData>();
		return getSubProtocols(protocolData, subProtocols);
	}

	private List<ProtocolData> getSubProtocols(ProtocolData protocolData, List<ProtocolData> subProtocols) {
		for (ProtocolData subProtocol : protocolData.getSubProtocols()) {
			subProtocols.add(subProtocol);
			getSubProtocols(subProtocol,  subProtocols);
		}
		return subProtocols;
	}

	private void postProcessData(ArrayList<String> errors) {
		for (Map.Entry<Type, ProtocolData> entry : protocols.entrySet()) {
			ProtocolData protocol = entry.getValue();
			validateProtocol(protocol, protocols, errors);
			if (protocol.isRootProtocol()) {
				validateRootProtocol(protocol, protocols, new HashSet<ProtocolData>(), errors);
			}
		}
	}

	private void validateRootProtocol(ProtocolData protocol, Map<Type, ProtocolData> protocols, HashSet<ProtocolData> visited, ArrayList<String> errors) {
		if (visited.contains(protocol)) {
			errors.add("Protocol " + protocol + " was used more than once.");
		} else {
			visited.add(protocol);
			List<Method> methods = protocol.getMethods();
			for (Method method : methods) {
				ProtocolData protocolData = protocols.get(method.getReturnType());
				if (protocolData != null) {
					validateRootProtocol(protocolData, protocols, visited, errors);
				}
			}
		}
	}

	private void validateProtocol(ProtocolData protocol, Map<Type, ProtocolData> protocols, ArrayList<String> errors) {
		List<Method> methods = protocol.getMethods();
		for (Method method : methods) {
			validateMethod(protocol, method, protocols, errors);
		}
	}

	private void validateMethod(ProtocolData protocol, Method method, Map<Type, ProtocolData> protocols, ArrayList<String> errors) {
		Type type = method.getReturnType();
		if (type.getFullName().equals("void")) {
			validateMessageMethod(protocol, method, protocols, errors);
		} else {
			validateSubProtocol(protocol, method, protocols, errors);
		}
	}

	private void validateSubProtocol(ProtocolData protocol, Method method, Map<Type, ProtocolData> protocols, ArrayList<String> errors) {
		Type type = method.getReturnType();
		if (!protocols.containsKey(type)) {
			errors.add("Invalid protocol in " + protocol + ", method " + method + " must return void or a sub protocol");
		}

		if (!method.getParameters().isEmpty()) {
			errors.add("Invalid protocol in " + protocol + ", method " + method + " must have no parameters if it returns a sub protocol");
		}
	}

	private void validateMessageMethod(ProtocolData protocol, Method method, Map<Type, ProtocolData> protocols, ArrayList<String> errors) {

	}

	private void findTypes(ProtocolData protocol, Set<String> types) {
		for (Method method : protocol.getMethods()) {
			if (method.getReturnType().getFullName().equals("void")) {
				findTypes(method, types);
			} else {
				ProtocolData subProtocol = protocols.get(method.getReturnType());
				findTypes(subProtocol, types);
			}
		}
	}

	private void findTypes(Method method, Set<String> types) {
		for (Parameter parameter : method.getParameters()) {
			findType(parameter.getType(), types);
		}
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


	private String withSlashes(String packageName) {
		return packageName.replace('.', '/');
	}

	private void getTemplates(Collection<String> collection, File dir, String path) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				getTemplates(collection, f, path + "/" + f.getName());
			} else {
				String filename = path + "/" + f.getName();
				if (filename.endsWith(".vm")) {
					collection.add(filename.replaceFirst("\\.vm$", ""));
				}
			}
		}
	}

	private VelocityContext getContext(VelocityEngine engine) throws Exception {
		VelocityContext context = new VelocityContext();

		engine.evaluate(context, new StringWriter(), "macros.vm", new FileReader(new File(templateDir, "core/macros.vm")));

		return context;
	}

	private void dumpOutput() {
		/*
		for (String messageInterface : protocols.keySet()) {
			ProtocolData bundle = protocols.get(messageInterface);

			System.out.println(messageInterface + ":");
			Collection<MessageClass> classes = bundle.getClasses();
			for (MessageClass aClass : classes) {
				System.out.println("\t" + aClass.getType().getFullName());
				List<Method> methods = aClass.getMethods();
				for (Method method : methods) {
					System.out.println("\t\t" + method.toString());
				}
			}
		}
		*/
	}

	private void generate(VelocityEngine engine, ProtocolData protocol, VelocityContext context, String templateFileName, String outputFileName) throws IOException {
		String packageBase = protocol.getType().getPackage();
		context.put("packageBase", packageBase);

		String packageExtension = getPackageExtension(templateFileName);
		String myPackage = join(packageBase, packageExtension);
		context.put("myPackage", myPackage);

		if (packageExtension.length() > 0) {
			outputFileName = outputFileName.substring(packageExtension.length() + 1);
		}

		ImportManager importManager = new ImportManager(myPackage);
		context.put("importManager", importManager);

		FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, myPackage, outputFileName, (Element) null);
		StringWriter writer = new StringWriter();
		engine.evaluate(context, writer, templateFileName + ".vm", new FileReader(new File(templateDir, templateFileName + ".vm")));
		writer.flush();
		String result = writer.getBuffer().toString();
		result = importManager.postProcess(result);

		Writer writer2 = resource.openWriter();
		engine.evaluate(context, writer2, "header.vm", new FileReader(new File(templateDir, "core/header.vm")));
		writer2.append(result);
		writer2.flush();
	}

	private static final Pattern packageExtracter = Pattern.compile("[^/]*/(.*)/[^/]*$");
	private String getPackageExtension(String templateFileName) {
		Matcher matcher = packageExtracter.matcher(templateFileName);
		if (matcher.matches()) {
			return matcher.group(1).replace('/', '.');
		}
		return "";
	}

	private String join(String prefix, String suffix) {
		if (suffix == null || suffix.length() == 0) {
			return prefix;
		}
		if (prefix == null || prefix.length() == 0) {
			return suffix;
		}
		return prefix + "." + suffix;
	}
}
