/*
 * NoFound.java
 *
 * Created on March 25, 2003, 10:59 AM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  dzuo
 */
public class NoFound {
    protected String searchTerm;
    protected String reason;
    
    public static final String GENOMIC_SEQ = "Genomic Sequence";
    
    /** Creates a new instance of NoFound */
    public NoFound() {
    }
    
    public NoFound(String searchTerm, String reason) {
        this.searchTerm = searchTerm;
        this.reason = reason;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public String getReason() {
        return reason;
    }
}
