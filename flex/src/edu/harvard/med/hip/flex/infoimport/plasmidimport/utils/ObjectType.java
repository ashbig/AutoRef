/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.utils;

import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.core.*;
import java.util.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;
import  edu.harvard.med.hip.flex.report.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*;
import static edu.harvard.med.hip.flex.RearrayConstants.PLATE_TYPE;
import static edu.harvard.med.hip.flex.RearrayConstants.SAMPLE_TYPE;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;

/**
 *
 * @author htaycher
 */
public class ObjectType {
    public enum OBJECT_TYPE
    {
      REFSEQUENCE,
      CLONE,
      VECTOR
    }
    
    public enum CLONE_PROPERTY
    {
        CLONETYPE,
        CLONEHIPNAME,
        VERIFIED,
        VERMETHOD,
        DOMAIN,
        SUBDOMAIN ,
        RESTRICTION,
        CLONEMAPFILENAME,
        NAME ,
        STATUS,
        SPECIALTREATMENT,
        AUTHOR
        
    }
    
    public enum REFSEQUENCE_PROPERTY
    {
        SPECIES,
        SPECIESCLONETYPE,
        CDS,
        NAME
    }
    
    
    public String getValueFromObject(String submission_param, 
            ImportClone clone,
            ImportFlexSequence refsequence,
            HashMap<String, FlexPlasmidMap> map)
             throws Exception
    {
        String retvalue="NA";CLONE_PROPERTY cp;REFSEQUENCE_PROPERTY refp;
         PublicInfoItem pitem ;
        String[] param_description=submission_param.split("\\.");
        if ( param_description.length < 2)
            throw new Exception ("Cannot define object type, please check batch submission properties file");
        try
        {
            if( OBJECT_TYPE.valueOf(param_description[0]) == OBJECT_TYPE.REFSEQUENCE)
            {
                 refp = REFSEQUENCE_PROPERTY.valueOf(param_description[1]);
                 switch(refp)
                 {
                     case SPECIES: return map.get( refsequence.getSpesies() ).getPlasmidName();
                     case    SPECIESCLONETYPE: return map.get( refsequence.getSpesies() ).getValue1();
                     case   NAME:
                     {
                         pitem = PublicInfoItem.getPublicInfoByName(param_description[2],refsequence.getPublicInfo());
                         return (pitem == null)? "NA" : pitem.getValue();
                     }
                     case CDS: return refsequence.getSequenceText().substring(refsequence.getCDSStart()-1, refsequence.getCDSStop());
                         
                 }
                 
            }
            else if( OBJECT_TYPE.valueOf(param_description[0]) == OBJECT_TYPE.CLONE)
            {
                 cp = CLONE_PROPERTY.valueOf(param_description[1]);
                 switch(cp)
                 {
                     case CLONEHIPNAME: 
                     {
                          pitem = PublicInfoItem.getPublicInfoByName(param_description[1],                                 clone.getPublicInfo());
                         return (pitem == null)? null : pitem.getValue();
                         
                     }
                     case   NAME:
                     {
                         pitem = PublicInfoItem.getPublicInfoByName(param_description[2],clone.getPublicInfo());
                         return (pitem == null)? "NA" : pitem.getValue();
                     }
                     
                 }
            }
            else if( OBJECT_TYPE.valueOf(param_description[0]) == OBJECT_TYPE.VECTOR)
            {

            }
            return retvalue;
       }catch(Exception e)
       {
           throw new Exception ("Cannot define object type, please check batch submission properties file");
       }
    }
        
     
    
      public String    getParamValue(String param_name, 
              HashMap<String, String>         flex_plasmid_submission_map,
              ImportFlexSequence refsequence,
              ImportClone clone,
              HashMap<String,FlexPlasmidMap> flexplasmid_map
              
              
              )
              throws Exception
      {
          return  getParamValue( param_name,  flex_plasmid_submission_map,
               refsequence, clone, flexplasmid_map, true);
      }
       public String    getParamValue(String param_name, 
              HashMap<String, String>         flex_plasmid_submission_map,
              ImportFlexSequence refsequence,
              ImportClone clone,
              HashMap<String,FlexPlasmidMap> flexplasmid_map,
              boolean isParamRequeired
              
              
              )
              throws Exception
       {
          String submission_param=flex_plasmid_submission_map.get(param_name);
          
          if (submission_param == null )
          {
              if ( isParamRequeired )
                throw new Exception("Parameter "+param_name+"  is not specifies");
              else 
                  return null;
          }
          if ( submission_param.startsWith("_"))
          {            return   submission_param.substring(1); }
          else//get value fr1om objects
          { 
              return getValueFromObject(submission_param, clone, refsequence,flexplasmid_map);
          }
       }    
       
       
       public static void main(String[] a)
       {
           String aa = "REFSEQUENCE.SPECIESCLONETYPE";
           String[] ww = aa.split("\\.");
           System.out.print(ww.length);
       }
    
}
