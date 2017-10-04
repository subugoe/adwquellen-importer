package sub.adw;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImporterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void test() throws Exception {
		Importer importer = new Importer();
		//importer.convert("/home/dennis/temp/in08/FWB-Quellenliste.xlsx", "/home/dennis/temp/testout");
		//importer.upload("/home/dennis/temp/testout", "http://localhost:8984/solr", "adwquellen");
		importer.upload("/home/dennis/temp/testout", "http://abc.sub.uni-goettingen.de:8073/solr", "adwquellen");
	}

}
