/*
 * GeneRecord.java
 *
 * Created on July 18, 2003, 11:18 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;

/**
 *
 * @author  dzuo
 */
public class GeneRecord {
    private int locusid;
    private String isconfirmed;
    private String organism;
    private String status;
    private String genename;
    private String unigeneid;
    
    private List genesymbol;
    private List genbank;
    
    /** Creates a new instance of GeneRecord */
    public GeneRecord() {
    }
    
    public GeneRecord(int locusid) {
        this.locusid = locusid;
    }

    public int getLocusid() {return locusid;}
    public String getIsconfirmed() {return isconfirmed;}
    public String getOrganism() {return organism;}
    public String getStatus() {return status;}
    public String getGenename() {return genename;}
    public String getUnigeneid() {return unigeneid;}
    public List getGenesymbol() {return genesymbol;}
    public List getGenbank() {return genbank;}
    
    public void setLocusid(int locusid) {this.locusid=locusid;}
    public void setIsconfirmed(String isconfirmed) {this.isconfirmed = isconfirmed;}
    public void setOrganism(String organism) {this.organism = organism;}
    public void setStatus(String status) {this.status = status;}
    public void setGenename(String genename) {this.genename = genename;}
    public void setUnigeneid(String unigeneid) {this.unigeneid = unigeneid;}
    public void setGenesymbol(List genesymbol) {this.genesymbol = genesymbol;}
    public void setGenbank(List genbank) {this.genbank = genbank;}
}