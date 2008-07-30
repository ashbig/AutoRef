/*
 * Plate96To384ProgramMappingFileParser.java
 *
 * Created on February 27, 2007, 2:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import transfer.ProgrammappingTO;
import core.Well;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import transfer.ProgramcontainerTO;

/**
 *
 * @author DZuo
 */
public class Plate96To384ProgramMappingFileParser extends AbstractProgramMappingFileParser {
    public static final String DELIM = "\t";
    public static final int SRCROWNUM = 8;
    public static final int DESTROWNUM = 16;
    public static final int SRCCOLNUM = 12;
    public static final int DESTCOLNUM = 24;
    
    /**
     * Creates a new instance of Plate96To384ProgramMappingFileParser
     */
    public Plate96To384ProgramMappingFileParser() {
        super();
    }
    
    public String getSrccontainertype() {
        return ProgramcontainerTO.PLATE96;
    }
    
    public String getDestcontainertype() {
        return ProgramcontainerTO.PLATE384;
    }
    
    public List<ProgrammappingTO> parseMappingFile(InputStream input, boolean isNumber) throws ProgramMappingFileParserException {
        List<ProgrammappingTO> mappings = new ArrayList<ProgrammappingTO>();
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            while((line = in.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, DELIM);
                String srcContainer = tokenizer.nextToken();
                String srcWell = tokenizer.nextToken();
                String destContainer = tokenizer.nextToken();
                String destWell = tokenizer.nextToken();
                
                if(!isNumber) {
                    srcWell = ""+Well.convertWellToVPos(srcWell, SRCROWNUM);
                    destWell = ""+Well.convertWellToVPos(destWell, DESTROWNUM);
                }
                
                int srcPos = Integer.parseInt(srcWell);
                int destPos = Integer.parseInt(destWell);
                Well sWell = Well.convertVPosToWell(srcPos, SRCROWNUM);
                Well dWell = Well.convertVPosToWell(destPos, DESTROWNUM);
                
                ProgrammappingTO p = new ProgrammappingTO();
                p.setSrcplate(srcContainer);
                p.setSrcpos(srcPos);
                p.setSrcwellx(sWell.getX());
                p.setSrcwelly(sWell.getY());
                p.setDestplate(destContainer);
                p.setDestpos(destPos);
                p.setDestwellx(dWell.getX());
                p.setDestwelly(dWell.getY());
                mappings.add(p);
                addToContainers(srcContainers, srcContainer);
                addToContainers(destContainers, destContainer);
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProgramMappingFileParserException(ex.getMessage());
        } 
        
        return mappings;
    }
}
