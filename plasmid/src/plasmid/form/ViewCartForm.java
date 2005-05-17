/*
 * ViewCartForm.java
 *
 * Created on May 6, 2005, 3:52 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

import plasmid.query.coreobject.CloneInfo;

/**
 *
 * @author  DZuo
 */
public class ViewCartForm extends ActionForm {
    private List cloneCount;
    private String submitButton;
    
    /** Creates a new instance of ViewCartForm */
    public ViewCartForm() {
    }
    
    public String getCloneCount(int i) {
        return (String)cloneCount.get(i);
    }
    
    public void setCloneCount(int i, String s) {
        cloneCount.set(i,s);
    }
    
    public void setCloneCountList(List clones) {        
        cloneCount = new ArrayList();
        for(int i=0; i<clones.size(); i++) {
            CloneInfo c = (CloneInfo)clones.get(i);
            int q = c.getQuantity();
            cloneCount.add((new Integer(q)).toString());
        }
    }
    
    public List getCloneCountList() {
        return cloneCount;
    }  
    
    public void removeClone(int i) {
        cloneCount.remove(i);
    }
    
    public String getSubmitButton() {
        return submitButton;
    }
    
    public void setSubmitButton(String s) {
        this.submitButton = s;
    }
}
