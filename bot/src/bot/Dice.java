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
	 * @return the int
	 */
    public int rand(int a) {
        randlim = generator.nextInt(a);
        return randlim + 1;
    }

	/**
	 * Test.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return the string
	 */
    public int test(int a, int b, int c) {
        int result = 0;
        for (int i = a; i > 0; i--) {
            Dice rand = new Dice();
            int getDice = (rand.rand(b));
            result += getDice;
        }
        result += c;
        return result;
      }
}

