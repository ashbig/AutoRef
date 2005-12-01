/*
 * StringConvertor.java
 *
 * Created on March 31, 2005, 11:56 AM
 */

package plasmid.util;

import java.util.*;
import java.lang.*;

import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class StringConvertor {
    
    /** Creates a new instance of StringConvertor */
    public StringConvertor() {
    }
    
    public String convertFromListToString(List l) {
        if(l == null)
            return "";
        
        String s = "";
        for(int i=0; i<l.size(); i++) {
            s = s + l.get(i)+", ";
        }
        
        int index = s.lastIndexOf(",");
        if(index > 0)
            s = s.substring(0, index);
        
        return s;
    }
   
    public List convertFromStringToList(String s, String delimiter) {
        List l = new ArrayList();
        
        if(s == null)
            return l;
        
        StringTokenizer tokenizer = new StringTokenizer(s, delimiter);
        try {
            while(tokenizer.hasMoreTokens()) {
                String st = tokenizer.nextToken().trim();
                if(st.length()>0)
                    l.add(st);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        
        return l;            
    }
       
    public static String convertFromListToSqlString(List l) {
        if(l == null)
            return "''";
        
        String s = "";
        for(int i=0; i<l.size(); i++) {
            s = s + "'"+l.get(i)+"', ";
        }
        
        int index = s.lastIndexOf(",");
        if(index > 0)
            s = s.substring(0, index);
        
        return s;
    }
    
    public static void main(String args[]) {
        StringConvertor sc = new StringConvertor();
        List l = new ArrayList();
        l.add("abc");
        l.add("def");
        l.add("mnh");
        System.out.println(sc.convertFromListToString(l));
        
        String s = "abc\ndef\n gh   jk";
        List lt = sc.convertFromStringToList(s, "\n \t");
        System.out.println(lt);
        
        System.exit(0);
    }
}
