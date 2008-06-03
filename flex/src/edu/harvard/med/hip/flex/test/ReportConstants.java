/*
 * ReportConstants.java
 *
 * Created on May 20, 2008, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.test;


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
          SAMPLE_ID ("Sample ID");
          
           String  i_display_title;
           ITEM_TYPE(String dt){ i_display_title=dt;}
           public String getDisplayTitle(){ return i_display_title;}
    };
    
  
   
    
    public enum REPORT_COLUMN
    {
        //location 
        
        PLATE_LABEL ("Plate label","Plate label"),
        USER_PLATE_LABEL ("Submitter plate label", "Submitter plate label"),//if applicable
        WELL_NUMBER ("Position", ""),
        WELL_NAME ("Well name",""),
        SAMPLE_ID ("Sample ID",  ""),
        SAMPLE_TYPE ("Sample type",  ""),
                
        //ref info
        FLEXSEQUENCE_ID ("Sequence ID", ""),
        CDS_START ("CDS start", ""),
        CDS_STOP("CDS Stop", ""),
        CDSLENGTH("CDS length", ""),
        SPECIES("Species", ""),
        NAMES("All available identifiers", ""),
        CDS_TEXT("CDS", ""),
        FS_SEQUENCE("Sequence", ""),
        FLEXSEQUENCE_STATUS("Sequence status", ""),
        CONSTRUCT_TYPE("Construct type", ""),
        
        
        // processing location
        PROJECT_NAME("Project", ""),
        WORKFLOW_NAME("Workflow", ""),
        QUEUE_PROTOCOL_NAME("Next protocol in processing", ""),
        //clone property
        CLONE_ID("Clone ID", ""),
        CLONE_STATUS("Clone status", ""),
        CLONE_NAMES("Clone Names", ""),
        CLONE_AUTHOR("Clone authors",""),
        CLONE_PUBLICATION("Clone publications",""),
        CLONE_SEQUENCE("Clone sequence text",""),
        CLONE_S_LINKER_5P("Linker 5p discrepancies",""),
        CLONE_S_LINKER_3P("Linker 3p discrepancies",""),
        CLONE_S_CDS_START("CDS start",""),
        CLONE_S_CDS_STOP("CDS stop",""),
        CLONE_S_CDS_LENGTH("CDS length",""),
        CLONE_S_DISCREPANCY("Discrepancies",""),
        
        //cloning strategy
        VECTOR("Vector", ""),
        LINKER_3p_NAME("3p Linker name", ""),
        LINKER_3p_SEQUENCE("3p Linker sequence", ""),
        LINKER_5p_NAME("5p Linker name", ""),
        LINKER_5p_SEQUENCE("5p Linker sequence", ""),
   //     STRATEGY_RESTRICTION_GROUPS,
        STRATEGY_NAME("Cloning strategy name", "");
        
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
    antMessages.put(REPORT_COLUMN.LINKER_3p_NAME,"ll");
    // Iterate and print messages
    for (REPORT_COLUMN status : REPORT_COLUMN.values() ) {
      System.out.println("For status " + status + ", message is: " +
                  antMessages.get(status));
    }
  }
    
}
