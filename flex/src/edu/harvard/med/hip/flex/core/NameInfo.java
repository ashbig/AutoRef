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
    
    /** Creates a new instance of NameInfo */
    public NameInfo() {
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
}
