/*
 * CloneInfo.java
 *
 * Created on July 17, 2007, 2:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import transfer.ReagentTO;

/**
 *
 * @author dzuo
 */
public class CloneInfo extends ReagentInfo {
    private String srccloneid;
    private String source;
    private String genbank;
    private String gi;
    private String geneid;
    private String symbol;
    private String vectorname;
    private String species;
    private String growth;
    
    /** Creates a new instance of CloneInfo */
    public CloneInfo() {
    }
    
    public CloneInfo(String srcid, String src, String genbank, String gi, String geneid, String symbol, String vector,
            String growth, String species, String plate, String well) {
        super(symbol,ReagentTO.getTYPE_CLONE(),null,plate,well);
        
        this.setSrccloneid(srcid);
        this.setSource(src);
        this.setGenbank(genbank);
        this.setGi(gi);
        this.setGeneid(geneid);
        this.setSymbol(symbol);
        this.setVectorname(vector);
        this.setSpecies(species);
        this.setGrowth(growth);
    }

    public String getSrccloneid() {
        return srccloneid;
    }

    public void setSrccloneid(String srccloneid) {
        this.srccloneid = srccloneid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGenbank() {
        return genbank;
    }

    public void setGenbank(String genbank) {
        this.genbank = genbank;
    }

    public String getGi() {
        return gi;
    }

    public void setGi(String gi) {
        this.gi = gi;
    }

    public String getGeneid() {
        return geneid;
    }

    public void setGeneid(String geneid) {
        this.geneid = geneid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getVectorname() {
        return vectorname;
    }

    public void setVectorname(String vectorname) {
        this.vectorname = vectorname;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }
}
