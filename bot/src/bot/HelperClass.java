package bot;

public class HelperClass {

    public static String htmlEnc(String query){
        return query.toLowerCase().replaceAll(" ", "%20")
                                  .replaceAll("\\+", "%2b")
                                  .replaceAll("ø", "%F8")
                                  .replaceAll("æ", "%E6")
                                  .replaceAll("å", "%E5");

    }
    public static String stripNewLine(String query){
        return query.toLowerCase().replaceAll("\\n", " ")
                                  .replaceAll("<", "")
                                  .replaceAll(">", "")
                                  .replaceAll("\\r", "");
    }
}
