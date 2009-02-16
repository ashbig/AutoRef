/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import plasmid.coreobject.Clone;

/**
 *
 * @author jasonx
 */
public class PReceiveSearchForm extends ActionForm {

    private String cloneid = null;
    private String submit = null;
    private List clones = null;

    public String getCloneid() {
        return cloneid;
    }
    public void setCloneid(String s) {
        cloneid = s;
    }
    public List getClones() {
        return clones;
    }
    public void setClones(List cs) {
        clones = cs;
    }
    public String getSubmit() {
        return submit;
    }
    public void setSubmit(String s) {
        this.submit = s;
    }

    public PReceiveSearchForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        cloneid = null;
        submit = null;
        clones = null;
    }


    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Find")) {
            if ((cloneid == null) || (cloneid.length() < 1)) {
                errors.add("PRF", new ActionError("error.CID.required"));
                // TODO: add 'error.VN.required' key to your resources
            }
        }
        return errors;
    }
}
