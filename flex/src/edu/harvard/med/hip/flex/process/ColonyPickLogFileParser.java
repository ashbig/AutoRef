/*
 * ColonyPickLogFileParser.java
 *
 * This class is used to parse the colony picking log file to count
 * the colony number in each well on a plate. It stores the colony
 * picking information into an array of object.
 *
 * Created on March 22, 2002, 11:20 AM
 */

package edu.harvard.med.hip.flex.process;

import java.io.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ColonyPickLogFileParser {
    public static final String DILIM = " ";
    public static final int TOTALCOUNT = 96;
    public static final String NOBARCODE = "N/A";
    
    private InputStream input;
    private String errorMessage;
    private Hashtable colonyInfo;
    
    /** Creates new ColonyPickLogFileParser
     * @param input The InputStream to be parsed.
     * @return A ColonyPickLogFileParser object.
     */
    public ColonyPickLogFileParser(InputStream input) {
        this.input = input;
        colonyInfo = new Hashtable();
    }
    
    /**
     * Set the errorMessage to be the given value.
     * @param errorMessage The value to be set to.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Return the errorMessage.
     * @return The errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Set the colony information.
     *
     * @param colonyInfo The value to be set to.
     */
    public void setColonyInfo(Hashtable colonyInfo) {
        this.colonyInfo = colonyInfo;
    }
    
    /**
     * Return the colony information as a Hashtable.
     *
     * @return The colony information as a Hashtable.
     */
    public Hashtable getColonyInfo() {
        return colonyInfo;
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
        boolean end = false;
        int totalCount = 0;
        
        try {
            while((line = in.readLine()) != null) {
                if(start && !end) {
                    totalCount++;
                    
                    if(totalCount == TOTALCOUNT)
                        end = true;
                    
                    StringTokenizer st = new StringTokenizer(line, DILIM);
                    int well = -1;
                    int found = -1;
                    int pick = -1;
                    String barcode = null;
                    int number = 0;
                    
                    try {
                        while(st.hasMoreTokens()) {
                            number++;
                            String ignore = st.nextToken();
                            
                            if(number == 1) {
                                well = Integer.parseInt(ignore);
                            }
                            
                            if(number == 5) {
                                found = Integer.parseInt(ignore);
                            }
                            
                            if(number == 6) {
                                pick = Integer.parseInt(ignore);
                            }
                            
                            if(number == 8) {
                            //if(number == 7) {
                                barcode = ignore;
                            }
                        }
                    } catch (NoSuchElementException ex) {
                        setErrorMessage(ex.getMessage());
                        return false;
                    }
                    
                    // check the information to make sure they are all correct.
                    if(well == -1 || found == -1 || pick == -1) {
                        setErrorMessage("Wrong data format with line: "+line);
                        return false;
                    }
                    
                    if(barcode == null) {
                        setErrorMessage("No barcode found.");
                        return false;
                    }
                    
                    if(barcode.equals("none")) {
                        setErrorMessage("A barcode has to be entered");
                        return false;
                    }
                    
                    if(barcode.equals(NOBARCODE)) {
                        continue;
                    }
                    
                    ColonyCountInfo info = new ColonyCountInfo(well, found, pick);
                    Vector plateInfo = (Vector)colonyInfo.get(barcode);
                    
                    if(plateInfo == null) {
                        Vector newInfo = new Vector();
                        newInfo.addElement(info);
                        colonyInfo.put(barcode, newInfo);
                    } else {
                        plateInfo.addElement(info);
                    }
                } else if(end) {
                    break;
                } else {
                    try {
                        if(line.length()>6 && "Region".equals(line.substring(0, 6))) {
                            line = in.readLine();
                            start = true;
                            continue;
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        setErrorMessage("Data not found");
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static void main(String [] args) {
        String file = "E:\\flexDev\\colonyLogFile.txt";
        ColonyPickLogFileParser parser = null;
        
        try {
            InputStream input = new FileInputStream(file);
            parser = new ColonyPickLogFileParser(input);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        
        if(parser.parseFile()) {
            Hashtable info = parser.getColonyInfo();
            Enumeration enum = info.keys();
            while(enum.hasMoreElements()) {
                String barcode = (String)enum.nextElement();
                Vector plateInfo = (Vector)info.get(barcode);
                System.out.println("Plate: "+barcode);
                
                int i;
                for(i=0; i<plateInfo.size(); i++) {
                    ColonyCountInfo count = (ColonyCountInfo)(plateInfo.elementAt(i));
                    System.out.println("Well: "+count.getWell()+"\t"+"Found: "+count.getFound()+"\t"+"Pick: "+count.getPick());
                }
                System.out.println();
            }
        } else {
            System.out.println("Parsing failed:\n"+parser.getErrorMessage());
        }
    }
}
