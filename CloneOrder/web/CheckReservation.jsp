<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page contentType="text/html"%>

<html>
<head><title>check reservation</title></head>
<body>


<html:form action="checkReservation.do" focus="email">
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">    
    <tr>
      <td width="100%">
        <h2><font color="#000066"><i><b>Check</b><b> Reservation</b></i></font></h2>
        <p>&nbsp;</td>
    </tr>
    <tr>
      <td width="100%">
        <logic:present name="checkReservation.error">
            <p><font color="#FF0000"><bean:write name="checkReservation.error"/></font></p><br>
        </logic:present>
      </td>
    </tr>
    <tr>
      <td width="100%">Please type in your email address:</td>
    </tr>
    <tr>
      <td width="100%">
          <p><br>
          <html:text property="email" size="40"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </p>
   
        <p>&nbsp;</td>
    </tr>

    <tr>
      <td width="100%">Please type in your password:</td>
    </tr>
    <tr>
      <td width="100%">
          <p><br>
          <html:password property="password" size="40"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <html:submit property="submit" value="Submit"/></p>   
        <p>&nbsp;</td>
    </tr>
  </table>
  </center>
</div>
</html:form>
<p>&nbsp;</p>


</body>
</html>
