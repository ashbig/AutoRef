/*
 * StringConvertor.java
 *
 * Created on October 18, 2007, 2:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author dzuo
 */
public class StringConvertor {
    
    /** Creates a new instance of StringConvertor */
    public StringConvertor() {
    }
    
    public static List convertFromStringToList(String s, String dilim) throws UtilException {
        if(s == null)
            return null;
        
        StringTokenizer tok = new StringTokenizer(s.trim(), dilim);
        List l = new ArrayList();
        
        try {
            while(tok.hasMoreTokens()) {
                String ss = tok.nextToken().trim();
                l.add(ss);
            }
        } catch (Exception ex) {
            throw new UtilException("Error occured while parsing string: "+s+"\n"+ex.getMessage());
        }
        
        return l;
    }
    
    public static String convertFromListToString(Collection l) {
        if(l == null)
            return "";
        
        String s = "";
        Iterator iter = l.iterator();
        while(iter.hasNext()) {
            s = s + iter.next()+", ";
        }
        
        int index = s.lastIndexOf(",");
        if(index > 0)
            s = s.substring(0, index);
        
        return s;
    }
    
    public static String convertFromListToSqlList(List l) {
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
}
