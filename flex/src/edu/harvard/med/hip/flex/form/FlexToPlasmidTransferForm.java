/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import java.util.*;

import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS;
import static edu.harvard.med.hip.flex.core.growthcondition.BioDefinitions.BIO_UNITS;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;
/**
 *
 * @author htaycher
 */
public class FlexToPlasmidTransferForm extends ActionForm
{
      private String forwardName = IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.toString();
      private boolean isCreateLogFile = false;
      private String   itemids = null;
      private String cloneStatus=PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.toString();
      private List plasmidCollections=null;
private List plasmidGrowthConditions=null;
private String[] plasmidRestrictions=null;
private String[] plasmidHostType=null;
private String[] plasmidMarkers=null;
private String[] plasmidHost=null;
private String[] fl_items=null;
 private String[] pl_items=null;
 

private String hoststrain ;
private String hoststrainIsInUse="Y";
private String hoststrainDescription;
private String hoststrain1 ;
private String hoststrainIsInUse1="N";
private String hoststrainDescription1;
private String restriction;
private String  marker;
private String hosttype;
private String  growthCondition1;
private boolean isRecomendedGC1;
private String  growthCondition2;
private boolean isRecomendedGC2;
private String[] cloneCollections=null;


private String flexSelectedItem;
private String plasmidSelectedItem;
      
      
      
      
      public String getForwardName() {        return forwardName;    }
      public void setForwardName(String b) {        forwardName = b;    }
      
      public boolean    getIsCreateLogFile(){ return isCreateLogFile;}
      public void       setIsCreateLogFile(boolean i){isCreateLogFile = i;}
      
      public String getItemids(){ return itemids;}
      public void setItemids(String v){ itemids = v;}
      
      public String     getCloneStatus(){ return cloneStatus;}
      public void   setCloneStatus(String v){ cloneStatus = v;}
    
 public void reset(ActionMapping mapping, HttpServletRequest request) {
        forwardName = IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.toString();
        isCreateLogFile= false;
        cloneStatus=PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.toString();
      
    }
 //verification rules
// clone collections not empty 
// restriction/marker/growth condition/host selected
 public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        IMPORT_ACTIONS cur_pr_type = IMPORT_ACTIONS.valueOf(forwardName);
        
        switch(cur_pr_type)
        {
            case CHANGE_CLONE_STATUS_SUBMITTED :
            {
                if ( itemids == null || itemids.trim().length()==0)
                {
                       errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No clone ID submitted"));
                 
                }
            }
        /*    case CREATE_SUBMISSION_FILES_SUBMITTED :
            {
                
                if ( cloneCollections == null ||
                         (cloneCollections.length==1 && 
                         cloneCollections[0].equalsIgnoreCase(BIO_UNITS.NONE.toString())))
                {
                     System.out.print("hhhh");
                       errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No collection specified"));
                 }
                if ( restriction == null ||
                         restriction.equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                {System.out.print("1hhhh"); errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No restriction specified"));}
                if ( marker == null || marker.equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                {System.out.print("2hhhh"); errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No marker specified"));  }
                 if ( hosttype == null ||  hosttype .equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                {System.out.print("3hhhh"); errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No host type specified"));  }
                 if ( growthCondition1 == null ||
                    growthCondition1 .equalsIgnoreCase(BIO_UNITS.NONE.toString()))
                { System.out.print("4hhhh");errors.add("error_parameter_invalid", new ActionError("error.parameter.invalid","No growth condition specified"));  }
       
            }*/
           /* case IMPORT_CLONING_STRATEGIES:
            {
                 {
                    if (inputFile.getFileName() == null || inputFile.getFileName().trim().length() < 1)
                    {
                        errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));
                    }
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
        if ( errors.size() > 0 ) forwardName = cur_pr_type.getPreviousProcess().toString();
        return errors;
    }        
    
 
 
 public List getPlasmidCollections   (){ return plasmidCollections;}
public List getPlasmidGrowthConditions  (){ return plasmidGrowthConditions;}
public String[] getPlasmidRestrictions   (){ return plasmidRestrictions;}
public String[] getPlasmidHostType   (){ return plasmidHostType;}
public String[] getPlasmidMarkers   (){ return plasmidMarkers;}
public String[] getPlasmidHost   (){ return plasmidHost;}
public String[] getFlexItems(){ return fl_items;}
public String[]  getPlasmidItems(){ return        pl_items;}
 

public void setPlasmidCollections   (List v){   plasmidCollections=v;}
public void setPlasmidGrowthConditions  (List v){   plasmidGrowthConditions=v;}
public void setPlasmidRestrictions   (String[] v){   plasmidRestrictions=v;}
public void setPlasmidHostType   (String[] v){   plasmidHostType=v;}
public void setPlasmidMarkers   (String[] v){   plasmidMarkers=v;}
public void setPlasmidHost   (String[] v){   plasmidHost=v;}
public void setFlexItems  (String[] v){fl_items=v;}
public void setPlasmidItems  (String[] v){pl_items=v;}
 
//--------------- item to get from user -----------------
public String getHoststrain(){ return hoststrain ;}
public String getHoststrainIsInUse(){ return hoststrainIsInUse;}
public String getHoststrainDescription(){ return hoststrainDescription;}
      
public void     setHoststrain(String v){   hoststrain =v ;}
public void     setHoststrainIsInUse(String v){  hoststrainIsInUse = v;}
public void     setHoststrainDescription(String v){  hoststrainDescription = v;}
      
public String getHoststrain1(){ return hoststrain1 ;}
public String getHoststrainIsInUse1(){ return hoststrainIsInUse1;}
public String getHoststrainDescription1(){ return hoststrainDescription1;}
      
public void     setHoststrain1(String v){   hoststrain1 =v ;}
public void     setHoststrainIsInUse1(String v){  hoststrainIsInUse1 = v;}
public void     setHoststrainDescription1(String v){  hoststrainDescription1 = v;}

public String   getRestriction(){ return restriction;}
public String   getMarker(){ return marker;}
public String  getGrowthCondition1(){ return growthCondition1;}
public boolean  getIsRecomendedGC1(){ return isRecomendedGC1;}
public String   getGrowthCondition2(){ return growthCondition2;}
public boolean  getIsRecomendedGC2(){ return isRecomendedGC2;}
public String[]     getCloneCollections(){ return cloneCollections;}
public String   getHosttype(){ return hosttype;}

public void     setRestriction(String v){ restriction=v;}
public void     setMarker(String v){ marker=v;}
public void     setGrowthCondition1(String v){  growthCondition1=v;}
public void     setIsRecomendedGC1(boolean v){   isRecomendedGC1=v;}
public void     setGrowthCondition2(String v){  growthCondition2=v;}
public void     setIsRecomendedGC2(boolean v){   isRecomendedGC2=v;}
public void     setCloneCollections(String[] v){   cloneCollections=v;}
public void setHosttype(String v){ hosttype = v;}
//---------------------------------------------------

public  String getFlexSelectedItem(){ return flexSelectedItem;}
public  String getPlasmidSelectedItem(){ return plasmidSelectedItem;}

public  void setFlexSelectedItem(String v){  flexSelectedItem =v;}
public  void setPlasmidSelectedItem(String v){ plasmidSelectedItem=v;}
      


 
}
