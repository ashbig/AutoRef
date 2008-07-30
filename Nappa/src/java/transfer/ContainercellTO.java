/*
 * ContainercellTO.java
 *
 * Created on March 22, 2007, 10:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ContainercellTO implements Serializable {
    private static final String TYPE_GENE = "GENE";
    private static final String TYPE_CONTROL = "CONTROL";
    public static final String TYPE_EMPTY = "EMPTY";
    public static final String TYPE_REAGENT = "REAGENT";
    
    private int pos;
    private String posx;
    private String posy;
    private String type;
    private int containerid;
    private int sampleid;
    private String containerlabel;
    
    private String controlreagent;
    private String templatename;
    
    /**
     * Creates a new instance of ContainercellTO
     */
    public ContainercellTO() {
    }

    public ContainercellTO(int pos, String x, String y, String type, int containerid, int sampleid) {
        this.setPos(pos);
        this.setPosx(x);
        this.setPosy(y);
        this.setType(type);
        this.setContainerid(containerid);
        this.setSampleid(sampleid);
    }

    public ContainercellTO(int pos, String x, String y, String type) {
        this.setPos(pos);
        this.setPosx(x);
        this.setPosy(y);
        this.setType(type);
    }
    
    public int getPos() {
        return pos;
    }

    public void setPos(int p) {
        this.pos = p;
    }

    public String getPosx() {
        return posx;
    }

    public void setPosx(String posx) {
        this.posx = posx;
    }

    public String getPosy() {
        return posy;
    }

    public void setPosy(String posy) {
        this.posy = posy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getContainerid() {
        return containerid;
    }

    public void setContainerid(int containerid) {
        this.containerid = containerid;
    }

    public static String getTYPE_GENE() {
        return TYPE_GENE;
    }

    public static String getTYPE_CONTROL() {
        return TYPE_CONTROL;
    }
    
    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }

    public String getContainerlabel() {
        return containerlabel;
    }

    public void setContainerlabel(String containerlabel) {
        this.containerlabel = containerlabel;
    }

    public boolean isControl() {
        if(getTYPE_CONTROL().equals(type))
            return true;
        return false;
    }

    public String getControlreagent() {
        return controlreagent;
    }

    public void setControlreagent(String control) {
        this.controlreagent = control;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }
}
