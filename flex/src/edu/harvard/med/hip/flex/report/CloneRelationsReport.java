/*
 * CloneRelationsReport.java
 *
 * Created on June 4, 2008, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;

import java.util.*;



/**
 *
 * @author htaycher
 */
public class CloneRelationsReport extends ReportDefinition
{
     private final REPORT_TYPE     REPORT_TYPE_FOR_THIS_REPORT = REPORT_TYPE.CLONE_RELATIONS;
    public static final REPORT_COLUMN[]     REPORT_COLUMNS = {
            REPORT_COLUMN.MASTER_CLONE_ID,
            REPORT_COLUMN.CLONE_ID,
            REPORT_COLUMN.CLONE_STATUS,
            REPORT_COLUMN.PLATE_LABEL ,
            REPORT_COLUMN.WELL_NUMBER,
            REPORT_COLUMN.CLONE_NAME,
            REPORT_COLUMN.STORAGE_TYPE ,
            REPORT_COLUMN.STORAGE_FORM,
            REPORT_COLUMN.CLONE_TYPE,      
            REPORT_COLUMN.FLEXSEQUENCE_ID ,
            REPORT_COLUMN.FLEXSEQUENCE_STATUS,
            REPORT_COLUMN.CONSTRUCT_TYPE,
            REPORT_COLUMN.STRATEGY_NAME,
            REPORT_COLUMN.VECTOR

     };
     
     private boolean isVector = false;
     private boolean isConstructType = false;
     private boolean isFlexSequenceStatus = false;
     
    public CloneRelationsReport()
    {
        m_report_type = REPORT_TYPE_FOR_THIS_REPORT;
    }
    
  
     protected  List<String[]>  getDataForReport(String sql_items,
             ITEM_TYPE items_type,             ReportProperties fr)
             
     {
         try
         {
         // get all master clone id
            String master_clone_id = getAllMasterCloneIDAsSQLString( sql_items,items_type);
         //get all data
             String sql = getMainQuerySql(master_clone_id, fr);
             List<String[]>   report_data =     UtilSQL.getDataFromDatabase( sql, m_user_report_columns.length);
             
            return report_data;
         }
         catch (Exception e)
         {
             this.getErrorMessages().add(e.getMessage());
            return null;
         }
     }
     
     
     protected void setUpReportCheckList()
    {
          for (REPORT_COLUMN column : m_user_report_columns)
        {
              switch (column )  
              {
                  case  VECTOR: case STRATEGY_NAME:{isVector=true;break;}
                  case CONSTRUCT_TYPE: {isConstructType=true;break;}
                  case FLEXSEQUENCE_STATUS:{ isFlexSequenceStatus=true;break;}
                 
              }
          }
         
     }
     
    
      
      private String            getAllMasterCloneIDAsSQLString( String sql_items, ITEM_TYPE items_type)
      throws Exception
      {
          String sql = "select '' || mastercloneid as item1 from clones where ";
          if (items_type.equals(ITEM_TYPE.CLONE_NAME) )
          {
              sql+= "clonename in ("+sql_items+")";
          }
          else if (items_type.equals(ITEM_TYPE.CLONE_ID))
          {
              sql+= "cloneid in ("+sql_items+")";
          }
          List<String[]> db_data = UtilSQL.getDataFromDatabase(sql, 1);
          ArrayList items = new ArrayList<String>(db_data.size());
          for (String[] record : db_data)
          {
              items.add(record[0]);
          }
          String result = UtilSQL.transferArrayOfItemsIntoSQLString( items, items_type);
          
          return result;
      }
    
  
      
      private String    getMainQuerySql(String master_clone_id, ReportProperties fr)
      {
          StringBuffer sql = new StringBuffer();
          
          REPORT_COLUMN[] user_column_in_order =  m_report_type.getUserSelectedReportColumnsSettingsDefinedOrder(
                  fr, m_report_type,   m_user_report_columns);
     
          int column_count = 1;String item_name ="";
       //   m_user_report_columns
         sql.append("select distinct ");
              for ( REPORT_COLUMN column  : user_column_in_order)
              {
                  if ( column  == null) continue;// 1 indexed
                  switch(  column)
                  {
case MASTER_CLONE_ID:
{item_name="item"+column_count++; sql.append(" '' || c.mastercloneid as "+item_name+",");break;}
case CLONE_ID:
{item_name="item"+column_count++; sql.append(" '' || c.cloneid as "+item_name+",");break;}
case CLONE_STATUS:
{item_name="item"+column_count++; sql.append(" c.status as "+item_name+",");break;}
case PLATE_LABEL :
{item_name="item"+column_count++; sql.append(" storagecontainerlabel as "+item_name+",");break;}
case WELL_NUMBER:
{item_name="item"+column_count++; sql.append(" storagecontainerposition as "+item_name+",");break;}
case CLONE_NAME:
{item_name="item"+column_count++; sql.append(" clonename as "+item_name+",");break;}
case STORAGE_TYPE :
{item_name="item"+column_count++; sql.append(" storagetype as "+item_name+",");break;}
case STORAGE_FORM:
{item_name="item"+column_count++; sql.append(" storageform as "+item_name+",");break;}
case CLONE_TYPE:
{item_name="item"+column_count++; sql.append(" c.clonetype as "+item_name+",");break;}      
case FLEXSEQUENCE_ID :
{item_name="item"+column_count++; sql.append(" '' || c.sequenceid as "+item_name+",");break;}
case FLEXSEQUENCE_STATUS:
{item_name="item"+column_count++; sql.append(" flexstatus as "+item_name+",");break;}
case CONSTRUCT_TYPE:
{item_name="item"+column_count++; sql.append("constructtype as "+item_name+",");break;}
case STRATEGY_NAME:
{item_name="item"+column_count++; sql.append("strategyname as "+item_name+",");break;}
case VECTOR:
{item_name="item"+column_count++; sql.append("vectorname as "+item_name+",");break;}
                  }
              }
          // cut last ,
              String query_fields = sql.toString();
              query_fields = query_fields.substring(0, query_fields.length() - 1);
           sql = new StringBuffer();sql.append(query_fields);
           
           if  ( ! isVector && !isConstructType && !isFlexSequenceStatus )
          {
              sql.append(" from clones c where mastercloneid in (");
              sql.append(master_clone_id);
              sql.append(")");
          }
          else 
          {   sql.append(" from clones c, cloningstrategy cs, constructdesign d, flexsequence f, clonestorage g ");
              sql.append(" where c.cloneid=g.cloneid and c.strategyid=cs.strategyid and d.constructid=c.constructid and f.sequenceid = c.sequenceid ");
              sql.append(" and mastercloneid in (");
              sql.append(master_clone_id);
              sql.append(")");
     
          }
          return sql.toString();
      }
}
