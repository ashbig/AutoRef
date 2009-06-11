<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions" %>
<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="../application_styles.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS.getMainPageTitle() %>
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS.getPageTitle() %></h2><hr>
<html:errors/>
 

<html:form action="/FlexToPlasmidTransfer.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS_SUBMITTED.toString() %>" >   

<table cellpadding="2" cellspacing="2">
<tr>
    <td class="prompt">Please submit clone ID:</td>
    <td>  <textarea rows=10  name="itemids"> </textarea></td>
    
</tr>
<tr>
    <td class="prompt">Please select clone status:</td>
    <td>  
        <html:radio property="cloneStatus" value="<%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.toString() %>" />
        <%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.getDisplayTitle() %>
<bR> <html:radio property="cloneStatus" value="<%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.NOT_READY_FOR_TRANSFER.toString() %>" />
        <%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.NOT_READY_FOR_TRANSFER.getDisplayTitle() %>

        </td>
    
</tr>
<tr>
    <td  colspan="2"><bR> <html:checkbox property="isCreateLogFile"><i>Create log file?</i></html:checkbox></td>
    
</tr>
<tr><td align="center" colspan="2"><html:submit/></td></tr>

</table>
<P><P>

</html:form>


</body>
</html:html>
