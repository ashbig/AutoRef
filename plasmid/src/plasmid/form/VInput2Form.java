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

import plasmid.coreobject.User;
import plasmid.coreobject.CloneVector;

/**
 *
 * @author jasonx
 */
public class VInput2Form extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String maptype = null;
    private String name = null;
    private String description = null;
    private int start = 0;
    private int stop = 0;
    private int featureid = -1;
    private int fid = -1;
    private List features = null;
    private String submit = null;

    public String getStep() {
        return step;
    }

    public void setStep(String i) {
        step = i;
    }

    public int getVectorid() {
        return vectorid;
    }

    public void setVectorid(int v) {
        vectorid = v;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String s) {
        this.submit = s;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String string) {
        maptype = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String string) {
        description = string;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int s) {
        start = s;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int s) {
        stop = s;
    }

    public int getFeatureid() {
        return featureid;
    }

    public void setFeatureid(int s) {
        featureid = s;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int s) {
        fid = s;
    }
    public List getFeatures() {
        return features;
    }

    public void setFeatures(List f) {
        features = f;
    }

    public VInput2Form() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
    public void reset() {
        step = null;
        vectorid = 0;
        maptype = null;
        name = null;
        description = null;
        start = 0;
        stop = 0;
        featureid = -1;
        fid = -1;
        features = null;
        submit = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getSubmit().equals("Add To List")) {
            if ((start > 0) && (stop > 0)) {
            CloneVector v = (CloneVector) request.getSession().getAttribute("Vector");
            int size = v.getSize();
            // if ((getStart() >= getStop()) || (getStart() >= size) || (getStop() > size)) {
            if ((start >= size) || (stop > size)) {
                errors.add("Position", new ActionError("error.position.incorrect"));
            }
            }
        } else if (getSubmit().equals("Continue")) {
            List f = (List) request.getSession().getAttribute("Features");
            if ((f == null) || (f.size() < 1)) {
                errors.add("Features", new ActionError("error.Features.required"));
            }
        }
        

        return errors;
    }
}
