<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>FLEXGene : Create Process Plate </title>
</head>
<body bgcolor="white">

<h2>FlexGene : Create Process Plate</h2>
<hr>
<html:errors/>
<html:form action="/EnterSourcePlate.do" focus="sourcePlate">
<input type="hidden" name="page" value="1">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Enter the source plate barcode:</b></td>
    <td><html:text property="sourcePlate" size="40"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="QueueItemsDisplay.jsp" flush="true" />

</body>
</html>

