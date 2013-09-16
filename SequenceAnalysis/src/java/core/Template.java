/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class Template implements Serializable {

    private int cloneid;
    private String originalcloneid;
    private String vector;
    private String source;
    private String format;
    private int geneid;
    private String sequence;
    private int isorfeome;
    private int isgateway;
    private String plate;
    private int position;
    private String posx;
    private int posy;

    public Template() {
    }

    public Template(int cloneid, String originalcloneid, String vector, String source, String format,
            int geneid, String sequence, int isorfeome) {
        this.cloneid = cloneid;
        this.originalcloneid = originalcloneid;
        this.vector = vector;
        this.source = source;
        this.format = format;
        this.geneid = geneid;
        this.sequence = sequence;
        this.isorfeome = isorfeome;
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
     * @return the originalcloneid
     */
    public String getOriginalcloneid() {
        return originalcloneid;
    }

    /**
     * @param originalcloneid the originalcloneid to set
     */
    public void setOriginalcloneid(String originalcloneid) {
        this.originalcloneid = originalcloneid;
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
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        if (format == null) {
            return "";
        }
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the geneid
     */
    public int getGeneid() {
        return geneid;
    }

    /**
     * @param geneid the geneid to set
     */
    public void setGeneid(int geneid) {
        this.geneid = geneid;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        if (sequence != null) {
            return sequence.toUpperCase();
        }
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the isorfeome
     */
    public int getIsorfeome() {
        return isorfeome;
    }

    /**
     * @param isorfeome the isorfeome to set
     */
    public void setIsorfeome(int isorfeome) {
        this.isorfeome = isorfeome;
    }

    public String getOrfeomeString() {
        if (isorfeome == 1) {
            return "Yes";
        }
        return "No";
    }

    public String getSequenceFasta() {
        try {
            return StringConvertor.convertToFasta(getSequence(), 0);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * @return the isgateway
     */
    public int getIsgateway() {
        return isgateway;
    }

    /**
     * @param isgateway the isgateway to set
     */
    public void setIsgateway(int isgateway) {
        this.isgateway = isgateway;
    }

    public String getGatewayString() {
        if (isgateway == 1) {
            return "Yes";
        }
        return "No";
    }

    /**
     * @return the plate
     */
    public String getPlate() {
        if (plate == null) {
            return "NA";
        }
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the posx
     */
    public String getPosx() {
        return posx;
    }

    /**
     * @param posx the posx to set
     */
    public void setPosx(String posx) {
        this.posx = posx;
    }

    /**
     * @return the posy
     */
    public int getPosy() {
        return posy;
    }

    /**
     * @param posy the posy to set
     */
    public void setPosy(int posy) {
        this.posy = posy;
    }

    public String getWell() {
        if (posx == null) {
            return "NA";
        }
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(2);
        fmt.setMinimumIntegerDigits(2);
        fmt.setGroupingUsed(false);
        return getPosx() + fmt.format(getPosy());
    }

    public String getLocation() {
        if (plate == null) {
            return "NA";
        }
        return plate + ":" + getWell();
    }
}
