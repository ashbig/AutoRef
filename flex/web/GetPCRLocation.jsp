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
<html:form action="/GetPCRLocation.do" focus="researcherBarcode">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Library:</b></td>
    <td><bean:write name="EnterOligoPlateAction.cdnaLibrary" /></td>
    </tr>

    <tr>
    <td><b>5P oligo plate barcode:</b></td>
    <td><bean:write name="EnterOligoPlateAction.fivep" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="fivepSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>3P open oligo plate barcode:</b></td>
    <td><bean:write name="EnterOligoPlateAction.threepOpen" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="threepOpenSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>3P closed oligo plate barcode:</b></td>
    <td><bean:write name="EnterOligoPlateAction.threepClosed" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="threepClosedSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Open PCR plate barcode:</b></td>
    <td><bean:write name="EnterOligoPlateAction.pcrOpen" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="pcrOpenLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td><b>Open PCR plate barcode:</b></td>
    <td><bean:write name="EnterOligoPlateAction.pcrClosed" property="label"/></td>
    <td><b>Location:</b></td>
    <td><html:select property="pcrClosedLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
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

