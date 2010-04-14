package bot;

import java.util.Random;

public class Dice {

	private Random generator;
	private int randlim;

	public Dice() {
		generator = new Random();
	}

	public int rand(int a, int b) {
		randlim = generator.nextInt(a);
		return randlim + 1 + b;
	}

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
