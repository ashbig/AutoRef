<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page isErrorPage="true"%>

<html>
<head><title><bean:message key="flex.name"/> : Error</title></head>
<body>
    <h2>METAGENE : Error</h2>
    <hr>
    <html:errors/>
    <p>
    <h3>An error has occured and been logged</h3>
    <hr>

    <logic:present name="exception">
        <bean:write name="exception"/>
    </logic:present>
</body>
</html>
