<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.oligo.*" %>
<html>

<head>

<title>Primer Calculating Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/> : Set Parameters for Primer Calculation</h2>
<hr>
<p>&nbsp;</p><html:errors/>
<html:form action="/Seq_SubmitSpec.do" > 

<html:hidden property="forwardName" />
<h3 >Create new set of primer picking parameters </h3>
<i>If you are not sure about certain
parameter settings, leave them unchanged </i> <a href="helpPrimer3Param.htm">[parameter help file]</a>.</b>
<P>
<table border="0" width="100%" height="6">
	<tr>
    <td width="50%" colspan="2" height="48"><font color="#2693A6" size="4">
        <b>Set Name</b></font></td>
    <td width="50%" colspan="2" height="48"> <input name="SET_NAME" type="text" id="SET_NAME" value="" size="53"></td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="48"><font color="#2693A6" size="4">
        <b>Primer Picking Parameters</b></font></td>
    <td width="50%" colspan="2" height="48"></td>
  </tr>
  <tr>
    <td width="25%" valign="top" height="1" background="barbkgde.gif">
        <b>Primer Length (bp)</b></td>
    <td width="25%" height="1" background="barbkgde.gif">
        <p><b>Min:</b> <input type="text" name="P_primer_min" size="20" value="18"></p>

    </td>
    <td width="25%" height="1" background="barbkgde.gif">
 
     
        <p><b>Opt:</b> <input type="text" name="p_primer_opt" size="20" value="21"></p>
  
    </td>
    <td width="25%" height="1" background="barbkgde.gif">
     
        <p><b>Max:</b> <input type="text" name="p_primer_max" size="20" value="27"></p>
     
    </td>
  </tr>
  <tr>
    <td width="25%" height="26" background="barbkgde.gif" valign="top"><b>Primer
      Tm (�C)</b></td>
    <td width="25%" height="26" background="barbkgde.gif">
 
        <p><b>Min: </b><input type="text" name="p_primer_tm_min" size="20" value="57"></p>
     
    </td>
    <td width="25%" height="26" background="barbkgde.gif">
  <p><b>Opt:</b> <input type="text" name="p_primer_tm_opt" size="20" value="60"></p>

    </td>
    <td width="25%" height="26" background="barbkgde.gif">
    
        <p><b>Max:</b>&nbsp; <input type="text" name="p_primer_tm_max" size="20" value="63"></p>
    
    </td>
  </tr>
  <tr>
    <td width="25%" height="1" background="barbkgde.gif" valign="top"><b>Primer
      GC%</b></td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
   
        <p><b>Min:</b>&nbsp; <input type="text" name="p_primer_gc_min" size="20" value="30"></p>
     
    </td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
    
        <p><b>Opt:</b> <input type="text" name="p_primer_gc_opt" size="20" value="50"></p>

    </td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
     
        <p><b>Max:</b>&nbsp; <input type="text" name="p_primer_gc_max" size="20" value="70"></p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="1"></td>
    <td width="50%" colspan="2" height="1"></td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="39"><b><font size="4" color="#2693A6">Sequencing
      Parameters</font></b></td>
    <td width="50%" colspan="2" height="39"></td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Distance
      between 5p Universal Primer and START codon</b>&nbsp;&nbsp;&nbsp; <font size="2"><b>(For
      a left primer, primer start position is the position of the leftmost base)</b></font></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
     
        <p><input type="text" name="p_upstream_distance" size="20" value="100">
        bases</p>
   
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Distance
      between 3p Universal Primer and STOP codon&nbsp;&nbsp; <font size="2">(For
      a right primer, primer start position is the position of the rightmost
      base)&nbsp;&nbsp;</font></b></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
 
        <p><input type="text" name="p_downstream_distance" size="20" value="100">
        bases</p>
    
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Estimated
      high quality read length (ERL)</b></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
     
        <p><input type="text" name="p_single_read_length" size="20" value="400">
        bases</p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="3" background="barbkgde.gif" valign="top"><b>Window
      size for testing primers</b></td>
    <td width="50%" colspan="2" height="3" background="barbkgde.gif">
      
        <p><input type="text" name="p_buffer_window_len" size="20" value="50">
        bases</p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="3" align="center" valign="top" background="barbkgde.gif">
      <p align="left"><b>Number of strands to sequence</b></td>
    <td width="50%" colspan="2" height="3" align="center" valign="bottom" background="barbkgde.gif">
         <p align="left"><input type="radio" value="0" name="p_number_of_strands">
        <b>Single Strand</b> (Coding strand, forward primers)</p>
        <p align="left"><input type="radio" name="p_number_of_strands" value="1" checked>
        <b>Both Strands</b>  (Both forward and reverse primers)</p>
      
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="4" height="3" align="center" valign="bottom"></td>
  </tr>
  <tr>
    <td width="100%" colspan="4" height="1" align="center" valign="bottom">
   
        <p><input type="submit" value="Submit" name="B1">
		<input type="reset" value="Reset" name="B2"></p>
   
