<%@ page language="java" %>
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
<html:form action="/PrintBarcode.do">
<Center>
<b>The following plate has been created:</b>
<logic:iterate id="newContainer" name="EnterSourcePlateAction.newContainers">
<p><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="newContainer" property="id"/>"><bean:write name="newContainer" property="label"/></a>
</logic:iterate>
<p><html:submit property="submit" value="Reprint Barcode"/></td>
</center>
</html:form>
</body>
</html>

