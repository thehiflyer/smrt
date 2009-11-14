package se.smrt.generator;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;

import se.smrt.generator.types.Type;

public class TypeTest {

	@Test
	public void testSimpleType() throws IOException {
		Type t = new Type("se.blabla.ClassName");
		assertEquals("ClassName", t.getSimpleName());
		assertEquals("se.blabla.ClassName", t.getFullName());
		assertEquals("se.blabla", t.getPackage());
		assertEquals(0, t.getGenericParameters().size());
	}

	@Test
	public void testGenericsType() throws IOException {
		Type t = new Type("java.util.List<java.lang.String>");
		assertEquals("java.util.List", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("List", t.getSimpleName());
		assertEquals(1, t.getGenericParameters().size());
		assertEquals("String", t.getGenericParameters().get(0).getSimpleName());
	}

	@Test
	public void testGenericsType2() throws IOException {
		Type t = new Type("java.util.List<java.lang.String, java.lang.Double>");
		assertEquals("java.util.List", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("List", t.getSimpleName());
		assertEquals(2, t.getGenericParameters().size());
		assertEquals("String", t.getGenericParameters().get(0).getSimpleName());
		assertEquals("Double", t.getGenericParameters().get(1).getSimpleName());
	}

	@Test
	public void testGenericsTypeComplex() throws IOException {
		Type t = new Type("java.util.List<java.util.Map<Foo, Bar>, java.lang.Double>");
		assertEquals("java.util.List", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("List", t.getSimpleName());
		assertEquals(2, t.getGenericParameters().size());
		assertEquals("Map", t.getGenericParameters().get(0).getSimpleName());
		assertEquals("Double", t.getGenericParameters().get(1).getSimpleName());

		assertEquals(2, t.getGenericParameters().get(0).getGenericParameters().size());
		assertEquals("Foo", t.getGenericParameters().get(0).getGenericParameters().get(0).getSimpleName());
		assertEquals("Bar", t.getGenericParameters().get(0).getGenericParameters().get(1).getSimpleName());
	}

	@Test
	public void testSimpleWildcard() throws IOException {
		Type t = new Type("?");
		assertEquals(t.getFullName(), "");
		assertEquals(t.getSimpleName(), "");
		assertEquals(t.getPackage(), "");
	}

	@Test
	public void testSimpleWildcard2() throws IOException {
		Type t = new Type("List<?>");
		assertEquals(t.getFullName(), "List");
		assertEquals(t.getSimpleName(), "List");
		assertEquals(t.getPackage(), "");
		assertEquals(t.getFullNameWithGenerics(), "List<?>");
	}

	@Test
	public void testExtendsWildcard() throws IOException {
		Type t = new Type("? extends java.util.List");
		assertEquals(t.getSimpleName(), "List");
		assertEquals(t.getFullName(), "java.util.List");
		assertEquals(t.getPackage(), "java.util");
		assertEquals(t.getFullNameWithGenerics(), "java.util.List");
	}

	@Test
	public void testExtendsWildcard2() throws IOException {
		Type t = new Type("java.util.List<? extends Foo>");
		assertEquals(t.getSimpleName(), "List");
		assertEquals(t.getFullName(), "java.util.List");
		assertEquals(t.getPackage(), "java.util");
		assertEquals(t.getFullNameWithGenerics(), "java.util.List<? extends Foo>");
	}

}
