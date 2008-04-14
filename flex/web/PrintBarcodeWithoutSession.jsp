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
<html:form action="/PrintBarcode.do">
<Center>
<h3>The following plate(s) have been created:</h3>
<logic:iterate id="container" name="containers">
<p><a href="ViewContainerDetails.do?<%= edu.harvard.med.hip.flex.Constants.CONTAINER_ID_KEY %>=<bean:write name="container" property="id"/>"><bean:write name="container" property="label"/></a>
</logic:iterate>
<p><html:submit property="submit" value="Reprint Barcode"/>
</center>
</html:form>

</body>
</html>

