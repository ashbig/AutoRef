<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

<html>

<head>

<title>Parameters for Sequence trimming using Sliding Window Algorithm</title>
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

<!--<div align="center">
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
</div>-->


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 
 if (sets.size()==0)
{%>
<p><table border="0" cellpadding="2" cellspacing="2" width="90%" align=center>
  <tr> <td><b>No sets are available</b></td></tr></table>
<%}
else if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
	SlidingWindowTrimmingSpec spec = (SlidingWindowTrimmingSpec) sets.get(count);
	%>

<p>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
  <tr> 
    <td colspan =2><b>Set Name</b>&nbsp    <%= spec.getName() %>
      <P></P></td>
  </tr>
  <tr > 
    <td width="70%" bgColor="#e4e9f8" ><font color="#000080"><strong>Phred base score defining high quality sequence:</strong> 
      </font></td>
    <td bgColor="#e4e9f8"> <%= spec.getParameterByNameString("SW_Q_CUTT_OFF") %></td>
  </tr>
<tr><td colspan='2'>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><font color="#000080"><b>Sliding window size for low-quality trimming:</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_Q_WINDOW_SIZE")%></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><font color="#000080"><b>Max number of low-quality bases allowed:</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_Q_NUMBER_LOW_QUALITY_BASES")%></td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><font color="#000080"><b>Max number of consecutive low-quality bases allowed (not implemented yet):</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_Q_N_LOW_QUALITY_BASES_CONQ")%></td>
  </tr>

<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Sliding window size for ambiguous base trimming (not implemented yet):</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_A_WINDOW_SIZE") %> </td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Max number of ambiguous bases allowed (not implemented yet):</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_A_NUMBER_BASES") %></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Max number of consecutive ambiguous bases allowed (not implemented yet):</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_A_N_LOW_QUALITY_BASES_CONQ") %> </td>
  </tr>
<TR><TD COlspan=2>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Minimum distance between stretches:</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_MIN_DISTANCE_BTW_CONTIGS")%></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Minimum distance between sequence stretches needed to treat them separately (Gap Mapper, Low Quality Finder) :</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_MIN_CONTIG_LENGTH")%></td>
  </tr>
<TR><TD COlspan=2>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Sequence Trimming Type:</font></b> </td>
    <td bgColor="#b8c6ed">
<% int trim_type = spec.getParameterByNameInt("SW_TRIM_TYPE" );
    String title = "";
   if (trim_type == SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW_NODISC)
        title = "Moving Window with extension for no discrepancies regions";
    else if (trim_type == SlidingWindowTrimmingSpec.TRIM_TYPE_NONE )
        title = "No Trimming";
    else if (trim_type == SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW) 
        title = "Moving Window";
%>    <%= title %></td>
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
