/*
 * EnterResultForm.java
 *
 * Created on August 11, 2003, 2:22 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.Researcher;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class EnterResultForm extends CreateExpressionPlateForm {
    protected String newPlate = null;
    protected String nextForward = null;
    
    protected boolean well = true;
    protected boolean sampleid = true;
    protected boolean geneSymbol = false;
    protected boolean pa = false;
    protected boolean sgd = false;
    protected boolean masterClone = false;
    protected boolean researcher = true;
    protected boolean restriction = false;
    protected boolean pcr = false;
    protected boolean colony = false;
    protected boolean florescence = false;
    protected boolean protein = false;
    protected boolean status = true;
    protected boolean date = false;
    
    // store the results as an index property
    protected List pcrResultList;
    protected List floResultList;
    protected List proteinResultList;
    protected List restrictionResultList;
    protected List colonyResultList;
    protected List authorList;
    protected List dateList;
    protected List statusList;
    protected List commentsList;
    protected List cloneidList;
    
    protected Researcher r;
    protected FormFile filename = null;
    
    /** Creates a new instance of EnterResultForm */
    public EnterResultForm() {
        super();
    }
    
    public void setNewPlate(String s) {this.newPlate = s;}
    public void setWell(boolean b) {this.well = b;}
    public void setSampleid(boolean b) {this.sampleid = b;}
    public void setGeneSymbol(boolean b) {this.geneSymbol = b;}
    public void setPa(boolean b) {this.pa = b;}
    public void setSgd(boolean b) {this.sgd = b;}
    public void setMasterClone(boolean b) {this.masterClone = b;}
    public void setResearcher(boolean b) {this.researcher = b;}
    public void setRestriction(boolean b) {this.restriction = b;}
    public void setPcr(boolean b) {this.pcr = b;}
    public void setColony(boolean b) {this.colony = b;}
    public void setFlorescence(boolean b) {this.florescence = b;}
    public void setProtein(boolean b) {this.protein = b;}
    public void setStatus(boolean b) {this.status = b;}
    public void setDate(boolean b) {this.date =b;}
    public void setPcrResultList(List l) {this.pcrResultList = l;}
    public void setFloResultList(List l) {this.floResultList = l;}
    public void setProteinResultList(List l) {this.proteinResultList = l;}
    public void setRestrictionResultList(List l) {this.restrictionResultList = l;}
    public void setColonyResultList(List l) {this.colonyResultList = l;}
    public void setStatusList(List l) {this.statusList = l;}
    public void setCommentsList(List l) {this.commentsList = l;}
    public void setPcrResult(int index, String value) {this.pcrResultList.set(index, value);}
    public void setFloResult(int index, String value) {this.floResultList.set(index, value);}
    public void setRestrictionResult(int index, String value) {this.restrictionResultList.set(index,value);}
    public void setProteinResult(int index, String value) {this.proteinResultList.set(index, value);}
    public void setColonyResult(int index, String value) {this.colonyResultList.set(index, value);}
    public void setAuthor(int index, String value) {this.authorList.set(index,value);}
    public void setStartDate(int index, String value) {this.dateList.set(index,value);}
    public void setCloneStatus(int index, String value) {this.statusList.set(index, value);}
    public void setComments(int index, String value) {this.commentsList.set(index,value);}
    public void setCloneid(int index, String value) {this.cloneidList.set(index, value);}
    public void setNextForward(String s) {this.nextForward = s;}
    public void setResearcherObject(Researcher r) {this.r = r;}
    public void setFilename(FormFile f) {this.filename = f;}
    
    public String getNewPlate() {return newPlate;}
    public boolean getWell() {return well;}
    public boolean getSampleid() {return sampleid;}
    public boolean getGeneSymbol() {return geneSymbol;}
    public boolean getPa() {return pa;}
    public boolean getSgd() {return sgd;}
    public boolean getMasterClone() {return masterClone;}
    public boolean getResearcher() {return researcher;}
    public boolean getRestriction() {return restriction;}
    public boolean getPcr() {return pcr;}
    public boolean getColony() {return colony;}
    public boolean getFlorescence() {return florescence;}
    public boolean getProtein() {return protein;}
    public boolean getStatus() {return status;}
    public boolean getDate() {return date;}
    public List getPcrResultList() {return pcrResultList;}
    public List getFloResultList() {return floResultList;}
    public List getProteinResultList() {return proteinResultList;}
    public List getRestrictionResultList() {return restrictionResultList;}
    public List getColonyResultList() {return colonyResultList;}
    public List getCloneidList() {return cloneidList;}
    public List getCommentsList() {return commentsList;}
    public List getStatusList() {return statusList;}
    public String getPcrResult(int index) {return (String)pcrResultList.get(index);}
    public String getFloResult(int index) {return (String)floResultList.get(index);}
    public String getRestrictionResult(int index) {return (String)restrictionResultList.get(index);}
    public String getProteinResult(int index) {return (String)proteinResultList.get(index);}
    public String getColonyResult(int index) {return (String)colonyResultList.get(index);}
    public String getAuthor(int index) {return (String)authorList.get(index);}
    public String getStartDate(int index) {return (String)dateList.get(index);}
    public String getCloneStatus(int index) {return (String)statusList.get(index);}
    public String getComments(int index) {return (String)commentsList.get(index);}
    public String getCloneid(int index) {return (String)cloneidList.get(index);}
    public String getNextForward() {return nextForward;}
    public Researcher getResearcherObject() {return r;}
    public FormFile getFilename() {return filename;}
    
    public void setCloneValues(Vector samples) {
        pcrResultList = new ArrayList();
        floResultList = new ArrayList();
        proteinResultList = new ArrayList();
        restrictionResultList = new ArrayList();
        colonyResultList = new ArrayList();
        authorList = new ArrayList();
        dateList = new ArrayList();
        statusList = new ArrayList();
        commentsList = new ArrayList();
        cloneidList = new ArrayList();
        
        for(int i=0; i<samples.size(); i++) {
            ExpressionCloneSample s = (ExpressionCloneSample)samples.get(i);
            pcrResultList.add(s.getPcrresult());
            floResultList.add(s.getFloresult());
            proteinResultList.add(s.getProteinresult());
            restrictionResultList.add(s.getRestrictionresult());
            colonyResultList.add(s.getColonyresult());
            authorList.add(s.getAuthor());
            dateList.add(s.getStartdate());
            statusList.add(s.getClonestatus());
            commentsList.add(s.getComments());
            cloneidList.add((new Integer(s.getCloneid())).toString());
        }
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        well = false;
        sampleid = false;
        geneSymbol = false;
        pa = false;
        sgd = false;
        masterClone = false;
        researcher = false;
        restriction = false;
        pcr = false;
        colony = false;
        florescence = false;
        protein = false;
        status = false;
        date = false;
        sourcePlate = null;
        newPlate = null;
        researcherBarcode = null;
        filename = null;
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
        
        if ((newPlate == null) || (newPlate.trim().length() < 1)) {
            errors.add("newPlate", new ActionError("error.plate.invalid.master", newPlate));
        } 
        
        return errors;        
    }       
}
