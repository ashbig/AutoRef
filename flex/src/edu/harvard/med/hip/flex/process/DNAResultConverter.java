/*
 * DNAResultConverter.java
 *
 * Created on March 9, 2005, 10:53 AM
 */

package edu.harvard.med.hip.flex.process;

import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class DNAResultConverter extends FileResultConverter {
    public static final String DILIM = "\t";
    public static final int TOTALCOUNT = 96;
    public static final int ROW = 8;
    public static final String STARTSTRING = "DNA Concentrations";
    public static final double DROPOUT = 0.0;
    
    
    /** Creates a new instance of DNAResultConverter */
    public DNAResultConverter() {
        super();
    }
    
    public DNAResultConverter(InputStream input) {
        super(input);
        this.size = TOTALCOUNT;
    }
    
    public DNAResultConverter(InputStream input, int size) {
        super(input, size);
    }
    
    /**
     * Parsing the log file and store the results into objects.
     *
     * @return True if parsing successful; return false if failed at any step.
     */
    public boolean parseFile() {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        boolean start = false;
        int index = 0;
        int count = 0;
        
        try {
            while((line = in.readLine()) != null) {
                if(line.indexOf(STARTSTRING) != -1) {
                    start = true;
                }
                
                if(start) {
                    String ignore = in.readLine();
                    
                    for(int i=0; i<ROW; i++) {
                        line = in.readLine();
                        StringTokenizer st = new StringTokenizer(line, DILIM);
                        String rownum = st.nextToken();
                        
                        while(st.hasMoreTokens() && index < size) {
                            odList.put(new Integer(index), st.nextToken());
                            index = index+8;
                        }
                        count++;
                        index = count;
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public boolean convertResults() {
        if(odList == null) {
            setErrorMessage("No results to be converted.");
            return false;
        }
        
        try {
            for(int i = 0; i<odList.size(); i++) {
                Integer k = new Integer(i);
                if(odList.containsKey(k)) {
                    String result = (String)odList.get(k);
                    resultList.add(result);
                }
            }
        } catch (Exception ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
}
