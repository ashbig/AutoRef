<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page isErrorPage="true"%>

<html>
<head><title>Error</title></head>
<body>
<center>
    <h1>METAGENE : Error</h1>
</center>

    <table width="80%" align="center" border="0">
    <html:errors/>
    <p>
    <h3>An error has occured and been logged</h3>
    <hr>

    <logic:present name="exception">
        <bean:write name="exception"/>
    </logic:present>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>
