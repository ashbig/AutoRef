<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Add Name Type</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Add Name Type</h2>
<hr>
<html:errors/>

<p>The name type <b><bean:write name="nametype"/></b> cannot be added to
the database. Please try again later.

</body>
</html:html>