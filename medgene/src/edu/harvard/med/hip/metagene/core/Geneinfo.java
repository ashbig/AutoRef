/*
 * Geneinfo.java
 *
 * Created on January 18, 2002, 3:25 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Geneinfo {
    public static final String GO = "GO";
    public static final String REFSEQ_NM = "REFSEQ_NM";
    public static final String PROTEOME = "EXTANNOT";
    
    private String type;
    private String value;
    private String extraInfo;
    private int refSeq_NM_order;
    
    /** Creates new Geneinfo */
    public Geneinfo(String type, String value, String extraInfo, int order){
        this.type = type;
        this.value = value;
        this.extraInfo = extraInfo;
        this.refSeq_NM_order = order;
    }
    
    public String getType() {
        return type;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getExtraInfo() {
        return extraInfo;
    }
    
    public int getRefSeq_NM_order(){
        return refSeq_NM_order;
    }
}
