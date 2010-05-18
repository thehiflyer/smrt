package se.smrt.generator.types;

import se.smrt.generator.Util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Type implements Comparable {
	private final String name;
	private final Wildcard wildcard;

	private final List<Type> genericParameters = new ArrayList<Type>();

	public Type(String fullName) {
		this(new StringReader(fullName));
	}

	private Type(StringReader reader) {
		this(new TypeTokenizer(reader));
	}

	private Type(TypeTokenizer tokenizer) {

		List<? super InputStream> list = new ArrayList<InputStream>();
		list.add(new DataInputStream(null));
		
		Wildcard wildcard = Wildcard.NONE;
		TypeToken token = tokenizer.peek();

		if (token.getKind() == TypeKind.question) {
			token = tokenizer.consume();
			if (token.getKind() == TypeKind.text) {
				if (token.getValue().equals("extends")) {
					wildcard = Wildcard.EXTENDS;
				} else if (token.getValue().equals("super")) {
					wildcard = Wildcard.SUPER;
				} else {
					throw new IllegalArgumentException("Invalid syntax");
				}
				token = tokenizer.consume();
			} else {
				this.name = "";
				this.wildcard = Wildcard.ANY;
				return;
			}
		} else if (token.getKind() != TypeKind.text) {
			throw new IllegalArgumentException("Invalid syntax");
		}
		this.wildcard = wildcard;

		name = token.getValue();
		token = tokenizer.consume();
		
		if (token.getKind() == TypeKind.left) {
			tokenizer.consume();
			while (true) {
				genericParameters.add(new Type(tokenizer));
				token = tokenizer.peek();
				if (token.getKind() != TypeKind.comma) {
					break;
				}
				tokenizer.consume();
			}
			if (token.getKind() == TypeKind.right) {
				tokenizer.consume();
			} else {
				throw new IllegalArgumentException("Invalid syntax");
			}
		}
	}

	public String getFullName() {
		return name;
	}

	private String getFullUsageName() {
		return wildcard.getName(getFullName());
	}

	public String getSimpleName() {
		return Util.getSimpleName(name);
	}

	private String getSimpleUsageName() {
		return wildcard.getName(getSimpleName());
	}

	public String getPackage() {
		return Util.getPackage(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Type)) {
			return false;
		}

		Type type = (Type) o;

		if (wildcard != type.wildcard) {
			return false;
		}

		if (!genericParameters.equals(type.genericParameters)) {
			return false;
		}
		if (!name.equals(type.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + genericParameters.hashCode();
		return result;
	}

	public String getDescriptionWithGenerics() {
		StringBuilder builder = new StringBuilder();
		builder.append(getSimpleDescription());
		if (getGenericParameters().size() > 0) {
			builder.append("Of");
			boolean first = true;
			for (Type genericParameter : genericParameters) {
				if (first) {
					first = false;
				} else {
					builder.append("And");
				}
				builder.append(genericParameter.getDescriptionWithGenerics());
			}
		}
		return builder.toString();
	}

	public boolean isArray() {
		return getSimpleName().endsWith("[]");
	}

	public Type getArrayComponent() {
		if (isArray()) {
			return new Type(name.substring(0, name.length() - 2));
		}
		return null;
	}

	private String getSimpleDescription() {
		return getSimpleName().replace("[]", "Array");

	}

	public String getFullNameWithGenericsAndWildcards() {
		return getFullNameWithGenerics(false);
	}

	public String getFullNameWithGenerics() {
		return getFullNameWithGenerics(true);
	}

	private String getFullNameWithGenerics(boolean toplevel) {
		StringBuilder builder = new StringBuilder();
		if (toplevel) {
			builder.append(getFullName());
		} else {
			builder.append(getFullUsageName());
		}
		if (getGenericParameters().size() > 0) {
			builder.append('<');
			boolean first = true;
			for (Type genericParameter : genericParameters) {
				if (first) {
					first = false;
				} else {
					builder.append(", ");
				}
				builder.append(genericParameter.getFullNameWithGenerics(false));
			}
			builder.append('>');
		}
		return builder.toString();
	}

	public String getNameWithGenerics(String myPackage, List<String> imports) {
		return getNameWithGenerics(myPackage, imports, true);
	}

	private String getNameWithGenerics(String myPackage, List<String> imports, boolean topLevel) {
		StringBuilder builder = new StringBuilder();
		if (imports.contains(this.getFullName()) || !canImport(myPackage)) {
			if (topLevel) {
				builder.append(getSimpleName());
			} else {
				builder.append(getSimpleUsageName());
			}
		} else {
			if (topLevel) {
				builder.append(getFullName());
			} else {
				builder.append(getFullUsageName());
			}
		}

		if (getGenericParameters().size() > 0) {
			builder.append('<');
			boolean first = true;
			for (Type genericParameter : genericParameters) {
				if (first) {
					first = false;
				} else {
					builder.append(", ");
				}
				builder.append(genericParameter.getNameWithGenerics(myPackage, imports, false));
			}
			builder.append('>');
		}
		return builder.toString();
	}

	@Override
	public int compareTo(Object o) {
		if (this == o) {
			return 0;
		}
		if (!(o instanceof Type)) {
			return -1;
		}

		Type type = (Type) o;
		int v = name.compareTo(type.name);
		if (v != 0) {
			return v;
		}

		v = wildcard.ordinal() - type.wildcard.ordinal();
		if (v != 0) {
			return v;
		}

		int numShared = Math.min(genericParameters.size(), type.genericParameters.size());
		for (int i = 0; i < numShared; i++) {
			v = genericParameters.get(i).compareTo(type.genericParameters.get(i));
			if (v != 0) {
				return v;
			}
		}
		v = genericParameters.size() - type.genericParameters.size();
		return 0;
	}

	@Override
	public String toString() {
		return getFullNameWithGenerics();
	}

	public List<Type> getGenericParameters() {
		return genericParameters;
	}

	public boolean canImport(String myPackage) {
		String packageName = getPackage();
		return Util.canImport(packageName, myPackage);
	}

	enum TypeKind {whitespace, comma, left, right, text, question, eof};

	static class TypeToken {
		@Override
		public String toString() {
			return "TypeToken{" +
					"value='" + value + '\'' +
					", kind=" + kind +
					'}';
		}

		public static TypeToken EOF = new TypeToken(TypeKind.eof, null);
		public static TypeToken LEFT = new TypeToken(TypeKind.left, null);
		public static TypeToken RIGHT = new TypeToken(TypeKind.right, null);
		public static TypeToken COMMA = new TypeToken(TypeKind.comma, null);
		public static TypeToken QUESTION = new TypeToken(TypeKind.question, null);

		private final String value;

		private final TypeKind kind;

		private TypeToken(TypeKind kind, String value) {
			this.kind = kind;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public TypeKind getKind() {
			return kind;
		}

		private static TypeToken parseClassName(StringReader reader, StringBuilder data) throws IOException {
			while (true) {
				reader.mark(0);
				int i = reader.read();
				if (i == -1) {
					break;
				}
				char ch = (char) i;
				if (ch == ' ' || ch == '\t' || ch == '<' || ch == '>' || ch == ',' || ch == '?') {
					reader.reset();
					break;
				} else {
					data.append(ch);
				}
			}
			return new TypeToken(TypeKind.text, data.toString());
		}

		private static TypeToken parseWhitespace(StringReader reader, StringBuilder data) throws IOException {
			while (true) {
				reader.mark(0);
				char ch = (char) reader.read();
				if (ch == ' ' || ch == '\t') {
					data.append(ch);
				} else {
					reader.reset();
					break;
				}
			}
			return new TypeToken(TypeKind.whitespace, data.toString());
		}

		public static TypeToken get(StringReader reader) {
			try {
				StringBuilder data = new StringBuilder();
				int i = reader.read();
				if (i < 0) {
					return TypeToken.EOF;
				}
				char ch = (char) i;
				data.append(ch);
				if (ch == ' ' || ch == '\t') {
					return parseWhitespace(reader, data);
				}  else if (ch == '<') {
					return TypeToken.LEFT;
				} else if (ch == '>') {
					return TypeToken.RIGHT;
				} else if (ch == ',') {
					return TypeToken.COMMA;
				} else if (ch == '?') {
					return TypeToken.QUESTION;
				} else {
					return parseClassName(reader, data);
				}
			} catch (IOException e) {
				return TypeToken.EOF;
			}
		}
	}

	static class TypeTokenizer {
		private final StringReader reader;
		private TypeToken nextToken;

		public TypeTokenizer(StringReader reader) {
			this.reader = reader;
			nextToken = getNextRealToken();
		}

		public TypeToken peek() {
			return nextToken;
		}

		public TypeToken consume() {
			nextToken = getNextRealToken();
			return nextToken;
		}

		private TypeToken getNextRealToken() {
			while (true) {
				TypeToken token = null;
				token = TypeToken.get(reader);
				if (token.kind != TypeKind.whitespace) {
					return token;
				}
			}
		}
	}
}
