/*
 * ExportSearchResultsForm.java
 *
 * Created on March 24, 2004, 4:41 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.*;
import javax.servlet.http.*;

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
 * @author  DZuo
 */
public class ExportSearchResultsForm extends ActionForm {
    private Map cloneList;
    
    /** Creates a new instance of ExportSearchResultsForm */
    public ExportSearchResultsForm() {
        cloneList = new HashMap();
    }
    
    public void setCloneList(String key, String value) {
        Set keys = cloneList.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String k = (String)iter.next();
            if(k.equals(key)) {
                cloneList.put(k, value);
                return;
            }
        }
        cloneList.put(key, value);
    }
    
    public String getCloneList(String key) {
        Set keys = cloneList.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String k = (String)iter.next();
            if(k.equals(key)) {
                return (String)cloneList.get(k);
            }
        }
        
        return null;
    }
    
    public Map getAllClones() {return cloneList;}
}
