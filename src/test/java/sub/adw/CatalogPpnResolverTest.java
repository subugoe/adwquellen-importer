package sub.adw;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatalogPpnResolverTest {

	private CatalogPpnResolver resolver = new CatalogPpnResolver();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=FileNotFoundException.class)
	public void shouldFailToFetch() throws Exception {
		resolver.fetchByPpn("787266841", CatalogPpnResolver.MODS_FORMAT);
	}

	@Test
	public void shouldFetchPica() throws Exception {
		String pica = resolver.fetchByPpn("145520943", CatalogPpnResolver.PICA_FORMAT);
		assertThat(pica, containsString("002@ $0Aau"));
	}

	@Test
	public void shouldFetchMods() throws Exception {
		String mods = resolver.fetchByPpn("145520943", CatalogPpnResolver.MODS_FORMAT);
		assertThat(mods, containsString("Friedrich von Schwaben"));
	}

}
