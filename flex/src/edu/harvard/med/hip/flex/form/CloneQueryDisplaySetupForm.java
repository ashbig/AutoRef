/*
 * CloneQueryDisplaySetupForm.java
 *
 * Created on October 2, 2002, 5:14 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class CloneQueryDisplaySetupForm extends QueryDisplaySetupForm {
    private boolean clonename = false;
    private boolean pubhit = false;
    private boolean cloneResult = false;
    
    /** Creates new CloneQueryDisplaySetupForm */
    public CloneQueryDisplaySetupForm() {
        super();
    }    
    
    public boolean getClonename() {
        return clonename;
    }
    
    public void setClonename(boolean clonename) {
        this.clonename = clonename;
    }
    
    public boolean getPubhit() {
        return pubhit;
    }
    
    public void setPubhit(boolean pubhit) {
        this.pubhit = pubhit;
    }
    
    public boolean getCloneResult() {
        return cloneResult;
    }
    
    public void setCloneResult(boolean cloneResult) {
        this.cloneResult = cloneResult;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
        clonename = false;
        pubhit = false;
        cloneResult = false;
    }        
}
