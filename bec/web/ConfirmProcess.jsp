<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<html>
<head><title>
ConfirmProcess</title></head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><%=request.getAttribute( Constants.JSP_TITLE) %> </font>
    <hr>
    
    <p>
    </td>
    </tr>
<tr><td> <h3> <%=request.getAttribute( Constants.ADDITIONAL_JSP) %></h3></td></tr>
</table>
   
    
   
</body>
</html> 
