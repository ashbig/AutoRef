/*
 * AddNewItemForm.java
 *
 * Created on August 22, 2007, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

/**
 *
 * @author htaycher
 */
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AddItemsForm extends ProjectWorkflowForm 
{
    private String forwardName = PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.toString();
    private FormFile inputFile = null;
    private FormFile inputFile1 = null;
    private String   plateLabels = null;
    private String   facilityName = null;
    private int processid = -1;
   
    
    public String getForwardName() {        return forwardName;    }
    
    public void setForwardName(String b) {        forwardName = b;    }
    
    public void setInputFile(FormFile v) {        this.inputFile = v;    }
    public FormFile getInputFile() {        return inputFile;    }
   
    public void setInputFile1(FormFile v) {        this.inputFile1 = v;    }
    public FormFile getInputFile1() {        return inputFile1;    }
   
     public void        setPlateLabels (String v){ plateLabels = v;}
     public String      getPlateLabels (){ return  plateLabels ;}
    
      public void        setFacilityName (String v){ facilityName = v;}
     public String      getFacilityName (){ return  facilityName ;}
   
 public void    setProcessid(int v){ processid = v;}
 public int     getProcessid(){ return processid;}
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
        PROCESS_NTYPE cur_pr_type = PROCESS_NTYPE.valueOf(forwardName);
        switch(cur_pr_type)
        {
            case      IMPORT_VECTORS:
            case IMPORT_LINKERS :
            case IMPORT_INTO_NAMESTABLE:
            case IMPORT_CLONING_STRATEGIES:
            {
                 {
                    if (inputFile.getFileName() == null || inputFile.getFileName().trim().length() < 1)
                    {
                        errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));
                    }
                }
                break;
            }
            case PUT_PLATES_FOR_SEQUENCING:
            {
                if ( plateLabels == null || plateLabels.trim().length() < 1
                        || facilityName == null || facilityName.trim().length() < 1)
                {
                    errors.add("mgcCloneFile", new ActionError("error.empty.fields.putplatesforsequencing.wrong"));
                }
                break;
            }
        }
        if ( errors.size() > 0 ) forwardName = cur_pr_type.getPreviousProcess().toString();
        return errors;
    }        
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        forwardName = PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.toString();
    }
}
