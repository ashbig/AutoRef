<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>

<html>

<head>

<title>Primer Calculating Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/>: Set parameters for Biological Evaluation 
  of Clones</h2>
<html:errors/>
 <html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=FullSeqSpec.FULL_SEQ_SPEC_INT%>">
<HR>
<h3>Create new set of parameters for clone quality assessment.</h3>
  <i> If you are not sure about certain 
  parameter settings, leave them unchanged </i> <a href="Help_SequenceEvaluation.html">[parameter 
  help file]</a>
  
  <p></p>
<font color="#2693A6" size="4"> <b>Set Name</b></font> 
<input name="SET_NAME" type="text" id="SET_NAME" value="" size="53">
<P>&nbsp; 
<table width="85%" border="0" align="center">
  <tr> 
    <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off) 
      <input name="FS_PHRED_CUT_OFF" type="text" id="ER_PHRED_CUT_OFF" value="20" size="5">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" > <b>Phred base score (low quality cut-off) 
      <input name="FS_PHRED_LOW_CUT_OFF" type="text" id="ER_PHRED_LOW_CUT_OFF" value="10" size="5">
      </b></td>
  </tr>
  <tr>
    <td >&nbsp; </td>
  </tr>
  <tr> 
    <td background="barbkgde.gif"><b>Maximum acceptable number of mutations:</b> 
    </td>
  </tr>
  <td align="center" ><table width="85%" border="1">
        <tr> 
          <td><strong>Threshold</strong></td>
          <td colspan="2"><div align="center"><strong>PASS</strong></div></td>
          <td colspan="2"><div align="center"><strong>FAIL</strong></div></td>
		  <td ><div align="center"><strong>Ignor if polymorphism</strong></div></td>
        </tr>
        <tr> 
          <td><div align="right"><strong>Base Confidence</strong></div></td>
          <td><div align="center"><strong>High </strong></div></td>
          <td><div align="center"><strong>Low </strong></div></td>
          <td><div align="center"><strong>High </strong></div></td>
          <td><div align="center"><strong>Low </strong></div></td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td width="44%"><strong>Silent mutation</strong></td>
          <td width="16%"><div align="center"> 
              <input name="FS_S_PASS_H" type="input" id="FS_S_PASS_H" value="3" size="5" >
            </div></td>
          <td width="14%"><div align="center"> 
              <input name="FS_S_PASS_L" type="input" id="FS_S_PASS_L" value="4" size="5">
            </div></td>
          <td width="13%"><div align="center"> 
              <input name="FS_S_FAIL_H" type="input" id="FS_S_FAIL_H" value="5" size="5" >
            </div></td>
          <td width="13%"><div align="center"> 
              <input name="FS_S_FAIL_L" type="input" id="FS_S_FAIL_L" value="6" size="5">
            </div></td>
          <td width="13%"> <div align="center"><input type="checkbox" name="FS_S_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Conservative substitution</strong></td>
          <td><div align="center"> 
              <input name="FS_C_PASS_H" type="input" value="3" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_C_PASS_L" type="input" value="4" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_C_FAIL_H" type="input" id="FS_S_FAIL_H" value="3" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_C_FAIL_L" type="input" id="FS_S_FAIL_L" value="4" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_C_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Nonconservative substitution</strong></td>
          <td><div align="center"> 
              <input name="FS_NC_PASS_H" type="input" value="1" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NC_PASS_L" type="input" value="1" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NC_FAIL_H" type="input" id="FS_NC_FAIL_H" value="1" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NC_FAIL_L" type="input" id="FS_NC_FAIL_L" value="1" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_NC_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Frameshift</strong></td>
          <td><div align="center"> 
              <input name="FS_FR_PASS_H" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_FR_PASS_L" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_FR_FAIL_H" type="input" id="FS_FR_FAIL_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_FR_FAIL_L" type="input" id="FS_FR_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_FR_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Inframe deletion</strong></td>
          <td><div align="center"> 
              <input name="FS_IDEL_PASS_H" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IDEL_PASS_L" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IDEL_FAIL_H" type="input" id="FS_IDEL_FAIL_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IDEL_FAIL_L" type="input" id="FS_IDEL_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_IDEL_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Inframe insertion</strong></td>
          <td><div align="center"> 
              <input name="FS_IINS_PASS_H" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IINS_PASS_L" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IINS_FAIL_H" type="input" id="FS_IINS_FAIL_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_IINS_FAIL_L" type="input" id="FS_IINS_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_IINS_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Truncation</strong></td>
          <td><div align="center"> 
              <input name="FS_TRANC_PASS_H" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_TRANC_PASS_L" type="input" id="FS_TRANC_PASS_L" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_TRANC_FAIL_H" type="input" id="FS_TRANC_FAIL_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_TRANC_FAIL_L" type="input" id="FS_TRANC_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_TRANCS_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>No translation (e.g., no ATG)</strong></td>
          <td><div align="center"> 
              <input name="FS_NOTRANSLATION_PASS_H" type="input" id="FS_NOTRANSLATION_PASS_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NOTRANSLATION_PASS_L" type="input" id="FS_NOTRANSLATION_PASS_L" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NOTRANSLATION_FAIL_H" type="input" id="FS_NOTRANSLATION_FAILS_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_NOTRANSLATION_FAIL_L" type="input" id="FS_NOTRANSLATION_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_NOTRANSLATION_POLM" value="1"></div></td>
        </tr>
        <tr> 
          <td><strong>Post-elongation(e.g., no stop codon)</strong></td>
          <td><div align="center"> 
              <input name="FS_PELONG_PASS_H" type="input" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_PELONG_PASS_L" type="input" id="FS_PELONG_PASS_L" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_PELONG_FAIL_H" type="input" id="FS_PELONG_FAIL_H" value="0" size="5">
            </div></td>
          <td><div align="center"> 
              <input name="FS_PELONG_FAIL_L" type="input" id="FS_PELONG_FAIL_L" value="0" size="5">
            </div></td>
          <td> <div align="center"><input type="checkbox" name="FS_PELONG_POLM"  value="1"></div></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td > <table width="100%" border="0" cellpadding="2" cellspacing="2">
        <tr> 
          <td nowrap>&nbsp;</td>
          <td align="right" nowrap>&nbsp;</td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td > <table width="100%" border="0" cellpadding="2" cellspacing="2">
        <tr> 
          <td  nowrap><strong>Maximum number of ambiquous bases per 100 bases:</strong></td>
          <td  align="left" nowrap><input name="FS_N_100" type="input" id="FS_N_100" value="10"></td>
        </tr>
        <tr> 
          <td width="64%"  nowrap> <strong>Max number of consequative ambiquous 
            bases :</strong></td>
          <td width="36%"  align="left" nowrap><input name="FS_N_ROW" type="input" id="FS_N_ROW" value="3"></td>
        </tr>
      </table></td>
  </tr>
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
