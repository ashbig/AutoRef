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
<html:form action="/GetAgarLocation.do">
<table>
    <tr>
    <td class="label">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="label">Protocol:</td>
    <td><bean:write name="EnterSourcePlateAction.subprotocol" property="description"/></td>
    </tr>

    <tr>
    <td class="label">Fusion agar plate barcode:</td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateF1" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="agarF1Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td class="label">Closed agar plate barcode:</td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateC1" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="agarC1Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td class="label">Fusion agar plate barcode:</td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateF2" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="agarF2Location">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <tr>
    <td class="label">Closed agar plate barcode:</td>
    <td><bean:write name="EnterSourcePlateAction.agarPlateC2" property="label"/></td>
    <td class="prompt">Location:</td>
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
    <td class="label">Destination plate barcode:</td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td class="prompt">Location:</td>
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

