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
<html:form action="/GetPCRResearcher.do" focus="researcherBarcode">
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
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterOligoPlateAction.fivep" property="id"/>"><bean:write name="EnterOligoPlateAction.fivep" property="label"/></a></td>
    <td class="label">Location:</td>
    <td><bean:write name="EnterOligoPlateAction.fivep" property="location.type"/></td>
    </tr>

    <tr>
    <td class="label">3P open oligo plate barcode:</td>
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterOligoPlateAction.threepOpen" property="id"/>"><bean:write name="EnterOligoPlateAction.threepOpen" property="label"/></a></td>
    <td class="label">Location:</td>
    <td><bean:write name="EnterOligoPlateAction.threepOpen" property="location.type"/></td>
    </tr>

    <tr>
    <td class="label">3P closed oligo plate barcode:</td>
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterOligoPlateAction.threepClosed" property="id"/>"><bean:write name="EnterOligoPlateAction.threepClosed" property="label"/></a></td>
    <td class="label">Location:</td>
    <td><bean:write name="EnterOligoPlateAction.threepClosed" property="location.type"/></td>
    </tr>

    <tr>
    <td class="label">Open PCR plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrOpen" property="label"/></td>
    <td class="label">Location:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrOpen" property="location.type"/></td>
    </tr>

    <tr>
    <td class="label">Open PCR plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrClosed" property="label"/></td>
    <td class="label">Location:</td>
    <td><bean:write name="EnterOligoPlateAction.pcrClosed" property="location.type"/></td>
    </tr>
</table>

    <Center>
    <p><em><bean:message key="flex.researcher.barcode.prompt"/></em>
    <html:text property="researcherBarcode" size="40"/>
    <p>
    <html:submit property="submit" value="Create PCR Plates"/>
    </center>
</html:form>

</body>
</html>

