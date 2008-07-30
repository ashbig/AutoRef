/*
 * CultureFileParser.java
 *
 * Created on December 5, 2007, 10:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.Fileresult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 * @author dzuo
 */
public class CultureFileParser extends ResultFileParser {
    public static final int IGNORELINES = 3;
    public static final String DILIM = "\t";
    public static final int ROW = 8;
    public static final int COL = 12;
    
    /** Creates a new instance of CultureFileParser */
    public CultureFileParser() {
    }
    
    public List<Fileresult> parseFile(InputStream input) throws NappaIOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        int col = 0;
        int row = 0;
        List<Fileresult> results = new ArrayList<Fileresult>();
        
        try {
            for(int i=0; i<IGNORELINES; i++) {
                in.readLine();
            }
            
            while((line = in.readLine()) != null) {
                if(line.trim().length() == 0 || row == ROW)
                    break;
                
                StringTokenizer st = new StringTokenizer(line, DILIM);
                
                try {
                    if(row == 0) {
                        String ignore = st.nextToken();
                    }
                    
                    while(st.hasMoreTokens() && col < COL) {
                        Fileresult result = new Fileresult(row+1, col+1, st.nextToken());
                        result.setPos(ROW);
                        results.add(result);
                        col ++;
                    }
                    row++;
                    col = 0;
                } catch (NoSuchElementException ex) {
                    throw new NappaIOException(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            throw new NappaIOException(ex.getMessage());
        }
        
        return results;
    }
}
