<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<html>
<head><title><bean:message key="flex.name"/> : Registration Confirmation</title></head>
<body>
    <h2><bean:message key="flex.name"/> : Registration Confirmation</h2>
    <hr>
    <html:errors/>
    <p>
    <h3>You have successfully registrated to FLEXGene.</h3>
<a href="login.jsp">Login to <bean:message key="flex.name"/></a>
</body>
</html>
 