/*
 * RunReportAction.java
 *
 * Created on June 3, 2008, 11:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.user.*;
import  edu.harvard.med.hip.flex.form.*;
import  edu.harvard.med.hip.flex.report.*;
import  edu.harvard.med.hip.flex.infoimport.*;
import  edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;
/**
 *
 * @author htaycher
 */
public class ReportRunAction extends FlexAction {
    
     public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
         ActionErrors errors = new ActionErrors();
       
        try
        {
             User user = ((User)request.getSession().getAttribute(Constants.USER_KEY));
            AccessManager manager = AccessManager.getInstance();
            user.setUserEmail( manager.getEmail(user.getUsername()));
           String temp = (String)request.getParameter("reportType");
            REPORT_TYPE report_type = REPORT_TYPE.valueOf(temp);
            ReportRunner report_runner =new ReportRunner();
            report_runner.setUser(user);
            
            report_runner.setReportType(report_type);
            request.setAttribute("forwardName", String.valueOf( ConstantsImport.PROCESS_NTYPE.RUN_REPORT));
            REPORT_COLUMN selected_columns[]=null;
            if ( report_type != REPORT_TYPE.CLONING_STRATEGY)
            {
                 String items = ((ReportRunForm)form).getItems();
                temp = ((ReportRunForm)form).getItemType();
                ITEM_TYPE item_type =ITEM_TYPE.valueOf(temp);
           
                String tmp[] = ((ReportRunForm)form).getSelectedColumns();
                selected_columns = new REPORT_COLUMN[tmp.length]; 

                int count=0;
                for (String column_name : tmp )
                {
                    selected_columns[count++]=REPORT_COLUMN.valueOf(column_name);
                }
            
            
                report_runner.setInputData(item_type, items);
                 boolean mode =((ReportRunForm)form).getIsRemoveDuplicateRecords() ;
                report_runner.setIsRemoveDuplicateRecords(mode);
                //report_runner.setUserSelectedReportColumns(selected_columns);
            }
            else
            {
                selected_columns= CloningStrategyReport.REPORT_COLUMNS;
            }
            report_runner.setUserSelectedReportColumns(selected_columns);
            Thread  t = new Thread( report_runner);     
            t.start();
            return (mapping.findForward("success"));
        
          } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        
     }
    
}
