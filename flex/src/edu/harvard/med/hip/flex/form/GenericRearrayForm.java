/*
 * GenericRearrayForm.java
 *
 * Created on June 3, 2003, 3:02 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;

/**
 *
 * @author  dzuo
 */
public class GenericRearrayForm extends ActionForm {
    public static final String REARRAYSAMPLE = "rearraySample";
    public static final String REARRAYCLONE = "rearrayClone";
    
    protected String rearrayType = REARRAYSAMPLE;
    protected String fileFormat = "format1";
    protected String plateFormat = "label";
    protected String wellFormat = "number";
    protected String destWellFormat = "number";
    protected String plateType = "96 Well Plate";
    protected String sampleType = null;
    protected String location = Location.UNAVAILABLE;
    protected int project;
    protected int workflow;
    
    protected boolean isArrangeBySize = false;
    protected boolean isSmall = false;
    protected boolean isMedium = false;
    protected boolean isLarge = false;
    protected int lower = GenericRearrayer.SIZELOWER;
    protected int upper = GenericRearrayer.SIZEHIGHER;
    protected boolean isArrangeByFormat = false;
    protected boolean isControl = false;
    protected boolean isFullPlate = false;
    protected boolean isOligo = false;
    protected String isNewOligo = "false";
    protected boolean isSourceDup = false;
    
    protected String oligoFormat = "Both";
    protected FormFile inputFile = null;
    protected int sortBy = GenericRearrayer.SORT_BY_NONE;
    protected String output = "morefile";
    protected String researcherBarcode = null;
    //protected String rearrayTarget = null;
    protected String sourceFormat = null;
    protected String userEmail = null;
    protected String rearrayOption = null;
    protected String dist = null;
    protected String projectName = null;
    protected String workflowName = null;
    
    /** Creates a new instance of GenericRearrayForm */
    public GenericRearrayForm() {
    }
    
    public String getRearrayType() {return rearrayType;}
    public String getFileFormat() {return fileFormat;}
    public String getPlateFormat() {return plateFormat;}
    public String getWellFormat() {return wellFormat;}
    public String getDestWellFormat() {return destWellFormat;}
    public String getPlateType() {return plateType;}
    public String getSampleType() {return sampleType;}
    public String getLocation() {return location;}
    public int getProject() {return project;}
    public int getWorkflow() {return workflow;}
    public boolean getIsArrangeBySize() {return isArrangeBySize;}
    public boolean getIsSmall() {return isSmall;}
    public boolean getIsMedium() {return isMedium;}
    public boolean getIsLarge() {return isLarge;}
    public int getLower() {return lower;}
    public int getUpper() {return upper;}
    public boolean getIsArrangeByFormat() {return isArrangeByFormat;}
    public boolean getIsControl() {return isControl;}
    public boolean getIsFullPlate() {return isFullPlate;}
    public boolean getIsOligo() {return isOligo;}
    public String getIsNewOligo() {return isNewOligo;}
    public String getOligoFormat() {return oligoFormat;}
    public FormFile getInputFile() {return inputFile;}
    public int getSortBy() {return sortBy;}
    public String getOutput() {return output;}
    public String getResearcherBarcode() {return researcherBarcode;}
   // public String getRearrayTarget() {return rearrayTarget;}
    public String getUserEmail() {return userEmail;}
    public String getRearrayOption() {return rearrayOption;}
    public String getSourceFormat() {return sourceFormat;}
    public String getDist() {return dist;}
    public String getProjectName() {return projectName;}
    public String getWorkflowName() {return workflowName;}
    public boolean getIsSourceDup() {return isSourceDup;}
    
    public void setRearrayType(String s) {this.rearrayType = s;}
    public void setFileFormat(String s) {this.fileFormat = s;}
    public void setPlateFormat(String s) {this.plateFormat = s;}
    public void setWellFormat(String s) {this.wellFormat = s;}
    public void setDestWellFormat(String s) {this.destWellFormat = s;}
    public void setPlateType(String plateType) {this.plateType = plateType;}
    public void setSampleType(String sampleType) {this.sampleType = sampleType;}
    public void setLocation(String location) {this.location = location;}
    public void setProject(int project) {this.project = project;}
    public void setWorkflow(int workflow) {this.workflow = workflow;}
    public void setIsArrangeBySize(boolean b) {this.isArrangeBySize = b;}
    public void setIsSmall(boolean b) {this.isSmall = b;}
    public void setIsMedium(boolean b) {this.isMedium = b;}
    public void setIsLarge(boolean b) {this.isLarge = b;}
    public void setLower(int i) {this.lower = i;}
    public void setUpper(int i) {this.upper = i;}
    public void setIsArrangeByFormat(boolean b) {this.isArrangeByFormat = b;}
    public void setIsControl(boolean b) {this.isControl = b;}
    public void setIsFullPlate(boolean b) {this.isFullPlate = b;}
    public void setIsOligo(boolean b) {this.isOligo = b;}
    public void setIsNewOligo(String b) {this.isNewOligo = b;}
    public void setOligoFormat(String s) {this.oligoFormat = s;}
    public void setInputFile(FormFile f) {this.inputFile = f;}
    public void setSortBy(int i) {this.sortBy = i;}
    public void setOutput(String output) {this.output = output;}
    public void setResearcherBarcode(String researcherBarcode) {this.researcherBarcode = researcherBarcode;}
   // public void setRearrayTarget(String s) {this.rearrayTarget = s;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public void setRearrayOption(String rearrayOption) {this.rearrayOption = rearrayOption;}
    public void setSourceFormat(String s) {this.sourceFormat = s;}
    public void setDist(String s) {this.dist = s;}
    public void setProjectName(String s) {this.projectName = s;}
    public void setWorkflowName(String s) {this.workflowName = s;}
    public void setIsSourceDup(boolean b) {this.isSourceDup = b;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        rearrayType = "rearraySample";
        fileFormat = "format1";
        plateFormat = "label";
        wellFormat = "number";
        destWellFormat = "number";
        plateType = null;
        sampleType = null;
        location = Location.UNAVAILABLE;
        isArrangeBySize = false;
        isSmall = false;
        isMedium = false;
        isLarge = false;
        lower = GenericRearrayer.SIZELOWER;
        upper = GenericRearrayer.SIZEHIGHER;
        
       // if("storage".equals(rearrayTarget))
        //    isArrangeByFormat = true;
       // else
       //     isArrangeByFormat = false;
        
        isControl = false;
        isFullPlate = false;
        isOligo = false;
        isNewOligo = "false";
        oligoFormat = "Both";
        sortBy = GenericRearrayer.SORT_BY_NONE;
        output = "morefile";
        researcherBarcode = null;
        //rearrayTarget = "storage";
        sourceFormat = null;
        dist = null;
        projectName = null;
        workflowName = null;
        isSourceDup = false;
    }    
}
