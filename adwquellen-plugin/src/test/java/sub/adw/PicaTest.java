package sub.adw;

import static org.junit.Assert.*;

import org.junit.Test;

public class PicaTest {

	private Pica pica = new Pica();

	@Test
	public void shouldRecognizeParent() {
		pica.init("bla\n002@ $0Aau\nblub");
		assertEquals("true", pica.isParent());
	}

	@Test
	public void shouldRecognizeAutopsy() {
		pica.init("bla\n002@ $0Aau\nblub");
		assertEquals("autopsy", pica.getCreationMethod());
	}

}
