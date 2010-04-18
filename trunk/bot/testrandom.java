/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

/**
 * 
 * @author b9
 */
public class testrandom {

	public static void main(String[] args) {

		// example of input. the + can also be -
		String input = "!roll d5d6+2";

		// checks wether the string contains "+" or "-" then changes it to a
		// "d".
		if (input.contains("+")) {
			input = input.replaceAll("[+]", "d");
			// splits the string at d's.
			String[] res = input.split("d");
			// Parsing the strings.
			int a = Integer.parseInt(res[1]);
			int b = Integer.parseInt(res[2]);
			int c = Integer.parseInt(res[3]);
			// creating dice object
			Dice diceObject = new Dice();
			// runs the method using the user input and returns
			// the result to the string named result.
			String result = diceObject.test(a, b, c);
			System.out.println(result);

			// same as the if test above, but in this instance the string
			// contains a "-".
		} else if (input.contains("-")) {
			input = input.replaceAll("-", "d-");
			String[] res = input.split("d");
			int a = Integer.parseInt(res[1]);
			int b = Integer.parseInt(res[2]);
			int c = Integer.parseInt(res[3]);
			Dice diceObject = new Dice();
			String result = diceObject.test(a, b, c);
			System.out.println(result);

			// this code is run in cases where there is no "+" or "-" in the
			// input.
		} else {
			String[] res = input.split("d");
			int a = Integer.parseInt(res[1]);
			int b = Integer.parseInt(res[2]);
			int c = 0;
			Dice diceObject = new Dice();
			String result = diceObject.test(a, b, c);
			System.out.println(result);
		}

	}
}
