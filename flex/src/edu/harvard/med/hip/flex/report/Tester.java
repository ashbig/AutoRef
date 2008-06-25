/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.user.*;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;
import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
/**
 *
 * @author htaycher
 */
public class Tester
{
 public static void main(String[] args) 
 {
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
            String items = "YOC006213 EGP003558 ";
              items = "123";
           // items="271557 271578    271591  271534  ";
            ITEM_TYPE item_type =ITEM_TYPE.valueOf("CLONE_ID");//FLEXSEQUENCE_ID");//PLATE_LABELS");//
            REPORT_TYPE report_type = REPORT_TYPE.valueOf("GENERAL");

           // String tmp[] = {"MASTER_CLONE_ID","CLONE_ID","STORAGE_TYPE","STORAGE_FORM","CLONE_TYPE","FLEXSEQUENCE_ID","FLEXSEQUENCE_STATUS","CONSTRUCT_TYPE","VECTOR","STRATEGY_NAME" };
        //   String tmp[] =  {"PLATE_LABEL", "USER_PLATE_LABEL", "WELL_NUMBER", "WELL_NAME", "SAMPLE_ID", "SAMPLE_TYPE", "FLEXSEQUENCE_ID", "CDS_START", "CDS_STOP", "CDSLENGTH", "SPECIES", "NAMES", "CDS_TEXT", "FS_SEQUENCE", "FLEXSEQUENCE_STATUS", "CONSTRUCT_TYPE", "PROJECT_NAME", "WORKFLOW_NAME",  "CLONE_ID", "CLONE_STATUS", "CLONE_NAMES", "CLONE_AUTHOR", "CLONE_PUBLICATION", "CLONE_SEQUENCE", "CLONE_S_LINKER_5P", "CLONE_S_LINKER_3P", "CLONE_S_CDS_START", "CLONE_S_CDS_STOP", "CLONE_S_DISCREPANCY", "VECTOR", "LINKER_3P_NAME", "LINKER_3P_SEQUENCE", "LINKER_5P_NAME", "LINKER_5P_SEQUENCE", "STRATEGY_NAME"};
        //  String tmp[] =  { "VECTOR", "LINKER_3P_NAME", "LINKER_3P_SEQUENCE", "LINKER_5P_NAME", "LINKER_5P_SEQUENCE", "STRATEGY_NAME"};
        //    String tmp[] =  {"FLEXSEQUENCE_ID", "CDS_START", "CDS_STOP", "CDSLENGTH", "SPECIES", "NAMES", "CDS_TEXT", "FS_SEQUENCE", "FLEXSEQUENCE_STATUS"};
          //  String tmp[] =  {"FLEXSEQUENCE_ID", "CDS_START", "CDS_STOP", "CDSLENGTH", "SPECIES", "NAMES", "CDS_TEXT", "FS_SEQUENCE", "FLEXSEQUENCE_STATUS", "CONSTRUCT_TYPE", "PROJECT_NAME", "WORKFLOW_NAME",  "CLONE_ID", "CLONE_STATUS", "CLONE_NAMES", "CLONE_AUTHOR", "CLONE_PUBLICATION", "CLONE_SEQUENCE", "CLONE_S_LINKER_5P", "CLONE_S_LINKER_3P", "CLONE_S_CDS_START", "CLONE_S_CDS_STOP", "CLONE_S_DISCREPANCY", "VECTOR", "LINKER_3P_NAME", "LINKER_3P_SEQUENCE", "LINKER_5P_NAME", "LINKER_5P_SEQUENCE", "STRATEGY_NAME"};
            String tmp[] =  {"PLATE_LABEL", "USER_PLATE_LABEL", "WELL_NUMBER", "WELL_NAME", "SAMPLE_ID", "SAMPLE_TYPE", "FLEXSEQUENCE_ID", "CDS_START", "CDS_STOP", "CDSLENGTH", "SPECIES", "NAMES", "CDS_TEXT", "FS_SEQUENCE", "FLEXSEQUENCE_STATUS", "CONSTRUCT_TYPE", "PROJECT_NAME", "WORKFLOW_NAME",  "CLONE_ID", "CLONE_STATUS", "CLONE_NAMES", "CLONE_AUTHOR", "CLONE_PUBLICATION", "CLONE_SEQUENCE", "CLONE_S_LINKER_5P", "CLONE_S_LINKER_3P", "CLONE_S_CDS_START", "CLONE_S_CDS_STOP", "CLONE_S_DISCREPANCY", "VECTOR", "LINKER_3P_NAME", "LINKER_3P_SEQUENCE", "LINKER_5P_NAME", "LINKER_5P_SEQUENCE", "STRATEGY_NAME"};
         
            REPORT_COLUMN selected_columns[] = new REPORT_COLUMN[tmp.length]; 

            int count=0;
            for (String column_name : tmp )
            {
               selected_columns[count++]=REPORT_COLUMN.valueOf(column_name);
            }
            ReportRunner report_runner =new ReportRunner();
            report_runner.setUser(user);
            report_runner.setInputData(item_type, items);
            report_runner.setReportType(report_type);
            report_runner.setUserSelectedReportColumns(selected_columns);
            report_runner.run();
      }
}