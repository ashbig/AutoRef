<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Add New Researcher</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Add New Researcher</h2>
<hr>
<html:errors/>

<p>
<html:form action="/AddResearcher.do" focus="researcherName"> 

<table>
<tr>
    <td class="prompt">Please enter the researcher name (firstname lastname):</td>
    <td><html:text property="researcherName" size="20"/></td>
</tr>
<tr>
    <td class="prompt">Please enter the researcher barcode:</td>
    <td><html:text property="researcherBarcode" size="20"/></td>
</tr>
<tr>
    <td></td>
    <td><html:submit/></td>
</tr>
</table>
</html:form>

</body>
</html:html>

