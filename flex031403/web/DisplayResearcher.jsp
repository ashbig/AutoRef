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

<html:form action="/PrintLabel.do" focus="researcherName"> 
<input type="hidden" name="label" value=<bean:write name="researcherBarcode"/>>
<h4>The following researcher has been added to the database successfully:</h4>

<p>
<table>
<tr>
    <td class="prompt">Researcher Name:</td>
    <td><bean:write name="researcherName"/></td>
</tr>
<tr>
    <td class="prompt">Researcher Barcode:</td>
    <td><bean:write name="researcherBarcode"/></td>
</tr>
<tr>
    <td></td>
    <td><html:submit value="Print Barcode"/></td>
</tr>
</table>
 
</html:form>

</body>
</html:html>

