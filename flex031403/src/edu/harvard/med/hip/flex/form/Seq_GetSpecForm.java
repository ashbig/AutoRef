/*
 * Seq_GetSpecForm.java
 *
 * Created on October 7, 2002, 1:16 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
/**
 *
 * @author  htaycher
 */
public class Seq_GetSpecForm extends ActionForm
{
    protected String m_forwardName = null;
   
    
    public void setForwardName(String forwardName)
    {
        m_forwardName = forwardName;
    }
    
    public String getForwardName()
    {
        return m_forwardName;
    }
    
}
