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

<html:hidden property="projectid" />
<html:hidden property="workflowid" />
<html:hidden property="forwardName" />

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>

    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>

</table>
<p><i>This page allows you to upload the MGC request  into the
database from <b>request file: <b>. 
The <b>request file <h3>must</h3></b> contains GI numbers only.
<b>User of the request:</b> the current user of the system.
GI numbers should be separated by delimiter. 
We currently support the following delimiters: !, tab (\t). 
<p>
Request upload can take some time, after request would be uploaded the
e-mail notification will be sent to the user.
</i>

<p>
<html:form action="/ImportMgcRequest.do" enctype="multipart/form-data"> 

<table>
<tr>
    <td class="prompt">Please select the request file:</td>
    <td><html:file property="mgcRequestFile" /></td>
    <td>[<a href="ViewMgcRequestSampleFile.jsp">sample file</a>]</td>
</tr>



</table>
<html:submit/>
</html:form>


</html:form>

</body>
</html>