/*
 * SearchTerm.java
 *
 * Created on February 10, 2004, 10:58 AM
 */

package edu.harvard.med.hip.flex.query.bean;

import edu.harvard.med.hip.flex.query.core.SearchRecord;

/**
 *
 * @author  DZuo
 */
public class SearchTerm {
    public static final String GI = SearchRecord.GI;
    public static final String ACCESSION = SearchRecord.GENBANK;
    public static final String SYMBOL = SearchRecord.GENESYMBOL;
    public static final String LOCUS = SearchRecord.LOCUSID;
    
    private String searchName;
    private String searchValue;
    
    /** Creates a new instance of SearchTerm */
    public SearchTerm() {
    }
    
    public SearchTerm(String searchName, String searchValue) {
        this.searchName = searchName;
        this.searchValue = searchValue;
    }
    
    public void setSearchName(String s) {this.searchName = s;}
    public void setSearchValue(String s) {this.searchValue = s;}
    public String getSearchName() {return searchName;}
    public String getSearchValue() {return searchValue;}
}
