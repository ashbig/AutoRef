<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
<head><title>
<bean:message key="bec.name"/> : <bean:message key="title"/></title></head>
<body>
    <h2><bean:message key="bec.name"/> : <bean:message key="title"/></h2>
    <hr>
    <html:errors/>
    <p>
    <h3> <bean:message key="bec.message"/></h3>
</body>
</html> 
