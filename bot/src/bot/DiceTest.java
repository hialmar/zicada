package bot;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiceTest {

	@Test
	// test wether rolling a onesided die once and subtracting 1 equals 0
	public void testRollPrint() {
		Dice tester = new Dice();
		assertEquals("Result", 0, tester.roll(1, 1, -1));
	}

}
