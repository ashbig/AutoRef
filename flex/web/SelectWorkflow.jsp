<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Select Workflow</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Select Workflow</h2>      
<hr>
<html:errors/>
<p>
<html:form action="/SelectWorkflow.do">
<html:hidden property="projectid" />
<html:hidden property="forwardName" />
<html:hidden property="projectname" />

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>

    <tr>
    <td class="prompt">Select the workflow:</td>
    <td><html:select property="workflowid">
        <html:options
        collection="workflows"
        property="id"
        labelProperty="name"
        />
    </html:select>
    </td>

    <td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>