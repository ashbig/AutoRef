/*
 * Plate96To384MappingForm.java
 *
 * Created on May 10, 2007, 1:04 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class Plate96To384MappingForm extends ActionForm {
    private String plateA;
    private String plateB;
    private String plateC;
    private String plateD;
    private String plate;
    
    /** Creates a new instance of Plate96To384MappingForm */
    public Plate96To384MappingForm() {
    }
    
    public String getPlateA() {return plateA;}
    public String getPlateB() {return plateB;}
    public String getPlateC() {return plateC;}
    public String getPlateD() {return plateD;}
    public String getPlate() {return plate;}
    
    public void setPlateA(String s) {this.plateA = s;}
    public void setPlateB(String s) {this.plateB = s;}
    public void setPlateC(String s) {this.plateC = s;}
    public void setPlateD(String s) {this.plateD = s;}
    public void setPlate(String s) {this.plate = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        plateA=plateB=plateC=plateD=plate=null;
    }  
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((plateA == null) || (plateA.trim().length() < 1))
            errors.add("plateA", new ActionError("error.container.required"));
        if ((plateB == null) || (plateB.trim().length() < 1))
            errors.add("plateB", new ActionError("error.container.required"));
        if ((plateC == null) || (plateC.trim().length() < 1))
            errors.add("plateC", new ActionError("error.container.required"));
        if ((plate == null) || (plate.trim().length() < 1))
            errors.add("plate", new ActionError("error.container.required"));
        
        return errors;
    }
}
