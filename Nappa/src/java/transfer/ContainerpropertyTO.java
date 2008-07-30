/*
 * ContainerpropertyTO.java
 *
 * Created on October 19, 2007, 9:22 AM
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
public class ContainerpropertyTO implements Serializable {
    private int containerid;
    private String type;
    private String value;
    
    /** Creates a new instance of ContainerpropertyTO */
    public ContainerpropertyTO() {
    }

    public ContainerpropertyTO(int id, String t, String v) {
        this.containerid = id;
        this.type = t;
        this.value = v;
    }
    
    public int getContainerid() {
        return containerid;
    }

    public void setContainerid(int containerid) {
        this.containerid = containerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
