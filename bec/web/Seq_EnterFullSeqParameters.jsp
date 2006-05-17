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

<title>Clone Bioevaluation Parameters</title>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >



<!--<div align="center">
 <center>
 <table border="0" cellpadding="0" cellspacing="0" width="80%">
  <tr>
   <td width="100%"><html:errors/></td>
  </tr>	<tr>
    <td><i>If you are not sure about certain parameter settings, leave them 
     unchanged </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
     </i></td>   </tr> </table>
 </center>
</div>-->

 
<html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=FullSeqSpec.FULL_SEQ_SPEC_INT%>">
<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
 <tr> 
  <td colspan =2><div align="right"><b> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
    View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT%>"> 
    View All </a></b> </div>
   <p>    <p>    <p></p>   <p></p>   <p></p></td>
 </tr>
 <tr> 
  <td colspan="2"> <p> <font size="4"> <b>Set Name</b></font> 
    <input type="text" name="SET_NAME" size="53" value=""><p> </td> </tr>
 
 <% String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;%>
 
 <tr><td colspan="2">&nbsp; </td></tr>
 <tr> <td colspan=2><b>Maximum acceptable number of discrepancies (insert region)</b> 
 <P> <input type='radio' name='isMissense' value='1' checked>Process conservative & non-conservative substitutions together (System will use the penalty values for missense substitutions; system will ignore values for conservative and non-conservative substitutions)
 <P><input type='radio' name='isMissense' value='0'>Process conservative & non-conservative substitutions separately (System will use the penalty values for conservative and non-conservative substitutions; system will ignore values for missense substitutions)
   
   
   <table width="85%" border="0" align="center">
    <tr class='headerRow' height=42>     
     <td ><div align="center">Threshold</div></td>
     <td colspan=2><div align="center">PASS        </div></td>
     <td colspan=2><div align="center">FAIL        </div></td>
     <td ><div align="center">Ignore if polymorphism </div></td>
    </tr>
    <tr class='headerRow'>    
        <td ><div align="right">Base        Confidence </div></td>    
        <td ><div align="center">High </div></td> 
        <td ><div align="center">Low        </div></td>
        <td ><div align="center">High      </div></td>
        <td ><div align="center">Low        </div></td>
        <td ><div align="center">&nbsp       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td width="44%"   > Silent       mutation</td>
     <td width="16%"  ><div align="center">        <input name="FS_S_PASS_H" type="input" id="FS_S_PASS_H" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="14%" ><div align="center">        <input name="FS_S_PASS_L" type="input" id="FS_S_PASS_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="13%" ><div align="center">        <input name="FS_S_FAIL_H" type="input" id="FS_S_FAIL_H" value="15" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="13%" ><div align="center">        <input name="FS_S_FAIL_L" type="input" id="FS_S_FAIL_L" value="15" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="13%"> <div align="center">        <input type="checkbox" name="FS_S_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  ><b> Missense       substitution</b></td>
     <td  ><div align="center">        <input name="FS_MISS_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_MISS_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">       <input name="FS_MISS_FAIL_H" type="input" id="FS_S_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_MISS_FAIL_L" type="input" id="FS_S_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_MISS_POLM" value="1" >      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  ><b> Conservative 
      substitution</b></td>
     <td  ><div align="center">        <input name="FS_C_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">    </div></td>
     <td  ><div align="center">  <input name="FS_C_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">   <input name="FS_C_FAIL_H" type="input" id="FS_S_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">   <input name="FS_C_FAIL_L" type="input" id="FS_S_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">  <input type="checkbox" name="FS_C_POLM" value="1" >      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Nonconservative       substitution</td>
     <td  ><div align="center">        <input name="FS_NC_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NC_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NC_FAIL_H" type="input" id="FS_NC_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NC_FAIL_L" type="input" id="FS_NC_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_NC_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Frameshift</td>
     <td  ><div align="center">        <input name="FS_FR_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_FR_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_FR_FAIL_H" type="input" id="FS_FR_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_FR_FAIL_L" type="input" id="FS_FR_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_FR_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Inframe deletion</td>
     <td  ><div align="center">        <input name="FS_IDEL_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IDEL_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IDEL_FAIL_H" type="input" id="FS_IDEL_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IDEL_FAIL_L" type="input" id="FS_IDEL_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_IDEL_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Inframe insertion</td>
     <td  ><div align="center">        <input name="FS_IINS_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IINS_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IINS_FAIL_H" type="input" id="FS_IINS_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_IINS_FAIL_L" type="input" id="FS_IINS_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_IINS_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Truncation</td>
     <td  ><div align="center">        <input name="FS_TRANC_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_TRANC_PASS_L" type="input" id="FS_TRANC_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_TRANC_FAIL_H" type="input" id="FS_TRANC_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_TRANC_FAIL_L" type="input" id="FS_TRANC_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_TRANC_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > No translation (e.g.,       no ATG)</td>
     <td  ><div align="center">        <input name="FS_NOTRANSLATION_PASS_H" type="input" id="FS_NOTRANSLATION_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NOTRANSLATION_PASS_L" type="input" id="FS_NOTRANSLATION_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NOTRANSLATION_FAIL_H" type="input" id="FS_NOTRANSLATION_FAILS_H" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_NOTRANSLATION_FAIL_L" type="input" id="FS_NOTRANSLATION_FAIL_L" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_NOTRANSLATION_POLM" value="1">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Post-elongation(e.g.,       no stop codon)</td>
     <td  ><div align="center">        <input name="FS_PELONG_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">
      </div></td>
     <td  ><div align="center">        <input name="FS_PELONG_PASS_L" type="input" id="FS_PELONG_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_PELONG_FAIL_H" type="input" id="FS_PELONG_FAIL_H" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_PELONG_FAIL_L" type="input" id="FS_PELONG_FAIL_L" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">      </div></td>
     <td  > <div align="center">        <input type="checkbox" name="FS_PELONG_POLM" value="1">      </div></td>
    </tr>
   </table></td>
 </tr>
 <tr><td colspan="2">&nbsp; </td></tr>
  <tr><td colspan=2><b>Maximum acceptable number of discrepancies (linker region):</b> 
   <p></p>
   <table width="85%" border="0" align="center">
    <tr class='headerRow'> 
     <td ><div align="center">Threshold</div></td>
     <td colspan=2><div align="center">PASS        </div></td>
     <td colspan=2><div align="center">FAIL        </div></td>
    </tr>
    <tr class='headerRow'> 
     <td ><div align="right">Base        Confidence </div></td>
     <td ><div align="center">High        </font> </strong></div></td>
     <td ><div align="center">Low        </div></td>
     <td ><div align="center">High        </font> </strong></div></td>
     <td ><div align="center">Low        </div></td>
    </tr>
    <tr class=<%= row_class[row_count % 2]%>> 
     <td width="44%"   > 5'       substitution </td>
     <td width="16%"  ><div align="center">        <input name="FS_5S_PASS_H" type="input" id="FS_5S_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="14%" ><div align="center">        <input name="FS_5S_PASS_L" type="input" id="FS_5S_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="13%" ><div align="center">        <input name="FS_5S_FAIL_H" type="input" id="FS_5S_FAIL_H" value="4" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="13%" ><div align="center">        <input name="FS_5S_FAIL_L" type="input" id="FS_5S_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > 5' deletion/insertion</td>
     <td  ><div align="center">    <input name="FS_5DI_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">     <input name="FS_5DI_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center"><input name="FS_5DI_FAIL_H" type="input" id="FS_5DI_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">   <input name="FS_5DI_FAIL_L" type="input" id="FS_5DI_FAIL_L" value="6" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count % 2]%>> 
     <td  > 3' substitution</td>
     <td  ><div align="center">        <input name="FS_3S_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3S_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3S_FAIL_H" type="input" id="FS_3S_FAIL_H" value="4" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3S_FAIL_L" type="input" id="FS_3S_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > 3' deletion/insertion</td>
     <td  ><div align="center">        <input name="FS_3DI_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3DI_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3DI_FAIL_H" type="input" id="FS_3DI_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">        <input name="FS_3DI_FAIL_L" type="input" id="FS_3DI_FAIL_L" value="6" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
    </tr>
   </table></td>
 </tr>
 <tr><td colspan=2>&nbsp; </td></tr>
