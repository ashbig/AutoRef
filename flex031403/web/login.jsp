<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>

<html>
    <head><title>FLEX: Login</title></head>
    <body> <center>
    <h2>Welcome to FLEXGene Database</h2>
    <hr>
   <html:errors/>


    <form:form action="logon.do" focus="username">
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
</center>
</body>
</html>