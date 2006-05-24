/*
 * CultureResultConverter.java
 *
 * Created on February 4, 2005, 1:19 PM
 */

package edu.harvard.med.hip.flex.process;

import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class CultureResultConverter extends FileResultConverter {
    public static final String DILIM = "\t";
    public static final int TOTALCOUNT = 96;
    public static final int IGNORELINES = 3;
    public static final int ROW = 8;
    
    public static final double GROW = 0.110;
    public static final double NOGROW = 0.055;    
    
    /** Creates a new instance of CultureResultConverter */
    public CultureResultConverter() {
        super();
    }

    public CultureResultConverter(InputStream input) {
        super(input);
        this.size = TOTALCOUNT;
    }

    public CultureResultConverter(InputStream input, int size) {
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
        int index = 0;
        int count = 0;
        
        try {
            for(int i=0; i<IGNORELINES; i++) {
                in.readLine();
            }
            
            while((line = in.readLine()) != null) {
                if(line.trim().length() == 0 || count == ROW)
                    break;
                
                StringTokenizer st = new StringTokenizer(line, DILIM);
                
                try {
                    if(index == 0) {
                        String ignore = st.nextToken();
                    }
                    
                    while(st.hasMoreTokens() && index < size) {
                        odList.put(new Integer(index), st.nextToken());
                        index = index+8;
                    }
                    count++;
                    index = count;
                } catch (NoSuchElementException ex) {
                    setErrorMessage(ex.getMessage());
                    return false;
                }
            }
        } catch (IOException ex) {
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
        
        String result;
        
        try {
            for(int i = 0; i<odList.size(); i++) {
                Integer k = new Integer(i);
                if(odList.containsKey(k)) {
                    String s = (String)odList.get(k);
                    double odread = Double.parseDouble(s);
                    if(odread < NOGROW) {
                        result = Result.NOGROW;
                    } else {
                        if(odread < GROW) {
                            result = Result.WEAKGROW;
                        } else {
                            result = Result.GROW;
                        }
                    }
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
