<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page isErrorPage="true"%>

<html>
<head><title><bean:message key="flex.name"/> : Find Registration Confirmation</title></head>
<body>
    <h2><bean:message key="flex.name"/> : Find Registration Confirmation</h2>
    <hr>
    <html:errors/>
    <p>
    <h3>The information for your registration has been sent to you through email.</h3>
<a href="login.jsp">Login to <bean:message key="flex.name"/></a>
</body>
</html> 
