<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<html>
<head>
<title><bean:message key="flex.name"/> : Print Confirmation</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Print Confirmation</h2>
<hr>
<html:errors/>
<p>

    <h3>Label Printed</h3>

</body>
</html>
