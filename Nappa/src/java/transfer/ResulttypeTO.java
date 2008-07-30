/*
 * ResulttypeTO.java
 *
 * Created on December 10, 2007, 3:35 PM
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
public class ResulttypeTO implements Serializable {
    private String type;
    private String description;
    
    /** Creates a new instance of ResulttypeTO */
    public ResulttypeTO() {
    }

    public ResulttypeTO(String type, String description) {
        this.type = type;
        this.description = description;
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
    
}
