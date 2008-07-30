/*
 * Plate384ToSlideLogFileParser.java
 *
 * Created on April 30, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

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
public class Plate384ToSlideLogFileParser {
    private String startdate;
    private String programname;
    private List<String> plates;
    
    /** Creates a new instance of Plate384ToSlideLogFileParser */
    public Plate384ToSlideLogFileParser() {
        setPlates(new ArrayList<String>());
    }
    
    public void parseLogfile(InputStream input) throws ProgramMappingFileParserException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            boolean isstart = false;
            
            while((line = in.readLine()) != null) {
                if(line.trim().length()==0)
                    continue;
                
                if(line.indexOf("MICROARRAYING RUN - Started:") == 0) {
                    String startdatetime = line.substring(line.indexOf(":")+2);
                    setStartdate(startdatetime.substring(0, startdatetime.indexOf(" ")).trim());
                    continue;
                }
                if(line.indexOf("Routine Name:") == 0) {
                    setProgramname(line.substring(line.indexOf(":")+1).trim());
                    continue;
                }
                //if(line.indexOf("Number:\tIndex:") == 0) {
                if(line.indexOf("Number: Index:  Number: Index:  Number:") == 0) {
                    isstart = true;
                    continue;
                }
                if(line.indexOf("MICROARRAYING RUN - Ended:") == 0) {
                    break;
                }
                if(isstart) {
                    String label = getlabel(line);
                    if(label == null)
                        throw new ProgramMappingFileParserException("Invalid plate label found.");
                    
                    addToPlate(label);
                }
            }
            
            in.close();
        } catch (Exception ex) {
            throw new ProgramMappingFileParserException(ex.getMessage());
        }
    }
    
    public void addToPlate(String label) {
        for(String plate:plates) {
            if(plate.equals(label)) {
                return;
            }
        }
        plates.add(label);
    }

    private String getlabel(String line) throws ProgramMappingFileParserException {
        if(line == null)
            throw new ProgramMappingFileParserException("Input line is null");
        
        StringTokenizer token = new StringTokenizer(line);
        String label = null;
        while(token.hasMoreTokens()) {
            label = token.nextToken();
        }
        return label;
    }
    
    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public List<String> getPlates() {
        return plates;
    }

    public void setPlates(List<String> plates) {
        this.plates = plates;
    }
}
