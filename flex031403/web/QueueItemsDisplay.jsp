<%@ page language="java" %>
<%--<%@ page errorPage="ProcessError.do"%>--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<hr>

<p>
<logic:present name="SelectProtocolAction.queueItems">

<b>The following containers are available:</b>
<table border="1" cellpadding="2" cellspacing="0">
<tr class="headerRow">
    <th>ID</th><th>Label</th>
</tr>

<logic:iterate id="queueItem" name="SelectProtocolAction.queueItems">
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
    <td><bean:write name="queueItem" property="item.id"/></td>
    <td><bean:write name="queueItem" property="item.label"/></td>
</flex:row>
</logic:iterate>
</table>
</logic:present>


<logic:notPresent name="SelectProtocolAction.queueItems">
There are no items on the queue for this protocol.
</logic:notPresent>
</body>
</html>
