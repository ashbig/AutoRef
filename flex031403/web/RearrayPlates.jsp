<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="RearrayPlates.do" focus="logFile" enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Process Rearray</h2>
    <hr>
    <html:errors/>
   
<logic:present name="projectid">
    <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
    <input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
</logic:present>
<logic:present name="workflowid">
    <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
    <input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowid")%>">
   <input type="hidden" name="workflowname" value="<%= request.getAttribute("processname")%>">
</logic:present>
<logic:present name="protocolid">
    
    <input type="hidden" name="protocolid" value="<bean:write name="protocolid"/>">
</logic:present>




<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>   
     <td class="prompt">Process name:</td>
    <td><bean:write name="processname" /></td>
    </tr>   
    <tr>
        <td class="prompt">Please enter the rearray file:</td>    
        <td><html:file property="mgcRequestFile" /></td>
    </tr>

 
    </table>
    <br>

    <html:submit/>

</html:form>