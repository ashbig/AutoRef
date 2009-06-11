/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.Location;
import org.apache.struts.upload.*;
 
/**
 *
 * @author htaycher
 */
public class NorgenRearrayForm  extends CreateProcessPlateForm{
    private int     numberOfPlates = 0;   
    private String  researcherBarcode = null;
    private FormFile logFile;
    private String protocolname = null;
    private int     protocolid=-1;
    
    public void setProtocolname(String v)    {        protocolname = v;    }
    public String getProtocolname()    {        return protocolname;    }
    public void setProtocolid(int v)    {        protocolid = v;    }
    public int getProtocolid()    {        return protocolid;    }
    
    
    public int      getNumberOfPlates(){ return numberOfPlates;}
    public void     setNumberOfPlates(int v){ numberOfPlates = v;}
     public void setResearcherBarcode(String v) { researcherBarcode = v;    }
     public String getResearcherBarcode() {        return researcherBarcode;    }
   public void setLogFile(FormFile v) {       logFile = v;    }
    
    public FormFile getLogFile() {        return logFile;    }
       
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
        if ((researcherBarcode == null) || (researcherBarcode.trim().length() < 1))
        {errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));}

        if (Protocol.POPULATE_CONTAINERS_FROM_NORGEN_LOG_FILE.equals(protocolname) && 
             (
             logFile == null || logFile.getFileName().trim().length()<1 || logFile.getFileSize() == 0))
        { errors.add("logFile", new ActionError("error.colonylogfile.invalid"));
        };

        
        return errors;
    }   
}
