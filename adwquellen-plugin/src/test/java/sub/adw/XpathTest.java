package sub.adw;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XpathTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Xpath xpath = new Xpath(xml());
		assertEquals("testTitle", xpath.getString("//title"));
	}

	@Test
	public void testWithList() throws Exception {
		Xpath xpath = new Xpath(xmlWithList());
		List<String> results = xpath.getList("//name");
		assertEquals("name1", results.get(0));
		assertEquals("name2", results.get(1));
	}

	private String xml() {
		return "<mods><title>testTitle</title></mods>";
	}

	private String xmlWithList() {
		return "<mods><name>name1</name><name>name2</name></mods>";
	}
}
