/*
 * QueryDisplaySetupForm.java
 *
 * Created on September 17, 2002, 1:50 PM
 */

package edu.harvard.med.hip.flex.form;

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
 * @author  dzuo
 * @version
 */
public class QueryDisplaySetupForm extends ActionForm {
    private boolean id = false;
    private boolean gi = false;
    private boolean genename = false;
    private boolean genbank = false;
    private boolean genesymbol = false;
    private boolean pa = false;
    private boolean label = false;
    private boolean well = false;
    private boolean type = false;
    private boolean oligo = false;
    private boolean project = false;
    private boolean workflow = false;
    private String display = "html";
    private boolean isResultDisplay = false;
    
    public boolean getId() {
        return id;
    }
    
    public void setId(boolean id) {
        this.id = id;
    }
    
    public boolean getGi() {
        return gi;
    }
    
    public void setGi(boolean gi) {
        this.gi = gi;
    }
    
    public boolean getGenename() {
        return genename;
    }
    
    public void setGenename(boolean genename) {
        this.genename = genename;
    }
    
    public boolean getGenbank() {
        return genbank;
    }
    
    public void setGenbank(boolean genbank) {
        this.genbank = genbank;
    }
    
    public boolean getGenesymbol() {
        return genesymbol;
    }
    
    public void setGenesymbol(boolean genesymbol) {
        this.genesymbol = genesymbol;
    }
    
    public boolean getPa() {
        return pa;
    }
    
    public void setPa(boolean pa) {
        this.pa = pa;
    }
    
    public boolean getLabel() {
        return label;
    }
    
    public void setLabel(boolean label) {
        this.label = label;
    }
    
    public boolean getWell() {
        return well;
    }
    
    public void setWell(boolean well) {
        this.well = well;
    }
    
    public boolean getType() {
        return type;
    }
    
    public void setType(boolean type) {
        this.type = type;
    }
    
    public boolean getOligo() {
        return oligo;
    }
    
    public void setOligo(boolean oligo) {
        this.oligo = oligo;
    }
    
    public boolean getProject() {
        return project;
    }
    
    public void setProject(boolean project) {
        this.project = project;
    }
    
    public boolean getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(boolean workflow) {
        this.workflow = workflow;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public void setDisplay(String display) {
        this.display = display;
    }
    
    public boolean getIsResultDisplay() {
        return isResultDisplay;
    }
    
    public void setIsResultDisplay(boolean isResultDisplay) {
        this.isResultDisplay = isResultDisplay;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        id = false;
        gi = false;
        genename = false;
        genbank = false;
        genesymbol = false;
        pa = false;
        label = false;
        well = false;
        type = false;
        oligo = false;
        project = false;
        workflow = false;
        display = "html";
        isResultDisplay = false;
    }    
}
