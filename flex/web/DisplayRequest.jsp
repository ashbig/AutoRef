<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Request</h2>
<hr>
<html:errors/>

<logic:present name="sequences" scope="request">
<p>
<table>
<tr>
    <td>Request Date:</td><td><bean:write name="customerRequestForm" property="requestDate"/></td>
</tr>
</table>

<p>
<table border="1" cellpading="2" cellspacing="0" width=90%>
<tr class="headerRow">
<th>FLEXGene ID</th><th>Description</th><th>Status</th>
</tr>
<logic:iterate id="sequence" name="sequences">
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="sequence" property="id"/>"><bean:write name="sequence" property="id"/></a></td>
<td><bean:write name="sequence" property="description"/></td>
<td><bean:write name="sequence" property="flexstatus"/></td>
</flex:row>
</logic:iterate>
</table>
</logic:present>

</body>
</html:html>
