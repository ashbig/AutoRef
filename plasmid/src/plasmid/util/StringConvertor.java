/*
 * StringConvertor.java
 *
 * Created on March 31, 2005, 11:56 AM
 */

package plasmid.util;

import java.util.*;
import java.lang.*;

import plasmid.Constants;
import plasmid.query.coreobject.QueryOperator;

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
   
    public List convertFromStringToCapList(String s, String delimiter) {
        List l = new ArrayList();
        
        if(s == null)
            return l;
        
        StringTokenizer tokenizer = new StringTokenizer(s, delimiter);
        try {
            while(tokenizer.hasMoreTokens()) {
                String st = tokenizer.nextToken().trim();
                if(st.length()>0)
                    l.add(st.toUpperCase());
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
    
    public static String convertFromQueryOperatorToSqlString(List operators, String logicOperator, String columnName) {
        String sql = null;
        for(int i=0; i<operators.size();i++) {
            if(sql != null) {
                sql += " "+logicOperator;
            }
            
            QueryOperator op = (QueryOperator)operators.get(i);
            String logicOp = op.getLogicOperator();
            List l = op.getValues();
            for(int j=0; j<l.size(); j++) {
                String s = (String)l.get(j);
                if(sql == null) {
                    sql = "("+columnName+"='"+s+"'";
                } else if(j==0) {
                    sql += " ("+columnName+"='"+s+"'";
                } else {
                    sql += " "+logicOp+" "+columnName+"='"+s+"'";
                }
            }
            sql += ")";
        }
        return sql;
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
