/*
 * MgcOrderOligoForm.java
 *
 * Created on June 3, 2002, 2:50 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public class MgcOrderOligoForm extends ProjectWorkflowForm
{
    private boolean         m_isFullPlate = false;
    private String          m_processname=null;
    
    
    public void             setIsFullPlate(boolean f){ m_isFullPlate = f;}
    public boolean          getIsFullPlate(){ return m_isFullPlate;}
    public void             setProcessname(String pn){ m_processname = pn;}
    public String           getProcessname(){ return m_processname;}
    
  /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        m_isFullPlate = true;
       
    }   
    
}
