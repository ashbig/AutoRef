<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Find Registration</title></head>
<body bgcolor="white">

<h2>FLEXGene : Find Registration</h2>
<hr>

<html:errors/>

<html:form action="FindRegistration.do" focus="firstName">
    <table>
        <tr>
        <td><b>User Name:</b></td>
        <td><html:text property="userID" size="40"/></td>
        </tr>

        <tr>
        <td><b>OR Reminder Text:</b></td>
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
 