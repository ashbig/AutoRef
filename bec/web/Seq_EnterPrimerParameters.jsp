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
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> create new set of parameters for Primer Designer</font>
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
        <td><i>If you are not sure about certain parameter settings, leave them 
          unchanged </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>
 <html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=Primer3Spec.PRIMER3_SPEC_INT%>" > 


<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan=2><div align="right"> <b> <a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT%>"> 
        View All </a></b> </div>
      <p> 
      <p> 
      <p></p></td>
  </tr>
  <tr> 
    <td colspan="2"> <b>Set Name</b> <input type="text" name="SET_NAME" size="53" value=""> 
      <p> </td>
  </tr>
  <tr> 
    <td height="48" colspan="2" bgcolor="#1145A6"><font color="white" size="4" > 
      <b>Primer Picking Parameters</b></font></td>
  </tr>
  <tr> 
    <td colspan="2"> <table width="100%">
        <tr> 
          <td width="25%" valign="top" height="1" bgColor="#e4e9f8"> <font color="#000080"><b>Primer 
            Length (bp)</b></font></td>
          <td width="25%" height="1" bgColor="#e4e9f8"> <p><b>Min:</b> 
              <input type="text" name="P_primer_min" size="20" value="18">
            </p></td>
          <td width="25%" height="1" bgColor="#e4e9f8"> <p><b>Opt:</b> 
              <input type="text" name="p_primer_opt" size="20" value="21">
            </p></td>
          <td width="25%" height="1" bgColor="#e4e9f8"> <p><b>Max:</b> 
              <input type="text" name="p_primer_max" size="20" value="27">
            </p></td>
        </tr>
        <tr> 
          <td width="25%" height="26"  bgcolor="#b8c6ed" valign="top"><font color="#000080"><b>Primer 
            Tm (°C)</b></font></td>
          <td width="25%" height="26"  bgcolor="#b8c6ed"> <p><b>Min: </b> 
              <input type="text" name="p_primer_tm_min" size="20" value="57">
            </p></td>
          <td width="25%" height="26"  bgcolor="#b8c6ed"> <p><b>Opt:</b> 
              <input type="text" name="p_primer_tm_opt" size="20" value="60">
            </p></td>
          <td width="25%" height="26"  bgcolor="#b8c6ed"> <p><b>Max:</b>&nbsp; 
              <input type="text" name="p_primer_tm_max" size="20" value="63">
            </p></td>
        </tr>
        <tr> 
          <td width="25%" height="1" bgColor="#e4e9f8" valign="top"><font color="#000080"><b>Primer 
            GC%</b></font></td>
          <td width="25%" height="1" bgColor="#e4e9f8" valign="top"> <p><b>Min:</b>&nbsp; 
              <input type="text" name="p_primer_gc_min" size="20" value="30">
            </p></td>
          <td width="25%" height="1" bgColor="#e4e9f8" valign="top"> <p><b>Opt:</b> 
              <input type="text" name="p_primer_gc_opt" size="20" value="50">
            </p></td>
          <td width="25%" height="1" bgColor="#e4e9f8" valign="top"> <p><b>Max:</b>&nbsp; 
              <input type="text" name="p_primer_gc_max" size="20" value="70">
            </p></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td width="57%" height="1"></td>
    <td width="43%" height="1"></td>
  </tr>
  <tr><td colspan="2">&nbsp;</td></tr>
  <tr> 
    <td height="39" colspan="2" bgcolor="#1145A6"><b><font size="4" color="white">Sequencing 
      Parameters</font></b></td>
  </tr>
  <tr> 
    <td height=44 bgColor="#e4e9f8" valign="middle"> <p><IMG SRC="greenarrow.gif"   HSPACE=5><b><font color="#000080">Check 
        here if NO 5p Universal Primer is used </font></b> <font color="#000080"> 
        <input type=checkbox name="universal_primer_5p" id="universal_primer_5p" value="-1" onClick="update5pUPrimer(this,this.checked)"></br> 
        <b>(<font size =2>The most upstream forward PCR primer is used when No 
        5p Universal Primer is </font></b></font><b><font color="#000080" size =2>checked)</font></b></td>
    <td height=44 bgColor="#e4e9f8" valign="middle"> <p><IMG SRC="greenarrow.gif"   HSPACE=5><b><font color="#000080">Check 
        here if NO 3p Universal Primer is used</font> </b> 
        <input type=checkbox name=universal_primer_3p value="-1" onClick="update3pUniversalPrimer(this,this.checked)">
      </p></td>
  </tr>
  <tr> 
    <td width="57%" height="44"  bgcolor="#b8c6ed" valign="top"><b><font color="#000080">Distance 
      between 5p Universal Primer and START codon</font></b><font color="#000080">&nbsp;&nbsp;&nbsp; 
      <font size="2"><b>(For a left primer, primer start position is the position 
      of the leftmost base)</b></font></font></td>
    <td width="43%" height="44"  bgcolor="#b8c6ed"> <p> 
        <input type="text" name="p_upstream_distance_view"  onchange='onChangeValue(this, this.value)' size="20" value="100" id="p_upstream_distance_view">
        <input type="hidden" name="p_upstream_distance"  value="100" id="p_upstream_distance" >
        <strong>bases</strong></p></td>
  </tr>
  <tr> 
    <td width="57%" height="44" bgColor="#e4e9f8" valign="top"><font color="#000080"><b>Distance 
      between 3p Universal Primer and STOP codon&nbsp;&nbsp; <font size="2">(For 
      a right primer, primer start position is the position of the rightmost base)&nbsp;&nbsp;</font></b></font></td>
    <td width="43%" height="44"bgColor="#e4e9f8"> <p> 
        <input type="text" name="p_downstream_distance_view" size="20" value="100" id="p_downstream_distance_view" onchange='onChangeValue(this, this.value)'>
        <input type="hidden" name="p_downstream_distance"  value="100" id="p_downstream_distance" onchange='onChangeValue(this, this.value)'>
        <strong>bases</strong></p></td>
  </tr>
  <tr> 
    <td width="57%" height="44"  bgcolor="#b8c6ed" valign="top"><font color="#000080"><b>Estimated 
      high quality read length (ERL)</b></font></td>
    <td width="43%" height="44"  bgcolor="#b8c6ed"> <p> 
        <input type="text" name="p_single_read_length" size="20" value="400">
        <strong>bases</strong></p></td>
  </tr>
  <tr> 
    <td width="57%" height="3" bgColor="#e4e9f8" valign="top"><font color="#000080"><b>Window 
      size for testing primers</b></font></td>
    <td width="43%" height="3" bgColor="#e4e9f8"> <p> 
        <input type="text" name="p_buffer_window_len" size="20" value="50">
        <strong> bases</strong></p></td>
  </tr>
