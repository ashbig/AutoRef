<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>

<head>

<title>Polymorphism Detector Parameters</title>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="application_styles.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html" %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp" %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
		  <jsp:include page="page_left_menu.jsp" /></td>
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td>
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	<tr>
        <td><i>Parameters Definition </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 if (sets.size()==0)
{%>
<p><table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> <td><b>No sets are available</b></td></tr></table>
<%}
else if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
		PolymorphismSpec spec = (PolymorphismSpec) sets.get(count);
 
	%>

<p>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan =2><b>Set Name&nbsp;&nbsp;&nbsp;</b><%= spec.getName() %> 
      <P></P></td>
  </tr>
  <tr> 
    <td bgcolor="#b8c6ed" width='50%'><strong><font color="#000080">Please select GenBank 
      database to search in:</font></strong></td>
    <td bgcolor="#b8c6ed" align='left'>
   <table border='0'>
    <% ArrayList values = Algorithms.splitString( (String)spec.getParameterByNameString("PL_DATABASE")," ");
    for (int count_db = 0; count_db < values.size(); count_db++)
    {%>      <tr><td>  <%= values.get(count_db) %> </td></tr>  <% } %>
</table>
    </td>
  </tr>
  <tr >
    <td colspan="2">&nbsp; </td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Number of flanking bases to 
      append on both sides of discrepancy for search: </font></b> </td>
	
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("PL_BASES")%></td>
  </tr>
  <tr><td colspan=2 >&nbsp;</td></tr>
  <tr> 
    <td bgcolor="#b8c6ed" colspan = 2> <b><font color="#000080">Parameters to confirm similarity 
      between the query and any database hits to confirm they are the same gene: 
      </font></b></td>
   
  </tr>
  <tr>
    <td bgColor="#e4e9f8"><div align="right"><font color="#000080">minimum match 
        length:&nbsp;&nbsp;&nbsp;</font></div></td>
	<td bgColor="#e4e9f8"><font color="#000080"><%= spec.getParameterByNameString("PL_MATCH_LENGTH") %></font></td>
  </tr>
  <tr>
    <td bgColor="#e4e9f8"><div align="right"><font color="#000080">requered percent 
        identity:&nbsp;&nbsp;&nbsp;</font></div></td>
	<td bgColor="#e4e9f8"><font color="#000080">
      <%= spec.getParameterByNameString("PL_MATCH_IDENTITY") %> </font></td>
  </tr>

  </table>

<%}}%>
 </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
</html>
