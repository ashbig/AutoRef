/*
 * Clone.java
 *
 * Created on July 16, 2007, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

/**
 * @author dzuo
 */

public class CloneTO extends ReagentTO {
    private String srccloneid;
    private String source;
    private String genbank;
    private String gi;
    private String geneid;
    private String symbol;

    private GrowthconditionTO growthname;
    private VectorTO vectorname;
    
    /** Creates a new instance of Clone */
    public CloneTO() {
    }

    public CloneTO(String srcid, String src, String genbank, String gi, String geneid, String symbol, GrowthconditionTO growth, VectorTO vector) {
        super(symbol, ReagentTO.getTYPE_CLONE(), null);
        this.setSrccloneid(srcid);
        this.setSource(src);
        this.setGenbank(genbank);
        this.setGi(gi);
        this.setGeneid(geneid);
        this.setSymbol(symbol);
        this.setGrowthname(growth);
        this.setVectorname(vector);
    }

    public CloneTO(int cloneid, String vector, String growth, String srccloneid, String src, String genbank, String gi, String geneid, String symbol) {
        super(cloneid,symbol, ReagentTO.getTYPE_CLONE(), null);
        this.setSrccloneid(srccloneid);
        this.setSource(src);
        this.setGenbank(genbank);
        this.setGi(gi);
        this.setGeneid(geneid);
        this.setSymbol(symbol);
        this.setGrowthname(new GrowthconditionTO(growth));
        this.setVectorname(new VectorTO(vector));
    }
    
    /**
     * Gets the srccloneid of this Clone.
     * @return the srccloneid
     */
    public String getSrccloneid() {
        return this.srccloneid;
    }

    /**
     * Sets the srccloneid of this Clone to the specified value.
     * @param srccloneid the new srccloneid
     */
    public void setSrccloneid(String srccloneid) {
        this.srccloneid = srccloneid;
    }

    /**
     * Gets the source of this Clone.
     * @return the source
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Sets the source of this Clone to the specified value.
     * @param source the new source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the genbank of this Clone.
     * @return the genbank
     */
    public String getGenbank() {
        return this.genbank;
    }

    /**
     * Sets the genbank of this Clone to the specified value.
     * @param genbank the new genbank
     */
    public void setGenbank(String genbank) {
        this.genbank = genbank;
    }

    /**
     * Gets the gi of this Clone.
     * @return the gi
     */
    public String getGi() {
        return this.gi;
    }

    /**
     * Sets the gi of this Clone to the specified value.
     * @param gi the new gi
     */
    public void setGi(String gi) {
        this.gi = gi;
    }

    /**
     * Gets the geneid of this Clone.
     * @return the geneid
     */
    public String getGeneid() {
        return this.geneid;
    }

    /**
     * Sets the geneid of this Clone to the specified value.
     * @param geneid the new geneid
     */
    public void setGeneid(String geneid) {
        this.geneid = geneid;
    }

    /**
     * Gets the symbol of this Clone.
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the symbol of this Clone to the specified value.
     * @param symbol the new symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the growthname of this Clone.
     * @return the growthname
     */
    public GrowthconditionTO getGrowthname() {
        return this.growthname;
    }

    /**
     * Sets the growthname of this Clone to the specified value.
     * @param growthname the new growthname
     */
    public void setGrowthname(GrowthconditionTO growthname) {
        this.growthname = growthname;
    }

    /**
     * Gets the vectorname of this Clone.
     * @return the vectorname
     */
    public VectorTO getVectorname() {
        return this.vectorname;
    }

    /**
     * Sets the vectorname of this Clone to the specified value.
     * @param vectorname the new vectorname
     */
    public void setVectorname(VectorTO vectorname) {
        this.vectorname = vectorname;
    }

}
