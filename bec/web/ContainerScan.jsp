<%@page contentType="text/html"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="bec.name"/> : Container Process History</title>
    
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<html:errors/>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Container Process History</font>
    <hr>
    
    <p>
    </td>
    </tr>
    <tr><td>
    <H3>Please Scan the Container</h3>
    <form action="logon.do" >
    <table>
        <tr>
            <td class="prompt">Label:</td>
            <td><input type="text" name="<%=Constants.CONTAINER_BARCODE_KEY%>"/></td>
        </tr>
    </table>
    <input type="SUBMIT"/>
    </td></tr>
    
    </form>
</body>
</html>
