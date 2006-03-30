/*
 * PrintLabelsForm.java
 *
 * Created on July 29, 2005, 4:20 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class PrintLabelsForm extends ActionForm {
    private List labels;
    
    /** Creates a new instance of PrintLabelsForm */
    public PrintLabelsForm() {
        labels = new ArrayList();
    }
    
    public String getLabel(int i) {
        return (String)labels.get(i);
    }
    
    public void setLabel(int i, String s) {
        labels.set(i, s);
    }
    
    public List getLabels() {
        return labels;
    }
    
    public void setLabels(List l) {this.labels = l;}   
}
