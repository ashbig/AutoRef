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
<html:form action="/GetAgarLocation.do">
<table>
    <tr>
    <td><b>Process name:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="EnterSourcePlateAction.subprotocol" property="description"/></td>
    </tr>

    <tr>
    <td><b>Fusion agar plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateF1" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarF1Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Closed agar plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateC1" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarC1Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Fusion agar plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateF2" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarF2Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Closed agar plate barcode:</b></td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateC2" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarC2Location">
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

