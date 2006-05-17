<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<html>
<head><title><bean:message key="bec.name"/> : Registration Confirmation</title></head>
<body>
    <h2><bean:message key="bec.name"/> : Registration Confirmation</h2>
    <hr>
    <html:errors/>
    <p>
    <h3>You have successfully registered to ACE.</h3>
<a href="login.jsp">Login to <bean:message key="bec.name"/></a>
</body>
</html>
 