<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create DNA from glycerol </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create DNA from glycerol</h2>
<hr>
<html:errors/>
<p>
<html:form action="/EnterSrcPlates.do" focus="sourcePlates">
<html:hidden property="projectid"/>
<html:hidden property="workflowid"/>
<html:hidden property="projectname"/>
<html:hidden property="workflowname"/>

<table>
 <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname"/></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname"/></td>
    </tr>
</table>

<p>
<table>
    <tr>
    <td colspan="2" class="prompt">Enter the barcode labels for all the source plates (separated by white space):</td>
    </tr>
    <tr>
    <td><html:textarea rows="10" cols="30" property="sourcePlates"/></td><td></td>
    </tr>

    <tr>
    <td><html:submit property="submit" value="Continue"/></td><td></td>
    </tr>
</table>
</html:form>

</body>
</html>

