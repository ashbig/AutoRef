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
    /*
    public void setForwardName(String forwardName)
    {
        m_forwardName = forwardName;
    }
    
    public String getForwardName()
    {
        return m_forwardName;
    }
     **/
    public void setRefseqid(int s)
    {
        m_refseqid = s;
    }
    
    public int getRefseqid()
    {
        return m_refseqid;
    }
    public void setGI(int s)
    {
        m_gi = s;
    }
    
    public int getGI()
    {
        return m_gi;
    }
    
}
