<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<html>
<head>
<title><bean:message key="flex.name"/> : Cloning Request</title>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request : Sequence Selection</h2>
<hr>
<html:errors/>
<p>
No matching sequences found.

</body>
</html>
