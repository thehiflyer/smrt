package se.smrt.generator.template;

import se.smrt.generator.parser.MessageClass;
import se.smrt.generator.parser.ProtocolData;
import se.smrt.generator.types.Type;
import se.smrt.generator.Util;

import java.io.IOException;
import java.util.*;

public class ImportManager {
	private final Map<String, Set<String>> usedClasses;
	private final Set<Type> tokens;

	private String outputString;
	private final String myPackage;

	public ImportManager(String myPackage) {
		this.myPackage = myPackage;
		usedClasses = new HashMap<String, Set<String>>();
		tokens = new HashSet<Type>();
	}


	public List<String> getImports() {
		ArrayList<String> imports = new ArrayList<String>();
		for (Map.Entry<String, Set<String>> entry : usedClasses.entrySet()) {
			if (entry.getValue().size() == 1) {
				String type = entry.getValue().iterator().next();
				String packageName = Util.getPackage(type);
				if (Util.canImport(packageName, myPackage)) {
					imports.add(type);
				}
			}
		}
		Collections.sort(imports);
		return imports;
	}

	public String postProcess(String javaSource) {
		StringBuilder builder = new StringBuilder();
		List<String> importList = getImports();
		for (String type : importList) {
			builder.append("import ").append(type).append(";\n");
		}
		javaSource = javaSource.replace("@@IMPORTS@@", builder.toString());

		for (Type type : tokens) {
			String token = getClassToken(type);
			javaSource = javaSource.replace(token, type.getNameWithGenerics(myPackage, importList));
		}
		return javaSource;
	}

	public String register(String fullName) {
		return register(new Type(fullName));
	}

	private Set<String> getOrCreate(Map<String, Set<String>> map, String s) {
		Set<String> collection = map.get(s);
		if (collection == null) {
			collection = new HashSet<String>();
			map.put(s, collection);
		}
		return collection;
	}

	public String register(ProtocolData protocol) {
		return register(protocol.getType());
	}

	public String register(Type type) {
		String simpleName = type.getSimpleName();

		Set<String> collection = getOrCreate(usedClasses, simpleName);
		collection.add(type.getFullName());
		tokens.add(type);

		for (Type subtype : type.getGenericParameters()) {
			register(subtype);
		}
		return getClassToken(type);

	}

	public String register(MessageClass clazz) {
		return register(clazz.getType());
	}

	private String getClassToken(Type type) {
		return "@@CLASS:" + type.getFullNameWithGenerics() + "@@";
	}

}
