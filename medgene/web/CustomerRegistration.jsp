<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title>Member Registration</title>
</head>
<body>

<center>
<h1>Member Registration</h1>
</center>

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

</body>
</html>
