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
<link href="application_styles.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Create new set of parameters for Biological Evaluation of Clones</font>
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
<input name="forwardName" type="hidden" value="<%=FullSeqSpec.FULL_SEQ_SPEC_INT%>">
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan =2><div align="right"><b> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT%>"> 
        View All </a></b> </div>
      <p> 
      <p> 
      <p></p>
      <p></p>
      <p></p></td>
  </tr>
  <tr> 
    <td colspan="2"> <p> <font  size="4"> <b>Set Name</b></font> 
        <input type="text" name="SET_NAME" size="53" value=""><p> </td>
  </tr>
  <tr>   <td  bgcolor="#e4e9f8" width="64%"><b>Phred base score (high quality cut-off) 
      </b></td>
    <td bgcolor="#e4e9f8"> <input name="FS_PHRED_CUT_OFF" type="text" id="FS_PHRED_CUT_OFF" value="25" size="10" onBlur="checkNumeric(this,5,99,'','','');"> 
    </td>
  </tr>
 <!-- <tr> 
    <td bgcolor="#b8c6ed"> <b>Phred base score (low quality cut-off) </b></td>
    <td bgcolor="#b8c6ed"> <input name="FS_PHRED_LOW_CUT_OFF" type="text" id="FS_PHRED_LOW_CUT_OFF" value="10" size="10" onBlur="checkNumeric(this,5,99,'','','');"> 
    </td>
  </tr>-->
  
 <tr><td colspan="2">&nbsp; </td></tr>
 <tr> <td colspan=2><b>Maximum acceptable number of discrepancies (gene region)</b> 
 <P> <input type='radio' name='isMissense' value='1' checked>Process conservative & non-conservative substitutions together (set parameters for Missense substitution, parameters for conservative & non-conservative substitutions will not be taken into account)
 <P><input type='radio' name='isMissense' value='0'>Process conservative & non-conservative substitutions separately (set parameters for Conservative and Non-conservative substitution, parameters for Missense substitution will not be taken into account)
      
     
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Threshold</font></strong></div></td>
          <td bgcolor="#1145A6" colspan=2><div align="center"><strong><font color="#FFFFFF">PASS 
              </font></strong></div></td>
          <td bgcolor="#1145A6"colspan=2><div align="center"><strong><font color="#FFFFFF">FAIL 
              </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Ignor 
              if polymorphism </font></strong></div></td>
        </tr>
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base 
              Confidence </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
          <td bgcolor="#1145A6">&nbsp;</td>
        </tr>
        <tr> 
          <td width="44%" bgcolor="#b8c6ed" ><strong><font color="#000080">Silent 
            mutation</font></strong></td>
          <td width="16%" bgcolor="#b8c6ed"><div align="center"> 
              <input name="FS_S_PASS_H" type="input" id="FS_S_PASS_H" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="14%"bgcolor="#b8c6ed"><div align="center"> 
              <input name="FS_S_PASS_L" type="input" id="FS_S_PASS_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="13%"bgcolor="#b8c6ed"><div align="center"> 
              <input name="FS_S_FAIL_H" type="input" id="FS_S_FAIL_H" value="15" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="13%"bgcolor="#b8c6ed"><div align="center"> 
              <input name="FS_S_FAIL_L" type="input" id="FS_S_FAIL_L" value="15" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="13%"bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox" name="FS_S_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><b><strong><font color="#000080">Missense 
            substitution</font></strong></b></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_MISS_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_MISS_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_MISS_FAIL_H" type="input" id="FS_S_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_MISS_FAIL_L" type="input" id="FS_S_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"> <div align="center"> <font color="#000080"><strong><b> 
              <input type="checkbox" name="FS_MISS_POLM" value="1" >
              </b></strong></font></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><b><strong><font color="#000080">Conservative 
            substitution</font></strong></b></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_C_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_C_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_C_FAIL_H" type="input" id="FS_S_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_C_FAIL_L" type="input" id="FS_S_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#b8c6ed"> <div align="center"> <font color="#000080"><strong><b> 
              <input type="checkbox" name="FS_C_POLM" value="1" >
              </b></strong></font></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Nonconservative 
            substitution</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_NC_PASS_H" type="input" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_NC_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_NC_FAIL_H" type="input" id="FS_NC_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_NC_FAIL_L" type="input" id="FS_NC_FAIL_L" value="10" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox" name="FS_NC_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_FR_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_FR_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_FR_FAIL_H" type="input" id="FS_FR_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_FR_FAIL_L" type="input" id="FS_FR_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox" name="FS_FR_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Inframe deletion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_IDEL_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_IDEL_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_IDEL_FAIL_H" type="input" id="FS_IDEL_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_IDEL_FAIL_L" type="input" id="FS_IDEL_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox" name="FS_IDEL_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Inframe insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_IINS_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_IINS_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_IINS_FAIL_H" type="input" id="FS_IINS_FAIL_H" value="3" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_IINS_FAIL_L" type="input" id="FS_IINS_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox" name="FS_IINS_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Truncation</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_TRANC_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_TRANC_PASS_L" type="input" id="FS_TRANC_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_TRANC_FAIL_H" type="input" id="FS_TRANC_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_TRANC_FAIL_L" type="input" id="FS_TRANC_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox" name="FS_TRANCS_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">No translation (e.g., 
            no ATG)</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_NOTRANSLATION_PASS_H" type="input" id="FS_NOTRANSLATION_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_NOTRANSLATION_PASS_L" type="input" id="FS_NOTRANSLATION_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_NOTRANSLATION_FAIL_H" type="input" id="FS_NOTRANSLATION_FAILS_H" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_NOTRANSLATION_FAIL_L" type="input" id="FS_NOTRANSLATION_FAIL_L" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox" name="FS_NOTRANSLATION_POLM" value="1">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Post-elongation(e.g., 
            no stop codon)</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_PELONG_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_PELONG_PASS_L" type="input" id="FS_PELONG_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_PELONG_FAIL_H" type="input" id="FS_PELONG_FAIL_H" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="FS_PELONG_FAIL_L" type="input" id="FS_PELONG_FAIL_L" value="2" size="5" onBlur="checkNumeric(this,0,2,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox" name="FS_PELONG_POLM"  value="1">
            </div></td>
        </tr>
      </table></td>
  </tr>
 <tr><td colspan="2">&nbsp; </td></tr>
   <tr><td colspan=2><b>Maximum acceptable number of discrepancies (linker region):</b> 
      <p></p>
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Threshold</font></strong></div></td>
          <td bgcolor="#1145A6" colspan=2><div align="center"><strong><font color="#FFFFFF">PASS 
              </font></strong></div></td>
          <td bgcolor="#1145A6"colspan=2><div align="center"><strong><font color="#FFFFFF">FAIL 
              </font></strong></div></td>
        </tr>
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base 
              Confidence </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgcolor="#e4e9f8" ><strong><font color="#000080">5' 
            substitution </font></strong></td>
          <td width="16%" bgcolor="#e4e9f8"><div align="center"> 
              <input name="FS_5S_PASS_H" type="input" id="FS_5S_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="14%"bgcolor="#e4e9f8"><div align="center"> 
              <input name="FS_5S_PASS_L" type="input" id="FS_5S_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
              <input name="FS_5S_FAIL_H" type="input" id="FS_5S_FAIL_H" value="4" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
              <input name="FS_5S_FAIL_L" type="input" id="FS_5S_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><b><strong><font color="#000080">5' deletion/insertion</font></strong></b></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_5DI_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_5DI_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_5DI_FAIL_H" type="input" id="FS_5DI_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <input name="FS_5DI_FAIL_L" type="input" id="FS_5DI_FAIL_L" value="6" size="5" onBlur="checkNumeric(this,0,100,'','','');">
              </b></strong></font></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3S_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3S_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3S_FAIL_H" type="input" id="FS_3S_FAIL_H" value="4" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3S_FAIL_L" type="input" id="FS_3S_FAIL_L" value="9" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' deletion/insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3DI_PASS_H" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3DI_PASS_L" type="input" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3DI_FAIL_H" type="input" id="FS_3DI_FAIL_H" value="1" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="FS_3DI_FAIL_L" type="input" id="FS_3DI_FAIL_L" value="6" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
        </tr>
      </table></td>
  </tr>
  <tr><td colspan=2>&nbsp; </td></tr>
<tr>
    <td colspan="2"> <p><b> Maximum acceptable number of discrepancies 
        introduced by ambiquous bases: </b>
		<P><input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show 
      
	  <DIV ID="divShowHide" STYLE="
  position:relative;
  clip:rect(0px 120px 120px 0px);
 ">
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Threshold</font></strong></div></td>
          <td bgcolor="#1145A6" colspan=2><div align="center"><strong><font color="#FFFFFF">PASS 
              </font></strong></div></td>
          <td bgcolor="#1145A6"colspan=2><div align="center"><strong><font color="#FFFFFF">FAIL 
              </font></strong></div></td>
        </tr>
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base 
              Confidence </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgcolor="#e4e9f8" ><strong><font color="#000080">Start 
            codon substitution</font></strong></td>
          <td width="16%" bgcolor="#e4e9f8"><div align="center"> 
                <input name="FS_NSTART_PASS_H" type="input" id="FS_NSTART_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td width="14%"bgcolor="#e4e9f8"><div align="center"> 
                <input name="FS_NSTART_PASS_L" type="input" id="FS_NSTART_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
                <input name="FS_NSTART_FAIL_H" type="input" id="FS_NSTART_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
                <input name="FS_NSTART_FAIL_L" type="input" id="FS_NSTART_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><b><strong><font color="#000080">Stop codon substitution</font></strong></b></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
                <input name="FS_NSTOP_PASS_H" type="input" id="FS_NSTOP_PASS_H" value="0" size="5" >
                 </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
                <input name="FS_NSTOP_PASS_L" type="input" id="FS_NSTOP_PASS_L" value="0" size="5" >
                 </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
                <input name="FS_NSTOP_FAIL_H" type="input" id="FS_NSTOP_FAIL_H" value="19" size="5" >
                 </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
                <input name="FS_NSTOP_FAIL_L" type="input" id="FS_NSTOP_FAIL_L" value="99" size="5" >
                 </b></strong></font></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Substituttion cds 
            region </font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NCDS_PASS_H"  type="input" id="FS_NCDS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NCDS_PASS_L" type="input" id="FS_NCDS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NCDS_FAIL_H"  type="input" id="FS_NCDS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NCDS_FAIL_L" type="input" id="FS_NCDS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift Insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NFRAME_PASS_H" type="input" id="FS_NFRAME_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NFRAME_PASS_L" type="input" id="FS_NFRAME_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NFRAME_FAIL_H" type="input" id="FS_NFRAME_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NFRAME_FAIL_L" type="input" id="FS_NFRAME_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
		<tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Inframe Insertion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NINFRAME_PASS_H"  type="input" id="FS_NINFRAME_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NINFRAME_PASS_L" type="input" id="FS_NINFRAME_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_NINFRAME_FAIL_H"  type="input" id="FS_NINFRAME_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_NINFRAME_FAIL_L" type="input" id="FS_NINFRAME_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Substitution 5' 
            linker region</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N5SUB_PASS_H" type="input" id="FS_N5SUB_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N5SUB_PASS_L" type="input" id="FS_N5SUB_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N5SUB_FAIL_H" type="input" id="FS_N5SUB_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N5SUB_FAIL_L" type="input" id="FS_N5SUB_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
		<tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Insertion 5' linker 
            region </font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N5INS_PASS_H" type="input" id="FS_N5INS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N5INS_PASS_L" type="input" id="FS_N5INS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N5INS_FAIL_H" type="input" id="FS_N5INS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N5INS_FAIL_L" type="input" id="FS_N5INS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Substitution 3' 
            linker region</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N3SUB_PASS_H" type="input" id="FS_N3SUB_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N3SUB_PASS_L" type="input" id="FS_N3SUB_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N3SUB_FAIL_H" type="input" id="FS_N3SUB_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N3SUB_FAIL_L" type="input" id="FS_N3SUB_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
		<tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Insertion 3' linker 
            region </font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N3INS_PASS_H" type="input" id="FS_N3INS_PASS_H" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N3INS_PASS_L" type="input" id="FS_N3INS_PASS_L" value="0" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
                <input name="FS_N3INS_FAIL_H" type="input" id="FS_N3INS_FAIL_H" value="19" size="5" onBlur="checkNumeric(this,0,100,'','','');">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
                <input name="FS_N3INS_FAIL_L" type="input" id="FS_N3INS_FAIL_L" value="99" size="5" onBlur="checkNumeric(this,0,100,'','','');"> 
            </div></td>
        </tr>
        
      </table></div>
  </td></tr>
  <tr> 
    <td > <P></P>
      &nbsp;</td>
  </tr>
  <!--<tr> 
    <td  nowrap bgColor="#b8c6ed"><strong><font color="#000080">Maximum number 
      of ambiquous bases per 100 bases:</font></strong></td>
    <td  align="left"bgColor="#b8c6ed" nowrap><input name="FS_N_100" type="input" id="FS_N_100" value="10" onBlur="checkNumeric(this,0,10,'','','');"></td>
  </tr>
  <tr> 
    <td width="64%"  nowrap bgColor="#e4e9f8"> <strong><font color="#000080">Max 
      number of consequative ambiquous bases :</font></strong></td>
    <td width="26%"  align="left" nowrap bgColor="#e4e9f8"><input name="FS_N_ROW" type="input" id="FS_N_ROW" value="3" onBlur="checkNumeric(this,0,10,'','','');"></td>
  </tr></tr>-->
</table>
<p> 
<div align="center"> 
  <input type="submit" name="Submit" value="Submit">
  &nbsp; 
  <input type="reset" name="Reset" value="Reset">
</div>
</html:form> 
<HR>


</body>
</html>
