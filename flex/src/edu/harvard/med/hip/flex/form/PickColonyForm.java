/*
 * PickColonyForm.java
 *
 * Created on July 3, 2001, 5:20 PM
 *
 * The form bean used for creating culture block.
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.Location;
import edu.harvard.med.hip.flex.workflow.Project;

/**
 *
 * @author  dzuo
 * @version
 */
public class PickColonyForm extends ProjectWorkflowForm
{
    private String processname = null;
    
    public static final int LASTINDEX = 10;
    
    private String agarPlateF1 = null;
    private String agarPlateC1 = null;
    
    private int agarF1Location;
    private int agarC1Location;
    private int [] destLocations;
    private String subProtocolName = null;
    
    private String pickingMethod = "interleaved";
    private String isSeqPlates = "Yes";
    
    /**
     * Set the processname to the given value.
     *
     * @param processname The value to be set to.
     */
    public void setProcessname(String processname)
    {
        this.processname = processname;
    }
    
    /**
     * Return the processname.
     *
     * @return The processname.
     */
    public String getProcessname()
    {
        return processname;
    }
    
    /**
     * Get the first fusion agar plate barcode.
     *
     * @return The first fusion agar plate barcode.
     */
    public String getAgarPlateF1()
    {
        return agarPlateF1;
    }
    
    /**
     * Set the first fusion agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateF1(String agarPlateF1)
    {
        this.agarPlateF1 = agarPlateF1;
    }
    
    /**
     * Get the first closed agar plate barcode.
     *
     * @return The first closed agar plate barcode.
     */
    public String getAgarPlateC1()
    {
        return agarPlateC1;
    }
    
    /**
     * Set the first closed agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateC1(String agarPlateC1)
    {
        this.agarPlateC1 = agarPlateC1;
    }
    
    /**
     * Return the location of the first open agar plate.
     *
     * @return The location of the first open agar plate.
     */
    public int getAgarF1Location()
    {
        return agarF1Location;
    }
    
    /**
     * Set the location of the first open agar plate.
     *
     * @param agarF1Location The value to be set to.
     */
    public void setAgarF1Location(int agarF1Location)
    {
        this.agarF1Location = agarF1Location;
    }
    
    /**
     * Return the location of the first closed agar plate.
     *
     * @return The location of the first closed agar plate.
     */
    public int getAgarC1Location()
    {
        return agarC1Location;
    }
    
    /**
     * Set the location of the first closed agar plate.
     *
     * @param agarC1Location The value to be set to.
     */
    public void setAgarC1Location(int agarC1Location)
    {
        this.agarC1Location = agarC1Location;
    }
    
    /**
     * Set the destination locations.
     *
     * @param destLocations The destination locations.
     */
    public void setDestLocations(int [] destLocations)
    {
        this.destLocations = destLocations;
    }
    
    /**
     * Return the destination locations.
     *
     * @return The destination locations.
     */
    public int [] getDestLocations()
    {
        return destLocations;
    }
    
    /**
     * Set the subProtocolName to the given value.
     *
     * @param subProtocolName The value to be set to.
     */
    public void setSubProtocolName(String subProtocolName)
    {
        this.subProtocolName = subProtocolName;
    }
    
    /**
     * Return the subProtocolName.
     *
     * @return The subProtocolName.
     */
    public String getSubProtocolName()
    {
        return subProtocolName;
    }
    
    public void setPickingMethod(String method)
    {
        this.pickingMethod = method;
    }
    
    public String getPickingMethod()
    {
        return pickingMethod;
    }
    
    public void setIsSeqPlates(String s) {
        this.isSeqPlates = s;
    }
    
    public String getIsSeqPlates() {
        return isSeqPlates;
    }
    
    /**
     * Reset all properties to their default values.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        processname = null;
        agarPlateF1 = null;
        agarPlateC1 = null;
        subProtocolName = null;
        pickingMethod = "interleaved";
        isSeqPlates = "Yes";
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
    HttpServletRequest request)
    {
        
        ActionErrors errors = new ActionErrors();
        boolean isReturn = false;
       
        if((agarPlateF1 == null) || (agarPlateF1.trim().length()==0))
        {
            errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
             return errors;
        }
        
        if(pickingMethod.equals("interleaved"))
        {
            if(projectid == Project.YEAST || projectid == Project.YP)
            {
                if ( agarPlateF1.trim().length()!=11 || agarPlateF1.charAt(LASTINDEX) != 'C' )
                {
              
                    errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
                    isReturn = true;
                }
            }
            else
            {
                if (agarPlateF1.trim().length()!=11 || agarPlateF1.charAt(LASTINDEX) != 'F')
               {
                      
                        errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
                        isReturn = true;
                }
            }
            if(projectid != Project.PSEUDOMONAS && projectid != Project.KINASE)
            {
                if((agarPlateC1 == null) || (agarPlateC1.trim().length()!=11))
                {
                  
                    errors.add("agarPlateC1", new ActionError("error.plate.invalid.barcode", agarPlateC1));
                    isReturn = true;
                }
            }
        }
 
        if(isReturn)
        {
            return errors;
        }
        
        if(pickingMethod.equals("interleaved"))
        {
            if(projectid == Project.PSEUDOMONAS || projectid == Project.KINASE)
            {
                if(agarPlateC1 != null && agarPlateC1.trim().length() >= 11 && (agarPlateC1.charAt(LASTINDEX) != 'F'))
                {
              
                    errors.add("agarPlateC1", new ActionError("error.plate.invalid.barcode", agarPlateC1));
                    isReturn = true;
                }
            } 
            else
            {
                if((agarPlateC1.charAt(LASTINDEX) != 'C'))
                {
                 
                    errors.add("agarPlateC1", new ActionError("error.plate.invalid.barcode", agarPlateC1));
                    isReturn = true;
                }
            }
            
            if(isReturn)
            {
                return errors;
            }
            
            // Check whether the two pairs matching with each other.
            if(projectid == Project.PSEUDOMONAS || projectid == Project.KINASE ||  projectid == Project.YEAST || projectid==Project.YP)
            {
                if(agarPlateC1 != null && agarPlateC1.trim().length()>= 11)
                {
                    if(!(agarPlateF1.substring(3, 9).equals(agarPlateC1.substring(3, 9))))
                    {
                      
                        errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateC1));
                    }
                }
            } else
            {
                if(!(agarPlateF1.substring(0, 3).equals(agarPlateC1.substring(0, 3))))
                {
                  
                    errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateC1));
                }
            }
        }
        
        return errors;
    }
}

