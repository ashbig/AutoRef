<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title><bean:message key="flex.name"/> : Login</title>
    </head>
    <body> 
    <center>
    <h1>Welcome to <bean:message key="flex.name"/></h1>
    <hr>
    <html:errors/>
    <p>
  


    <html:form action="logon.do" focus="username" target="_top">
        <table>
            <tr>
                <td><b>User Name:</b></td>
                <td><html:text property="username"/></td>
            </tr>
            <tr>
                <td><b>Password:</b></td>
                <td><html:password property="password"/></td>
            </tr>
        </table>
        <p>
        <html:submit property="submit" value="Submit"/>
    </html:form>
 
    <html:link forward="register" target="_top">Customer registration</html:link>
    <br>
    <html:link forward="findRegistration" target="_top">Forgot your password?</html:link>
</center>
</body>
</html>