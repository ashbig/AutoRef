/*
 * QueryParser.java
 *
 * Created on September 17, 2002, 11:24 AM
 */

package edu.harvard.med.hip.flex.query;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class QueryParser {    
    private ArrayList output = null;
    private String message = null;
    
    /** Creates new QueryParser */
    public QueryParser() {
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public ArrayList getOutput() {
        return output;
    }
    
    /**
     * Parse a string by dilimiter and put all the dilimited fields into an array.
     *
     * @param term The string needs to be parsed.
     * @return True if parsing successful; false otherwise.
     */
    public boolean parse(String term) {
        output = new ArrayList();
        StringTokenizer st = new StringTokenizer(term);
        
        try {
            while(st.hasMoreTokens()) {
                String t = st.nextToken();
                output.add(t.trim());
            }
        } catch (NoSuchElementException ex) {
            setMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static void main(String args[]) {
        QueryParser parser = new QueryParser();
        String term = "abc d, en\nfk\tghi";
                    
        if(parser.parse(term)) {
            ArrayList output = parser.getOutput();
            System.out.println(output);
        } else {
            System.out.println(parser.getMessage());
        }
    }
}
