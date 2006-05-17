<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

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
		<script language="JavaScript">
		<!--
		var submissionIsEnabled = true;
        function onChangeValue(e, val)
		{
 		   // Identify the form
   		   var form = e.form;
 		   if ( e == form.elements.p_downstream_distance_view)
 		   {
                        form.elements.p_downstream_distance.value = val;
                    }
                    if ( e == form.elements.p_upstream_distance_view)
                    {  
                        form.elements.p_upstream_distance.value = val;
                    }
		}
		function update3pUniversalPrimer(e,checked)
		{
 		   // Identify the form
   		   var form = e.form;
 		   if (checked)
 		   {
  			 form.elements.p_downstream_distance_view.value = 0;
             form.elements.p_downstream_distance.value = 0;
  			 form.elements.p_downstream_distance_view.disabled=true;
 		   }
 		   else
 		   {
		   	 form.elements.p_downstream_distance.value = 130;
             form.elements.p_downstream_distance_view.value = 130;
		   	 form.elements.p_downstream_distance_view.disabled= false;
		   }
		}
		
		function update5pUPrimer(e,checked)
		{
 		   // Identify the form
   		   var form = e.form;
 		  
 		  
 		  if (checked)
 		   {
  			 form.elements.p_upstream_distance_view.value = 0;
                         form.elements.p_upstream_distance.value = 0;
  			 form.elements.p_upstream_distance_view.disabled=true;
 		   }
 		   else
 		   {
		   	 form.elements.p_upstream_distance.value = 120;
                         form.elements.p_upstream_distance_view.value = 120;
		   	 form.elements.p_upstream_distance_view.disabled=false;
		   }
		}

		//--> <p>This page is best viewed with a JavaScript-capable browser. It appears that your browser does not support JavaScript.<p>
		</script>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="application_styles.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>

<body >


<!--<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	<tr>
        <td><i>If you are not sure about certain parameter settings, leave them 
          unchanged </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>-->
 <html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=Primer3Spec.PRIMER3_SPEC_INT%>" > 

<% String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;%>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
  <tr> 
    <td colspan=2><div align="right"> <b> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> ">        View Mine </a>
