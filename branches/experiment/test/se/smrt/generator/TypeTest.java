package se.smrt.generator;

import org.junit.Test;
import se.smrt.generator.types.Type;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
	public void testGenericsType2WithWildcards() throws IOException {
		Type t = new Type("java.util.Map<java.lang.String, ? extends java.lang.Number>");
		assertEquals("java.util.Map", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("Map", t.getSimpleName());
		assertEquals(2, t.getGenericParameters().size());
		assertEquals("String", t.getGenericParameters().get(0).getSimpleName());
		assertEquals("Number", t.getGenericParameters().get(1).getSimpleName());
		assertEquals("? extends java.lang.Number", t.getGenericParameters().get(1).getFullNameWithGenericsAndWildcards());
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
		assertEquals("", t.getFullName());
		assertEquals("", t.getSimpleName());
		assertEquals("", t.getPackage());
	}

	@Test
	public void testSimpleWildcard2() throws IOException {
		Type t = new Type("List<?>");
		assertEquals("List", t.getFullName());
		assertEquals("List", t.getSimpleName());
		assertEquals("", t.getPackage());
		assertEquals("List<?>", t.getFullNameWithGenerics());
	}

	@Test
	public void testExtendsWildcard() throws IOException {
		Type t = new Type("? extends java.util.List");
		assertEquals("List", t.getSimpleName());
		assertEquals("java.util.List", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("java.util.List", t.getFullNameWithGenerics());
	}

	@Test
	public void testExtendsWildcard2() throws IOException {
		Type t = new Type("java.util.List<? extends Foo>");
		assertEquals("List", t.getSimpleName());
		assertEquals("java.util.List", t.getFullName());
		assertEquals("java.util", t.getPackage());
		assertEquals("java.util.List<? extends Foo>", t.getFullNameWithGenerics());
	}

}
