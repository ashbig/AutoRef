<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>gene sequence</title></head>
<body>

<p><font color="#003366"><b>Flex ID:</b></font> &nbsp;&nbsp;&nbsp;&nbsp; <bean:write name="flexid"/></p>
<p><font color="#003366"><b>Sequence:</b></font></p>
<p><bean:write name="sequence"/></p>


</body>
</html>
