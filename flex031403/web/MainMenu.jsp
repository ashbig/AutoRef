
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>

<html>
<head><title>JSP Page</title></head>
<body>
<logic:present scope="session" name="user">
    Hello <bean:write name="USER" property="username"/>
</logic:present>
<struts:errors/>
</body>
</html>