&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT%>">         View All </a></b>
&nbsp;&nbsp;<b><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Spec.PRIMER3_SPEC_INT * Spec.SPEC_DELETE_SPEC%>">Delete </a></b>
    
    </div>
      <p> 
      <p> 
      <p></p></td>
  </tr>
  <tr> 
    <td colspan="2"> <b>Set Name</b> <input type="text" name="SET_NAME" size="53" value=""> 
      <p> </td>
  </tr>
  <tr class='headerRow'>     <td height="42" colspan="2">Primer Design Parameters</td>  </tr>
  <tr> 
    <td colspan="2"> 
    <table width="100%">
        <tr class=<%= row_class[row_count++ % 2]%> > 
          <td width="25%" >Primer             Length (bp)</td>
          <td width="25%" >Min:             <input type="text" name="P_primer_min" size="10" value="18" onBlur="checkNumeric(this,5,100,'','','');"></td>
          <td width="25%" >Optimal:<input type="text" name="p_primer_opt" size="10" value="21" onBlur="checkNumeric(this,5,100,'','','');"></td>
          <td width="25%" >Max:   <input type="text" name="p_primer_max" size="10" value="27" onBlur="checkNumeric(this,5,100,'','','');">            </td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> > 
          <td width="25%" >Primer             Tm (C)</td>
          <td width="25%" >Min: <input type="text" name="p_primer_tm_min" size="10" value="57" onBlur="checkNumeric(this,5,100,'','','');">            </td>
          <td width="25%" >Optimal: <input type="text" name="p_primer_tm_opt" size="10" value="60" onBlur="checkNumeric(this,5,100,'','','');">            </td>
          <td width="25%" >Max: <input type="text" name="p_primer_tm_max" size="10" value="63" onBlur="checkNumeric(this,5,100,'','','');">            </td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> > 
          <td width="25%" >Primer             GC%</td>
          <td width="25%">Min: <input type="text" name="p_primer_gc_min" size="10" value="30">         </td>
          <td width="25%" >Optimal: <input type="text" name="p_primer_gc_opt" size="10" value="50">            </td>
          <td width="25%" >Max: <input type="text" name="p_primer_gc_max" size="10" value="70">         </td>
        </tr>
      </table></td>
  </tr>
  <% row_count = 0;%>
  <tr><td colspan="2">&nbsp;</td></tr>
  <tr class='headerRow'>     <td height="42" colspan="2" >Sequencing       Parameters</td>  </tr>
  <tr class=<%= row_class[row_count % 2]%>> 
    <td colspan=2  ><IMG SRC="greenarrow.gif"   HSPACE=5>
    <input type=checkbox name="universal_primer_5p" id="universal_primer_5p" value="-1" onClick="update5pUPrimer(this,this.checked)">
     Check      here if NO 5p Universal Primer is used 
      (<font size =2>The most upstream forward PCR primer is used when No 
        5p Universal Primer is checked)</font></td></tr>
   <tr class=<%= row_class[row_count++ % 2]%>> 
    <td colspan=2><IMG SRC="greenarrow.gif"   HSPACE=5>
    <input type=checkbox name=universal_primer_3p value="-1" onClick="update3pUniversalPrimer(this,this.checked)">
    Check         here if NO 3p Universal Primer is used 
     </td>
  </tr>
  <tr  class=<%= row_class[row_count++ % 2]%>  > 
    <td height="35">Distance       between 5p Universal Primer and START codon 
      <font size="2">(For a left primer, primer start position is the position       of the leftmost base)</font></td>
    <td > 
        <input type="text" name="p_upstream_distance_view"  onchange='onChangeValue(this, this.value)' size="20" value="100" id="p_upstream_distance_view" onBlur="checkNumeric(this,0,1000,'','','');">
        <input type="hidden" name="p_upstream_distance"  value="100" id="p_upstream_distance" >        bases</td>
  </tr>
  <tr  class=<%= row_class[row_count++ % 2]%> ='30'> 
    <td width="57%"  height="35">Distance       between 3p Universal Primer and STOP codon 
    <font size="2">(For       a right primer, primer start position is the position of the rightmost base)</font></td>
    <td > 
        <input type="text" name="p_downstream_distance_view" size="20" value="100" id="p_downstream_distance_view" onchange='onChangeValue(this, this.value)' onBlur="checkNumeric(this,0,1000,'','','');">
        <input type="hidden" name="p_downstream_distance"  value="100" id="p_downstream_distance" onchange='onChangeValue(this, this.value)'>
       bases</td>
  </tr>
  <tr  class=<%= row_class[row_count++ % 2]%>> 
    <td height="35" >Estimated       high quality read length (ERL)</td>
    <td  >         <input type="text" name="p_single_read_length" size="20" value="400" onBlur="checkNumeric(this,0,1000,'','','');">        bases</td>
  </tr>
  <tr  class=<%= row_class[row_count++ % 2]%>> 
    <td height="35">Window       size for testing primers</td>
    <td >         <input type="text" name="p_buffer_window_len" size="20" value="50" onBlur="checkNumeric(this,0,1000,'','','');">      bases</td>
  </tr>
<tr  class=<%= row_class[row_count++ % 2]%>> 
    <td height="35">Distance between sequencing primer and start of high quality read length</td>
    <td ><input type="text" name="P_EST_SEQ" size="20" value="50" onBlur="checkNumeric(this,0,1000,'','','');">       bases</td>
  </tr>
  <tr class=<%= row_class[row_count++ % 2]%>> 
    <td >Number of strands to sequence</td>
    <td  height="3" align="center" valign="bottom"  bgcolor="#e4e9f8"> 
      <p align="left"> 
        <input type="radio" value="<%= Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD %>" name="p_number_of_strands" checked>
        Single Strand (Coding strand, forward primers only)
      <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE %>" >
        Single Strand (Compliment to coding strand, reverse primers only)</p>
      <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_BOTH_STRAND %>" >
        Both Strands (Both forward and reverse primers until meet in middle)
     <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE %>" >
      Both Strands (Both forward and reverse primers, double coverage)</td>
  </tr>



  <tr><td colspan=3 height='2'>&nbsp;</td></tr>
  <tr> 
    <td colspan="3" height="1" align="center" valign="bottom">
        <input type="submit" value="Submit" name="B1">
        <input type="reset" value="Reset" name="B2">
      </p>
</table>



</html:form>
<HR>


</body>

</html>


