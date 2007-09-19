<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head>
<title><bean:message key="flex.name"/> : <%= request.getAttribute("title") %><!--Master list of MGC clones: import information--></title>
</head>
<body>
<h2><bean:message key="flex.name"/> : <%= request.getAttribute("title") %><!--Master list of MGC clones: import information --></h2>
<hr>

<p><%= request.getAttribute("message") %>


</body>
</html>