<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate</title>
</head>
<body>
<hr>

<p>
<logic:present name="SelectProtocolAction.queueItems">

<b>The following containers are available:</b>
<table>
<tr>
    <th>ID</th><th>Label</th>
</tr>

<logic:iterate id="queueItem" name="SelectProtocolAction.queueItems">
<tr>
    <td><bean:write name="queueItem" property="item.id"/></td>
    <td><bean:write name="queueItem" property="item.label"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>


<logic:notPresent name="SelectProtocolAction.queueItems">
There are no items on the queue for this protocol.
</logic:notPresent>
</body>
</html>