</table>



</html:form>
<HR>

<% ArrayList sets = (ArrayList)request.getAttribute("specs");

   if (sets.size() > 0 )
  {
%><h3>Available Sets </h3>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) sets.get(count);
 
	%>
<P>
 <P> <font color="#2693A6" size="4"> <b>Set Name</b></font>
<%= spec.getName() %>



<table border="0" width="100%" height="6">
 <tr>
    <td width="50%" colspan="2" height="48"><font color="#2693A6" size="4">
        <b>Primer Picking Parameters</b></font></td>
    <td width="50%" colspan="2" height="48"></td>
  </tr>
  <tr>
    <td width="25%" valign="top" height="1" background="barbkgde.gif">
        <b>Primer Length (bp)</b></td>
    <td width="25%" height="1" background="barbkgde.gif">
        <p><b>Min:</b><%= spec.getParameterByNameString("p_primer_min".toUpperCase()) %></p>

    </td>
    <td width="25%" height="1" background="barbkgde.gif">
 
     
        <p><b>Opt:</b> <%= spec.getParameterByNameString("p_primer_opt".toUpperCase())%></p>

    </td>
    <td width="25%" height="1" background="barbkgde.gif">
     
        <p><b>Max:</b> <%= spec.getParameterByNameString("p_primer_max".toUpperCase())%></p>
     
    </td>
  </tr>
  <tr>
    <td width="25%" height="26" background="barbkgde.gif" valign="top"><b>Primer
      Tm (�C)</b></td>
    <td width="25%" height="26" background="barbkgde.gif">
 
        <p><b>Min: </b><%= spec.getParameterByNameString("p_primer_tm_min".toUpperCase())%></p>
     
    </td>
    <td width="25%" height="26" background="barbkgde.gif">
  <p><b>Opt:</b> <%= spec.getParameterByNameString("p_primer_tm_opt".toUpperCase())%></p>

    </td>
    <td width="25%" height="26" background="barbkgde.gif">
    
        <p><b>Max:</b><%= spec.getParameterByNameString("p_primer_tm_max".toUpperCase())%></p>
    
    </td>
  </tr>
  <tr>
    <td width="25%" height="1" background="barbkgde.gif" valign="top"><b>Primer
      GC%</b></td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
   
        <p><b>Min:</b><%= spec.getParameterByNameString("p_primer_gc_min".toUpperCase())%></p>
     
    </td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
    
        <p><b>Opt:</b> <%= spec.getParameterByNameString("p_primer_gc_opt".toUpperCase())%></p>

    </td>
    <td width="25%" height="1" background="barbkgde.gif" valign="top">
     
        <p><b>Max:</b>&nbsp; <%= spec.getParameterByNameString("p_primer_gc_max".toUpperCase())%></p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="1"></td>
    <td width="50%" colspan="2" height="1"></td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="39"><b><font size="4" color="#2693A6">Sequencing
      Parameters</font></b></td>
    <td width="50%" colspan="2" height="39"></td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Distance
      between 5p Universal Primer and START codon</b>&nbsp;&nbsp;&nbsp; <font size="2"><b>(For
      a left primer, primer start position is the position of the leftmost base)</b></font></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
     
        <p><%= spec.getParameterByNameString("p_upstream_distance".toUpperCase()) %>
        bases</p>
   
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Distance
      between 3p Universal Primer and STOP codon&nbsp;&nbsp; <font size="2">(For
      a right primer, primer start position is the position of the rightmost
      base)&nbsp;&nbsp;</font></b></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
 
        <p><%= spec.getParameterByNameString("p_downstream_distance".toUpperCase())%>
        bases</p>
    
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif" valign="top"><b>Estimated
      high quality read length (ERL)</b></td>
    <td width="50%" colspan="2" height="44" background="barbkgde.gif">
     
        <p><%= spec.getParameterByNameString("p_single_read_length".toUpperCase())%>
        bases</p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="3" background="barbkgde.gif" valign="top"><b>Window
      size for testing primers</b></td>
    <td width="50%" colspan="2" height="3" background="barbkgde.gif">
      
        <p><%= spec.getParameterByNameString("p_buffer_window_len".toUpperCase())%>
        bases</p>
     
    </td>
  </tr>
  <tr>
    <td width="50%" colspan="2" height="3" align="center" valign="top" background="barbkgde.gif">
      <p align="left"><b>Number of strands to sequence</b></td>
    <td width="50%" colspan="2" height="3" align="center" valign="bottom" background="barbkgde.gif">
<% 

	
if ( spec.getParameterByNameString("p_number_of_strands".toUpperCase()).equals("1") )
     { %>  <p><b>Single Strand</b> (Coding strand, forward primers)</p>
     <%}else{%>
        
        <b>Both Strands</b>  (Both forward and reverse primers)</p>
<%}%>
      
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="4" height="3" align="center" valign="bottom"></td>
  </tr>
  
</table>

<%}}%>

<HR>


</body>

</html>


