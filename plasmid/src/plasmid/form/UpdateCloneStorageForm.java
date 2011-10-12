/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Dongmei
 */
public class UpdateCloneStorageForm extends ActionForm {
    private String cloneidList;
    private int [] selectedSampleids;
    private int [] cloneid;
    private String [] labels;
    
    public UpdateCloneStorageForm() {
    }

    public void reset() {
        selectedSampleids = new int[0];
    }
    
    public String getCloneidList() {return cloneidList;}
    
    public void setCloneidList(String s) {this.cloneidList = s;}
    
        
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
        if ((cloneidList == null) || (cloneidList.trim().length() < 1))
            errors.add("cloneidList", new ActionError("error.cloneid.required"));
            
        return errors;
    }

    public int[] getSelectedSampleids() {
        return selectedSampleids;
    }

    public void setSelectedSampleids(int[] selectedSampleids) {
        this.selectedSampleids = selectedSampleids;
    }

    public int[] getCloneid() {
        return cloneid;
    }

    public void setCloneid(int[] cloneid) {
        this.cloneid = cloneid;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabel(int i, String label) {
        this.labels[i] = label;
    }
    
    public String getLabel(int i) {
        return labels[i];
    }
    
    public void initiateLabels(int n) {
        this.labels = new String[n];
    }
}

