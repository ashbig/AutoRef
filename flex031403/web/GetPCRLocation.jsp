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
<html:form action="/GetPCRLocation.do" focus="researcherBarcode">
<input type="hidden" name="workflowid" value=<bean:write name="workflowid"/>>
<input type="hidden" name="projectid" value=<bean:write name="projectid"/>> 
<table>
    <tr>
    <td class="label">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="label">PCR protocol:</td>
    <td><bean:write name="EnterOligoPlateAction.subprotocol" property="name"/></td>
    </tr>

    <tr>
    <td class="label">5P oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.fivep" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="fivepSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td class="label">3P fusion oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepOpen" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepOpenSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <logic:present name="EnterOligoPlateAction.threepClosed">
    <tr>
    <td class="label">3P closed oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepClosed" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepClosedSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:present>

    <tr>
    <td class="label">Fusion PCR plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrOpen" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="pcrOpenLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <logic:present name="EnterOligoPlateAction.pcrClosed">
    <tr>
    <td class="label">Closed PCR plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrClosed" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="pcrClosedLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:present>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>