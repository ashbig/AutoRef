/*
 * ReceiveOligo.java
 *
 * Created on June 14, 2001, 3:19 PM
 */

package edu.harvard.med.hip.flex.form;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 *
 * @author  Wendy Mar
 * @version
 */
public class ReceiveOligoOrdersForm {
    private String oligoPlateIds = null;
    private String receiveDate = null;
    private String researcherBarcode = null;
    
    /** Creates new ReceiveOligo */
    public ReceiveOligoOrdersForm() {
    }
    
    /**
     * Set the oligoPlateIds.
     *
     * @param oligoPlateIds The value to be set to.
     */
    public void setOligoPlateIds(String oligoPlateIds) {
        this.oligoPlateIds = oligoPlateIds;
    }
    
    /**
     * Return the oligoPlateId.
     *
     * @return The oligoPlateId.
     */
    public String getOligoPlateIds() {
        return this.oligoPlateIds;
    }
    
    /**
     * Return the receiveDate.
     *
     * @return receive date
     */
    public String getReceiveDate() {

	return (this.receiveDate);

    }

    /**
     * Set the receive date.
     *
     * @param receiveDate The new receive date
     */
    public void setReceiveDate(String receiveDate) {

        this.receiveDate = receiveDate;

    }
    
    /**
     * Set the researcher barcode to the given value.
     *
     * @param researcherBarcode The value to be set to.
     */
    public void setResearcherBarcode(String researcherBarcode) {
        this.researcherBarcode = researcherBarcode;
    }
    
    /**
     * Return the researcher barcode.
     *
     * @return The researcher barcode.
     */
    public String getResearcherBarcode() {
        return researcherBarcode;
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
        if((researcherBarcode == null) || (researcherBarcode.trim().length()<1)) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
        }    
        if((oligoPlateIds == null) || (oligoPlateIds.trim().length()<1)) {
            errors.add("oligoPlateIds", new ActionError("error.plateId.invalid", oligoPlateIds));
        }
        if((receiveDate == null) || (receiveDate.trim().length()<1)) {
            errors.add("receiveDate", new ActionError("error.invalid.date", receiveDate));
        }
        
        return errors;
    }
}
