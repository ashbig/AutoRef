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
    <h2><bean:message key="flex.name"/> : <%=  edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.GENERAL.getReportTitle()%> </h2>
<hr>
<html:errors/>  
    
<form action="ReportRun.do" method='POST' onSubmit="return validate_run_report(this);"> 
<input name="reportType" type="hidden" value="<%= edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.GENERAL %>" >
        
<table width="100%" border="0" cellspacing="2" cellpadding="2">
      <tr class='headerRow'> <td colspan=2 ><strong>Select items type:</strong></td>   </tr>

<tr>  <td width='40%'> 
<strong>        <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.PLATE_LABELS %>' checked ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.PLATE_LABELS.getDisplayTitle() %>
</strong></td> <td> 
<strong>       <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.FLEXSEQUENCE_ID %>'   ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.FLEXSEQUENCE_ID.getDisplayTitle() %>
</strong></td></tr>
<tr>  <td width='40%'> 
<strong><input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_ID %>'   ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_ID.getDisplayTitle() %>
</strong></td> <td >
<strong><input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.SAMPLE_ID %>' disabled ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.SAMPLE_ID.getDisplayTitle() %>
</strong></td></tr>
<tr>  <td width='40%'> 
<strong><input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.USER_PLATE_LABELS %>'   ><%= edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.USER_PLATE_LABELS.getDisplayTitle() %>
</strong></td> <td >
<strong>&nbsp;
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
    <td width="50%" bgColor="#e4e9f8" ><div align="center"><font color="000080"><strong>&nbsp;&nbsp;&nbsp;Sample 
        information: </strong></font></div></td>
    <td width="50%" bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Cloning strategy information:</strong></font></div></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.PLATE_LABEL%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.PLATE_LABEL.getColumnDisplayTitle() %></td> 
    <td >   <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STRATEGY_NAME%>"  > 
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.STRATEGY_NAME.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.USER_PLATE_LABEL%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.USER_PLATE_LABEL.getColumnDisplayTitle() %></td> 
    <td> <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.VECTOR%>"  >
         <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.VECTOR.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SAMPLE_ID%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SAMPLE_ID.getColumnDisplayTitle() %></td> 
    <td> <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_5P_NAME%>"  > 
   <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_5P_NAME.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SAMPLE_TYPE%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SAMPLE_TYPE.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_5P_SEQUENCE%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_5P_SEQUENCE.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NUMBER%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NUMBER.getColumnDisplayTitle() %></td>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_3P_NAME%>"  > 
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_3P_NAME.getColumnDisplayTitle() %></td>
  </tr>
<tr>
  <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NAME%>"  >
    <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.WELL_NAME.getColumnDisplayTitle() %></td>
  <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_3P_SEQUENCE%>"  > 
     <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.LINKER_3P_SEQUENCE.getColumnDisplayTitle() %></td>
  </tr>

  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Reference 
        sequence information:</strong></font></div></td>
    <td bgColor="#e4e9f8"><div align="center" ><font color="000080"><strong>Clone information:</strong></font></div></td>
  </tr>
 


  <tr>
    <td>   <input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_ID%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_ID.getColumnDisplayTitle() %> </td>
    
    <td ><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_ID%>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_ID.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_STATUS %>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FLEXSEQUENCE_STATUS.getColumnDisplayTitle() %></td>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_STATUS %>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_STATUS.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_START%>"  >     
     <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_START.getColumnDisplayTitle() %></td>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_NAMES%>"  > 
     <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_NAMES.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_STOP%>"  >    
    <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_STOP.getColumnDisplayTitle() %></td>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_AUTHOR%>"  >    
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_AUTHOR.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDSLENGTH%>"  >    
     <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDSLENGTH.getColumnDisplayTitle() %></td>
    <td>  <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_PUBLICATION%>"  >  
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_PUBLICATION.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td ><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FS_SEQUENCE%>"  >
 <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.FS_SEQUENCE.getColumnDisplayTitle() %></td>
      <td>  <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_SEQUENCE%>"  >  
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_SEQUENCE.getColumnDisplayTitle() %></td>
   </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_TEXT%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CDS_TEXT.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_CDS_STOP%>"  >  
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_CDS_STOP.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SPECIES%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.SPECIES.getColumnDisplayTitle() %> </td> 
    <td> <input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_CDS_STOP%>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_CDS_STOP.getColumnDisplayTitle() %></td>
  </tr>
  <tr>
    <td><input type="checkbox" name="selectedColumns"  value="<%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.NAMES%>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.NAMES.getColumnDisplayTitle() %></td> 
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_DISCREPANCY%>"  >
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_DISCREPANCY.getColumnDisplayTitle() %></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_LINKER_5P%>"  > 
       <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_LINKER_5P.getColumnDisplayTitle() %></td>
  </tr>
    <tr>
    <td>&nbsp;</td><td><input type="checkbox" name="selectedColumns"  value="<%=edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_LINKER_3P%>"  >
      <%= edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN.CLONE_S_LINKER_3P.getColumnDisplayTitle() %></td> 
    </tr>
  <tr>
     <td>&nbsp;</td> <td>&nbsp;</td></tr>
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