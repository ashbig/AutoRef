/*
 * VectorTO.java
 *
 * Created on July 16, 2007, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 * @author dzuo
 */

public class VectorTO implements Serializable {
    private String name;
    private String marker;
    private String description;
    
   //private Collection<Clone> cloneCollection;
    
    /**
     * Creates a new instance of VectorTO
     */
    public VectorTO() {
    }

    /**
     * Creates a new instance of VectorTO with the specified values.
     * 
     * @param name the name of the VectorTO
     */
    public VectorTO(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this VectorTO.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this VectorTO to the specified value.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the marker of this VectorTO.
     * 
     * @return the marker
     */
    public String getMarker() {
        return this.marker;
    }

    /**
     * Sets the marker of this VectorTO to the specified value.
     * 
     * @param marker the new marker
     */
    public void setMarker(String marker) {
        this.marker = marker;
    }

    /**
     * Gets the description of this VectorTO.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this VectorTO to the specified value.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
