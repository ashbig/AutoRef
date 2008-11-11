/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class VInput4Form extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String submit = null;
    private String hoststrain = null;
    private String description = null;
    private int HSID = -1;
    private String growthcondition = null;
    private String isrecommended = "N";
    private int GCID = -1;
    private String hosttype = null;
    private String marker = null;
    private int SMID = -1;

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

    public String getHoststrain() {
        return hoststrain;
    }

    public void setHoststrain(String i) {
        hoststrain = i;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String i) {
        description = i;
    }

    public String getGrowthcondition() {
        return growthcondition;
    }

    public void setGrowthcondition(String i) {
        growthcondition = i;
    }

    public String getIsrecommended() {
        return isrecommended;
    }

    public void setIsrecommended(String i) {
        isrecommended = i;
    }

    public String getHosttype() {
        return hosttype;
    }

    public void setHosttype(String i) {
        hosttype = i;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String i) {
        marker = i;
    }

    public int getHSID() {
        return HSID;
    }

    public void setHSID(int v) {
        HSID = v;
    }

    public int getGCID() {
        return GCID;
    }

    public void setGCID(int v) {
        GCID = v;
    }

    public int getSMID() {
        return SMID;
    }

    public void setSMID(int v) {
        SMID = v;
    }

    public VInput4Form() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        step = null;
        vectorid = 0;
        submit = null;
        hoststrain = null;
        description = null;
        HSID = -1;
        growthcondition = null;
        isrecommended = "N";
        GCID = -1;
        hosttype = null;
        marker = null;
        SMID = -1;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (vectorid < 1) {
            errors.add("VIF4", new ActionError("error.VID.required"));
        // TODO: add 'error.VN.required' key to your resources
        }
        if (submit.equals("Add To Host Strain List")) {
            if ((hoststrain == null) || (hoststrain.length() < 1)) {
                errors.add("VIF4", new ActionError("error.HS.required"));
            }
        } else if (submit.equals("Add To Growth Condition List")) {
            if ((growthcondition == null) || (growthcondition.length() < 1)) {
                errors.add("VIF4", new ActionError("error.GC.required"));
            }
        } else if (submit.equals("Add To Selectable Marker List")) {
            if ((hosttype == null) || (hosttype.length() < 1)) {
                errors.add("VIF4", new ActionError("error.HT.required"));
            }
            if ((marker == null) || (marker.length() < 1)) {
                errors.add("VIF4", new ActionError("error.MK.required"));
            }
        } else if (submit.equals("Remove From Host Strain List")) {
            if (HSID < 0) {
                errors.add("VIF4", new ActionError("error.HSID.required"));
            }
        } else if (submit.equals("Remove From Growth Condition List")) {
            if (GCID < 0) {
                errors.add("VIF4", new ActionError("error.GCID.required"));
            }
        } else if (submit.equals("Remove From Selectable Marker List")) {
            if (SMID < 0) {
                errors.add("VIF4", new ActionError("error.SMID.required"));
            }
        } else if (submit.equals("Continue") || (submit.equals("Save..."))) {
            HttpSession session = request.getSession();
            List VHS = (List) session.getAttribute("VHS");
            if ((VHS == null) || (VHS.size() < 1)) {
                errors.add("VIF9", new ActionError("error.VHS.required"));
            }
            List VGC = (List) session.getAttribute("VGC");
            if ((VGC == null) || (VGC.size() < 1)) {
                errors.add("VIF9", new ActionError("error.VGC.required"));
            }
            List VSM = (List) session.getAttribute("VSM");
            if ((VSM == null) || (VSM.size() < 1)) {
                errors.add("VIF9", new ActionError("error.VSM.required"));
            }
        }
        return errors;
    }
}
