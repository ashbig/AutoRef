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
<html:form action="/GetAgarLocation.do">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Fusion agar plate barcode:</b></td>
    <td><bean:write name="EnterAgarPlatesAction.agarF1" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarF1Location">
        <html:options
        collection="EnterAgarPlatesAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Closed agar plate barcode:</b></td>
    <td><bean:write name="EnterAgarPlatesAction.agarC1" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarC1Location">
        <html:options
        collection="EnterAgarPlatesAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Fusion agar plate barcode:</b></td>
    <td><bean:write name="EnterAgarPlatesAction.agarF2" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarF2Location">
        <html:options
        collection="EnterAgarPlatesAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Closed agar plate barcode:</b></td>
    <td><bean:write name="EnterAgarPlatesAction.agarC2" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="agarC2Location">
        <html:options
        collection="EnterAgarPlatesAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Culture block barcode:</b></td>
    <td><bean:write name="EnterAgarPlatesAction.newContainer" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="destLocation">
        <html:options
        collection="EnterAgarPlatesAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

