<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Plate Handling</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Plate Handling</h2>
<hr>
<html:errors/>
<p>
<html:form action="/SelectProtocol.do">
<logic:present name="projectid">
    <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
</logic:present>
<logic:present name="workflowid">
    <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
</logic:present>
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">



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
    <td class="prompt">Select the process name:</td>
    <td><html:select property="processname">
        <html:options
        collection="CreateProcessPlateAction.protocols"
        property="processname"
        labelProperty="processname"
        />
    </html:select>
    </td>

    <td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>