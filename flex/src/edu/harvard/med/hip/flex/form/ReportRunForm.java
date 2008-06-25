/*
 * ReportRunerForm.java
 *
 * Created on June 3, 2008, 12:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;
/**
 *
 * @author htaycher
 */
public class ReportRunForm  extends ActionForm
{
    private String[] selectedColumns = null;
    private String items = "";
    private String itemType = edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.PLATE_LABELS.toString();
    private String reportType= REPORT_TYPE.GENERAL.toString();


     public String[] getSelectedColumns() {   return this.selectedColumns;}
    public void setSelectedColumns(String[] selectedColumns) { this.selectedColumns = selectedColumns;
      }
     
     public String  getItems(){   return this.items;}
     public void setItems(String v){   this.items=v;}
     
     public void setItemType(String v){   itemType = v;}
     public String  getItemType(){   return itemType;}
     
     public void    setReportType(String  v){   reportType = v;}
     public         String getReportType(){  return reportType.toString();} 
     
    public void reset(ActionMapping mapping, HttpServletRequest request)     {
          itemType = ITEM_TYPE.CLONE_ID.toString() ;
        items="";
     }
    
}
