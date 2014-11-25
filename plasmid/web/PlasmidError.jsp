<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page isErrorPage="true"%>

<html>
<head><title> Error</title>
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">
<body>
    <h2>Error</h2>
    <hr>
    <html:errors/>
    <p>
    <h3>An error has occured and been logged</h3>
    <hr>

    <logic:present name="exception">
        <bean:write name="exception"/>
    </logic:present>
</body>
</div>
</html>
