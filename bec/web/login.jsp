<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title><bean:message key="bec.name"/> : Login</title>
        <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
    </head>
    <body> 
    <center>
    <h1>Welcome to <bean:message key="bec.name"/></h1>
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
 
    <html:link forward="register" target="_top">Customer registration</html:link>
    <br>
    <html:link forward="findRegistration" target="_top">Forgot your password?</html:link>

<p>&nbsp;</p>
<p>&nbsp;</p>

</center>
</body>
</html>