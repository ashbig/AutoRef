/*
 * DNAFileParser.java
 *
 * Created on December 5, 2007, 10:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.Fileresult;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author dzuo
 */
public class DNAFileParser extends ResultFileParser {
    public static final String DILIM = "\t";
    public static final int ROW = 8;
    public static final int COL = 12;
    public static final String STARTSTRING = "DNA Concentrations";
    
    /** Creates a new instance of DNAFileParser */
    public DNAFileParser() {
    }
    
    public List<Fileresult> parseFile(InputStream input) throws NappaIOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        boolean start = false;
        int col = 0;
        int row = 0;
        List<Fileresult> results = new ArrayList<Fileresult>();
        
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
                        
                        while(st.hasMoreTokens() && col < COL) {
                            Fileresult result = new Fileresult(row+1, col+1, st.nextToken());
                            result.setPos(ROW);
                            results.add(result);
                            col++;
                        }
                        row++;
                        col=0;
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            throw new NappaIOException(ex.getMessage());
        }
        
        return results;
    }
}
