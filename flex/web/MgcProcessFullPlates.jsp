<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Full Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Process full plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="MgcOrderOligo.do">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="processname" /></td>
    </tr>
    <tr>
    <td class="prompt">Number of clones:</td>
    <td><bean:write name="sequences_count" /></td>
    </tr>
<tr>
    <td class="prompt">Number of full plates:</td>
    <td><bean:write name="full_plates" /></td>
    </tr>

<logic:present name="wells_on_not_full_plate">
<tr>
    <td class="prompt">Number of clones on not full plate:</td>
    <td><bean:write name="wells_on_not_full_plate" /></td>
</tr>
<tr>   
    <td class="prompt">Is full plate required?</td>
    <td><html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
    </td>
</tr> 
</logic:present>
</table>

<p>
    <html:submit property="submit" value="Submit"/>
   

</html:form>

</body>
</html>