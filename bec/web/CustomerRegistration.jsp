<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="bec.name"/> : User Registration</title>
    <LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
</head>
<body>


<html:errors/>
<p>

<html:form action="CustomerRegistration.do" focus="userID">


 <table border="0" cellpadding="0" cellspacing="0" width="78%" height="46" align="center"> 
<tr>
      <td width="54%" height="22" valign="bottom" align="left">
        <h1><b><font color="#000080">User Registration </font></b></h1>
      </td>
        <td width="46%" height="22" valign="middle" align="right">&nbsp;</td>
    </tr>
    <tr>
      <td width="100%" colspan="2" height="24">
        <hr>
      </td>
    </tr>
    <tr>
      <td width="100%" colspan="2" height="21">
        <table border="0" cellpadding="0" cellspacing="0" width="80%">
  

<tr>
        <td class="prompt">User Name:</td>
        <td><html:text property="userID" size="40"/></td>
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
  </table>
</tr>
</table>
<div align="center">
        <td></td><td><html:submit property="submit" value="Register"/>&nbsp;&nbsp;&nbsp;
<INPUT TYPE=RESET value="Reset">
</div>

  
</center>
</div>
</html:form>




</body>
</html>
