<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Select Project</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Select Project</h2>
<hr>
<html:errors/>
<p>
<html:form action="/SelectProject.do">
<html:hidden property="forwardName" />
<table>
    <tr>
    <td class="prompt">Select the project:</td>
    <td><html:select property="projectid">
        <html:options
        collection="projects"
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