package sub.adw;

import static org.junit.Assert.*;

import org.junit.Test;

public class PicaTest {

	@Test
	public void test() {
		Pica pica = new Pica();
		pica.init("bla\n002@ $0Aau\nblub");
		pica.isParent();
	}

}
