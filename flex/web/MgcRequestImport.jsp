<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Import MGC request</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Import MGC request</h2>
<hr>
<html:errors/>
<p>

<input name="projectid" type="hidden" value="<bean:write name="projectid" />" >
<input name="workflowid" type="hidden" value="<bean:write name="workflowid" />" >
<input name="projectname" type="hidden" value="<bean:write name="projectname" />" >
<input name="workflowname" type="hidden" value="<bean:write name="workflowname" />" >


<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>

    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>
</table>
<p><i>This page allows you to upload the MGC request  into the
database from <b>request file</b>. 
The request file <b>must</b> contains GI numbers only.
GI numbers should be separated by delimiter. 
We currently support the following delimiters: !, tab (\t). 
<b>Request User</b>: the current user of the system would be considered the request owner.

<p>
The request upload can take some time, after request would be uploaded the
e-mail notification will be sent to the user.
</i> 

<p>
<html:form action="/MgcImportRequest.do" enctype="multipart/form-data"> 
<p>
<table>
<tr>
    <td class="prompt">Please select the request file:</td>
    <td><html:file property="mgcRequestFile" /></td>
    <td>[<a href="ViewMgcRequestSampleFile.jsp">sample file</a>]</td>
</tr>



</table>
<html:submit/>
</html:form>




</body>
</html>