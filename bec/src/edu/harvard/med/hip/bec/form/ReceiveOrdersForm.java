/*
 * ReceiveOligo.java
 *
 * Created on June 14, 2001, 3:19 PM
 */

package edu.harvard.med.hip.bec.form;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 *
 * @author  Wendy Mar
 * @version
 */
public class ReceiveOrdersForm extends ActionForm
{
    public final static String DELIMITER = "\t\n\r\f, ";
    private String m_plateIds = null;
    private String m_receiveDate = null;
    private String m_researcherBarcode = null;
    private List m_plateList = null;
    
   
    
    /**
     * Set the oligoPlateIds.
     *
     * @param oligoPlateIds The value to be set to.
     */
    public void setPlateIds(String plateIds)
    {
        m_plateIds = plateIds;
        m_plateList = parsePlateId(plateIds);
    }
    
    /**
     * Return the oligoPlateId.
     *
     * @return The oligoPlateId.
     */
    public String getPlateIds()    {        return m_plateIds;    }
    
    /**
     * Return the list of oligoPlate labels.
     *
     * @return The oligoPlateList.
     */
    public List getPlateList()    {        return m_plateList;    }
    
    /**
     * Return the receiveDate.
     *
     * @return receive date
     */
    public String getReceiveDate()    {         return m_receiveDate;    }
    
    /**
     * Set the receive date.
     *
     * @param receiveDate The new receive date
     */
    public void setReceiveDate(String receiveDate)    {    m_receiveDate = receiveDate;  }
    
    
    /**
     * Set the researcher barcode to the given value.
     *
     * @param researcherBarcode The value to be set to.
     */
    public void setResearcherBarcode(String researcherBarcode)
    {
        m_researcherBarcode = researcherBarcode;
    }
    
    /**
     * Return the researcher barcode.
     *
     * @return The researcher barcode.
     */
    public String getResearcherBarcode()
    {
        return m_researcherBarcode;
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
        if((m_researcherBarcode == null) || (m_researcherBarcode.trim().length()<1))
        {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", m_researcherBarcode));
        }
        if((m_plateIds == null) || (m_plateIds.trim().length()<1))
        {
            errors.add("oligoPlateIds", new ActionError("error.plateid.required", m_plateIds));
        }
        if((m_receiveDate == null) || (m_receiveDate.trim().length()<1))
        {
            errors.add("receiveDate", new ActionError("error.invalid.date", m_receiveDate));
        }
        
        return errors;
    }
    
    /**
     * parse the oligo plate IDs user entered into to textarea into a list
     * of plateIds.
     * @param oligoPlateIds The user input text
     * @return A list of oligoPlateIds
     */
    private LinkedList parsePlateId(String plateIds)
    {
        LinkedList plateList = new LinkedList();
        StringTokenizer tokenizer = null;
        String plateId = null;
        
        tokenizer = new StringTokenizer(plateIds, DELIMITER);
        while (tokenizer.hasMoreTokens())
        {
            plateId = tokenizer.nextToken();
            plateList.add(plateId);
        } //while
        
        return plateList;
    } //parseOligoPlateIds
    
    
    
}
