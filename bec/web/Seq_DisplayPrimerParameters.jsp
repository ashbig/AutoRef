<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.primer3.*" %>
<html>

<head>

<title>Primer Calculating Parameters</title>
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
    <font color="#008000" size="5"><b> available sets of parameters for Primer Designer</font>
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
        <td><i>Parameter Definition </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
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
else 
   if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) sets.get(count);
 
	%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr>     <td colspan ="4"><hr></TD></TR>
  <tr> 
    <td colspan ="2"><b>Set Name</b>&nbsp;&nbsp<%= spec.getName() %> <P></P></td>
  </tr>
  <tr> 
    <td colspan="4" height="48"  bgcolor="#1145A6"> <font color="white" size="4"> 
      <b>Primer Design Parameters</b></font></td>
  </tr>
  <tr> 
    <td width="35%" valign="top" height="1" bgColor="#e4e9f8" > <font color="#000080"><b>Primer 
      Length (bp)</b></font></td>
    <td width="20%" height="1" bgColor="#e4e9f8" > <p><font color="#000080"><b>Min:</b><%= spec.getParameterByNameString("p_primer_min".toUpperCase()) %></font></p></td>
    <td width="20%" height="1" bgColor="#e4e9f8" > <p><font color="#000080"><b>Optimal:</b> 
        <%= spec.getParameterByNameString("p_primer_opt".toUpperCase())%></font></p></td>
    <td width="25%" height="1" bgColor="#e4e9f8" > <p><font color="#000080"><b>Max:</b> 
        <%= spec.getParameterByNameString("p_primer_max".toUpperCase())%></font></p></td>
  </tr>
  <tr> 
    <td width="35%" height="26" bgcolor="#b8c6ed" valign="top"><font color="#000080"><b>Primer 
      Tm (°C)</b></font></td>
    <td width="20%" height="26" bgcolor="#b8c6ed"> <p><font color="#000080"><b>Min: 
        </b><%= spec.getParameterByNameString("p_primer_tm_min".toUpperCase())%></font></p></td>
    <td width="20%" height="26"bgcolor="#b8c6ed"> <p><font color="#000080"><b>Optimal:</b> 
        <%= spec.getParameterByNameString("p_primer_tm_opt".toUpperCase())%></font></p></td>
    <td width="25%" height="26" bgcolor="#b8c6ed"> <p><font color="#000080"><b>Max:</b><%= spec.getParameterByNameString("p_primer_tm_max".toUpperCase())%></font></p></td>
  </tr>
  <tr> 
    <td width="35%" height="1" bgColor="#e4e9f8"  valign="top"><font color="#000080"><b>Primer 
      %GC</b></font></td>
    <td width="20%" height="1" bgColor="#e4e9f8"  valign="top"> <p><font color="#000080"><b>Min:</b><%= spec.getParameterByNameString("p_primer_gc_min".toUpperCase())%></p></td>
    <td width="20%" height="1" bgColor="#e4e9f8"  valign="top"> <p><font color="#000080"><b>Optimal:</b> 
        <%= spec.getParameterByNameString("p_primer_gc_opt".toUpperCase())%></font></p></td>
    <td width="25%" height="1" bgColor="#e4e9f8"  valign="top"> <p><font color="#000080"><b>Max:</b> 
        <%= spec.getParameterByNameString("p_primer_gc_max".toUpperCase())%></font></p></td>
  </tr>
  <tr> 
    <td colspan="2" height="1"><font color="#000080">&nbsp;</font></td>
    <td colspan="2" height="1"></td>
  </tr>
  <tr> 
    <td colspan="4" height="39"  bgcolor="#1145A6"><b><font size="4" color=white>Sequencing 
      Parameters</font></b></td>
  </tr>
  <tr> 
    <td colspan="2" height="44" bgColor="#e4e9f8"  valign="top"><font color="#000080"><b>Distance 
      between 5p Universal Primer and START codon</b>&nbsp;&nbsp;&nbsp; <font size="2"><b>(For 
      a left primer, primer start position is the position of the leftmost base)</b></font></font></td>
    <td colspan="2" height="44" bgColor="#e4e9f8" > <p>
<font color="#000080"><b><%= spec.getParameterByNameString("p_upstream_distance".toUpperCase()) %> 
        bases</p></td>
  </tr>
  <tr> 
    <td colspan="2" height="44" bgcolor="#b8c6ed" valign="top"><font color="#000080"><b>Distance 
      between 3p Universal Primer and STOP codon&nbsp;&nbsp; <font size="2">(For 
      a right primer, primer start position is the position of the rightmost base)&nbsp;&nbsp;</font></b></font></td>
    <td colspan="2" height="44" bgcolor="#b8c6ed"> <p>
<font color="#000080"><b><%= spec.getParameterByNameString("p_downstream_distance".toUpperCase())%> 
        bases</p></td>
  </tr>
  <tr> 
    <td colspan="2" height="44" bgColor="#e4e9f8"  valign="top"><font color="#000080"><b>Estimated 
      high quality read length (ERL)</b></font></td>
    <td colspan="2" height="44" bgColor="#e4e9f8" > <p>
<font color="#000080"><b><%= spec.getParameterByNameString("p_single_read_length".toUpperCase())%> 
        bases</p></td>
  </tr>
  <tr> 
    <td colspan="2" height="3" bgcolor="#b8c6ed" valign="top"><font color="#000080"><b>Window 
      size for testing primers</b></font></td>
    <td colspan="2" height="3" bgcolor="#b8c6ed"> <p>
<font color="#000080"><b><%= spec.getParameterByNameString("p_buffer_window_len".toUpperCase())%> 
        bases</p></td>
  </tr>
<tr> 
    <td colspan="2"  width="100%" height="3" bgColor="#e4e9f8" valign="top"><font color="#000080"><b>
Distance between sequencing primer and start of high quality read length</b></font></td>
    <td  colspan="2"  height="3" bgColor="#e4e9f8"> <p> 
<font color="#000080"><b><%= spec.getParameterByNameString("P_EST_SEQ".toUpperCase()) %> 
        bases</p></td>
  </tr>
  <tr> 
    <td colspan="2" height="3" align="center" valign="top" bgColor="#e4e9f8" > 
      <p align="left"><font color="#000080"><b>Number of strands to sequence</b></font></td>
    <td colspan="2" height="3" align="center" valign="bottom" bgColor="#e4e9f8" > 
     <font color="#000080">   <p><b>
      <% 

	
if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD) ))
{ %> Single Strand (forward primers)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE) ))
{%>Single Strands (reverse primers only)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_BOTH_STRAND ) ))
{%>Both Strands (Both forward and reverse primers)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE) ))
{%>Both Strands (Both forward and reverse primers, double coverage)      <%}%>
  </p></b> 
    </td>
  </tr>
  <tr> 
    <td colspan="4" height="3" align="center" valign="bottom"></td>
  </tr>
</table>

<%}}%>

<HR>


</body>

</html>


