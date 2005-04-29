/*
 * Gene.java
 *
 * Created on April 26, 2005, 3:05 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class Gene {    
    private String locusid;
    private String isconfirmed;
    private String organism;
    private String status;
    private String genename;
    private String unigeneid;
    private String officialgenesymbol;
    
    private List genesymbol;
    private List genbank;
    
    /** Creates a new instance of Gene */
    public Gene() {
    }
    
    public Gene(String locusid) {
        this.locusid = locusid;
    }

    public Gene(String locusid, String isConfirmed, String organism, String status, 
    String genename, String unigene, String officialgenesymbol) {
        this.locusid = locusid;
        this.isconfirmed = isConfirmed;
        this.organism = organism;
        this.status = status;
        this.genename = genename;
        this.unigeneid = unigene;
        this.officialgenesymbol = officialgenesymbol;
    }
    
    public String getLocusid() {return locusid;}
    public String getIsconfirmed() {return isconfirmed;}
    public String getOrganism() {return organism;}
    public String getStatus() {return status;}
    public String getGenename() {return genename;}
    public String getUnigeneid() {return unigeneid;}
    public String getOfficialgenesymbol() {return officialgenesymbol;}
    public List getGenesymbol() {return genesymbol;}
    public List getGenbank() {return genbank;}
    
    public void setLocusid(String locusid) {this.locusid=locusid;}
    public void setIsconfirmed(String isconfirmed) {this.isconfirmed = isconfirmed;}
    public void setOrganism(String organism) {this.organism = organism;}
    public void setStatus(String status) {this.status = status;}
    public void setGenename(String genename) {this.genename = genename;}
    public void setUnigeneid(String unigeneid) {this.unigeneid = unigeneid;}
    public void setOfficialgenesymbol(String s) {this.officialgenesymbol = s;}
    public void setGenesymbol(List genesymbol) {this.genesymbol = genesymbol;}
    public void setGenbank(List genbank) {this.genbank = genbank;}
}