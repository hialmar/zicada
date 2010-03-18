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

    public int rand(int a) {




        randlim = generator.nextInt(a);

        return randlim + 1;


    }
}