<tr> 
    <td width="57%" height="3" bgColor="#b8c6ed" valign="top"><font color="#000080"><b>
Distance between sequencing primer and start of high quality read length</b></font></td>
    <td width="43%" height="3" bgColor="#b8c6ed"> <p> 
        <input type="text" name="P_EST_SEQ" size="20" value="50">
        <strong>bases</strong></p></td>
  </tr>
  <tr> 
    <td width="57%" height="3" align="center" valign="top"  bgcolor="#e4e9f8"> 
      <p align="left"><font color="#000080"><b>Number of strands to sequence</b></font></td>
    <td  height="3" align="center" valign="bottom"  bgcolor="#e4e9f8"> 
      <p align="left"> 
        <input type="radio" value="<%= Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD %>" name="p_number_of_strands" checked>
        <b>Single Strand</b> (Coding strand, forward primers only)</p>
      <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE %>" >
        <b>Single Strand</b> (Compliment to coding strand, reverse primers only)</p>
      <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_BOTH_STRAND %>" >
        <b>Both Strands</b> (Both forward and reverse primers)
     <p align="left"> 
        <input type="radio" name="p_number_of_strands" value="<%= Primer3Wrapper.WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE %>" >
        <b>Both Strands</b> (Both forward and reverse primers, double coverage)</td>
  </tr>



  <tr> 
    <td colspan="3" height="3" align="center" valign="bottom"></td>
  </tr>
  <tr> 
    <td colspan="3" height="1" align="center" valign="bottom"> <p> 
        <input type="submit" value="Submit" name="B1">
        <input type="reset" value="Reset" name="B2">
      </p>
</table>



</html:form>
<HR>


</body>

</html>


