<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Query Clone Storage</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Query Clone Storage</h2>
<hr>
<html:errors/>

<p>
<html:form action="/QueryStorageCloneInput.do" focus="cloneidList" method="POST">

<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">
        Enter all the clone ID: (separated by spaces)
    </td>
    <td></td>
    </tr>

    <tr>
    <td><html:textarea property="cloneidList" rows="10"/>
    <td></td>
    </tr>
</table>

<table>
    <tr>
    <td><html:submit property="submit" value="Submit"/></td>
    <td><html:reset/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>