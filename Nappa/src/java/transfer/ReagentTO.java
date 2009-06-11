/*
 * ReagentTO.java
 *
 * Created on February 20, 2007, 9:53 PM
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
public class ReagentTO extends ProcessobjectTO implements Serializable {
    private static final String TYPE_CLONE = ContainercellTO.getTYPE_GENE();
    private static final String TYPE_CONTROL = ContainercellTO.getTYPE_CONTROL();
    public static final String TYPE_MASTERMIX = "Master Mix";
    public static final String NON_SPOTS = "Non Spots";
    public static final String WATER = "water";
    public static final String HUMAN_IGG = "Human IgG";
    public static final String MOUSE_IGG = "Mouse IgG";
    public static final String MASTERMIX = "Mastermix";
    public static final String NOT_SELECTED = "Not Selected";
    
    private int reagentid;
    private String name;
    private String type;
    private String desc;
    private SampleTO sample;
    
    /** Creates a new instance of ReagentTO */
    public ReagentTO() {
    }
    
    public ReagentTO(String name) {
        this.setName(name);
    }
    
    public ReagentTO(int id) {
        this.setReagentid(id);
    }
    
    public ReagentTO(int id, SampleTO sample) {
        setReagentid(id);
        setSample(sample);
    }
    
    public ReagentTO(String name, String type, String desc) {
        this.setName(name);
        this.setType(type);
        this.setDesc(desc);
    }
    
    public ReagentTO(int id, String name, String type, String desc) {
        this.setReagentid(id);
        this.setName(name);
        this.setType(type);
        this.setDesc(desc);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.objectname = name;
    }
    
    public int getReagentid() {
        return reagentid;
    }
    
    public void setReagentid(int reagentid) {
        this.reagentid = reagentid;
        this.objectid = reagentid;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static String getTYPE_CLONE() {
        return TYPE_CLONE;
    }
    
    public static String getTYPE_CONTROL() {
        return TYPE_CONTROL;
    }

    public SampleTO getSample() {
        return sample;
    }

    public void setSample(SampleTO sample) {
        this.sample = sample;
    }
}
