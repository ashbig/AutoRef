<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page contentType="text/html"%>

<html>
<head><title>Menu</title></head>
<body>

<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<div align="center">
  <center>

<table border="0" cellpadding="0" cellspacing="0" width="73%">
  <tr>
    <td width="50%" valign="top" align="center"><font color="#002677"><b>Click
      here to make clone set reservation.<br>
      <br>
      </b></font>
    </td>
  </tr>
  <tr>
    <td width="50%" align="center"><a href="Agreement.jsp"><img border="0" src="jpg/button21.jpg" width="250" height="31"></a>
    </td>
  </tr>
  <tr>
    <td width="50%" align="center"><font color="#003399"><br>
      </font><font color="#002677"><b>Click here to view the available clone set.</b><br>
      <br>
      </font></td>
  </tr>
  <tr>
    <td width="50%" align="center"><a href="viewCloneSet.do?cloneSetName=Kinase Clone Set 1" target="_blank">
     <img border="0" src="jpg/button4.jpg" width="158" height="31"></a></td>
  </tr>
  <tr>
    <td width="50%" align="center"><br>
      <font color="#000080"><b>Click here to check your clone set reservation.<br>
      <br>
      </b></font></td>
  </tr>
  <tr>
    <td width="50%" align="center"><a href="CheckReservation.jsp"><img border="0" src="jpg/button1.jpg" width="158" height="31"></a></td>
  </tr>
</table>

  </center>
</div>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p align="center"><a href="mailto:flexkinase@hms.harvard.edu">Contact us</a></p>

</body>
</html>
