<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="flex.name"/> : Enter MGC Culture Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : Enter MGC Culture Results</h2>
<hr>
<html:errors/>
<p>

<logic:present name="<%=Constants.CONTAINER_KEY%>">
        <bean:write name="<%=Constants.CONTAINER_KEY%>" property="label"/> was processed successfully.
        <hr>
</logic:present>

</body>
</html>
