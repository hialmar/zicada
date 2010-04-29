package bot;

public class Roll {

    public static void main(String[] args) {
        //example of input.
        String input = "10d10+3";
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


    public String googleSearch(String query) throws IOException {

		if (query.isEmpty()) {
			return "Usage: !google <query>";
		} else {
			query = HelperClass.htmlEnc(query);
			String sourceUrlString = "http://www.google.com/search?q=" + query;
			try {
				source = new Source(new URL(sourceUrlString).openStream());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			// Parse the entire page right away.
			source.fullSequentialParse();

			// The first search result is in the class named "r".
			Element firstResultElement = source.getFirstElementByClass("r");
			// Fetch it and remove all the annoying newline characters by using
			// the HelperClass.
			return HelperClass.stripNewLine(firstResultElement.getRenderer()
					.toString());
		}
	}
}
