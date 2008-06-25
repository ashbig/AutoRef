/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;

import edu.harvard.med.hip.flex.core.*;

import java.util.*;


/**
 *
 * @author htaycher
 */
public class CloningStrategyReport  extends ReportDefinition
{
   private final REPORT_TYPE     REPORT_TYPE_FOR_THIS_REPORT = REPORT_TYPE.CLONING_STRATEGY;
    public static final REPORT_COLUMN[]     REPORT_COLUMNS = {
            
            REPORT_COLUMN.VECTOR,
            REPORT_COLUMN.STRATEGY_NAME,
            REPORT_COLUMN.VECTOR_TYPE,
            REPORT_COLUMN.VECTOR_DESCRIPTION,
            REPORT_COLUMN.VECTOR_FEATURES,
            REPORT_COLUMN.LINKER_3P_NAME,
            REPORT_COLUMN.LINKER_3P_SEQUENCE,
            REPORT_COLUMN.LINKER_5P_NAME,
            REPORT_COLUMN.LINKER_5P_SEQUENCE

     };
    
    
    public CloningStrategyReport()
    {
        m_report_type = REPORT_TYPE_FOR_THIS_REPORT;
    }
    
  
     protected  List<String[]>  getDataForReport(String sql_items,
             ITEM_TYPE items_type,             ReportProperties fr)
             
     {
         try
         {
          //get all data
             List<CloningStrategy> cloning_strategies = CloningStrategy.getAllCloningStrategies(true, true);
            List<String[]>  report_data = convertReportDataToReqFormat(cloning_strategies, fr);
            return report_data;
         }
         catch (Exception e)
         {
             this.getErrorMessages().add(e.getMessage());
            return null;
         }
     }
     
     
     protected void setUpReportCheckList()    {}
     private List<String[]>   convertReportDataToReqFormat(
             List<CloningStrategy> cloning_strategies ,ReportProperties prop)
      {
          String[]  user_column_in_order =      m_report_type.getColumnOrder( prop);
          List<String[]> report_data = new ArrayList<String[]>(cloning_strategies.size());
         
          int column_count = 1;String[] record_for_cloning_strategy;
          
          for (CloningStrategy cloning_strategy : cloning_strategies)
          {
            record_for_cloning_strategy = new String[user_column_in_order.length];
            report_data.add(record_for_cloning_strategy);
            column_count = 0;
            for (String order_column : user_column_in_order)
            {
                  if ( order_column  == null) continue;// 1 indexed
                  switch(  REPORT_COLUMN.valueOf(order_column))
                  {
case VECTOR:{record_for_cloning_strategy[column_count++]= cloning_strategy.getClonevector().getName();break;}
case STRATEGY_NAME:{record_for_cloning_strategy[column_count++]= cloning_strategy.getName();break;}
case VECTOR_TYPE:{record_for_cloning_strategy[column_count++]= cloning_strategy.getClonevector().getType();break;}
case VECTOR_DESCRIPTION:{record_for_cloning_strategy[column_count++]= cloning_strategy.getClonevector().getType();break;}

case VECTOR_FEATURES:
{
     List features = cloning_strategy.getClonevector().getFeatures();
    VectorFeature feature; StringBuffer vector_features = new StringBuffer();
  
    for ( Iterator iter = features.iterator(); iter.hasNext(); )
    {
        feature = (VectorFeature) iter.next();
        vector_features.append("|"+ feature.getName()+":"+feature.getDescription()+";");
    }
    record_for_cloning_strategy[column_count++]= vector_features.toString();break;
}
case LINKER_3P_NAME:{record_for_cloning_strategy[column_count++]= cloning_strategy.getLinker3p().getName();break;}
case LINKER_3P_SEQUENCE:{record_for_cloning_strategy[column_count++]= cloning_strategy.getLinker3p().getSequence();break;}
case LINKER_5P_NAME:{record_for_cloning_strategy[column_count++]= cloning_strategy.getLinker5p().getName();break;}
case LINKER_5P_SEQUENCE:    {record_for_cloning_strategy[column_count++]= cloning_strategy.getLinker5p().getSequence();break;}
  
                  }
            }
          }
          // cut last ,
              return report_data;
      }
}
