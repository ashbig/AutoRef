<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
</head>
<body>
<h2><bean:message key="flex.name"/> : Create Process Plate</h2>
<hr>
<html:errors/>
<p>
<logic:present name="SelectProtocolAction.queueItems">
<b>The following containers are available:</b>
<table>
<tr>
    <th>5P Oligo Plate</th><th>3P Open Oligo Plate</th><th>3P Fusion Oligo Plate</th>
</tr>

<logic:iterate id="queueItem" name="SelectProtocolAction.queueItems">
<tr>
    <td><bean:write name="queueItem" property="item.fivepContainer.label"/></td>
    <td><bean:write name="queueItem" property="item.threepOpenContainer.label"/></td>
    <td><bean:write name="queueItem" property="item.threepClosedContainer.label"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>


<logic:notPresent name="SelectProtocolAction.queueItems">
There are no items on the queue for this protocol.
</logic:notPresent>
</body>
</html>
