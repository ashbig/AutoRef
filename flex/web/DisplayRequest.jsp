<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Request</h2>
<hr>
<html:errors/>

<logic:present name="sequences" scope="request">
<p>
<table>
<tr>
    <td><b>Request Date:</b></td><td><b><bean:write name="customerRequestForm" property="requestDate"/></b></td>
</tr>
</table>

<p>
<table border=1 align=center width=90%>
<th>FLEXGene ID</th><th>Description</th><th>Status</th>

<logic:iterate id="sequence" name="sequences">
<tr>
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="sequence" property="id"/>"><bean:write name="sequence" property="id"/></a></td>
<td><bean:write name="sequence" property="description"/></td>
<td><bean:write name="sequence" property="flexstatus"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>

</body>
</html:html>
