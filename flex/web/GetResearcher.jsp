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
<html:form action="/GetResearcher.do" focus="researcherBarcode">
<input type="hidden" name="workflowid" value=<bean:write name="workflowid"/>>
<input type="hidden" name="projectid" value=<bean:write name="projectid"/>> 

<logic:present name="writeBarcode">
<input type="hidden" name="writeBarcode" value=<bean:write name="writeBarcode"/>> 
</logic:present>

<logic:present name="isMappingFile">
<input type="hidden" name="isMappingFile" value=<bean:write name="isMappingFile"/>> 
</logic:present>

<table>
    <tr>
    <td class="label">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="label">Protocol:</td>
    <td><bean:write name="EnterSourcePlateAction.subprotocol" property="name"/></td>
    </tr>

    <logic:iterate id="oldContainer" name="EnterSourcePlateAction.oldContainers">
    <tr>
    <td class="label">Source plate barcode:</td>
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="oldContainer" property="id"/>"><bean:write name="oldContainer" property="label"/></a></td>
    <td class="label">Source plate location:</td>
    <td><bean:write name="oldContainer" property="location.type"/></td>
    </tr>
    </logic:iterate> 

    <logic:iterate id="newContainer" name="EnterSourcePlateAction.newContainers">
    <tr>
    <td class="label">Destination plate barcode:</td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td class="label">Location:</td>
    <td><bean:write name="newContainer" property="location.type"/></td>
    </tr>
    </logic:iterate>  

</table>
    <Center>
    <p><em><bean:message key="flex.researcher.barcode.prompt"/></em>
    <html:password property="researcherBarcode" size="40"/>
    <p>
    <html:submit property="submit" value="Create Plate"/>
    </center>
</html:form>

</body>
</html>

