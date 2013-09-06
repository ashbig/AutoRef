/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author dongmei
 */
public class StringConvertor {
    public static final int FASTA_LENGTH = 80;
    public static final int FASTA_SUB_LENGTH = 10;
    
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
    
    public static String staticConvertFromListToString(List l) {
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
   
    public static List convertFromStringToList(String s, String delimiter) throws UtilException {
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
            throw new UtilException(ex.getMessage());
        }
        
        return l;            
    }
   
    public List convertFromStringToCapList(String s, String delimiter) throws UtilException {
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
            throw new UtilException(ex.getMessage());
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
    
    public static String convertToFasta(String seq, int startIndex) {
        if (seq == null || seq.length()==0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        int substart = startIndex%FASTA_SUB_LENGTH;
        int subindex = startIndex-substart;
        int offset = startIndex%FASTA_LENGTH-substart;
        while (subindex + (FASTA_LENGTH-offset) < seq.length()) {
            String s = seq.substring(subindex, subindex+FASTA_LENGTH-offset);
            constructFastaSequence(s, sb, substart);
            sb.append("\n");
            subindex += FASTA_LENGTH-offset;
            substart = 0;
            offset = 0;
        }
        constructFastaSequence(seq.substring(subindex), sb, substart);
        return sb.toString();
    }
    
    private static void constructFastaSequence(String s, StringBuilder sb, int j) {
        int start = 0;
        while (start+FASTA_SUB_LENGTH < s.length()) {
            sb.append(s.substring(j, start+FASTA_SUB_LENGTH));
            sb.append(" ");
            start += FASTA_SUB_LENGTH;
            j = start;
        }
        sb.append(s.substring(j));
    }
    
    public static String getUniqueName(String name) {
        Random r = new Random();
        return name + "_" + r.nextLong();
    }
}
