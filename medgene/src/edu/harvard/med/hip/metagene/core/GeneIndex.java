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
    private int indexid;
    private String index;
    private String type;
    private String date;
    private double statScore;
    
    /** Creates new GeneIndex */
    public GeneIndex(int indexid, String index, String type, String date, double statScore) {
        this.indexid = indexid;
        this.index = index;
        this.type = type;
        this.date = date;
        this.statScore = statScore;
    }

}
