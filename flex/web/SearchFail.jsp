<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>

<p>
<h3>Search Parameters:</h3>
<table>
<logic:iterate name="params" id="param">
<tr>
    <td class="prompt"><small><bean:write name="param" property="name"/></small></td>
    <td><small><bean:write name="param" property="value"/></small></td>
</tr>
</logic:iterate>
</table>

<p>

<h3>Your search failed due to the following error:</H3>
<p><bean:write name="searchError"/>

<p>
<logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
<jsp:include page="footer.jsp"/>
</logic:notPresent>

</body>
</html>