<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>METAGENE : Login</title>
    </head>
    <body> 
    <center>
    <h1>Welcome to METAGENE</h1>
    <hr>
    <html:errors/>
    <p>
  

    <html:form action="logon.do" focus="username" target="_top">
        <table>
            <tr>
                <TD class="prompt">User Name:</TD>
                <td><html:text property="username"/></td>
            </tr>
            <tr> 
                <td class="prompt">Password:</td>
                <td><html:password property="password"/></td>
            </tr>
        </table>
        <p>
        <html:submit property="submit" value="Submit"/>
    </html:form>
 
</center>
</body>
</html>
