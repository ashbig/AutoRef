/*
 * CloneFileParser.java
 *
 * Created on July 16, 2007, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.CloneInfo;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 *
 * @author dzuo
 */
public class CloneFileParser extends ReagentFileParser {
    private static final String CLONE_SOURCE = "HIP";
    
    /**
     * Creates a new instance of CloneFileParser
     */
    public CloneFileParser() {
        super();
    }
    
   /* The clone file is a tab-dilimited file with the following fields in order 
    * (NA means the field can be optional and NA will be used):
    * source clone id, gene id, symbol (NA), genbank (NA), GI (NA), vector, growth, species,
    * plate, well. The first line is the header information.
    */
    public void parseFile(InputStream input) throws CloneFileParserException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            int num = 0;
            
            while((line = in.readLine()) != null && line.trim().length()>0) {
                StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                String srcCloneid = tokenizer.nextToken();
                String geneid = tokenizer.nextToken();
                String symbol = tokenizer.nextToken();
                if(getNA().equals(symbol))
                    symbol = null;
                String genbank = tokenizer.nextToken();
                if(getNA().equals(genbank))
                    genbank = null;
                String gi = tokenizer.nextToken();
                if(getNA().equals(gi))
                    gi = null;
                String vector = tokenizer.nextToken();
                //String markers = tokenizer.nextToken();
                //String tag = tokenizer.nextToken();
                //if(NA.equals(tag))
                //  tag = null;
                String growth = tokenizer.nextToken();
                String species = tokenizer.nextToken();
                String plate = tokenizer.nextToken();
                String well = tokenizer.nextToken();
                String source = getCLONE_SOURCE();
                
                CloneInfo clone = new CloneInfo(srcCloneid,source,genbank,gi,geneid,symbol,vector,growth,species,plate,well);
                getReagents().add(clone);
                getLabels().add(plate);
                num++;
            }
            
            in.close();
        } catch (Exception ex) {
            throw new CloneFileParserException(ex.getMessage());
        }
    }

    public static String getCLONE_SOURCE() {
        return CLONE_SOURCE;
    }
}
