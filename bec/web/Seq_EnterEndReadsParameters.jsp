<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.core.spec.*" %>
<html>

<head>

<title>End Reads Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/bec/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Set End Reads Evaluation Parameters</h2>
<hr>
<html:errors/> <html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=EndReadsSpec.END_READS_SPEC_INT%>" >
<h3 >Create new set of parameters for Sequence Trimming and Evaluation of End 
  Reads</h3>
<i>If you are not sure about certain parameter settings, leave them unchanged 
</i> <a href="Help_SequenceEvaluation.html">[parameter help file]</a>.</b> 
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="SET_NAME" size="53" value="">
<P>
<P> <font size="4" color="#2693A6"><b> </b></font><b><font size="4" color="#2693A6">End 
  Reads Evaluation</font></b> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off) 
      <input name="ER_PHRED_CUT_OFF" type="text" id="ER_PHRED_CUT_OFF" value="20" size="10">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" > <b>Phred base score (low quality cut-off) 
      <input name="ER_PHRED_LOW_CUT_OFF" type="text" id="ER_PHRED_LOW_CUT_OFF" value="10" size="10">
      </b></td>
  </tr>
  <tr> 
    <td >&nbsp; </td>
  </tr>
  <tr> 
    <td  ><p><b>Mutation penalty for sequence scoring</b> </p> 
      <p>&nbsp;</p>
      <table width="85%" border="1" align="center">
        <tr> 
          <td background="barbkgde.gif"><div align="right"><strong>Base Confidence</strong></div></td>
          <td background="barbkgde.gif"><div align="center"><strong>High </strong></div></td>
          <td background="barbkgde.gif"><div align="center"><strong>Low </strong></div></td>
        </tr>
        <tr> 
          <td width="44%"><strong>Silent mutation</strong></td>
          <td width="16%"><div align="center"> 
              <input name="ER_S_H" type="input" id="ER_S_H" value="3" size="10" >
            </div></td>
          <td width="14%"><div align="center"> 
              <input name="ER_S_L" type="input" id="ER_S_L" value="1" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Conservative substitution</strong></td>
          <td><div align="center"> 
              <input name="ER_C_H" type="input" id="ER_C_H" value="20" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_C_L" type="input" id="ER_C_L" value="10" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Nonconservative substitution</strong></td>
          <td><div align="center"> 
              <input name="ER_NC_H" type="input" id="ER_NC_H" value="50" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_NC_L" type="input" id="ER_NC_L" value="30" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Frameshift</strong></td>
          <td><div align="center"> 
              <input name="ER_FR_H" type="input" id="ER_FR_H" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_FR_L" type="input" id="ER_FR_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Inframe deletion</strong></td>
          <td><div align="center"> 
              <input name="ER_IDEL_H" type="input" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_IDEL_L" type="input" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Inframe insertion</strong></td>
          <td><div align="center"> 
              <input name="ER_IINS_H" type="input" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_IINS_L" type="input" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Truncation</strong></td>
          <td><div align="center"> 
              <input name="ER_TRANC_H" type="input" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_TRANC_L" type="input" id="ER_TRANC_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>No translation (e.g., no ATG)</strong></td>
          <td><div align="center"> 
              <input name="ER_NOTRANSLATION_H" type="input" id="ER_NOTRANSLATION_H" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_NOTRANSLATION_L" type="input" id="ER_NOTRANSLATION_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td><strong>Post-elongation (e.g., no stop codon)</strong></td>
          <td><div align="center"> 
              <input name="ER_PLONG_H" type="input" id="ER_PLONG_H" value="1000" size="10">
            </div></td>
          <td><div align="center"> 
              <input name="ER_PLONG_L" type="input" id="ER_PLONG_L" value="1000" size="10">
            </div></td>
        </tr>
      </table></td>
  </tr>
</table>
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>

</html>


