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
<html:form action="/GetResearcher.do" focus="researcherBarcode">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <logic:iterate id="oldContainer" name="EnterSourcePlateAction.oldContainers">
    <tr>
    <td><b>Source plate barcode:</b></td>
    <td><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="oldContainer" property="id"/>"><bean:write name="oldContainer" property="label"/></a></td>
    <td><b>Source plate location:</b></td>
    <td><bean:write name="oldContainer" property="location.type"/></td>
    </tr>
    </logic:iterate> 

    <logic:iterate id="newContainer" name="EnterSourcePlateAction.newContainers">
    <tr>
    <td><b>Destination plate barcode:</b></td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td><b>Location:</b></td>
    <td><bean:write name="newContainer" property="location.type"/></td>
    </tr>
    </logic:iterate>  

</table>
    <Center>
    <p><b><bean:message key="flex.researcher.barcode.prompt"/>:</b>
    <html:text property="researcherBarcode" size="40"/>
    <p>
    <html:submit property="submit" value="Create Plate"/>
    </center>
</html:form>

</body>
</html>

