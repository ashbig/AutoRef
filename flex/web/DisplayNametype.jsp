<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Name Types</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Name Types</h2>
<hr>
<html:errors/>

<p>
<table width="70%" align="center">
<logic:iterate id="nametype" name="nametypes">
<tr><td><bean:write name="nametype" property="name"/></td></tr>
</logic:iterate>
</table>

</body>
</html:html>
