//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * Seq_SequenceAnalysisRequestForm.java
 *
 * Created on November 1, 2002, 10:44 AM
 */

package edu.harvard.med.hip.bec.form;

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
    private String m_searchTerm = null;
    private int m_searchValue = -1;
    private String m_sequencetext = null;
   
    public void setSearchType(String s)
    {
        
         m_searchTerm = s;
    }
    
    public String getSearchType()
    {
        return m_searchTerm;
    }
    public void setFullsequence(String s)
    {
      
         m_sequencetext = s.trim();
    }
    
    public String getFullsequence()
    {
        return m_sequencetext;
    }
    public void setSearchValue(int s)
    {
       
        m_searchValue = s;
    }
    
    public int getSearchValue()
    {
        return m_searchValue;
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
       
        if( m_searchValue == -1 || m_searchValue == 0) 
        {
           errors.add("searchTerm", new ActionError("error.searchTerm.required"));
         
        }
     
        return errors;
    }
}
