/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import javax.swing.JOptionPane;

/**
 *
 * @author b9
 */
public class testrandom {

    public static void main(String[] args) {


        // example of what the input from IRC would look like
        String input = "!roll d6d6+3";

        

        // fetching the parts wanted from the input
       
        // Number of eyes on the dice
        String dProp1 = input.substring(7,8);
        // Number of time the dice is rolled
        String dProp2 = input.substring(9,10);
        // Dice bonus. If it is +2, then when the scrip rolls 4, it will come out as 6.
        String dProp3 = input.substring(11,12);


        // Parsing the strings.
        int a = Integer.parseInt(dProp1);
        int b = Integer.parseInt(dProp2);
        int x = Integer.parseInt(dProp3);
        int c = 1;

        // Rolling the dice, then appending the results to the result string.
       String result = "";
        for (int i = b; i > 0; i--) {

            Dice rand = new Dice();


            String lol =  ("Roll " + c + ": " + rand.rand(a, x) + "\n");
            c++;

            result += lol;

        }

       // Prints out what has been appended to result.
       System.out.println(result);
    }
}
