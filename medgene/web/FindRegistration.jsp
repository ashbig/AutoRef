<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title>Find Registration</title>
</head>
<body>

<center><h2>Find Registration</h2></center>
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

</body>
</html>
 