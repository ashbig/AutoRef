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
    int searchResultId;
    
    public static final String GENOMIC_SEQ = "Genomic sequence";
    public static final String GI_NOT_IN_FLEX = "GI not in Flex database";
    public static final String GI_NOT_IN_NCBI = "GI not found in NCBI";
    public static final String ACCESSION_NOT_IN_FLEX = "Genbank accession not in Flex database";
    public static final String ACCESSION_NOT_IN_NCBI = "Genbank accession not found in NCBI";
    public static final String NO_MATCH_BLAST = "No match found by blast";
    public static final String NO_MATCH_GENBANK_ENTRY = "No match genbank entry found";
    public static final String UNKNOWN = "Unknown";
    public static final String INVALID_GI = "Invalid GI";
    public static final String INVALID_BLAST = "Cannot parse blast output";
    public static final String NO_MATCH_GI_QUERY = "No match found by GI or blast";
    
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
    
    public void setSearchResultId(int id) {
        this.searchResultId = id;
    }
    
    public int getSearchResultId() {
        return searchResultId;
    }
}
