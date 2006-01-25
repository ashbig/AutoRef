<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="java.util.*" %>
<%  String jsp_redirection = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION"); %>
<script language="JavaScript" src="<%= jsp_redirection %>scripts.js"></script>

<%if ( request.getAttribute("display_items") != null) 
    {%>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
    <tr>  <td align='center'> 
<hr><hr>
<font color="#008000" size="3"><b> <%= request.getAttribute("display_title") %> </font>
<hr>     </td>    </tr></table>
<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>
    </tr>  </table>  </center></div>

<table border="0" cellpadding="1" cellspacing="1" width="90%" align=center>

<% 
String title = "bgColor='#b8c6ed'"; String entry = " bgColor='#e4e9f8'";
String entry_style = null;ArrayList row_entries = null;
String align_center = "align='center'"; String align_style = "";

ArrayList display_items = (ArrayList)  request.getAttribute("display_items"); 


for (int count = 0; count < display_items.size(); count++)
{
    if ( count == 0 ){ entry_style = title;align_style=align_center;}
    else { entry_style = entry;align_style="";}
    row_entries = (ArrayList) display_items.get(count);
 %>
<tr> 
<% 
String temp = null;
for (int column = 0; column < row_entries.size(); column++)
{
    temp = String.valueOf( row_entries.get(column));
    if (temp != null && temp.trim().length()> 50 && temp.indexOf(jsp_redirection) < 0)
        {
         temp = temp.substring(0,50) + "<BR>" + temp.substring(51);
        }
        temp = temp.trim();
%>	<td <%=entry_style%> <%=align_style%> nonwrap><font size="2">
<%=  temp%>
</font></td><%}%>
</tr> <%}}%> 
</TABLE>

