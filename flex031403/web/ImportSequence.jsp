<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Import Sequences into FLEXGene</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Import Sequences into FLEXGene</h2>
<hr>
<html:errors/>
<p>
<html:form action="/ImportSequence.do" enctype="multipart/form-data">
<table>
<tr>
    <td class="prompt">Please select the sequence file:</td>
    <td><html:file property="sequenceFile" /></td>
</tr>
<tr>
    <td class="prompt">Please select the name file:</td>
    <td><html:file property="nameFile" /></td>
</tr>
</table>
<html:submit/>
</html:form>
</body>
</html:html>
