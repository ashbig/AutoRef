<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Member Registration</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Member Registration</h2>
<hr>
<html:errors/>
<p>

<html:form action="CustomerRegistration.do" focus="firstName">
    <table width="80%" align="center">
        <tr>
        <td class="prompt">First Name:</td>
        <td><html:text property="firstName" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Last Name:</td>
        <td><html:text property="lastName" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Streetline 1:</td>
        <td><html:text property="street1" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Streetline 2 (optional):</td>
        <td><html:text property="street2" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">City:</td>
        <td><html:text property="city" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">State (optional):</td>
        <td><html:text property="state" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Province (optional):</td>
        <td><html:text property="province" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Zip Code:</td>
        <td><html:text property="zipCode" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Country:</td>
        <td><html:text property="country" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Work Phone:</td>
        <td><html:text property="phone" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Email:</td>
        <td><html:text property="email" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Organization:</td>
        <td><html:text property="organization" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">User Name:</td>
        <td><html:text property="userID" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Password:</td>
        <td><html:password property="password" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Re-enter password:</td>
        <td><html:password property="password2" size="40"/></td>
        </tr>

        <tr>
        <td class="prompt">Reminder Text:</td>
        <td><html:text property="reminderText" size="40"/></td>
        </tr>

        <tr>
        <td></td><td><html:submit property="submit" value="Register"/>&nbsp;&nbsp;&nbsp;<html:reset/></td>
        </tr>

    </table>

</html:form>

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
