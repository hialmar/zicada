package bot;

import java.util.Random;

/**
 * The Class Dice.
 */
public class Dice {

	/** The generator. */
	private Random generator;
	
	/** The randlim. */
	private int randlim;

	/**
	 * Instantiates a new dice.
	 */
	public Dice() {
		generator = new Random();
	}

	/**
	 * Rand.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the int
	 */
	public int rand(int a, int b) {
		randlim = generator.nextInt(a);
		return randlim + 1 + b;
	}

	/**
	 * Test.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return the string
	 */
	public String test(int a, int b, int c) {
		String result = "";
		int x = 1;
		for (int i = b; i > 0; i--) {
			Dice rand = new Dice();
			String lol = ("Roll " + x + ": " + rand.rand(a, c) + "\n");
			x++;
			result += lol;
		}
		return result;
	}
}
