<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>Member Registration</title>
</head>

<body>
<div align="center">
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="78%">
 <tr><td><html:errors/></td></tr>
</table>
  </center>
</div>
<p>&nbsp;</p>
<html:form action="CustomerRegistration.do" focus="firstName">
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="78%" height="46">
    <tr>
      <td width="54%" height="22" valign="bottom" align="left">
        <h1><b><font color="#000080">Member Registration </font></b></h1>
      </td>
      <td width="46%" height="22" valign="middle" align="right"><font color="#000080"><img border="0" src="jpg/medgene_logo_s.jpg" align="right" width="176" height="36"></font></td>
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
            <td class="prompt" width="206"><br>
              <br>
            </td>
            <td width="503"></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>First Name:</b></td>
            <td width="503"><html:text property="firstName" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Last Name:</b></td>
            <td width="503"><html:text property="lastName" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Work Phone:</b></td>
            <td width="503"><html:text property="phone" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Email:</b></td>
            <td width="503"><html:text property="email" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Organization:</b></td>
            <td width="503"><html:text property="organization" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>User Name:</b></td>
            <td width="503"><html:text property="userID" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Password:</b></td>
            <td width="503"><html:password property="password" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Re-enter password:</b></td>
            <td width="503"><html:password property="password2" size="40"/></td>
          </tr>
          <tr>
            <td class="prompt" width="206"><b>Reminder Text:</b></td>
            <td width="503"><html:text property="reminderText" size="40"/></td>
          </tr>
          <tr>
            <td width="206"><br>
              <br>
            </td>
            <td width="503"></td>
          </tr>
          <tr>
            <td width="206"></td>
            <td width="503"><html:submit property="submit" value="Register"/>&nbsp;&nbsp;&nbsp;<html:reset/>
              <p>&nbsp;</p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  </center>
</div>
<p>&nbsp;</p>
</html:form>

<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>

</body>

</html>
