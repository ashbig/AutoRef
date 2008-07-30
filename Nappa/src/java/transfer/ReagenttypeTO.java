/*
 * ReagenttypeTO.java
 *
 * Created on July 16, 2007, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

/**
 * 
 * @author dzuo
 */
public class ReagenttypeTO  {
    private String type;
    //private Collection<Reagent> reagentCollection;
    
    /**
     * Creates a new instance of ReagenttypeTO
     */
    public ReagenttypeTO() {
    }

    /**
     * Creates a new instance of ReagenttypeTO with the specified values.
     * 
     * @param type the type of the ReagenttypeTO
     */
    public ReagenttypeTO(String type) {
        this.type = type;
    }

    /**
     * Gets the type of this ReagenttypeTO.
     * 
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type of this ReagenttypeTO to the specified value.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

}
