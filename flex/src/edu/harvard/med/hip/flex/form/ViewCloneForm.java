/*
 * ViewCloneForm.java
 *
 * Created on June 20, 2003, 3:26 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

/**
 * Form bean for the user profile page.  This form has the following fields,
 * with default values in square brackets:
 * <ul>
 * <li><b>password</b> - Entered password value
 * <li><b>username</b> - Entered username value
 * </ul>
 *
 * @author $Author: dzuo $
 * @version $Revision: 1.3 $ $Date: 2004-06-11 18:26:49 $
 */

public class ViewCloneForm extends ActionForm {
    protected int cloneid;    
    protected int isDisplay = 0;
    
    public void setCloneid(int cloneid) {this.cloneid = cloneid;}
    public void setIsDisplay(int i) {this.isDisplay = i;}
    public int getCloneid() {return cloneid;}
    public int getIsDisplay() {return isDisplay;}
}
