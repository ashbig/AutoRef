<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Export Container Details</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Export Container Details</h2>
<hr>
<html:errors/>

<html:form action="/SaveContainerInfo.do" method="POST" enctype="multipart/form-data"> 
<input type="hidden" name="id" value=<bean:write name="id"/>>
<input type="hidden" name="executionid" value=<bean:write name="executionid"/>>

<h3>Please check the fields to be exported:</h3>
<p><b>Sample Information:</b>
<table>
<tr>
    <td><html:checkbox property="sampleid" /></td>
    <td>Sample ID</td>
</tr>
<tr>
    <td><html:checkbox property="type" /></td>
    <td>Sample Type</td>
</tr>
<tr>
    <td><html:checkbox property="position" /></td>
    <td>Sample Position</td>
</tr>
<tr>
    <td><html:checkbox property="status" /></td>
    <td>Sample Status</td>
</tr>
<tr>
    <td><html:checkbox property="result" /></td>
    <td>Sample Result</td>
</tr>
<tr>
    <td><html:checkbox property="cloneid" /></td>
    <td>Clone ID</td>
</tr>
</table>
<p><b>Process Result Information:</b>
<table>
<tr>
    <td><html:checkbox property="pcr" /></td>
    <td>PCR Gel Results</td>
</tr>
<tr>
    <td><html:checkbox property="agar" /></td>
    <td>Agar Results</td>
</tr>
<tr>
    <td><html:checkbox property="culture" /></td>
    <td>Culture Results</td>
</tr>
</table>
<p><b>Sequence Information:</b>
<table>
<tr>
    <td><html:checkbox property="sequenceid" /></td>
    <td>Sequence ID</td>
</tr>
<tr>
    <td><html:checkbox property="cdsstart" /></td>
    <td>CDS Start</td>
</tr>
<tr>
    <td><html:checkbox property="cdsstop" /></td>
    <td>CDS Stop</td>
</tr>
<tr>
    <td><html:checkbox property="cdslength" /></td>
    <td>CDS Length</td>
</tr>
<tr>
    <td><html:checkbox property="gccontent" /></td>
    <td>GC Content</td>
</tr>
<tr>
    <td><html:checkbox property="sequencetext" /></td>
    <td>Sequence Text</td>
</tr>
<tr>
    <td><html:checkbox property="cds" /></td>
    <td>CDS</td>
</tr>
<tr>
    <td><html:checkbox property="gi" /></td>
    <td>GI Number</td>
</tr>
<tr>
    <td><html:checkbox property="genesymbol" /></td>
    <td>Gene Symbol</td>
</tr>
<tr>
    <td><html:checkbox property="panumber" /></td>
    <td>PA Number (for Pseudomonas project only)</td>
</tr>
<tr>
    <td><html:checkbox property="isEmpty" /></td>
    <td>Leave Sequence Info Empty for Empty Well</td>
</tr>
<tr>
    <td></td>
    <td><html:submit value="Export"/></td>
</tr>
</table>
</html:form>
    
</body>
</html:html>

