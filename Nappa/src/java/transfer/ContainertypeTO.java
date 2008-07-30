/*
 * ContainertypeTO.java
 *
 * Created on March 22, 2007, 10:34 AM
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
public class ContainertypeTO  implements Serializable{
    public static final String TYPE_SLIDE = "slide";
    public static final String TYPE_PLATE384 = "384-WELL PLATE";
    public static final String TYPE_PLATE96 = "96 Well Plate";
    public static final String TYPE_PLATE_Costar96flatb = "Costar96flatb on 30mmNest/MP16";
    public static final String TYPE_PLATE_Costar96ub = "Costar96ub on 30mmNest/MP16";
    
    private String type;
    private String description;
    private int numofrow;
    private int numofcol;
    
    /** Creates a new instance of ContainertypeTO */
    public ContainertypeTO() {
    }
    
    public ContainertypeTO(String type) {
        this.type = type;
    }
    
    public ContainertypeTO(String type, String desc, int row, int col) {
        this.type = type;
        this.description = desc;
        this.numofrow = row;
        this.numofcol = col;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getNumofrow() {
        return numofrow;
    }
    
    public void setNumofrow(int numofrow) {
        this.numofrow = numofrow;
    }
    
    public int getNumofcol() {
        return numofcol;
    }
    
    public void setNumofcol(int numofcol) {
        this.numofcol = numofcol;
    }
    
}
