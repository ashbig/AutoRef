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
    
    private String type;
    private String value;
    private String extraInfo;
    
    /** Creates new Geneinfo */
    public Geneinfo(String type, String value, String extraInfo){
        this.type = type;
        this.value = value;
        this.extraInfo = extraInfo;
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
}
