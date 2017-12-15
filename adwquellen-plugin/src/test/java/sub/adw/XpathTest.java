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
	public void shouldSelectString() throws Exception {
		Xpath xpath = new Xpath(xml());
		assertEquals("testTitle", xpath.getString("//title"));
	}

	@Test
	public void shouldSelectAllNames() throws Exception {
		Xpath xpath = new Xpath(xmlWithList());
		List<String> results = xpath.getList("//name");
		assertEquals("name1", results.get(0));
		assertEquals("name2", results.get(1));
	}

	@Test
	public void shouldSelectSomeSubelements() throws Exception {
		Xpath xpath = new Xpath(xmlWithTitle());
		List<String> results = xpath.getList("/mods/titleInfo/*[self::title or self::nonSort]");
		assertEquals(2, results.size());
		assertEquals("Die ", results.get(0));
		assertEquals("bla", results.get(1));
	}

	private String xml() {
		return "<mods><title>testTitle</title></mods>";
	}

	private String xmlWithList() {
		return "<mods><name>name1</name><name>name2</name></mods>";
	}
	private String xmlWithTitle() {
		return "<mods><titleInfo><nonSort>Die </nonSort><title>bla</title><test>test</test></titleInfo></mods>";
	}
}
