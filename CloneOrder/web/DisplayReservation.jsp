<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page contentType="text/html"%>

<html>
<head><title>Your reservation</title></head>
<body>

<p>&nbsp;</p>
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%">
        <h2><font color="#000066"><i><b>Check</b><b> Reservation</b></i></font></h2>
        <p>&nbsp;</td>
    </tr>
    <tr>
      <td width="100%">Your reservation list:<br>
        <br>
      </td>
    </tr>
    <tr>
      <td width="100%">
          <table border="1" cellpadding="0" cellspacing="0" width="80%">
            <tr>
              <td width="33%" align="center" bgcolor="#3333CC"><font color="#FFFFFF">Clone
                Set</font></td>
              <td width="33%" align="center" bgcolor="#3333CC"><font color="#FFFFFF">Reservation
                Date</font></td>
              <td width="34%" align="center" bgcolor="#3333CC"><font color="#FFFFFF">
                </font></td>
            </tr>
            <logic:iterate id="reservation" name="reservations">
            <tr>
              <td width="33%" align="center">
                <a href="viewCloneSet.do?cloneSetName=<bean:write name="reservation" property="cloneset_info.name"/>" target="_blank">
                    <bean:write name="reservation" property="cloneset_info.name"/></a>
              </td>
              <td width="33%" align="center"><bean:write name="reservation" property="reservation_date"/>
              </td>
              <td width="34%" align="center"><a href="cancelReservation.do?clonesetid=<bean:write name="reservation" property="cloneset_info.id"/>" >
                 Cancel</a>
              </td>
            </tr>
            </logic:iterate>
          </table>   
          <p>&nbsp;
      </td>
    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <a href="Logout.do">Log out </a>
    </tr>
  </table>

  </center>
</div>

</body>
</html>
