/*
 * NameInfo.java
 *
 * Created on June 17, 2003, 2:55 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class NameInfo {
    protected String gi;
    protected String genesymbol;
    protected String genbank;
    protected String locusid;
    protected String panumber;
    protected String sgd;
    protected String cloneAcc;
    protected String cloneGi;
    
    /** Creates a new instance of NameInfo */
    public NameInfo() {
    }
    
    public NameInfo(NameInfo n) {
        if(n != null) {
            this.gi = n.getGi();
            this.genesymbol = n.getGenesymbol();
            this.genbank = n.getGenbank();
            this.locusid = n.getLocusid();
            this.panumber = n.getPanumber();
            this.sgd = n.getSgd();
            this.cloneAcc = n.getCloneAcc();
            this.cloneGi = n.getCloneGi();
        }
    }
    
    public NameInfo(String gi, String genesymbol, String genbank, String locusid, String panumber, String sgd) {
        this.gi = gi;
        this.genesymbol = genesymbol;
        this.genbank = genbank;
        this.locusid = locusid;
        this.panumber = panumber;
        this.sgd = sgd;
    }
    
    public String getGi() {return gi;}
    public String getGenesymbol() {return genesymbol;}
    public String getGenbank() {return genbank;}
    public String getLocusid() {return locusid;}
    public String getPanumber() {return panumber;}
    public String getSgd() {return sgd;}
    public String getCloneAcc() {return cloneAcc;}
    public String getCloneGi() {return cloneGi;}
    
    public void setCloneAcc(String s) {this.cloneAcc = s;}
    public void setCloneGi(String s) {this.cloneGi = s;}
}
