/*
 * TestParser.java
 *
 * Created on April 26, 2001, 4:32 PM
 */

/**
 *
 * @author  twei
 * @version 
 */
public class TestParser extends Object {
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        BlastParser parser; 
        for (int i=0; i<args.length; i++) {
            try {
                parser = new BlastParser(args[i]);
                parser.parseBlast();
                parser.displayParsed();

            } catch (ParseException e) {
                System.out.println(e.toString()); 
            }
        }
    }

}
