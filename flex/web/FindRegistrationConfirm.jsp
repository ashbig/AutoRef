<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
<head><title><bean:message key="flex.name"/> : Find Registration Confirmation</title></head>
<body>
    <h2><bean:message key="flex.name"/> : Find Registration Confirmation</h2>
    <hr>
    <html:errors/>
    <center>
    <p>
    <h3>The information for your registration has been sent to you through email.</h3>
<a href="login.jsp">Login to <bean:message key="flex.name"/></a>
    </center>

<center>
<table width="710" cellpadding="0" cellspacing="0" border="0">
<tr>
              <td height="23" align="center"><font size="2">This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a></font> </td>
            </tr>
</table>

<hr size=1 noshade>
<table width="710" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="middle"><font size=-2 face=arial color="#8d8d8d">Copyright &copy; 2001-2004 Harvard Institute of Proteomics. All rights reserved.<br>
		Last updated: July 1, 2004
		</td>
	</tr>
</table>
</center>
</body>
</html> 
