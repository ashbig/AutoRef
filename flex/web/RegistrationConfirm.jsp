<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page isErrorPage="true"%>

<html>
<head><title><bean:message key="flex.name"/> Error</title></head>
<body>
    <h3>You have successfully registrated to FLEXGene.</h3>
    <html:errors/>
    <logic:exists name="exception">
        <bean:write name="exception"/>
    </logic:exists>

<p>
<a href="login.jsp">Login to FLEXGene</a>
</body>
</html>