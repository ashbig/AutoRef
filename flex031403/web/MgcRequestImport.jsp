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


<html:form action="/MgcImportRequest.do" enctype="multipart/form-data"> 

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

<% 
Integer project_id = (Integer)request.getAttribute("projectid");
if ( project_id.intValue() == 3)//pseudonomas
{

%>
<P>This project does not have MGC workflow.

<%}
else
{%>
<p><i>This page allows you to upload the MGC request  into the
database from <b>request file</b>. 
The request file <b>must</b> contains GI numbers only.
GI numbers should be separated by delimiter. 
We currently support the following delimiters: !, tab (\t). 
<P><b>Request Owner</b>: 
the current user of the system would be considered the request owner.

<p>
The request uploading may take some time. The e-mail notification will be sent to you upon completion.
</i> 

<p>

<p>

<input name="projectid" type="hidden" value="<bean:write name="projectid" />" >
<input name="workflowid" type="hidden" value="<bean:write name="workflowid" />" >
<input name="projectname" type="hidden" value="<bean:write name="projectname" />" >
<input name="workflowname" type="hidden" value="<bean:write name="workflowname" />" >
<table>
<tr>
    <td class="prompt">Please select the request file:</td>
    <td><html:file property="mgcRequestFile" /></td>
    <td>[<a href="ViewMgcRequestSampleFile.jsp">sample file</a>]</td>
</tr>



</table>
<P>
<b>Blast Parameters<b>:
<table>
    <tr>
        <td >&nbsp;&nbsp;Required percent identity<td>
        <td align="right"><html:text property="requiredPercentIdentity" size="4" value="90" /></td>
    </tr>
    <tr>
        <td >&nbsp;&nbsp;Required cds length limit<td>
        <td align="right"><html:text property="requiredCdslengthLimit" size="4" value="70"/></td>
    </tr>
</table>

<P>
<table>
    <tr>
         <td class="prompt">Put sequences on queue for processing?<td>
        <td align="right">
            <html:radio property="isPutOnQueue" value="false"/>Yes
            <html:radio property="isPutOnQueue" value="true"/>No
        </td>
    </tr>
</table>

<P><P>
<html:submit/>


<%}
%>
</html:form>




</body>
</html>