<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>

<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<html>

<head>

<title>End Reads Parameters</title>
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
    <font color="#008000" size="5"><b> create new set of parameters for End Rewads Evaluation </font>
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
	<tr><td><i>If you are not sure about certain parameter settings, leave them unchanged 
</i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. </td></tr>
  </table>
  </center>
</div>

<html:form action="/Seq_SubmitSpec.do" > 

<input name="forwardName" type="hidden" value="<%=EndReadsSpec.END_READS_SPEC_INT%>" >


<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>

<tr><td colspan =2><div align="right"><b>
<a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT%>"> 
        View All </a></b> </div><P><P><P></P></P></P></td></tr>
<TR><td colspan="2">

<P> <font  size="4"> <b>Set Name</b></font> 
  <input type="text" name="SET_NAME" size="53" value="">
<P>
</td></tr>
<tr><td>

  <tr> 
    <td  bgColor="#e4e9f8" width="60%"><b><font color="#000080">Phred base score 
      (high quality cut-off) </font></td>
     <td bgColor="#e4e9f8"> <input name="ER_PHRED_CUT_OFF" type="text" id="ER_PHRED_CUT_OFF" value="20" size="10">
          </td>
  </tr>
  <tr> 
    <td bgColor="#b8c6ed"> <b><font color="#000080">Phred base score (low quality 
      cut-off) </font></td>
     <td bgColor="#b8c6ed"> <input name="ER_PHRED_LOW_CUT_OFF" type="text" id="ER_PHRED_LOW_CUT_OFF" value="10" size="10">
      </td>
  </tr>
  <tr> 
    <td >&nbsp; </td>
  </tr>
 
  </td></tr>
  <tr> 
    <td colspan=2 ><p><b>Mutation penalty for sequence scoring (gene region)</b> </p> 
     
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base Confidence</font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgColor="#e4e9f8" ><strong><font color="#000080">Silent mutation</font></strong></td>
          <td width="16%" bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_S_H" type="input" id="ER_S_H" value="3" size="10" >
            </div></td>
          <td width="14%" bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_S_L" type="input" id="ER_S_L" value="1" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Conservative substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_C_H" type="input" id="ER_C_H" value="20" size="10">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_C_L" type="input" id="ER_C_L" value="10" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Nonconservative substitution</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_NC_H" type="input" id="ER_NC_H" value="50" size="10">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_NC_L" type="input" id="ER_NC_L" value="30" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_FR_H" type="input" id="ER_FR_H" value="1000" size="10">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_FR_L" type="input" id="ER_FR_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Inframe deletion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_IDEL_H" type="input" value="1000" size="10">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_IDEL_L" type="input" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Inframe insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_IINS_H" type="input" value="1000" size="10">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_IINS_L" type="input" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Truncation</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_TRANC_H" type="input" value="1000" size="10">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_TRANC_L" type="input" id="ER_TRANC_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">No translation (e.g., no ATG)</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_NOTRANSLATION_H" type="input" id="ER_NOTRANSLATION_H" value="1000" size="10">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_NOTRANSLATION_L" type="input" id="ER_NOTRANSLATION_L" value="1000" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Post-elongation (e.g., no stop codon)</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_PLONG_H" type="input" id="ER_PLONG_H" value="1000" size="10">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_PLONG_L" type="input" id="ER_PLONG_L" value="1000" size="10">
            </div></td>
        </tr>
      </table></td>
  </tr>
  <td colspan=2 ><p><b>Mutation penalty for sequence scoring (linker region)</b> </p> 
     
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base Confidence</font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgColor="#e4e9f8" ><strong><font color="#000080">5' substitution</font></strong></td>
          <td width="16%" bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_5S_H" type="input" id="ER_5S_H" value="3" size="10" >
            </div></td>
          <td width="14%" bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_5S_L" type="input" id="ER_5S_L" value="1" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">5' deletion/insertion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_5DI_H" type="input" id="ER_5DI_H" value="1000" size="10">
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> 
              <input name="ER_5DI_L" type="input" id="ER_5DI_L" value="500" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_3S_H" type="input" id="ER_3S_H" value="50" size="10">
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_3S_L" type="input" id="ER_3S_L" value="30" size="10">
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' deletion/insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_3DI_H" type="input" id="ER_3DI_H" value="1000" size="10">
            </div></td>
       	<td bgColor="#b8c6ed"><div align="center"> 
              <input name="ER_3DI_H" type="input" id="ER_3DI_H" value="500" size="10">
            </div></td>
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


