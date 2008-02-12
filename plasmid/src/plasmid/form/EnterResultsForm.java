/*
 * EnterResultsForm.java
 *
 * Created on October 11, 2005, 11:46 AM
 */
                                
package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import java.util.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class EnterResultsForm extends ActionForm {
    private String resultType;
    private FormFile resultFile;
    private List resultList;
    private String label;
    private Container container;
    
    /** Creates a new instance of EnterResultsForm */
    public EnterResultsForm() {
    }

    public String getResultType() {return resultType;}
    public FormFile getResultFile() {return resultFile;}    
    public String getResult(int index) {
        return (String)resultList.get(index);
    }    
    public String getLabel() {return label;}  
    public int getSize() {return resultList.size();}
    public Container getContainer() {return container;}
    
    public void setResultType(String s) {this.resultType = s;}
    public void setResultFile(FormFile f) {this.resultFile = f;}
    public void setResult(int index, String value) {
        resultList.set(index,value);
    }
    public void setLabel(String s) {this.label = s;}
    public void setContainer(Container c) {this.container = c;}
    
    public void initiate(Container c) {
        this.container = c;
        resultList = new ArrayList();
        for(int i=0; i<c.getSize(); i++) {
            resultList.add(i, c.getSample(i).getResult());
        }
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.resultFile = null;
    }  
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
                    
        if (resultFile == null || resultFile.getFileName().trim().length()<1 || resultFile.getFileSize() == 0)
            errors.add("resultFile", new ActionError("error.resultfile.invalid"));

        return errors;
    }
}

