<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Process Plate</h2>
<hr>
<html:errors/>
<p>
<html:form action="/EnterColonyPlates.do" focus="agarPlateF1">
<html:hidden property="projectid" />
<html:hidden property="workflowid" />

<table>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="prompt">Select the protocol:</td>
    <td><html:select property="subProtocolName">
        <bean:define id="subprotocols" name="SelectProtocolAction.protocol" property="subprotocol"/>
        <html:options
        collection="subprotocols"
        property="name"
        labelProperty="name"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <logic:equal name="projectid" value="3">
    <td class="prompt">Enter the first fusion agar plate barcode:</td>
    </logic:equal>
    <logic:notEqual name="projectid" value="3">
    <td class="prompt">Enter the fusion agar plate barcode:</td>
    </logic:notEqual>
    <td><html:text property="agarPlateF1" size="20"/></td>
    <logic:equal name="projectid" value="3">
    <td class="prompt">Enter the second fusion agar plate barcode:</td>
    </logic:equal>
    <logic:notEqual name="projectid" value="3">
    <td class="prompt">Enter the corresponding closed agar plate barcode:</td>
    </logic:notEqual>
    <td><html:text property="agarPlateC1" size="20"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="QueueItemsDisplay.jsp" flush="true" />

</body>
</html>


