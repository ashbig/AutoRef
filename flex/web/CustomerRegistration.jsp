<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Customer Registration</title></head>
<body bgcolor="white">

<h2>FlexGene : Customer Registration</h2>
<hr>

<html:errors/>

<html:form action="CustomerRegistration.do" focus="firstName">
    <table>
        <tr>
        <td><b>First Name:</b></td>
        <td><html:text property="firstName" size="40"/></td>
        </tr>

        <tr>
        <td><b>Last Name:</b></td>
        <td><html:text property="lastName" size="40"/></td>
        </tr>

        <tr>
        <td><b>Streetline 1:</b></td>
        <td><html:text property="street1" size="40"/></td>
        </tr>

        <tr>
        <td><b>Streetline 2 (optional):</b></td>
        <td><html:text property="street2" size="40"/></td>
        </tr>

        <tr>
        <td><b>City:</b></td>
        <td><html:text property="city" size="40"/></td>
        </tr>

        <tr>
        <td><b>State (optional):</b></td>
        <td><html:text property="state" size="40"/></td>
        </tr>

        <tr>
        <td><b>Province (optional):</b></td>
        <td><html:text property="province" size="40"/></td>
        </tr>

        <tr>
        <td><b>Zip Code:</b></td>
        <td><html:text property="zipCode" size="40"/></td>
        </tr>

        <tr>
        <td><b>Country:</b></td>
        <td><html:text property="country" size="40"/></td>
        </tr>

        <tr>
        <td><b>Work Phone:</b></td>
        <td><html:text property="phone" size="40"/></td>
        </tr>

        <tr>
        <td><b>Email:</b></td>
        <td><html:text property="email" size="40"/></td>
        </tr>

        <tr>
        <td><b>Organization:</b></td>
        <td><html:text property="organization" size="40"/></td>
        </tr>

        <tr>
        <td><b>User Name:</b></td>
        <td><html:text property="userID" size="40"/></td>
        </tr>

        <tr>
        <td><b>Password:</b></td>
        <td><html:password property="password" size="40"/></td>
        </tr>

        <tr>
        <td><b>Re-enter password:</b></td>
        <td><html:password property="password2" size="40"/></td>
        </tr>

        <tr>
        <td><b>Reminder Text:</b></td>
        <td><html:text property="reminderText" size="40"/></td>
        </tr>

        <tr>
        <td></td><td><html:submit property="submit" value="Register"/>&nbsp;&nbsp;&nbsp;<html:reset/></td>
        </tr>

    </table>

</html:form>

<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>

</body>
</html>
