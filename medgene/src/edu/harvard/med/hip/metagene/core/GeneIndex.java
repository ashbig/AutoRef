/*
 * GeneIndex.java
 *
 * Created on January 24, 2002, 3:44 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GeneIndex {
    public static final String SYMBOL = "Gene formal symbol";
    public static final String NAME = "Gene formal name";
    public static final String FAMILY = "Gene family term";
    
    private int indexid;
    private String index;
    private String type;
    private String date;

    public GeneIndex(int indexid) {
        this.indexid = indexid;
    }
    
    /** Creates new GeneIndex */
    public GeneIndex(int indexid, String index, String type, String date) {
        this.indexid = indexid;
        this.index = index;
        this.type = type;
        this.date = date;
    }

    public int getIndexid() {
        return indexid;
    }
    
    public String getIndex() {
        return index;
    }
    
    public String getType() {
        return type;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getSearchType() {
        if(GeneIndex.NAME.equals(type) || GeneIndex.SYMBOL.equals(type))
            return "By gene term";
        else
            return "By family term";
    }
}
