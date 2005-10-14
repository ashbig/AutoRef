/*
 * StringConvertor.java
 *
 * Created on March 31, 2005, 11:56 AM
 */

package plasmid.util;

import java.util.*;
import java.lang.*;

/**
 *
 * @author  DZuo
 */
public class StringConvertor {
    
    /** Creates a new instance of StringConvertor */
    public StringConvertor() {
    }
    
    public String converFromListToString(List l) {
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
   
    public static void main(String args[]) {
        StringConvertor sc = new StringConvertor();
        List l = new ArrayList();
        l.add("abc");
        l.add("def");
        l.add("mnh");
        System.out.println(sc.converFromListToString(l));
        System.exit(0);
    }
}
