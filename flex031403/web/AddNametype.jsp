<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Add Name Type</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Add Name Type</h2>
<hr>
<html:errors/>

<p>
<html:form action="/AddNametype.do" focus="nametype"> 

<table>
<tr>
    <td class="prompt">Please enter the name type:</td>
    <td><html:text property="nametype" size="20"/></td>
    <td><html:submit/></td>
</tr>
</table>
</html:form>

</body>
</html:html>
