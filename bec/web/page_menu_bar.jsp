<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page contentType="text/html"%>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN"
			"http://www.w3.org/TR/REC-html40/strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="page_elements.css" rel="stylesheet" type="text/css">

</head>
<body>

<table width="100%" height="38" border="0" align="center">
<tr bgcolor='black'>
    
    <% User user = (User)session.getAttribute(Constants.USER_KEY);

if (!user.getUserGroup().equals("Researcher")){%>
<td><table width="100%" height="30" border="0" cellpadding="4" cellspacing="1" bgcolor="#FFFFFF">
              <tr bgcolor="#1145A6"> 
<td align="center"  nowrap bgcolor="#1145A6">&nbsp</td>

<td align="center"  nowrap bgcolor="#1145A6" width='80'><a href="about_ace.jsp?<%= Constants.JSP_TITLE%>=ACE overview&amp;<%= Constants.JSP_CURRENT_LOCATION %>=Home > ACE Overview" STYLE="text-decoration:none"><strong><font color="#FFFFFF">About</font></strong></a></td>
<td align="center"  nowrap width='80'><a href="help_main.jsp?<%= Constants.JSP_TITLE%>=ACE Help&amp;<%= Constants.JSP_CURRENT_LOCATION %>=Home > ACE Help" STYLE="text-decoration:none"><font color="#FFFFFF"><strong>Help</strong></font></a></td>
<td  align="center"  nowrap width='120'><a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>" STYLE="text-decoration:none"><font color="#FFFFFF"><strong>Contact                 Us </strong></font></a></td>
<td align="center"  nowrap width='80'><a href="Logout.do" STYLE="text-decoration:none"><font color="#FFFFFF"><strong>Log Out</strong></font></a></td>
 </tr>      </table><%}%></td>
  </tr>
</table>

</body></html>