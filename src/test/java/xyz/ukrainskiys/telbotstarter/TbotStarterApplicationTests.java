package xyz.ukrainskiys.telbotstarter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZalupaServiceTest {

	private final ZalupaService zalupaService = new ZalupaService();

	@Test
	void zalupaTest() {
		assertEquals(zalupaService.zalupa(), "zalupa");
	}

}
