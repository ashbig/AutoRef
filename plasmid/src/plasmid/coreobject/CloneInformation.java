/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

/**
 *
 * @author Lab User
 */
public class CloneInformation {
    private int cloneid;
    private int gi;
    private double percentid;
    private int alength;
    private double coverage;
    private int mutation;
    private String clonename;
    private String restriction;
    private String vector;
    private String format;
    private int size;
    private int vectorid;
    private boolean inCart;
    private String type;

    public CloneInformation() {
        this.inCart = false;
    }
    
    public boolean isShowAlignment() {
        if(CloneAnalysis.TYPE_NOSEQ.equals(type)) {
            return false;
        }
        return true;
    }
    /**
     * @return the cloneid
     */
    public int getCloneid() {
        return cloneid;
    }

    /**
     * @param cloneid the cloneid to set
     */
    public void setCloneid(int cloneid) {
        this.cloneid = cloneid;
    }

    /**
     * @return the gi
     */
    public int getGi() {
        return gi;
    }

    /**
     * @param gi the gi to set
     */
    public void setGi(int gi) {
        this.gi = gi;
    }

    /**
     * @return the percentid
     */
    public double getPercentid() {
        return percentid;
    }

    /**
     * @param percentid the percentid to set
     */
    public void setPercentid(double percentid) {
        this.percentid = percentid;
    }

    /**
     * @return the alength
     */
    public int getAlength() {
        return alength;
    }

    /**
     * @param alength the alength to set
     */
    public void setAlength(int alength) {
        this.alength = alength;
    }

    /**
     * @return the coverage
     */
    public double getCoverage() {
        return coverage;
    }

    /**
     * @param coverage the coverage to set
     */
    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    /**
     * @return the mutation
     */
    public int getMutation() {
        return mutation;
    }

    /**
     * @param mutation the mutation to set
     */
    public void setMutation(int mutation) {
        this.mutation = mutation;
    }

    /**
     * @return the clonename
     */
    public String getClonename() {
        return clonename;
    }

    /**
     * @param clonename the clonename to set
     */
    public void setClonename(String clonename) {
        this.clonename = clonename;
    }

    /**
     * @return the restriction
     */
    public String getRestriction() {
        return restriction;
    }

    /**
     * @param restriction the restriction to set
     */
    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    /**
     * @return the vector
     */
    public String getVector() {
        return vector;
    }

    /**
     * @param vector the vector to set
     */
    public void setVector(String vector) {
        this.vector = vector;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the vectorid
     */
    public int getVectorid() {
        return vectorid;
    }

    /**
     * @param vectorid the vectorid to set
     */
    public void setVectorid(int vectorid) {
        this.vectorid = vectorid;
    }

    /**
     * @return the inCart
     */
    public boolean isInCart() {
        return inCart;
    }

    /**
     * @param inCart the inCart to set
     */
    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
