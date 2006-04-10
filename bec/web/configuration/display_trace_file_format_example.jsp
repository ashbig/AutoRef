<!--Copyright 2003-2005, 2006 President and Fellows of Harvard College. All Rights Reserved -->
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>


<html>
<head>
<title>Trace file name example</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

 
</head>

<body> 


<p>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
    <tr>        <td >    <font color="#008000" size="5"><b> Trace file name example</b></font>
    <hr>    </td> </tr></table>





<table width="74%" border="0" align="center">
<tr> 
    <td >Format Name:</td>
    <td ><%= request.getAttribute("FORMATNAME")%></td>
  </tr>

<tr><td>Example trace file name: </td>
  <td><%= request.getAttribute("EXAMPLE_TRACE_FILE_NAME")%>     </td>
</tr>
<tr><td colspan='2' >was parsed as follows:</td></tr>  
<% ArrayList arr = (ArrayList) request.getAttribute("EXAMPLE_FILE_NAME_RESULT");
    if  (arr != null)
        {
String[] item = null;
   for (int count = 0; count < arr.size(); count++)
       {
           item = (String[]) arr.get(count);
           %>
           <tr><td><%= item[0]%></td><td><% if (item[1]== null){%> none <%}else{%> <%= item[1] %> <%}%></td></tr>
           <%}}%>
           
           

</table>



</body>
</html>
