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
<html:form action="/GetLocation.do">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Source plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.oldContainer" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="sourceLocation">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <logic:iterate id="newContainer" name="EnterSourcePlateAction.newContainers">
    <tr>
    <td><b>Destination plate barcode:</b></td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="destLocations">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select>
    </td>
    </tr>
    </logic:iterate>    

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

