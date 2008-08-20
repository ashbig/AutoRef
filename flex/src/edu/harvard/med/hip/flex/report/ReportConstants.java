/*
 * ReportConstants.java
 *
 * Created on May 20, 2008, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;


import java.util.*;
/**
 *
 * @author htaycher
 */
public class ReportConstants 
{
    public enum ITEM_TYPE
    {
        Not_set("Not set"),
        PLATE_LABELS ("Plate labels") ,
        USER_PLATE_LABELS ("User plate labels") ,
          CLONE_ID ("Clone ID"),
          FLEXSEQUENCE_ID ("FLEX Sequence ID"),
          SAMPLE_ID ("Sample ID"),
          CLONE_NAME("Clone name");
          
           String  i_display_title;
           ITEM_TYPE(String dt){ i_display_title=dt;}
           public String getDisplayTitle(){ return i_display_title;}
    };
    
  
   
    
    public enum REPORT_COLUMN
    {
        //location 
        
        PLATE_LABEL ("Plate label","Plate label"),
        USER_PLATE_LABEL ("Submitter plate label", "Submitter plate label"),//if applicable
        WELL_NUMBER ("Position", "Position"),
        WELL_NAME ("Well name","Well name"),
        SAMPLE_ID ("Sample ID",  "SampleID"),
        SAMPLE_TYPE ("Sample type",  "S_Type"),
                
        //ref info
        FLEXSEQUENCE_ID ("Sequence ID", "FS: ID"),
        CDS_START ("CDS start", "FS: CDS start "),
        CDS_STOP("CDS Stop", "FS: CDS stop"),
        CDSLENGTH("CDS length", "FS: CDS length"),
        SPECIES("Species", "Species"),
        NAMES("All available identifiers", "FS: Identifiers"),
        CDS_TEXT("CDS", "FS: CDS"),
        FS_SEQUENCE("Sequence", "FS: Sequence"),
        FLEXSEQUENCE_STATUS("Sequence status", "FS: Status"),
        CONSTRUCT_TYPE("Version", "Construct"),
        
        
        // processing location
        PROJECT_NAME("Project", "Project"),
        WORKFLOW_NAME("Workflow", "Workflow"),
        QUEUE_PROTOCOL_NAME("Next protocol in processing", ""),
        //clone property
        CLONE_ID("Clone ID", "Clone ID"),
        CLONE_STATUS("Clone status", "CL: Status"),
        CLONE_NAMES("Clone Names", "CL: Identifiers"),
        CLONE_AUTHOR("Clone authors","CL: Authors"),
        CLONE_PUBLICATION("Clone publications","CL: Publications"),
        CLONE_SEQUENCE("Clone sequence text","CL: Sequence"),
        CLONE_S_LINKER_5P("Linker 5p discrepancies","CL: 5p discrepancies"),
        CLONE_S_LINKER_3P("Linker 3p discrepancies","CL: 3p discrepancies"),
        CLONE_S_CDS_START("CDS start","CL: CDS start"),
        CLONE_S_CDS_STOP("CDS stop","CL: CDS stop"),
        CLONE_S_CDS_LENGTH("CDS length","CL: CDS length"),
        CLONE_S_DISCREPANCY("Discrepancies","CL: CDS discrepancies"),
        
        //cloning strategy
        VECTOR("Vector", "Vector"),
        VECTOR_TYPE("Vector type", "V: Type"),
VECTOR_DESCRIPTION("Vector description", "V: Descripition"),
VECTOR_FEATURES("Vector features", "V: Features"),
        LINKER_3P_NAME("3p Linker name", "3p Linker name"),
        LINKER_3P_SEQUENCE("3p Linker sequence", "3p Linker sequence"),
        LINKER_5P_NAME("5p Linker name", "5p Linker name"),
        LINKER_5P_SEQUENCE("5p Linker sequence", "5p Linker sequence"),
   //     STRATEGY_RESTRICTION_GROUPS,
        STRATEGY_NAME("Cloning strategy name", "Cloning strategy name"),
        STRATEGY_ID("Cloning strategy id", "Cloning strategy id"),
         CLONE_NAME ("Clone name","Clone name"),
        STORAGE_TYPE ("Storage type","Storage type"),
         STORAGE_FORM("Storage form","Storage form"),
        CLONE_TYPE("Clone type","CL: Type"),
        MASTER_CLONE_ID("Master clone ID","Master clone ID")
        ;
        
        String   i_display_title;
        String   i_column_title ;
        
        REPORT_COLUMN( String title ,  String  column_title)
        {
            this.i_display_title = title;
            this.i_column_title = column_title;
            
        }
        public String getColumnDisplayTitle(){ return i_display_title;}
        public String getColumnTitle(){ return i_column_title;}
          
        
    
    }
    
  public static void main(String[] args) 
  {
      
      edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE st =
              edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.CLONE_RELATIONS;
       System.out.println(st.getFileNamePrefix());
       System.out.println(st.getReportPrefix());
       System.out.println(st.getReportTitle());
       
       
    System.out.println("lll"+Enum.valueOf(REPORT_COLUMN.class, "PLATE_LABEL"));
    System.out.println(REPORT_COLUMN.values()[3]);
    
    REPORT_COLUMN allapples[] = REPORT_COLUMN.values(); 
    for(REPORT_COLUMN a : allapples) 
    {
      System.out.println(a); 
      if (a.equals(REPORT_COLUMN.CLONE_S_CDS_START))
           System.out.println("equals "+a.getColumnDisplayTitle()); 
    }
    
    /* try
        {
            
            edu.harvard.med.hip.flex.util.FlexProperties sysProps =  ReportProperties.getInstance(  );
            ((ReportProperties)sysProps).verifyProperties();
            String[]   rep =        ((ReportProperties)sysProps).getColumnOrderForReport("G");
        }
        catch(Exception e ){}
 */
    System.out.println(); 
    
    // use valueOf() 
    REPORT_COLUMN ap = REPORT_COLUMN.valueOf("PLATE_LABEL"); 
    
    
    EnumMap<REPORT_COLUMN, String> antMessages =
      new EnumMap<REPORT_COLUMN, String>(REPORT_COLUMN.class);
 
    antMessages.put(REPORT_COLUMN.PLATE_LABEL,        "pl.");
    antMessages.put(REPORT_COLUMN.LINKER_3P_NAME,"ll");
    // Iterate and print messages
    for (REPORT_COLUMN status : REPORT_COLUMN.values() ) {
      System.out.println("For status " + status + ", message is: " +
                  antMessages.get(status));
    }
  }
    
}
