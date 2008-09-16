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
import java.util.StringTokenizer;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class CloneFileParser extends ReagentFileParser {
    private static final String CLONE_SOURCE = "HIP";
    private String idtype;
    
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
    @Override
    public void parseFile(InputStream input) throws CloneFileParserException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            int num = 0;
            
            while((line = in.readLine()) != null && line.trim().length()>0) {
                StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                String srcCloneid = tokenizer.nextToken();
                if(srcCloneid==null||srcCloneid.trim().length()==0||srcCloneid.equals(getNA())) {
                    throw new CloneFileParserException("Invalid clone ID: "+srcCloneid);
                }
                String geneid = tokenizer.nextToken();
                if(getNA().equals(geneid))
                    geneid = null;
                String symbol = tokenizer.nextToken();
                if(getNA().equals(symbol))
                    symbol = null;
                String genbank = tokenizer.nextToken();
                if(getNA().equals(genbank))
                    genbank = null;
                String gi = tokenizer.nextToken();
                if(getNA().equals(gi))
                    gi = null;
                
                String reagentname = srcCloneid;
                if(Constants.ID_GENENAME.equals(idtype)) {
                    if(symbol==null)
                        new CloneFileParserException("Gene name cannot be null.");
                    reagentname = symbol;
                }
                if(Constants.ID_GENEID.equals(idtype)) {
                    if(geneid==null)
                        new CloneFileParserException("Gene ID cannot be null.");
                    reagentname = geneid;
                }
                if(Constants.ID_GENBANK.equals(idtype)) {
                    if(genbank==null)
                        new CloneFileParserException("GenBank Accession cannot be null.");
                    reagentname = genbank;
                }
                if(Constants.ID_GI.equals(idtype)) {
                    if(gi==null)
                        new CloneFileParserException("GI cannot be null.");
                    reagentname = gi;
                }
                
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
                clone.setName(reagentname);
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

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }
}
