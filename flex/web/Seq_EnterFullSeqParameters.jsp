<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.*" %>
<html>

<head>

<title>Primer Calculating Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/>: Full Sequence Analysis</h2>
<hr>
<p>&nbsp;</p><html:errors/>
<html:form action="/Seq_SubmitSpec.do" > 

<html:hidden property="forwardName" />

<font color="#2693A6" size="4"> <b>Set Name</b></font> 
  
<input name="SET_NAME" type="text" id="SET_NAME" value="default" size="53">
<P> <strong>Maximum acceptable number of mutations. </strong>
<table border="0" cellspacing="4" width="60%">

		  <tr> 
          <td width="56%"  background="barbkgde.gif" ><b>Silent mutation</b></td>
		 <td width="44%"  background="barbkgde.gif" ><b>
				
      <input name="FS_SILENT" type="text" id="FS_SILENT" value="2" size="5">
      </b></td>
			
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            
      <input name="FS_CONSERVATIVE" type="text" id="FS_CONSERVATIVE" value="3" size="5">
            </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            
      <input name="FS_NUN_CONSERVATIVE" type="text" id="FS_NUN_CONSERVATIVE" value="1" size="5">
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Frameshift</b></td>
			
          <td  background="barbkgde.gif" ><b>
            
      <input name="FS_FRAMESHIFT" type="text" id="FS_FRAMESHIFT" value="0" size="5">
              </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Stop codon</b></td>
			<td  background="barbkgde.gif" ><b>
            
      <input name="FS_STOP" type="text" id="FS_STOP" value="0" size="5">
                  </b></td>
		
		 </tr>
	
</table>
<p>&nbsp;</p>
<table width="60%" border="0" cellspacing="2" cellpadding="0">
  <tr>
    <td width="56%" background=barbkgde.gif><strong>Number of '<i>N</i>' per 100 bases </strong></td>
    <td width="44%" background=barbkgde.gif><input type="text" name="FS_N_100" value ="5" size="5"></td>
  </tr>
  <tr>
    <td background="barbkgde.gif"><strong>Max number of consecvative 'N'</strong></td>
    <td background="barbkgde.gif"><input name="FS_N_ROW" type="text" value="2" size="5"></td>
  </tr>
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    <input type="reset" value="Reset" name="B2">
</div>

</html:form>
<HR>

<% ArrayList sets = (ArrayList)request.getAttribute("specs");

   if (sets.size() > 0 )
  {
%><h3>Available Sets </h3>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
	FullSeqSpec spec = (FullSeqSpec) sets.get(count);
 
	%>
<P>
 <P> <font color="#2693A6" size="4"> <b>Set Name</b></font>
<%= spec.getName() %>
<P> <strong>Maximum acceptable number of mutations. </strong>
<table border="0" cellspacing="4" width="60%">

		  <tr> 
          <td width="56%"  background="barbkgde.gif" ><b>Silent mutation</b></td>
		 <td width="44%"  background="barbkgde.gif" ><b>
				
      <%= spec.getParameterByNameString("FS_SILENT") %>
      </b></td>
			
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            
      <%= spec.getParameterByNameString("FS_CONSERVATIVE") %>
            </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            
     <%= spec.getParameterByNameString("FS_NUN_CONSERVATIVE") %>
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Frameshift</b></td>
			
          <td  background="barbkgde.gif" ><b>
            
      <%= spec.getParameterByNameString("FS_FRAMESHIFT") %>
              </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Stop codon</b></td>
			<td  background="barbkgde.gif" ><b>
            
     <%= spec.getParameterByNameString("FS_STOP") %>
                  </b></td>
		
		 </tr>
	
</table>
<p>&nbsp;</p>
<table width="60%" border="0" cellspacing="2" cellpadding="0">
  <tr>
    <td width="56%" background=barbkgde.gif><strong>Number of '<i>N</i>' per 100 bases </strong></td>
    <td width="44%" background=barbkgde.gif>
<%= spec.getParameterByNameString("FS_N_100") %></td>
  </tr>
  <tr>
    <td background="barbkgde.gif"><strong>Max number of consecvative 'N'</strong></td>
    <td background="barbkgde.gif">
<%= spec.getParameterByNameString("FS_N_ROW") %></td>
  </tr>
</table>

<%}}%>
</body>
</html>
