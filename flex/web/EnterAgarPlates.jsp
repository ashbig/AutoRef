<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
<html:form action="/EnterAgarPlates.do" focus="agarPlateF1">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Enter the fusion agar plate barcode:</b></td>
    <td><html:text property="agarPlateF1" size="20"/></td>
    <td><b>Enter the corresponding closed agar plate barcode:</b></td>
    <td><html:text property="agarPlateC1" size="20"/></td>
    </tr>

    <tr>
    <td><b>Enter the fusion agar plate barcode:</b></td>
    <td><html:text property="agarPlateF2" size="20"/></td>
    <td><b>Enter the corresponding closed agar plate barcode:</b></td>
    <td><html:text property="agarPlateC2" size="20"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="QueueItemsDisplay.jsp" flush="true" />

</body>
</html>

