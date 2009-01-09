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
import org.apache.struts.upload.FormFile;

import plasmid.coreobject.User;

/**
 *
 * @author jasonx
 */
public class VInputForm extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String name = null;
    private String description = null;
    private String syns = null;
    private String form = null;
    private String type = null;
    private int size = 0;
    private FormFile mapfile = null;
    private String mapfilename = null;
    private FormFile seqfile = null;
    private String seqfilename = null;
    private String comments = null;
    private String status = null;
    private String submit = null;

    public String getStep() {
        return step;
    }

    public void setStep(String s) {
        step = s;
    }

    public int getVectorid() {
        return vectorid;
    }

    public void setVectorid(int v) {
        vectorid = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String string) {
        description = string.trim();
    }

    public String getSyns() {
        return syns;
    }

    public void setSyns(String string) {
        syns = string.trim();
    }

    public String getForm() {
        return form;
    }

    public void setForm(String string) {
        form = string.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String string) {
        type = string.trim();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int string) {
        size = string;
    }

    public String getMapfilename() {
        return mapfilename;
    }

    public void setMapfilename(String f) {
        mapfilename = f.trim();
    }

    public String getSeqfilename() {
        return seqfilename;
    }

    public void setSeqfilename(String f) {
        seqfilename = f.trim();
    }

    public FormFile getMapfile() {
        return mapfile;
    }

    public void setMapfile(FormFile f) {
        if ((f != null) && (f.getFileName().trim().length() > 0) && (f.getFileSize() > 0)) {
            mapfile = f;
            // mapfilename = mapfile.getFileName().trim();
        }
    }

    public FormFile getSeqfile() {
        return seqfile;
    }

    public void setSeqfile(FormFile f) {
        if ((f != null) && (f.getFileName().trim().length() > 0) && (f.getFileSize() > 0)) {
            seqfile = f;
            // seqfilename = seqfile.getFileName().trim();
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String string) {
        comments = string.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String string) {
        status = string.trim();
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String s) {
        this.submit = s;
    }

    public VInputForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        step = null;
        vectorid = 0;
        name = null;
        description = null;
        syns = null;
        form = null;
        type = null;
        size = 0;
        mapfile = null;
        seqfile = null;
        mapfilename = null;
        seqfilename = null;
        comments = null;
        status = null;
        submit = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!submit.equals("Cancel")) {
            if (name == null || name.length() < 1) {
                errors.add("Name", new ActionError("error.NAME.required"));
            }
            if (form == null || form.length() < 1) {
                errors.add("Form", new ActionError("error.FORM.required"));
            }
            if (type == null || type.length() < 1) {
                errors.add("Form", new ActionError("error.TYPE.required"));
            }

//       if (size < 1) {
        //         errors.add("Size", new ActionError("error.SIZE.required"));
        // TODO: add 'error.VN.required' key to your resources
        //   }
        //      if (size != (int) Math.floor(size))
//              errors.add("Size", new ActionError("error.SIZE.needinteger"));

        }
        return errors;
    }
}
