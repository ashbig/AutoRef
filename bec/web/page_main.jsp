<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>

<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<%@ page import=" edu.harvard.med.hip.bec.coreobjects.spec.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html"  %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp"  %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
            <%@ include file="page_left_menu.jsp" %></td>
          <td  valign="top"><jsp:include page="page_location.jsp" /></td>
        </tr>
        <tr> 
          <td valign="top"><jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
       
<td valign="top"><jsp:include page="page_contents.jsp"   /></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp"  %></td>
  </tr>
</table>
</body>
</html>
