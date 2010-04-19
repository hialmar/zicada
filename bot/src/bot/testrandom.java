package bot;

public class testrandom {

    public static void main(String[] args) {
        //example of input.
        String input = "1d2+2";
        //Creates dice object.
        Dice diceObject = new Dice();
        /*Runs the function output in the Dice class,
        which converts the string to an array.*/
        String res[] = diceObject.output(input);
        /*Parses the array to integers*/
        int x = Integer.parseInt(res[0]);
        int y = Integer.parseInt(res[1]);
        int z = Integer.parseInt(res[2]);
        /*Runs the function which rolls the dice,
        using the numbers fetched from input */
        int result = diceObject.roll(x, y, z);
        /*prints out the output from the dicerolls, and the original input*/
        System.out.println(input + " is " + result);
    }
}
