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
     * Used to determine the number of eyes on the dice (int b)
     * then roll the dice the designated number of times (int a)
     * and lastly add or subtract int c from the total result before returning.
     *
     * @param a the a
     * @param b the b
     * @param c the c
     * @return the string
     */
    public int roll(int a, int b, int c) {
        int result = 0;
        for (int i = a; i > 0; i--) {
            Dice rand = new Dice();
            int getDice = (rand.rand(b));

            result += getDice;

        }
        result += c;
        return result;
    }

    /**
     * Does a series of tests to determine what to do with the input from IRC
     * which follows !roll. If the argument put by the user does not contain + nor -
     * it will add +0 to the input.
     * Then depending on wether the string contains either + or - it will replace this with "d"
     * so that the string is split and put into an array
     *
     * @param input the input
     * @return the string[]
     */
    public String[] output(String input) {

        String res[] = null;

        if (!input.contains("+") && !input.contains("-")) {
            input += "+0";

        }
        if (input.contains("-")) {
            input = input.replaceAll("[-]", "d-");

        } else {
            input = input.replaceAll("[+]", "d");
        }

        res = input.split("d");
        return res;
    }

    /**
     * Fetches the argument from the user, if no argument is present it will inform
     * the the user of this. When an argument is present, it will send the argument to
     * the output method and then parse the retrieved array into three int values
     * which is sent used in the roll method.
     * Lastly it puts the result into a string which is returned back to the user on IRC
     *
     * @param query the query
     * @return the string
     */
    public String rollPrint(String query) {

        if (query.isEmpty()) {

            return "Usage: !roll XdY+/-Z";

        } else {
            String res[] = output(query);
            /*Parses the array to integers*/
            int x = Integer.parseInt(res[0]);
            int y = Integer.parseInt(res[1]);
            int z = Integer.parseInt(res[2]);
            /*Runs the function which rolls the dice,
            using the numbers fetched from input */
            int result = roll(x, y, z);
            String diceResult = ("you rolled " + query + ". And got: " + result);
            return diceResult;

        }
    }
}