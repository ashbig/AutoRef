<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->


<%@ page language="java" %>
<%
String[] clone_style_color={"#99FFFF", "#99CCFF", "#9999FF",
"#9999CC","#0066CC","#006699",
"#006666","#006633","#006600","#003366"};

%>

<html><body>


<table width="90%" border="0" align="center" CELLSPACING="4">
  <th colspan="2">Color Schema </th>
  <% int count_color = 0;%>
  <tr> 
    <td width="80%" ><strong>Best Isolate in the group:</strong></td>
    <td width="20%" bgcolor='<%=clone_style_color[count_color++]%>'>&nbsp;</td>
  </tr>
  <tr> 
    <td  ><strong> Second best isolate in the group:</strong></td>
    <td  bgcolor='<%=clone_style_color[count_color++]%>'>&nbsp; </td>
  </tr>
  <tr> 
    <td ><strong>Third best isolate in the group:</strong></td>
    <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Forth best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Fifth best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Sixth best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Seventh best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Eighth best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Nineth best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr> 
        <td ><strong>Thens best isolate in the group:</strong></td>
        <td bgcolor='<%=clone_style_color[count_color++]%>' >&nbsp;</td>
    </tr>
    <tr><td> System supports 10 different color codes </td></tr>
  </table>
  
  </body></html>