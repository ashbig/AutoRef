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
<html:form action="/GetResearcher.do" focus="researcherBarcode">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Source plate barcode:</b></td>
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterSourcePlateAction.oldContainer" property="id"/>"><bean:write name="EnterSourcePlateAction.oldContainer" property="label"/></a></td>
    <td><b>Source plate location:</b></td>
    <td><bean:write name="EnterSourcePlateAction.oldContainer" property="location.type"/></td>
    </tr>

    <tr>
    <td><b>Destination plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.newContainer" property="label"/></td>
    <td><b>Destination plate location:</b></td>
    <td><bean:write name="EnterSourcePlateAction.newContainer" property="location.type"/></td>
    </tr>
</table>
    <Center>
    <p><b>Please enter your barcode:</b>
    <html:text property="researcherBarcode" size="40"/>
    <p>
    <html:submit property="submit" value="Create Plate"/>
    </center>
</html:form>

</body>
</html>

