/*
 * EnterOligoPlateLocationForm.java
 *
 * Created on July 9, 2001, 5:56 PM
 */

package edu.harvard.med.hip.flex.form;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 *
 * @author  Wendy Mar
 * @version
 */
public class EnterOligoPlateLocationForm extends ActionForm{
 //   private String oligoPlateIds = null;
    
    private LinkedList locations = null;
    
    /**
     * Constructor 
     *
     * @param the container this form is representing
     */
    public EnterOligoPlateLocationForm() {
        
        locations = new LinkedList();
         
    }
    
    
     /**
     * Accessor for the locations
     *
     * @param index The index of the location to get
     *
     * @return the location associated with the index property
     */
    public String getLocations(int index) {
        return (String)locations.get(index);
    }
    
    /**
     * Mutataor for the index location.
     *
     * @param index The index to change.
     * @param value the value to set to.
     */
    public void setLocations(int index, String value) {
        locations.set(index,value);
    }

    
    public String getLocations(){
     String s="FREEZER";
     return s;
    }
    
    public void setLocations() {
     
    }
}
