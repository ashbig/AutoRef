
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>

<html>
<head><title>JSP Page</title></head>
<body>
<logic:present scope="session" name="<%=edu.harvard.med.hip.flex.Constants.USER_KEY%>" property="username">
    <bean:message key="flex.welcome"/> <bean:write name="<%=edu.harvard.med.hip.flex.Constants.USER_KEY%>" property="username"/>
</logic:present>
<struts:errors/>
</body>
</html>
