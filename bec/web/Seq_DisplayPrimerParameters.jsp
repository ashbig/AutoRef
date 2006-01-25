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
        <td><i>Parameter Definition </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>-->

<hr>

<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 if (sets.size()==0)
{%>
<p><table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> <td><b>No sets are available</b></td></tr></table>
<%}
else 
   if (sets.size() > 0 )
  {
      String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
    for (int count = 0; count < sets.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) sets.get(count);
 
	%>
<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
<tr>     <td colspan ="2"><b>Set Name</b>&nbsp;&nbsp<%= spec.getName() %> <P></P></td>  </tr>
<tr class='headerRow' height="48">     <td colspan="4" >Primer Design Parameters</td>  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td width="35%" >Primer       Length (bp)</td>
    <td width="20%" >Min: <%= spec.getParameterByNameString("p_primer_min".toUpperCase()) %></td>
    <td width="20%" >Optimal:      <%= spec.getParameterByNameString("p_primer_opt".toUpperCase())%></td>
    <td width="25%" >Max:    <%= spec.getParameterByNameString("p_primer_max".toUpperCase())%></td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td >Primer       Tm (C)</td>
    <td >Min: <%= spec.getParameterByNameString("p_primer_tm_min".toUpperCase())%></td>
    <td >Optimal: <%= spec.getParameterByNameString("p_primer_tm_opt".toUpperCase())%></td>
    <td >Max: <%= spec.getParameterByNameString("p_primer_tm_max".toUpperCase())%></td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td >Primer       %GC</td>
    <td >Min: <%= spec.getParameterByNameString("p_primer_gc_min".toUpperCase())%></td>
    <td >Optimal: <%= spec.getParameterByNameString("p_primer_gc_opt".toUpperCase())%></td>
    <td >Max:  <%= spec.getParameterByNameString("p_primer_gc_max".toUpperCase())%></td>
  </tr>
<tr  >     <td colspan="2" height="1">&nbsp </tr>

<tr class='headerRow' height="44">     <td colspan="4" >Sequencing       Parameters</td>  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td colspan="2" >Distance       between 5p Universal Primer and START codon <font size="2">(For 
      a left primer, primer start position is the position of the leftmost base)</font></td>
    <td colspan="2" height="35" ><%= spec.getParameterByNameString("p_upstream_distance".toUpperCase()) %>         bases</td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td colspan="2" height="35" >Distance 
      between 3p Universal Primer and STOP codon <font size="2">(For 
      a right primer, primer start position is the position of the rightmost base)&nbsp;&nbsp;</font></b></font></td>
    <td colspan="2" ><%= spec.getParameterByNameString("p_downstream_distance".toUpperCase())%>         bases</td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%> > 
    <td colspan="2" height="35" >Estimated       high quality read length (ERL)</td>
    <td colspan="2"><%= spec.getParameterByNameString("p_single_read_length".toUpperCase())%>         bases</td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%>> 
    <td colspan="2" height="35" >Window       size for testing primers</td>
    <td colspan="2"><%= spec.getParameterByNameString("p_buffer_window_len".toUpperCase())%>         bases</td>
  </tr>
<tr class=<%= row_class[row_count++ % 2]%>> 
    <td colspan="2"  height="35">Distance between sequencing primer and start of high quality read length</td>
    <td  colspan="2" ><%= spec.getParameterByNameString("P_EST_SEQ".toUpperCase()) %>         bases</p></td>
  </tr>
  <tr class=<%= row_class[row_count++ % 2]%>> 
    <td colspan="2" height="35" >Number of strands to sequence</td>
    <td colspan="2" >
      <% 

	
if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD) ))
{ %> Single Strand (forward primers)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE) ))
{%>Single Strands (reverse primers only)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_BOTH_STRAND ) ))
{%>Both Strands (Both forward and reverse primers until meet in middle)      <%}
else if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals( String.valueOf(Primer3Wrapper.WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE) ))
{%>Both Strands (Both forward and reverse primers, double coverage)      <%}%>
 </td>
  </tr>
  <tr>     <td colspan="4" >&nbsp<HR></td>  </tr>
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


