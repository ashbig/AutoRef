/*
 * $Id: OligoPattern.java,v 1.2 2001-06-29 11:46:29 wenhong_mar Exp $
 * OligoPattern.java
 *
 * Created on May 29, 2001, 12:40 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 * This class groups three oligoIDs which derived from the same sequence
 * together with its oligo sequence and CDS length. When oligo plates are
 * generated, the oligo samples must be arranged in a saw-tooth pattern. 
 * The set of three oligo samples should be in the same position of each plate.
 * This class facilitates the arrangement of the oligo pattern.
 * @file OligoPattern.java
 * @author  Wendy Mar
 * @date   5/29/01
 * @version
 */
public class OligoPattern {
    private int oligoId_5p;
    private int oligoId_3s;
    private int oligoId_3op;
    private String oligoseq_5p;
    private String oligoseq_3s;
    private String oligoseq_3op;
    private int cdsLength;
    
    /**
     * Constructor.
     * Creates new OligoPattern
     * @param oligoId_5p The 5 prime oligoId
     * @param oligoId_3s The 3 prime stop oligoId
     * @param oligoId_3op The 3 prime open oligoId
     * @param oligoseq_5p The 5p oligo sequence
     * @param oligoseq_3s The 3p stop oligo sequence
     * @param oligoseq_3op The 3p open oligo sequence
     * @param cdsLength The CDS Length of the sequence
     */
    
    public OligoPattern(int oligoId_5p, int oligoId_3s, int oligoId_3op, 
        String oligoseq_5p, String oligoseq_3s, String oligoseq_3op, int cdsLength) {
        this.oligoId_5p = oligoId_5p;
        this.oligoId_3s = oligoId_3s;
        this.oligoId_3op = oligoId_3op;
        this.oligoseq_5p = oligoseq_5p;
        this.oligoseq_3s = oligoseq_3s;
        this.oligoseq_3op = oligoseq_3op;
        this.cdsLength = cdsLength;
    }
    
    public int getOligoId_5p() {
        return oligoId_5p;
    }
    
    public int getOligoId_3s() {
        return oligoId_3s;
    }
    
    public int getOligoId_3op() {
        return oligoId_3op;
    }
    
    public String getOligoseq_5p() {
        return oligoseq_5p;
    }
    
    public String getOligoseq_3s() {
        return oligoseq_3s;
    }
    
    public String getOligoseq_3op() {
        return oligoseq_3op;
    }
    
    public int getCDSLength() {
        return cdsLength;
    }
    
}

