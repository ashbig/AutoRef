/*
 * BlastResults.java
 *
 * Created on June 5, 2001, 5:35 PM
 *
 * This class stores the blast results generated from the FlexSeqAnalyzer class.
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  dzuo
 * @version 
 */
public class BlastResults {
   private String evalue = null;
   private String identity = null;
   private int cdslength = 0;
   private double percentIdentity;
   private double percentAlignment;
   
    /** Creates new BlastResults */
    public BlastResults() {}

    /**
     * Set the evalue to the given value.
     *
     * @param evalue The value to be set to.
     */
    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }
    
    /**
     * Returns the evalue field.
     *
     * @return The evalue field.
     */
    public String getEvalue() {
        return evalue;
    }
    
    /**
     * Set the identity to the given value.
     *
     * @param identity The value to be set to.
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    
    /**
     * Returns the identity field.
     *
     * @return The identity field.
     */
    public String getIdentity() {
        return identity;
    }
    
    /**
     * Set the cdslength to the given value.
     *
     * @param cdslength The value to be set to.
     */
    public void setCdslength(int cdslength) {
        this.cdslength = cdslength;
    }
    
    /**
     * Return the cdslength field.
     *
     * @return The cdslength field.
     */
    public int getCdslength() {
        return cdslength;
    }  
    
    /**
     * Set the percentIdentity to the given value.
     *
     * @param percentIdentity The value to be set to.
     */
    public void setPercentIdentity(double percentIdentity) {
        this.percentIdentity = percentIdentity;
    }
    
    /**
     * Get the percentIdentity.
     *
     * @return The percentIdentity value.
     */
    public double getPercentIdentity() {
        return percentIdentity;
    }
    
    /**
     * Set the setPercentAlignment to be the given value.
     *
     * @param percentAlignment The value to be set to.
     */
    public void setPercentAlignment(double percentAlignment) {
        this.percentAlignment = percentAlignment;
    }
    
    /**
     * Return the percentAlignment value.
     *
     * @return The percentAlignment value.
     */
    public double getPercentAlignment() {
        return percentAlignment;
    }
}
