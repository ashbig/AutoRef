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
<html:form action="/EnterOligoPlates.do" focus="fivepPlate">
<table>
    <tr>
    <td><b>Process name:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Enter 5P oligo plate barcode:</b></td>
    <td><html:text property="fivepPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Enter 3P open oligo plate barcode:</b></td>
    <td><html:text property="threepOpenPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Enter 3P closed oligo plate barcode:</b></td>
    <td><html:text property="threepClosedPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Select protocol used for PCR reaction:</b></td>
    <td><html:select property="subProtocolName">
        <bean:define id="subprotocols" name="SelectProtocolAction.protocol" property="subprotocol"/>
        <html:options
        collection="subprotocols"
        property="name"
        labelProperty="description"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="PlatesetQueueItemsDisplay.jsp" flush="true" />

</body>
</html>