<tr>
  <td colspan="2"> <p><b> Maximum acceptable number of discrepancies 
    introduced by ambiguous bases: </b>
		<P><input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show 
   
	 <DIV ID="divShowHide" STYLE="
 position:relative;
 clip:rect(0px 120px 120px 0px);
 ">
   <table width="85%" border="0" align="center">
    <tr class='headerRow'> 
     <td ><div align="center">Threshold</div></td>
     <td colspan=2><div align="center">PASS        </div></td>
     <td colspan=2><div align="center">FAIL        </div></td>
    </tr>
    <tr class='headerRow'> 
     <td ><div align="right">Base        Confidence </div></td>
     <td ><div align="center">High      </div></td>
     <td ><div align="center">Low        </div></td>
     <td ><div align="center">High       </div></td>
     <td ><div align="center">Low        </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td width="44%"   > Start 
      codon substitution</td>
     <td width="16%"  ><div align="center">         <input name="FS_NSTART_PASS_H" type="input" id="FS_NSTART_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td width="14%" ><div align="center">         <input name="FS_NSTART_PASS_L" type="input" id="FS_NSTART_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td width="13%" ><div align="center">         <input name="FS_NSTART_FAIL_H" type="input" id="FS_NSTART_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td width="13%" ><div align="center">         <input name="FS_NSTART_FAIL_L" type="input" id="FS_NSTART_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  ><b> Stop codon substitution</b></td>
     <td  ><div align="center">         <input name="FS_NSTOP_PASS_H" type="input" id="FS_NSTOP_PASS_H" value="0" size="5" >
      </div></td>
     <td  ><div align="center"><input name="FS_NSTOP_PASS_L" type="input" id="FS_NSTOP_PASS_L" value="0" size="5" >
        </div></td>
     <td  ><div align="center">    <input name="FS_NSTOP_FAIL_H" type="input" id="FS_NSTOP_FAIL_H" value="19" size="5" >
      </div></td>
     <td  ><div align="center">   <input name="FS_NSTOP_FAIL_L" type="input" id="FS_NSTOP_FAIL_L" value="99" size="5" >
       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Substitution cds       region </td>
     <td  ><div align="center">         <input name="FS_NCDS_PASS_H" type="input" id="FS_NCDS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_NCDS_PASS_L" type="input" id="FS_NCDS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NCDS_FAIL_H" type="input" id="FS_NCDS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_NCDS_FAIL_L" type="input" id="FS_NCDS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Frameshift insertion</td>
     <td  ><div align="center">         <input name="FS_NFRAME_PASS_H" type="input" id="FS_NFRAME_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NFRAME_PASS_L" type="input" id="FS_NFRAME_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NFRAME_FAIL_H" type="input" id="FS_NFRAME_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NFRAME_FAIL_L" type="input" id="FS_NFRAME_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
		<tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Inframe insertion</td>
     <td  ><div align="center">         <input name="FS_NINFRAME_PASS_H" type="input" id="FS_NINFRAME_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_NINFRAME_PASS_L" type="input" id="FS_NINFRAME_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NINFRAME_FAIL_H" type="input" id="FS_NINFRAME_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_NINFRAME_FAIL_L" type="input" id="FS_NINFRAME_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Substitution 5'       linker region</td>
     <td  ><div align="center">         <input name="FS_N5SUB_PASS_H" type="input" id="FS_N5SUB_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N5SUB_PASS_L" type="input" id="FS_N5SUB_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N5SUB_FAIL_H" type="input" id="FS_N5SUB_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N5SUB_FAIL_L" type="input" id="FS_N5SUB_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
		<tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Insertion 5' linker       region </td>
     <td  ><div align="center">         <input name="FS_N5INS_PASS_H" type="input" id="FS_N5INS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_N5INS_PASS_L" type="input" id="FS_N5INS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N5INS_FAIL_H" type="input" id="FS_N5INS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_N5INS_FAIL_L" type="input" id="FS_N5INS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
    <tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Substitution 3'       linker region</td>
     <td  ><div align="center">         <input name="FS_N3SUB_PASS_H" type="input" id="FS_N3SUB_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N3SUB_PASS_L" type="input" id="FS_N3SUB_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N3SUB_FAIL_H" type="input" id="FS_N3SUB_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N3SUB_FAIL_L" type="input" id="FS_N3SUB_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
		<tr class=<%= row_class[row_count++ % 2]%>> 
     <td  > Insertion 3' linker       region </td>
     <td  ><div align="center">        <input name="FS_N3INS_PASS_H" type="input" id="FS_N3INS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_N3INS_PASS_L" type="input" id="FS_N3INS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
     <td  ><div align="center">         <input name="FS_N3INS_FAIL_H" type="input" id="FS_N3INS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">      </div></td>
     <td  ><div align="center">         <input name="FS_N3INS_FAIL_L" type="input" id="FS_N3INS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');">       </div></td>
    </tr>
    
   </table></div>
 </td></tr>
 <tr>   <td > <P></P>   &nbsp;</td> </tr>
 
</table>
<p> 
<div align="center"> 
 <input type="submit" name="Submit" value="Submit"> &nbsp; 
 <input type="reset" name="Reset" value="Reset">
</div>
</html:form> 
<HR>


</body>
</html>
