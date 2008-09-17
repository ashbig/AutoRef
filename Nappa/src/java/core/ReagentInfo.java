/*
 * ReagentInfo.java
 *
 * Created on July 17, 2007, 4:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ReagentInfo implements Serializable {
    protected long reagentid;
    protected String name;
    protected String type;
    protected String desc;
    protected String plate;
    protected String well;
    
    /** Creates a new instance of ReagentInfo */
    public ReagentInfo() {
    }

    public ReagentInfo(String name, String type, String desc, String plate, String well) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.plate = plate;
        this.well = well;
    }
    
    public long getReagentid() {
        return reagentid;
    }

    public void setReagentid(long reagentid) {
        this.reagentid = reagentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getWell() {
        return well;
    }

    public void setWell(String well) {
        this.well = well;
    }
    
}
