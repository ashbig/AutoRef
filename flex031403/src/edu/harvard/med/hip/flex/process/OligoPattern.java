/*
 * $Id: OligoPattern.java,v 1.1 2001-06-04 14:58:13 wenhong_mar Exp $
 * OligoPattern.java
 *
 * Created on May 29, 2001, 12:40 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 * This class groups three oligoID which derived from the same sequence
 * together with the sequence ID and CDS length. When oligo plates are
 * generated, the oligos must be arranged in a saw-tooth pattern.  This
 * class facilitate the arrangement of the oligo pattern.
 * @file OligoPattern.java
 * @author  Wendy Mar
 * @date   5/29/01
 * @version
 */
public class OligoPattern {
    private int oligoId_5p;
    private int oligoId_3s;
    private int oligoId_3op;
    private int seqId;
    private int cdsLength;
    
    /**
     * Constructor.
     * Creates new OligoPattern
     * @param oligoId_5p The 5 prime oligoId
     * @param oligoId_3s The 3 prime stop oligoId
     * @param oligoId_3op The 3 prime open oligoId
     * @param seqId The sequencID where the oligos are derived from.
     * @param cdsLength The CDS Length of the sequence
     */
    
    public OligoPattern(int oligoId_5p, int oligoId_3s, int oligoId_3op, int seqId, int cdsLength) {
        this.oligoId_5p = oligoId_5p;
        this.oligoId_3s = oligoId_3s;
        this.oligoId_3op = oligoId_3op;
        this.seqId = seqId;
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
    
    public int getSeqId() {
        return seqId;
    }
    
    public int getCDSLength() {
        return cdsLength;
    }
    
}

