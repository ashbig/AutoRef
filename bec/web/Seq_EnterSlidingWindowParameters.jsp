<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>

<title>Sequence Quality Trimming Parameters</title>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="application_styles.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>
<body>


<!--<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="90%">
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
<input name="forwardName" type="hidden" value="<%=SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT %>" >

<table border="0" cellpadding="2" cellspacing="2" width="90%" align=center>
  <tr> 
    <td colspan =2><div align="right"><b> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.TRIM_SLIDING_WINDOW_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.TRIM_SLIDING_WINDOW_SPEC_INT%>"> 
        View All </a></b> </div>
      <p> 
      <p> 
      
      </td>
  </tr>
  <tr> 
    <td colspan="2"> <p> <font  size="4"> <b>Set Name</b></font> 
        <input type="text" name="SET_NAME" size="53" value="">
      <p> </td>
  </tr>

 <tr> 
    <td bgColor="#e4e9f8" width="70%"><b><font color="#000080">Phred base score defining high quality sequence:</font></b> </td>
    <td bgColor="#e4e9f8"><input name="SW_Q_CUTT_OFF" type="text" 
    id="SW_Q_CUTT_OFF"
    value="<%= SlidingWindowTrimmingSpec.QUALITY_CUTT_OFF %>" width="50" 
    onBlur="checkNumeric(this,5,99,'','','');" ></td>
  </tr>
<tr><td colspan='2'>&nbsp</TD></TR>
  <tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Sliding window size for low-quality trimming:</font></b> </td>
    <td bgColor="#b8c6ed"><input name="SW_Q_WINDOW_SIZE" type="text" id="SW_Q_WINDOW_SIZE"
    value="<%= SlidingWindowTrimmingSpec.QUALITY_WINDOW_SIZE %>" width="50" >nt</td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Max number of low-quality bases allowed:</font></b> </td>
    <td bgColor="#e4e9f8"><input name="SW_Q_NUMBER_LOW_QUALITY_BASES" type="text" id="SW_Q_NUMBER_LOW_QUALITY_BASES"
    value="<%= SlidingWindowTrimmingSpec.QUALITY_NUMBER_LOW_QUALITY_BASES %>" width="50" 
   onBlur="checkNumeric(this,1,50,'','','');"></td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Max number of consecutive low-quality bases allowed (not implemented):</font></b> </td>
    <td bgColor="#b8c6ed"><input name="SW_Q_N_LOW_QUALITY_BASES_CONQ" type="text" 
    id="SW_Q_N_LOW_QUALITY_BASES_CONQ"
    value="<%= SlidingWindowTrimmingSpec.QUALITY_NUMBER_LOW_QUALITY_BASES_CONQ %>" width="50" 
onBlur="checkNumeric(this,1,50,'','','');"></td>
  </tr>

<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Sliding window size for ambiguous base trimming (not implemented yet):</font></b> </td>
    <td bgColor="#e4e9f8"><input name="SW_A_WINDOW_SIZE" type="text" 
    id="SW_A_WINDOW_SIZE"     value="<%= SlidingWindowTrimmingSpec.AMBIQUATY_WINDOW_SIZE %>" width="50" 
onBlur="checkNumeric(this,1,50,'','','');"></td>
  </tr>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Max number of ambiguous bases allowed (not implemented yet):</font></b> </td>
    <td bgColor="#b8c6ed"><input name="SW_A_NUMBER_BASES" type="text" 
    id="SW_A_NUMBER_BASES"     value="<%= SlidingWindowTrimmingSpec.AMBIQUATY_NUMBER_BASES %>" width="50" 
onBlur="checkNumeric(this,1,50,'','','');"></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Max number of consecutive ambiguous bases allowed (not implemented yet):</font></b> </td>
    <td bgColor="#e4e9f8"><input name="SW_A_N_LOW_QUALITY_BASES_CONQ" type="text" 
    id="SW_A_N_LOW_QUALITY_BASES_CONQ"     value="<%= SlidingWindowTrimmingSpec.AMBIQUATY_NUMBER_LOW_QUALITY_BASES_CONQ %>"  
onBlur="checkNumeric(this,1,50,'','','');"></td>
  </tr>
<TR><TD COlspan=2>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Minimum distance between sequence stretches needed to treat them separately (Gap Mappere, Low Confidence Finder):</font></b> </td>
    <td bgColor="#b8c6ed"><input name="SW_MIN_DISTANCE_BTW_CONTIGS" type="text" 
    id="SW_MIN_DISTANCE_BTW_CONTIGS"     value="<%= SlidingWindowTrimmingSpec.MIN_DISTANCE_BETWEEN_CONTIGS %>"  
onBlur="checkNumeric(this,1,1000,'','','');"></td>
  </tr>
<tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Minimum contig length ( used by GAP Mapper only) :</font></b> </td>
    <td bgColor="#e4e9f8"><input name="SW_MIN_CONTIG_LENGTH" type="text" 
    id="SW_MIN_CONTIG_LENGTH"     value="<%= SlidingWindowTrimmingSpec.MIN_CONTIG_LENGTH %>"  
onBlur="checkNumeric(this,1,1000,'','','');"></td>
  </tr>
<TR><TD COlspan=2>&nbsp</TD></TR>
<tr> 
    <td bgColor="#b8c6ed"><b><font color="#000080">Sequence Trimming Type:</font></b> </td>
    <td bgColor="#b8c6ed">
<SELECT NAME='SW_TRIM_TYPE' id='SW_TRIM_TYPE'>
    <OPTION VALUE='<%= SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW_NODISC %>'>Moving Window with extension for no discrepancies regions
    <OPTION VALUE='<%= SlidingWindowTrimmingSpec.TRIM_TYPE_NONE  %>'>No Trimming
    <OPTION VALUE='<%= SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW  %>'>Moving Window
</select>  </td>
</tr>
   
  <tr><td>&nbsp;</td></tr>
  <tr><td colspan="2">
  <div align="center">
  <input type="submit" name="Submit" value="Submit">
  &nbsp; 
  <input type="reset" name="Reset" value="Reset">
</div>
  </td></tr>
</table>


<p>

</html:form>
</p>
</body>

