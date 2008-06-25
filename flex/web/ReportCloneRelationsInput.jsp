<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<!--< %@ page import="static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE" %>
 < %@ page import="static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE" %>
< %@ page import="static edu.harvard.med.hip.flex.report.ReportConstants.edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN" %>-->


<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>Report items</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>

<SCRIPT LANGUAGE="JavaScript" SRC="script.js"></SCRIPT>
</head>
<body>
    <h2><bean:message key="flex.name"/> : <%=  edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.CLONE_RELATIONS.getReportTitle()%> </h2>
<hr>
<html:errors/>  
    
<form action="ReportRun.do" onSubmit="return validate_run_report(this);"> 
<input name="reportType" type="hidden" value="<%= edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.CLONE_RELATIONS %>" >
        
<table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr class='headerRow'> <td colspan=2 ><strong>Select items type:</strong></td>   </tr>
<tr>  <td width='40%'> 

<strong>       <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_ID.toString() %>' checked ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_ID.getDisplayTitle() %>
</strong></td> <td> 
<strong>       <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_NAME.toString() %>'   ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_NAME.getDisplayTitle() %>
</strong></td></tr>
<tr class='headerRow'> <td colspan=2 ><strong>Enter all items:</strong></td>   </tr>
<tr> 
<td align="center" colspan=2><textarea name="items"  rows="10"></textarea></td>
</tr>
</table>
        
       <table width="100%" border="0" cellpadding="2" cellspacing="2">
  <tr> 
  
     
        
    <td colspan="2" bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select fields 
      to be reported:</strong></font></td>
  </tr>
  <tr> 
    <td width="50%" bgColor="#e4e9f8" ><div align="center"><font color="000080"><strong>&nbsp;&nbsp;&nbsp;Clone        information: </strong></font></div></td>
    <td width="50%" bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Clone location and storage information:</strong></font></div></td>
  </tr>
  
  <input type="hidden" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.MASTER_CLONE_ID.toString()%>"  name="selectedColumns">
  <input type="hidden" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_ID.toString()%>" name="selectedColumns" >
  
  <tr>
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.MASTER_CLONE_ID.toString()%>" checked name="selectedColumns" disabled>
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.MASTER_CLONE_ID.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.PLATE_LABEL.toString()%>" name="selectedColumns"  >
        <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.PLATE_LABEL.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_ID.toString()%>" checked name="selectedColumns" disabled>
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_ID.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NUMBER.toString()%>" name="selectedColumns"  >
        <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NUMBER.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_NAME.toString() %>" name="selectedColumns">
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_NAME.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STORAGE_TYPE.toString()%>" name="selectedColumns" >
        <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STORAGE_TYPE.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_TYPE.toString()%>" name="selectedColumns">
        <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_TYPE.getColumnDisplayTitle() %></td>
    <td>&nbsp;</td></tr>
  <tr>
    <td><input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_STATUS.toString() %>" name="selectedColumns">
        <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_STATUS.getColumnDisplayTitle() %></td>
    <td >&nbsp;</td>
  </tr>
  <tr>
    <td><input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STORAGE_FORM.toString() %>" name="selectedColumns">
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STORAGE_FORM.getColumnDisplayTitle() %></td>
    <td>&nbsp;</td>
  </tr>
<tr>
  <td><input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_ID.toString()%>" name="selectedColumns">
    <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_ID.getColumnDisplayTitle() %> </td>
  <td>&nbsp;</td>
  </tr>

  <tr> 
    <td><input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_STATUS.toString() %>" name="selectedColumns">
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_STATUS.getColumnDisplayTitle() %></td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td  >
      <input type="checkbox" value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CONSTRUCT_TYPE.toString() %>" name="selectedColumns">
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CONSTRUCT_TYPE.getColumnDisplayTitle() %>
      </td>
    <td   >&nbsp;</td>
  </tr>
 


  <tr>
    <td><input type="checkbox" value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.VECTOR.toString()%>" name="selectedColumns">
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.VECTOR.getColumnDisplayTitle() %></td>
    
    <td >&nbsp;</td>
  </tr>
</table>


<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
        
    </form>
</body>
</html:html>