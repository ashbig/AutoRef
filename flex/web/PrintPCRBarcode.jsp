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

<html:form action="/PrintPCRBarcode.do">
<Center>
<p><b>The following plate has been created:</b>
<p><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterOligoPlateAction.pcrOpen" property="id"/>"><bean:write name="EnterOligoPlateAction.pcrOpen" property="label"/></a>
<p><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="EnterOligoPlateAction.pcrClosed" property="id"/>"><bean:write name="EnterOligoPlateAction.pcrClosed" property="label"/></a>
<p><html:submit property="submit" value="Print Barcode"/></td>
</center>
</html:form>
</body>
</html>

