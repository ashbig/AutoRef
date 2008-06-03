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
</body>
</html:html>