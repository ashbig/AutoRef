/*
 * EnterSrcForSequencingForm.java
 *
 * Created on January 20, 2004, 1:33 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class EnterSrcForSequencingForm extends ProjectWorkflowForm {
    protected String plate1 = null;
    protected String plate2 = null;
    protected String plate3 = null;
    protected String plate4 = null;
    protected String plate5 = null;
    protected String plate6 = null;
    protected String plate7 = null;
    protected String plate8 = null;
    protected String row = "both";
    protected String researcher = null;
    protected int researcherid;
    protected String isMappingFile = "Yes";
    protected boolean isA = false;
    protected boolean isB = false;
    protected boolean isC = false;
    protected boolean isD = false;
    protected boolean isE = false;
    protected boolean isF = false;
    protected boolean isG = false;
    protected boolean isH = false;
    
    /** Creates a new instance of EnterSrcForSequencingForm */
    public EnterSrcForSequencingForm() {
    }   
    
    public String getPlate1() {return plate1;}
    public String getPlate2() {return plate2;}
    public String getPlate3() {return plate3;}
    public String getPlate4() {return plate4;}
    public String getPlate5() {return plate5;}
    public String getPlate6() {return plate6;}
    public String getPlate7() {return plate7;}
    public String getPlate8() {return plate8;}
    public String getRow() {return row;}
    public String getResearcher() {return researcher;}
    public int getResearcherid() {return researcherid;}
    public String getIsMappingFile() {return isMappingFile;}
    public boolean getIsA() {return isA;}
    public boolean getIsB() {return isB;}
    public boolean getIsC() {return isC;}
    public boolean getIsD() {return isD;}
    public boolean getIsE() {return isE;}
    public boolean getIsF() {return isF;}
    public boolean getIsG() {return isG;}
    public boolean getIsH() {return isH;}
    
    public void setPlate1(String plate1) {this.plate1 = plate1;}
    public void setPlate2(String plate2) {this.plate2 = plate2;}
    public void setPlate3(String plate3) {this.plate3 = plate3;}
    public void setPlate4(String plate4) {this.plate4 = plate4;}
    public void setPlate5(String plate5) {this.plate5 = plate5;}
    public void setPlate6(String plate6) {this.plate6 = plate6;}
    public void setPlate7(String plate7) {this.plate7 = plate7;}
    public void setPlate8(String plate8) {this.plate8 = plate8;}
    public void setRow(String row) {this.row = row;}
    public void setResearcher(String researcher) {this.researcher = researcher;}
    public void setResearcherid(int researcherid) {this.researcherid = researcherid;}
    public void setIsMappingFile(String s) {this.isMappingFile = s;}
    public void setIsA(boolean b) {this.isA = b;}
    public void setIsB(boolean b) {this.isB = b;}
    public void setIsC(boolean b) {this.isC = b;}
    public void setIsD(boolean b) {this.isD = b;}
    public void setIsE(boolean b) {this.isE = b;}
    public void setIsF(boolean b) {this.isF = b;}
    public void setIsG(boolean b) {this.isG = b;}
    public void setIsH(boolean b) {this.isH = b;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        plate1=null;
        plate2=null;
        plate3=null;
        plate4=null;
        plate5=null;
        plate6=null;
        plate7=null;
        plate8=null;
        row="both";
        researcher = null;
        isMappingFile = "Yes";
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
        if ((plate1 == null) || (plate1.trim().length() < 1))
            errors.add("plate1", new ActionError("error.plate1.required"));
        if ((researcher == null) || (researcher.trim().length() < 1))
            errors.add("researcher", new ActionError("error.researcherBarcode.required"));

        return errors;
    }
}
