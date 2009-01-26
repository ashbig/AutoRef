/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport;

/**
 *
 * @author htaycher
 */
public class PlasmidImporterDefinitions 
{
     public enum IMPORT_ACTIONS
    {
        CREATE_SUBMISSION_FILES ("Create submission files",1, "Create submission files"),
        CREATE_SUBMISSION_FILES_SUBMITTED ("Create submission files",-1, "Create submission files"),
        
        
        UPLOAD_SUBMISSION_FILES ("Transfer clone information using files",1, "Transfer clone information using files"),
        TRANSFER_CLONE_INFORMATION ("Transfer clone information from FLEX to PLASMID",1, "Transfer clone information"),
        
        CHANGE_CLONE_STATUS ("Change clone status",2, "Set clone status"),
        CHANGE_CLONE_STATUS_SUBMITTED ("Change clone status",-2, "Set clone status"),
        
        PLASMID_DICTIONARY_TABLE_POPULATE ("Populate PLASMID dictionary table",3, "Populate PLASMID dictionary tables"),
        PLASMID_DICTIONARY_TABLE_SHOW_CONTENT ("PLASMID dictionary table content",3, "PLASMID dictionary table content"),
        PLASMID_DICTIONARY_TABLE_STRUCTURE ("PLASMID dictionary table structure",3, "PLASMID dictionary table structure"),
        
        
        
        CONNECT_PLASMID_FLEX_VECTOR_NAMES ("FLEX should be able to map vector definition in FLEX database to the one in PLASMID database. It is known that historicly some vector was named different. ",3, "Map vector  definitions between FLEX and PLAMID"),
       CONNECT_PLASMID_FLEX_AUTHOR ("FLEX should know ",3, "Map author  definitions between FLEX and PLAMID"),
       CONNECT_PLASMID_FLEX_AUTHOR_TYPE ("FLEX should know ",3, "Map author type definitions between FLEX and PLAMID"),
       CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED ("Map vector definitions in FLEX and PLAMID",3, "Map vector  definitions between FLEX and PLAMID"),
       CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED ("Map author  definitions in FLEX and PLAMID",3, "Map author  definitions between FLEX and PLAMID"),
       CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED ("Map author type definitions in FLEX and PLAMID",3, "Map author type definitions between FLEX and PLAMID"),
      
          CONNECT_PLASMID_FLEX_SPECIES ("FLEX should know ",3, "Map species definitions between FLEX and PLAMID"),
              CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED ("Map species definitions in FLEX and PLAMID",3, "Map species definitions between FLEX and PLAMID"),
        CONNECT_PLASMID_FLEX_NAMETYPE ("FLEX should know ",3, "Map name type definitions between FLEX and PLAMID"),
              CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED ("Map reference sequence name type definitions in FLEX and PLAMID",3, "Map reference sequence name type definitions between FLEX and PLAMID"),
              
        CONNECT_PLASMID_FLEX_CLONE_NAMETYPE ("FLEX should know ",3, "Map clone name type definitions between FLEX and PLAMID"),
              CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED ("Map clone name type definitions in FLEX and PLAMID",3, "Map clone name type definitions between FLEX and PLAMID"),
        CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE ("FLEX should know ",3, "Map clone property name type definitions between FLEX and PLAMID"),
              CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED ("Map clone property name type definitions in FLEX and PLAMID",3, "Map clone property name type definitions between FLEX and PLAMID")
              
        
      
        ;
        
        IMPORT_ACTIONS(String d, int ii, String vv)
        {i_display_title = d; i_group = ii; i_page_title = vv;}
        String i_display_title;
        String i_page_title;
        int    i_group=0;
        
        public String   getTitle(){ return i_display_title;}
        public String   getPageTitle(){ return i_page_title;}
        public int      getGroup(){ return i_group;}
        
        public String   getMainPageTitle(){ return "FLEX to PLASMID data transfer";}
        
        public IMPORT_ACTIONS   getNextProcess()
         {
             switch(this)
             {
                 case CREATE_SUBMISSION_FILES :return CREATE_SUBMISSION_FILES_SUBMITTED;
                 case         UPLOAD_SUBMISSION_FILES:
                 case TRANSFER_CLONE_INFORMATION:
                 case CHANGE_CLONE_STATUS : return CHANGE_CLONE_STATUS_SUBMITTED ;
                 case         PLASMID_DICTIONARY_TABLE_POPULATE :
                 case CONNECT_PLASMID_FLEX_VECTOR_NAMES:return CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_AUTHOR :return CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_AUTHOR_TYPE :return CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_SPECIES: return CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_NAMETYPE: return CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE: return CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED;
                 case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE: return CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED;
                 default: return null;
             }
        }
        
            public IMPORT_ACTIONS   getPreviousProcess()
           {
             switch(this)
             {
                 case CREATE_SUBMISSION_FILES_SUBMITTED :return CREATE_SUBMISSION_FILES; 
                 case         UPLOAD_SUBMISSION_FILES:
                 case TRANSFER_CLONE_INFORMATION:
                 case CHANGE_CLONE_STATUS_SUBMITTED  : return CHANGE_CLONE_STATUS;
                 case         PLASMID_DICTIONARY_TABLE_POPULATE :
                  case CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED:return CONNECT_PLASMID_FLEX_VECTOR_NAMES;
                 case CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED :return CONNECT_PLASMID_FLEX_AUTHOR;
                 case CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED :return CONNECT_PLASMID_FLEX_AUTHOR_TYPE;
                 case CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED: return CONNECT_PLASMID_FLEX_SPECIES;
                 case CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED: return CONNECT_PLASMID_FLEX_NAMETYPE;
                 case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED:return CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE;
                 case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED: return CONNECT_PLASMID_FLEX_CLONE_NAMETYPE;
                 default: return null;
             }
            }
    };
    
    
     public enum PLASMID_TRANSFER_CLONE_STATUS
    {
        READY_FOR_TRANSFER ("Allow clone information transfer",1, "",
        "Allow submission of plates with some clones not allowed for submission"),
        NOT_READY_FOR_TRANSFER ("Not allow clone information transfer",0, "",
         "Not allow submission of plates with some clones not allowed for submission (warning will be send about each clone that can not be transfered"),
       
        TRANSFER_FINISHED ("",2, "","")
        
            
        ;
        
        PLASMID_TRANSFER_CLONE_STATUS(String d, int ii, String vv,String gg)
        {i_display_title = d; i_int_value = ii; i_flex_db_clone_status = vv;
        i_display_plate_submission_rule=gg;}
        String i_display_title;
        String i_flex_db_clone_status;
        int    i_int_value=0;
        String i_display_plate_submission_rule ;
        
        public String   getDisplayTitle(){ return i_display_title;}
        public int      getIntValue(){ return i_int_value;}
        public String   getCloneDBStatusValue(){ return i_flex_db_clone_status;}
        public String   getDisplayPlateSubmissionRule(){ return i_display_plate_submission_rule;}
        
        
    };
    
    
    
}
