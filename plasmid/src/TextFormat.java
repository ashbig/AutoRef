/*
 * TextFormat.java
 *
 * Created on March 15, 2006, 10:47 AM
 */

/**
 *
 * @author  DZuo
 */
public class TextFormat {
    
    /** Creates a new instance of TextFormat */
    public TextFormat() {
    }
    
    public static String format(String text, int width, boolean isLeft, char filler) {
        String s = "";
        if(isLeft) {
            s += text;
            for(int i=0; i<width-text.length(); i++) {
                s += filler;
            }
        } else {
            for(int i=0; i<width-text.length(); i++) {
                s += filler;
            }
            s += text;
        }
        return s;
    }
}
