package sub.adw;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatalogResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		CatalogResolver resolver = new CatalogResolver();

		String mods = resolver.fetchByPpn("145520943", CatalogResolver.MODS_FORMAT);
		assertThat(mods, containsString("Friedrich von Schwaben"));

		String pica = resolver.fetchByPpn("145520943", CatalogResolver.PICA_FORMAT);
		assertThat(pica, containsString("002@ $0Aau"));
	}

}
