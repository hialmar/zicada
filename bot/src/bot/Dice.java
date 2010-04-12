/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

/**
 *
 * @author b9
 */
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
        for (int i = b; i > 0; i--) {
            int x = 1;
            Dice rand = new Dice();
            String lol = ("Roll " + x + ": " + rand.rand(a, c) + "\n");
            x++;
            result += lol;
        }
        return result;
    }
}

                