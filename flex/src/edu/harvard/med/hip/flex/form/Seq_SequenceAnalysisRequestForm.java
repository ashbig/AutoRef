/*
 * Seq_SequenceAnalysisRequestForm.java
 *
 * Created on November 1, 2002, 10:44 AM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 *
 * @author  htaycher
 */
public class Seq_SequenceAnalysisRequestForm extends ActionForm  
{
    
   // private String m_forwardName = null;
    private int m_refseqid = -1;
    private int m_gi = -1;
   
    public void setRefseqid(int s)
    {
         System.out.println(s);
        m_refseqid = s;
    }
    
    public int getRefseqid()
    {
        return m_refseqid;
    }
    public void setGI(int s)
    {
         System.out.println(s);
        m_gi = s;
    }
    
    public int getGI()
    {
        return m_gi;
    }
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
  
    
    public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
    {
        
        ActionErrors errors = new ActionErrors();
        System.out.println("validate: " +m_gi +" "+ m_refseqid);
        if( m_gi == -1 && m_refseqid == -1) 
        {
           errors.add("searchTerm", new ActionError("error.searchTerm.required"));
        }
     
        return errors;
    }
}
