/*
 * AddNewPlatesForm.java
 *
 * Created on August 27, 2007, 4:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import edu.harvard.med.hip.flex.workflow.*;
/**
 *
 * @author htaycher
 */
public class AddNewPlatesFromFileForm    extends AddItemsForm 
{
   
    private int         m_projectid = -1;
   private String      m_projectname = null;
    private int         m_workflowid = -1;
    private String      m_workflowname = null;
    private int         m_number_of_wells=96;
   
    private String      m_processname = null;
    private int         m_processid = ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX;
    
    protected String plateType = "96 Well Plate";
    protected String sampleType = null;
    protected int plateLocation = Location.CODE_UNAVAILABLE;
    
 
   private boolean isPutOnQueue = false;
   private boolean isDefineConstructSizeBySequence= false;
   private boolean isCheckTargetSequenceInFLEX= false;
   private boolean isFillInCLoneTables= false;
   private boolean isGetFLEXSequenceFromNCBI = false;
   private boolean isFLEXSequenceIDGI = false;
   private boolean isInsertControlNegativeForEmptyWell = false;
  
   private FormFile mapFile = null;
   private FormFile inputGene= null;
   private FormFile inputAuthor= null;
   private FormFile inputAuthorConnection = null;
  
   private String       researcherBarcode = null;
   
   
   
   
   public String            getResearcherBarcode(){ return researcherBarcode;}
   public void              setResearcherBarcode(String v){ researcherBarcode = v;}
   
    public int         getLocation() {        return plateLocation;    }    
    public void        setLocation(int v) {        plateLocation = v;    }
   
   
   public String           getPlateType() {return plateType;}
    public void             setPlateType(String v) { plateType = v;}
    
    public String           getSampleType() {return sampleType;}
     public void            setSampleType(String v) { sampleType = v;}
   
   ///  public String getLocation() {return location;}
    public boolean 		getIsPutOnQueue(){ return isPutOnQueue    ;}
    public boolean 		getIsDefineConstructSizeBySequence() { return isDefineConstructSizeBySequence   ;}
    public boolean 		getIsCheckTargetSequenceInFLEX(){ return isCheckTargetSequenceInFLEX   ;}
    public boolean 		getIsFillInCLoneTables(){ return isFillInCLoneTables   ;}
    public boolean 		getIsGetFLEXSequenceFromNCBI (){ return isGetFLEXSequenceFromNCBI;}
    public boolean 		getIsFLEXSequenceIDGI (){ return isFLEXSequenceIDGI;}
    public boolean                 getIsInsertControlNegativeForEmptyWell(){ return isInsertControlNegativeForEmptyWell ;}

    public FormFile 		getMapFile(){ return mapFile    ;}
    public FormFile 		getInputGene(){ return inputGene   ;}
    public FormFile 		getInputAuthor(){ return inputAuthor   ;}
    public FormFile 		getInputAuthorConnection(){ return inputAuthorConnection;}
 

    public void             setIsPutOnQueue(boolean v){   isPutOnQueue    = v;}
    public void             setIsDefineConstructSizeBySequence(boolean v) {   isDefineConstructSizeBySequence   = v;}
    public void             setIsCheckTargetSequenceInFLEX(boolean v){   isCheckTargetSequenceInFLEX   = v;}
    public void             setIsFillInCLoneTables(boolean v){   isFillInCLoneTables   = v;}
    public void              setIsGetFLEXSequenceFromNCBI (boolean v){   isGetFLEXSequenceFromNCBI = v;}
   public void              setIsFLEXSequenceIDGI (boolean v){   isFLEXSequenceIDGI = v;}
  public void              setIsInsertControlNegativeForEmptyWell(boolean v){   isInsertControlNegativeForEmptyWell = v;}
       
   public void 		setMapFile(FormFile v){   mapFile    = v;}
    public void 		setInputGene(FormFile v){   inputGene   = v;}
    public void 		setInputAuthor(FormFile v){   inputAuthor   = v;}
    public void 		setInputAuthorConnection(FormFile v){  inputAuthorConnection = v;}

    
    public void         setWorkflowname(String name)    {        m_workflowname = name;    }
    public String       getWorkflowname()    {        return m_workflowname;    }
    
    public void         setProcessname(String name)    {        m_processname = name;    }
    public String       getProcessname()    {        return m_processname;    }
    
    public void         setProjectname(String name)    {     m_projectname = name;    }
    public String       getProjectname()    {        return m_projectname;    }
    
    public void         setProjectid(int projectid)    {      m_projectid = projectid;   }
    public int          getProjectid()    {    return m_projectid;    }
    
    public void         setWorkflowid(int workflowid)    {        m_workflowid = workflowid;    }
    public int          getWorkflowid()   {        return m_workflowid;    }
    
    public void         setProcessid(int processid)    {        m_processid = processid;    }
    public int          getProcessid()   {        return m_processid;    }
    
    public void         setNumberOfWells(int number_of_wells)    {        m_number_of_wells = number_of_wells;    }
    public int          getNumberOfWells()   {        return m_number_of_wells;    }

    
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
       if ( this.getForwardName() < 0 && (this.getInputFile() == null || this.getInputFile().getFileName().trim().length() < 1
        || mapFile == null || mapFile.getFileName().trim().length()<0))
        {
            errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));
        }
      return errors;
    }        
     
   
   
}
