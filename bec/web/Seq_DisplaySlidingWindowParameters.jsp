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
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> available sets of parameters for 
Sequence trimming using Sliding Window Algorithm</font>
    <hr>
    
    <p>
    </td>
    </tr>
</table>

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
	SlidingWindowTrimmingSpec spec = (SlidingWindowTrimmingSpec) sets.get(count);
	%>

<p>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan =2><b>Set Name</b>&nbsp    <%= spec.getName() %>
      <P></P></td>
  </tr>
  <tr > 
    <td width="70%" bgColor="#e4e9f8" ><font color="#000080"><strong>Phred base score quality cut-off:</strong> 
      </font></td>
    <td bgColor="#e4e9f8"> <%= spec.getParameterByNameString("SW_Q_CUTT_OFF") %></td>
  </tr>
<tr><td colspan='2'>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><font color="#000080"><b>Window size for low-quality trimming:</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_Q_WINDOW_SIZE")%></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><font color="#000080"><b>Max number of low-quality bases:</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_Q_NUMBER_LOW_QUALITY_BASES")%></td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><font color="#000080"><b>Max number of consequative low-quality bases (not implemented):</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_Q_N_LOW_QUALITY_BASES_CONQ")%></td>
  </tr>

<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Window size for ambiquous bases trimming (not implemented):</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_A_WINDOW_SIZE") %> </td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Max number of ambiquous bases (not implemented):</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_A_NUMBER_BASES") %></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Max number of consequative ambiquous bases (not implemented):</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_A_N_LOW_QUALITY_BASES_CONQ") %> </td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Minimum distance between stretches:</font></b> </td>
    <td bgColor="#b8c6ed"><%= spec.getParameterByNameString("SW_MIN_DISTANCE_BTW_CONTIGS")%></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Sequence Trimming Type:</font></b> </td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("SW_TRIM_TYPE" )%></td>
  </tr>
</table>

<%}}%>
</body>
</html>
