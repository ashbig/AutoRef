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
    private String locusid;
    private String type;
    private String value;
    private String url;
    private String date;
    
    /** Creates new Geneinfo */
    public Geneinfo(String locusid, String type, String value, String url, String date){
        this.locusid = locusid;
        this.type = type;
        this.value = value;
        this.url = url;
        this.date = date;
    }
}
