/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

/**
 *
 * @author htaycher
 */
 

 
 
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

import static edu.harvard.med.hip.flex.RearrayConstants.SAMPLE_TYPE;
import static edu.harvard.med.hip.flex.RearrayConstants.PLATE_TYPE;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS;
import static edu.harvard.med.hip.flex.core.growthcondition.BioDefinitions.BIO_UNITS;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;

import edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.PlasmIDFileType;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*;
/**
 *
 * @author htaycher
 */
public class FlexToPlasmidFileForm    extends FlexToPlasmidTransferForm 
{
   
    
    private String         plateType=PLATE_TYPE.PLATE_96_WELL.toString();
    private String         sampleType =SAMPLE_TYPE.WORKING_GLYCEROL.toString();
    private String          researcherBarcode;
    private String          tablename;
     private String itemType = edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE.ITEM_TYPE_PLATE_LABELS.toString();
   
    private String[]        displayData;
    DatabaseTable           displayTable ;
    
  /* private boolean isPutOnQueue = false;
   private boolean isDefineConstructSizeBySequence= false;
   private boolean isCheckTargetSequenceInFLEX= false;
   private boolean isFillInCLoneTables= false;
   private boolean isGetFLEXSequenceFromNCBI = false;
   private boolean isFLEXSequenceIDGI = false;
   private boolean isInsertControlNegativeForEmptyWell = false;*/
  
   private FormFile mapFile = null;
   private FormFile inputGene= null;
   private FormFile inputAuthor= null;
   private FormFile inputAuthorConnection = null;
    private FormFile inputPublication= null;
   private FormFile inputPublicationConnection = null;
 
   private FormFile    AUTHOR;
   private FormFile CLONEAUTHOR ;
   private FormFile PUBLICATION;
   private FormFile   CLONEPUBLICATION ;
   private FormFile CLONE ;
   private FormFile PLATE ;
   private FormFile REFSEQ;
   private FormFile INSERTREFSEQ ;
   private FormFile REFSEQNAME;
   private FormFile CLONESELECTION;
   private FormFile CLONEGROWTH ;
   private FormFile  CLONEHOST ;
   private FormFile CLONENAME ;
   private FormFile CLONECOLLECTION ;
   private FormFile CLONEINSERT ;
   private FormFile CLONEPROPERTY ;
   private FormFile     INSERTPROPERTY ;
   private FormFile     CLONEINSERTONLY ;
   private FormFile CLONENAMETYPE;
   private FormFile GROWTHCONDITION;
   private FormFile  REFSEQNAMETYPE ;
   
   
   public String[]           getDisplayData(){ return displayData;}
   public void              setDisplayData(String[] v){ displayData = v;}
   public String            getResearcherBarcode(){ return researcherBarcode;}
   public void              setResearcherBarcode(String v){ researcherBarcode = v;}
   
    
    public void setItemType(String v){   itemType = v;}
     public String  getItemType(){   return itemType;}
  
   public String           getPlateType() {return plateType;}
    public void             setPlateType(String v) { plateType = v;}
    
    public String           getSampleType() {return sampleType;}
     public void            setSampleType(String v) { sampleType = v;}
   
      public String           getTablename() {return tablename;}
     public void            setTablename(String v) { tablename = v;}
   
     public DatabaseTable        getDisplayTable(){ return    displayTable;}
     public void                setDisplayTable(DatabaseTable   b){        displayTable=b;}
   
