<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Expression Clones </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Expression Clones </h2>
<hr>

<p>
<ul>
    <li><a href="EnterMasterPlateBarcode.jsp">Create Expression Plate</a>
    <li><a href="EnterExpressionPlateBarcode.jsp">Enter Expression Plate Result</a>
    <li><a href="GetProjects.do?forwardName=<%=Constants.CREATE_EXP_DNA%>">Create DNA from glycerol</a>
    <li><a href="GetProjects.do?forwardName=<%=Constants.PERIMETER_REARRAY%>">Cell Culture Perimeter Rearray For Expression Clones</a>
</ul>

</body>
</html>