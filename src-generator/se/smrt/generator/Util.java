package se.smrt.generator;

public class Util {

	public static String getPackage(String className) {
		int index = className.lastIndexOf('.');
		if (index == -1) {
			return "";
		}
		return className.substring(0, index);
	}

	public static String getSimpleName(String className) {
		int index = className.lastIndexOf('.');
		if (index == -1) {
			return className;
		}
		return className.substring(index + 1);
	}

	public static boolean canImport(String packageName, String myPackage) {
		return !(packageName.equals("java.lang") || packageName.equals("") || packageName.equals(myPackage));
	}
}
