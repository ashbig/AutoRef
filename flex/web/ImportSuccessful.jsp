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

Import finished.
<p>
<table>
<tr>
    <td class="prompt">Total number of sequences for import:</td>
    <td><bean:write name="ImportSequenceAction.totalCount" /></td>
</tr>
<tr>
    <td class="prompt">Number of sequences imported successfully:</td>
    <td><bean:write name="ImportSequenceAction.successfulCount" /></td>
</tr>
<tr>
    <td class="prompt">Number of sequences failed import:</td>
    <td><bean:write name="ImportSequenceAction.failCount" /></td>
</tr>
</table>

<logic:present name="ImportSequenceAction.importResult">
<h4>List of sequences failed import:</h4>
<table width="80%" align="center">
    <logic:iterate id="logger" name="ImportSequenceAction.importResult">
    <logic:equal name="logger" property="successful" value="false">
    <tr>
        <td><bean:write name="logger" property="sequenceid" /></td>
        <td><bean:write name="logger" property="message" /></td>
    </tr>
    </logic:equal>
    </logic:iterate>
</table>
</logic:present>

<p>
<table>
<tr>
    <td class="prompt">Total number of requests for import:</td>
    <td><bean:write name="ImportSequenceAction.totalRequestCount" /></td>
</tr>
<tr>
    <td class="prompt">Number of requests imported successfully:</td>
    <td><bean:write name="ImportSequenceAction.successfulRequestCount" /></td>
</tr>
<tr>
    <td class="prompt">Number of requests failed import:</td>
    <td><bean:write name="ImportSequenceAction.failRequestCount" /></td>
</tr>
</table>

<logic:present name="ImportSequenceAction.importRequestResult">
<h4>List of requests failed import:</h4>
<table width="80%" align="center">
    <logic:iterate id="requestLogger" name="ImportSequenceAction.importRequestResult">
    <logic:equal name="requestLogger" property="successful" value="false">
    <tr>
        <td><bean:write name="requestLogger" property="sequenceid" /></td>
        <td><bean:write name="requestLogger" property="message" /></td>
        <td><bean:write name="requestLogger" property="username" /></td>
    </tr>
    </logic:equal>
    </logic:iterate>
</table>
</logic:present>

</body>
</html:html>
