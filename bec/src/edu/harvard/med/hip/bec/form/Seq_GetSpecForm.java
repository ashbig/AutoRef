/*
 * Seq_GetSpecForm.java
 *
 * Created on October 7, 2002, 1:16 PM
 */

package edu.harvard.med.hip.bec.form;

import org.apache.struts.action.*;
/**
 *
 * @author  htaycher
 */
public class Seq_GetSpecForm extends ActionForm
{
    private int m_forwardName = -1;//type of spec
   
    
    public void setForwardName(int forwardName)
    {
         
        m_forwardName = forwardName;
        
    }
    
    public int getForwardName()
    {
        return m_forwardName;
    }
    
}
