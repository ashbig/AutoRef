<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Find Registration</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Find Registration</h2>
<hr>

<html:errors/>
<p>

<html:form action="FindRegistration.do" focus="firstName">
    <table width="80%" align="center">
        <tr>
        <td class="prompt">User Name:</td>
        <td><html:text property="userID" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Or Reminder Text:</td>
        <td><html:text property="reminderText" size="40"/></td>
        </tr>

        <tr>
        <td></td><td><html:submit property="submit" value="submit"/>&nbsp;&nbsp;&nbsp;<html:reset/></td>
        </tr>

    </table>

</html:form>

<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
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
 