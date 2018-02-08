package sub.adw;

import static org.junit.Assert.*;

import org.junit.Test;

public class PicaTest {

	private Pica pica = new Pica();

	@Test
	public void shouldRecognizeAutopsy() {
		pica.init("bla\n002@ $0Aau\nblub");
		assertEquals("autopsy", pica.getCreationMethod());
	}

	@Test
	public void shouldGetParent() {
		pica.init("036D $X1.1928$912345678$aFranz$");
		assertEquals("12345678", pica.getParentPpn());
	}

}