   ///  public String getLocation() {return location;}
   /* public boolean 		getIsPutOnQueue(){ return isPutOnQueue    ;}
    public boolean 		getIsDefineConstructSizeBySequence() { return isDefineConstructSizeBySequence   ;}
    public boolean 		getIsCheckTargetSequenceInFLEX(){ return isCheckTargetSequenceInFLEX   ;}
    public boolean 		getIsFillInCLoneTables(){ return isFillInCLoneTables   ;}
    public boolean 		getIsGetFLEXSequenceFromNCBI (){ return isGetFLEXSequenceFromNCBI;}
    public boolean 		getIsFLEXSequenceIDGI (){ return isFLEXSequenceIDGI;}
    public boolean                 getIsInsertControlNegativeForEmptyWell(){ return isInsertControlNegativeForEmptyWell ;}
    public int                  getSubmissionType(){ return submissionType;}
    */
   public FormFile	getAUTHOR   (){ return AUTHOR;}
public FormFile	getCLONEAUTHOR   (){ return CLONEAUTHOR ;}
public FormFile	getPUBLICATION   (){ return PUBLICATION;}
public FormFile	getCLONEPUBLICATION   (){ return CLONEPUBLICATION ;}
public FormFile	getCLONE   (){ return CLONE ;}
public FormFile	getPLATE   (){ return PLATE ;}
public FormFile	getREFSEQ   (){ return REFSEQ;}
public FormFile	getINSERTREFSEQ   (){ return INSERTREFSEQ ;}
public FormFile	getREFSEQNAME   (){ return REFSEQNAME;}
public FormFile	getCLONESELECTION   (){ return CLONESELECTION;}
public FormFile	getCLONEGROWTH   (){ return CLONEGROWTH ;}
public FormFile	getCLONEHOST   (){ return CLONEHOST ;}
public FormFile	getCLONENAME   (){ return CLONENAME ;}
public FormFile	getCLONECOLLECTION   (){ return CLONECOLLECTION ;}
public FormFile	getCLONEINSERT   (){ return CLONEINSERT ;}
public FormFile	getCLONEPROPERTY   (){ return CLONEPROPERTY ;}
public FormFile	getINSERTPROPERTY   (){ return INSERTPROPERTY ;}
public FormFile	getCLONEINSERTONLY   (){ return CLONEINSERTONLY ;}
public FormFile	getCLONENAMETYPE   (){ return CLONENAMETYPE;}
public FormFile	getGROWTHCONDITION   (){ return GROWTHCONDITION;}
public FormFile	getREFSEQNAMETYPE   (){ return REFSEQNAMETYPE ;}
/*
    public void             setIsPutOnQueue(boolean v){   isPutOnQueue    = v;}
    public void             setIsDefineConstructSizeBySequence(boolean v) {   isDefineConstructSizeBySequence   = v;}
    public void             setIsCheckTargetSequenceInFLEX(boolean v){   isCheckTargetSequenceInFLEX   = v;}
    public void             setIsFillInCLoneTables(boolean v){   isFillInCLoneTables   = v;}
    public void              setIsGetFLEXSequenceFromNCBI (boolean v){   isGetFLEXSequenceFromNCBI = v;}
   public void              setIsFLEXSequenceIDGI (boolean v){   isFLEXSequenceIDGI = v;}
  public void              setIsInsertControlNegativeForEmptyWell(boolean v){   isInsertControlNegativeForEmptyWell = v;}
    */  
 public void	setAUTHOR   (FormFile v){  AUTHOR= v;}
public void	setCLONEAUTHOR   (FormFile v){  CLONEAUTHOR = v;}
public void	setPUBLICATION   (FormFile v){  PUBLICATION= v;}
public void	setCLONEPUBLICATION   (FormFile v){  CLONEPUBLICATION = v;}
public void	setCLONE   (FormFile v){  CLONE = v;}
public void	setPLATE   (FormFile v){  PLATE = v;}
public void	setREFSEQ   (FormFile v){  REFSEQ= v;}
public void	setINSERTREFSEQ   (FormFile v){  INSERTREFSEQ = v;}
public void	setREFSEQNAME   (FormFile v){  REFSEQNAME= v;}
public void	setCLONESELECTION   (FormFile v){  CLONESELECTION= v;}
public void	setCLONEGROWTH   (FormFile v){  CLONEGROWTH = v;}
public void	setCLONEHOST   (FormFile v){  CLONEHOST = v;}
public void	setCLONENAME   (FormFile v){  CLONENAME = v;}
public void	setCLONECOLLECTION   (FormFile v){  CLONECOLLECTION = v;}
public void	setCLONEINSERT   (FormFile v){  CLONEINSERT = v;}
public void	setCLONEPROPERTY   (FormFile v){  CLONEPROPERTY = v;}
public void	setINSERTPROPERTY   (FormFile v){  INSERTPROPERTY = v;}
public void	setCLONEINSERTONLY   (FormFile v){  CLONEINSERTONLY = v;}
public void	setCLONENAMETYPE   (FormFile v){  CLONENAMETYPE= v;}
public void	setGROWTHCONDITION   (FormFile v){  GROWTHCONDITION= v;}
public void	setREFSEQNAMETYPE   (FormFile v){  REFSEQNAMETYPE = v;}    
    
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
         // PROCESS_NTYPE cur_pr_type = PROCESS_NTYPE.valueOf(this.getForwardName() );
      
      /* if ( cur_pr_type == PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX
                && (this.getInputFile() == null || this.getInputFile().getFileName().trim().length() < 1
            || mapFile == null || mapFile.getFileName().trim().length()<0))
        {
            errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));
        }
       */
        //verification rules
// clone collections not empty 
// restriction/marker/growth condition/host selected
 public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        IMPORT_ACTIONS cur_pr_type = IMPORT_ACTIONS.valueOf(this.getForwardName());
        
         switch(cur_pr_type)
        {
           
            case CREATE_SUBMISSION_FILES_SUBMITTED :
            {
                if (this.getPLATE().getFileName() == null || this.getPLATE().getFileName().trim().length() < 1)
                     {
                  //   System.out.print("hhhh");
                       errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","Map file not selected"));
                 }
            /*    if ( this.getCloneCollections()== null ||
                         (this.getCloneCollections().length==1 && 
                         this.getCloneCollections()[0].equalsIgnoreCase(BIO_UNITS.NONE.toString())))
                {
                  //   System.out.print("hhhh");
                       errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No collection specified"));
                 }*/
                if (this.getRestriction()  == null ||
                         this.getRestriction().equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                { errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No restriction specified"));}
                if ( this.getMarker() == null || this.getMarker().equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                { errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No marker specified"));  }
                 if ( this.getHosttype() == null ||  this.getHosttype() .equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                {errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No host type specified"));  }
                 if ( this.getGrowthCondition1() == null ||
                    this.getGrowthCondition1() .equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                { errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No growth condition specified"));  }
       
            }
            case PLASMID_DICTIONARY_TABLE_POPULATE:
            {
                if (this.getPLATE().getFileName() == null || this.getPLATE().getFileName().trim().length() < 1)
                {
                       errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","Data submission file not selected"));
                 
                }
                break;
            }
          /*  case PUT_PLATES_FOR_SEQUENCING:
            {
                if ( plateLabels == null || plateLabels.trim().length() < 1
                        || facilityName == null || facilityName.trim().length() < 1)
                {
                    errors.add("mgcCloneFile", new ActionError("error.empty.fields.putplatesforsequencing.wrong"));
                }
                break;
            }*/
        }
         System.out.println("validation " +errors.size());
       
        if ( errors.size() > 0 ) this.setForwardName( cur_pr_type.getPreviousProcess().toString());
        return errors;
    }        
    
 
   
}
