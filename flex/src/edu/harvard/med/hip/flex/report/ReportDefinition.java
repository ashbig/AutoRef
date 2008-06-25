/*
 * ReportDefinition.java
 *
 * Created on May 21, 2008, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.util.*;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
/**
 *
 * @author htaycher
 */
public abstract class ReportDefinition
{
    private int             m_user_report_coulumns = 0;
    protected REPORT_COLUMN[] m_user_report_columns = null;
    private String          m_report_header = null;
    private ArrayList<String>       m_error_messages = null;
    private ArrayList<String>         m_file_names_list_for_report =null;
    private ReportProperties          m_resource = null;
    protected  REPORT_TYPE          m_report_type ;
  
    
    public enum REPORT_TYPE
    {
         GENERAL ("General Report","GeneralReport","G", true),
         CLONE_RELATIONS("Clone relations report", "CloneRelation","C", true),
         CLONING_STRATEGY("Cloning Strategy report", "CloningStrategy","A", false);
        
         private String i_report_prexif;
         private String i_report_title;
         private String i_file_name_prefix ;
         private REPORT_COLUMN[] m_allowed_report_columns = null;
         private REPORT_COLUMN[]        m_user_column_in_order=null;
         private boolean i_isItemsRequered = true;
         
         REPORT_TYPE(String t, String p, String f, boolean isItemsRequered){this.i_report_title=t;this.i_file_name_prefix = p;this.i_report_prexif=f; this.i_isItemsRequered=isItemsRequered;}
         public String getReportTitle(){ return i_report_title;}
         public String getReportPrefix(){ return i_report_prexif;}
         public String  getFileNamePrefix(){ return i_file_name_prefix;}
         public boolean isItemsRequered(){ return i_isItemsRequered;}
         protected void    setAllowedReportColumns(REPORT_COLUMN[] t){m_allowed_report_columns = t;}
         public String[]              getColumnOrder(ReportProperties f)
         {
             return ( f.getInstance()).getColumnOrderForReport(i_report_prexif);
         }
         
         public REPORT_COLUMN[]      getUserSelectedReportColumnsSettingsDefinedOrder(
                 ReportProperties fr, REPORT_TYPE report_type, REPORT_COLUMN[] user_report_columns)
         {
             
             if ( m_user_column_in_order != null)
                 return m_user_column_in_order;
              String[] column_order = report_type.getColumnOrder(  fr);
        
             //connect m_user_report_columns and column order
              REPORT_COLUMN[] m_user_column_in_order = new REPORT_COLUMN[user_report_columns.length];
              int count=0;
              for (String order_column : column_order)
              {
                  for (REPORT_COLUMN user_column : user_report_columns)
                  {
                      if ( order_column == null)continue;
                      if (user_column.equals(REPORT_COLUMN.valueOf(order_column)))
                      {
                          m_user_column_in_order[count++]=user_column;
                          break;
                      }
                  }
              }
              return m_user_column_in_order;
         }
         
     };
    
    
   public  REPORT_TYPE              getReportType(){return m_report_type;}
   public ArrayList<String>         getErrorMessages(){ return m_error_messages;} 
   public void                      setResource( ReportProperties v){ m_resource = v;}
   protected ReportProperties       getResource(){ return m_resource;}   
   protected ArrayList<String>      getReportFileNames(){ return m_file_names_list_for_report;}
   protected abstract  List<String[]> getDataForReport(String sql_items, ITEM_TYPE items_type, ReportProperties fr);
   protected abstract void                   setUpReportCheckList();
   protected void                   setUserSelectedColumns(REPORT_COLUMN[] user_selected_columns ){ m_user_report_columns=user_selected_columns; }
   
   
    public  void             buildReport(String items,  ITEM_TYPE items_type )
   {
          FlexProperties fr = ReportProperties.getInstance();
          
     //     String[]   column_order_for_report =   m_report_type.getColumnOrder( this.getResource().getInstance());
          setUpReportCheckList();
         //build report
          ArrayList<String>sql_items =  UtilSQL.prepareItemsListForSQL( items_type,  items);
          boolean isHeader = true;List<String[]> report_data ;
          String report_file_name = this.getReportFileNames().get(0);
          if ( m_report_type.isItemsRequered())
          {
              for (String sql : sql_items)
              {
                  report_data = getDataForReport(sql,  items_type, (ReportProperties)fr);
                  printReport( report_file_name, report_data, (ReportProperties)fr, isHeader);
                  isHeader = false;
              }
          }
          else
          {
                  report_data = getDataForReport(null,  null, (ReportProperties)fr);
                  printReport( report_file_name, report_data, (ReportProperties)fr, isHeader);
                  isHeader = false;
          }
   }
          
    protected void        setReportFileNames( )
    {
         if (m_file_names_list_for_report == null)
        {
            m_file_names_list_for_report = new ArrayList<String>(1);
            String report_file_name =   FlexProperties.getInstance().getProperty("tmp")+ m_report_type.getFileNamePrefix()+ "_"+System.currentTimeMillis()+ ".txt";
            m_file_names_list_for_report.add(report_file_name);  
       }
        
    }       
   
   private String                   buildReportHeader(ReportProperties prop)
   {
       //  String[] column_order = m_report_type.getColumnOrder(  prop);
         REPORT_COLUMN[] user_report_columns_in_order = m_report_type.getUserSelectedReportColumnsSettingsDefinedOrder(
                  prop, m_report_type, m_user_report_columns);
         StringBuffer header = new StringBuffer();
         for ( REPORT_COLUMN column  : user_report_columns_in_order)
          {
              if ( column  == null) continue;// 1 indexed
              header.append(column.getColumnTitle() + "\t");
           }
           return header.toString();
   }
   
     protected void printReport(String report_file_name,List<String[]> sample_info, 
             ReportProperties prop, boolean isHeader)
    {
        String temp = null;
        FileWriter fr = null;
        try
        {
            
            fr =  new FileWriter(report_file_name, true);
            if (isHeader) 
            {
                String report_header = buildReportHeader(prop);
                fr.write(report_header+"\n");
            }        
            for( String[] record : sample_info )
            {
                for (String item : record )
                {
                    if ( item != null) fr.write(item+"\t");
                    else fr.write("\t");
                }
                fr.write( "\n");

            }
            fr.flush();
            fr.close();
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
   
    }
}
