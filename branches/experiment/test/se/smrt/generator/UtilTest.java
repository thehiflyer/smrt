package se.smrt.generator;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

	@Test
	public void testSimpleNameEasy() {
		String fullName = "se.spaced.messages.protocol.Entity";
		String simpleName = Util.getSimpleName(fullName);

		assertEquals("Entity", simpleName);
	}

}

