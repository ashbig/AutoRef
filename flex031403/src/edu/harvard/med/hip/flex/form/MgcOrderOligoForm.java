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
public class MgcOrderOligoForm extends ProjectWorkflowForm {
    private boolean isFullPlate = true;
    private String processname = null;
        
    public void setIsFullPlate(boolean isFullPlate) {
        this.isFullPlate = isFullPlate;
    }
    
    public boolean getIsFullPlate() {
        return isFullPlate;
    }
    
    public void setProcessname(String processname) {
        this.processname = processname;
    }
    
    public String getProcessname() {
        return processname;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        isFullPlate = true;
    }    
}
