<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head><title><bean:message key="flex.name"/>: Login</title></head>
    <body> <center>
    <h2>Welcome to FLEXGene Database</h2>
    <hr>

   <html:errors/>


    <form:form action="logon.do" focus="username" target="_top">
        <table>
            <tr>
                <td bgcolor="lightgrey"><b>User Name:</b></td>
                <td bgcolor="lightgrey"><form:text property="username"/></td>
            </tr>
            <tr>
                <td bgcolor="lightgrey"><b>Password:</b></td>
                <td bgcolor="lightgrey"><form:password property="password"/></td>
            </tr>
        </table>
        <p>
        <form:submit property="submit" value="Submit"/>
    </form:form>
 
    <html:link forward="register" target="_top">Customer registration</html:link>
    <br>
    <html:link forward="findRegistration" target="_top">Forget your password</html:link>
</center>
</body>
</html>