<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<hr>
<p>
<logic:present name="SelectProtocolAction.queueItems">
<h3>The following containers are available:</h3>
<TABLE border="1" cellpadding="2" cellspacing="0">
<tr class="headerRow">
    <th>5P Oligo Plate</th><th>3P Fusion Oligo Plate</th><th>3P Closed Oligo Plate</th>
    <logic:equal name="SelectProtocolAction.protocol" property="processname" value="Generate step1 PCR plates">
    <th>MGC Template Plate</th>
    </logic:equal>
</tr>

<logic:iterate id="queueItem" name="SelectProtocolAction.queueItems">
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
    <td><bean:write name="queueItem" property="item.fivepContainer.label"/></td>
    
    <logic:present name="queueItem" property="item.threepOpenContainer">
    <td><bean:write name="queueItem" property="item.threepOpenContainer.label"/></td>
    </logic:present>
     <logic:notPresent name="queueItem" property="item.threepOpenContainer">
    <td>&nbsp</td>
    </logic:notPresent>


    <logic:present name="queueItem" property="item.threepClosedContainer">
    <td><bean:write name="queueItem" property="item.threepClosedContainer.label"/></td>
    </logic:present>
    <logic:notPresent name="queueItem" property="item.threepClosedContainer">
    <td>&nbsp</td>
    </logic:notPresent>
    <logic:equal name="SelectProtocolAction.protocol" property="processname" value="Generate step1 PCR plates">
    <logic:present name="queueItem" property="item.mgcContainer">
    <td><bean:write name="queueItem" property="item.mgcContainer.label"/></td>
    </logic:present>
    </logic:equal>
</flex:row>
</logic:iterate>
</table>
</logic:present>


<logic:notPresent name="SelectProtocolAction.queueItems">
There are no items on the queue for this protocol.
</logic:notPresent>
</body>
</html>
