<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>
<html>

<head>

<title>End Reads Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/>  Set End Reads Parameters</h2>
<hr>
<html:errors/> 
<html:form action="/Seq_SubmitSpec.do" > 
<h3 >Set parameters for Sequence Trimming and Analysis of End Reads</h3>
<i>If you are not sure about certain parameter settings, leave them unchanged 
</i> <a href="helpSequenceTrimmingParam.htm">[parameter help file]</a>.</b> 
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="SET_NAME" size="53" value="default">
<P><font color="#2693A6" size="4"><b> 
  <input type="radio" name="isTrim" value="0">
  Ends Trimming Parameters</b></font> 
<table border="0" CELLSPACING="4" width="95%">
  <tr> 
    <td background="barbkgde.gif"><strong><i><font size="+1">5 prime end</font></i></strong></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
      <input name="TRIM_1" type="checkbox"  value="1">
      Trim no more than 25%, trim until the first 
      <input name="TRIM_1_BASES" type="text"  value="25" size="5">
      bases contain less than 
      <input name="TRIM_1_AMB" type="text" value="3" size="5">
      ambiquities </b></td>
  </tr>
  <tr> 
    <td background="barbkgde.gif" valign="top"><b> 
      <input type="checkbox" name="TRIM_2" value="0">
      Trim no more than 25%, trim until the first 
      <input type="text" name="TRIM_2_BASE" size="5" value="25">
      bases contain less than 
      <input name="TRIM_2_AMB" type="text"  value="3" size="5">
      bases with confidence below 
      <input name="TRIM_2_CONF" type="text" value="7" size="5">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <input type="checkbox" name="TRIM_3" value="0">
      Trim ABI primer blobs, where 
      <input type="text" name="TRIM_3_BASE" size="5" value="3">
      bases from 5prime end </b></td>
  </tr>
  
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <p> 
      <p><i><font size="+1">3 prime end</font></i></b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
      <input type="checkbox" name="TRIM_4" value="0">
      Starting 
      <input type="text" name="TRIM_4_BASE" size="5" value="25">
      bases after 5prime trim, trim first 
      <input name="TRIM_4_FBASE" type="text" value="3" size="5">
      bases containing more than 
      <input name="TRIM_4_AMB" type="text" value="3" size="5">
      ambiquities </b> </td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" valign="top"><b> 
      <input type="checkbox" name="TRIM_5" value="0">
      Trim from 3prim end, until last 
      <input type="text" name="TRIM_5_BASE" size="5" value="25">
      bases contain less than 
      <input name="TRIM_5_AMB" type="text" value="3" size="5">
      ambiquities</b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <input type="checkbox" name="TRIM_6" value="0">
      Trim from 3prim end, until last 
      <input type="text" name="TRIM_6_BASE" size="5" value="25">
      bases contain less than 
      <input name="TRIM_6_FBASE" type="text" value="3" size="5">
      bases with confidence below 
      <input name="TRIM_6_CONF" type="text" value="7" size="5">
      </b></td>
  </tr>
</table>
<p>&nbsp; 
<p><font size="4" color="#2693A6"><b>
  <input type="radio" name="isTrim" value="1" checked>
  Sequence Trimming Parameters</b></font> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td width="758" background="barbkgde.gif" ><b> 
      <input type="radio" name="isPhredTrimMode" value="0">
      Standard Modd algorithm (-trim option for Phred)</b></td>
  </tr>
  <tr> 
    <td background="barbkgde.gif" ><b> 
      <input type="radio" name="isPhredTrimMode" value="1" checked>
      Modified Modd algorithm  (-trim_alt option for Phred)  
      <input name="ER_PHRED_TRIM_PR" type="text" id="ER_PHRED_TRIM_PR" value="0.05" size="10">
      </b></td>
  </tr>
<tr> 
    <td background="barbkgde.gif" ><b> 
      
      Vectror sequence (optional)
      <input name="ER_VECTOR" type="text" id="ER_VECTOR" value="" size="50">
      </b></td>
  </tr>
</table>
<P></P>
<b><font size="4" color="#2693A6">End Reads Analysis</font></b> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off)
      <input name="ER_PHRED_CUT_OFF" type="text" id="ER_PHRED_CUT_OFF" value="20" size="5">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" >
  <input type="radio" name="isLowScore" value="1" checked>
  <b>Phred base score (low quality cut-off)
      <input name="ER_PHRED_LOW_CUT_OFF" type="text" id="ER_PHRED_LOW_CUT_OFF" value="10" size="5">
       </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" >
  <input type="radio" name="isLowScore" value="0">
<b>Discard up to 
      <input name="ER_HIGH_QUAL" type="text" value="2" size="5">
      high quality bases surronded by no less than 
      <input name="ER_LOW_QUAL" type="text" value="3" size="5">
      low quality bases</b></td>
  </tr>
   <tr> 
    <td  background="barbkgde.gif" ><b>Mutation penalty for sequence scoring</b> 
       <P></P>
      <table border=0 width="90%" align="center">
        <tr > 
          <td width="30%"  background="barbkgde.gif" ><b>&nbsp;</b></td>
          <td width="30%" background="barbkgde.gif" ><b>High quality base</b></td>
          <td width="30%" background="barbkgde.gif" ><b>Low quality base</b></td>
        </tr>
        <tr> 
          <td  background="barbkgde.gif" ><b>Silent mutation</b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_HQ_SILENT" type="text" value="2" size="5">
            </b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_LQ_SILENT" type="text" value="1" size="5">
            </b></td>
        </tr>
        <tr> 
          <td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_HQ_CONSERVATIVE" type="text" value="3" size="5">
            </b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_LQ_CONSERVATIVE" type="text" value="1" size="5">
            </b></td>
        </tr>
        <tr> 
          <td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_HQ_NON_CONSERVATIVE" type="text" value="18" size="5">
            </b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_LQ_NON_CONSERVATIVE" type="text" value="3" size="5">
            </b></td>
        </tr>
        <tr> 
          <td  background="barbkgde.gif" ><b>Frameshift</b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_HQ_FRAMESHIFT" type="text" value="100" size="5">
            </b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_LQ_FRAMESHIFT" type="text" value="50" size="5">
            </b></td>
        </tr>
        <tr> 
          <td  background="barbkgde.gif" ><b>Stop codon</b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_HQ_STOP" type="text" value="100" size="5">
            </b></td>
          <td  background="barbkgde.gif" ><b> 
            <input name="ER_LQ_STOP" type="text" value="50" size="5">
            </b></td>
        </tr>
        <tr>
          <td  background="barbkgde.gif" >&nbsp;</td>
          <td  background="barbkgde.gif" >&nbsp;</td>
          <td  background="barbkgde.gif" >&nbsp;</td>
        </tr>
      </table>
		</td>
  </tr>
</table>
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
<HR>
<HR>
<% ArrayList specs = (ArrayList)request.getAttribute("specs");
   if (specs.size() > 0 )
{
    for (int spec_count = 0; spec_count < specs.size() ; spec_count++)
    {
        Spec current_spec = (Spec)specs.get(spec_count); 
        Hashtable current_params = current_spec.getParameters();
%>
<h3>Available Sets </h3>
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <% = current_spec.getName() %>
<P><b><font size="4" color="#2693A6">End Reads Analysis</font></b> 
<table border="0" cellspacing="4" width="95%">
    <tr> 
      <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off)
        <% =current_params.get("ER_PHRED_CUT_OFF") %>
        </b></td>
    </tr>
    <tr> 
      <td  background="barbkgde.gif" ><b>
	  <% 
	  if (current_params.get("isLowScore") ==1  )
	  {
	  %>
	     Phred base score (low quality cut-off)
        <% =current_params.get("ER_PHRED_LOW_CUT_OFF") %>
         
	 <%
	  
	  }
	  else
	  {%>
	  Discard up to 
       <% =current_params.get("ER_HIGH_QUAL" %>
        high quality bases surronded by no less than 
        <% =current_params.get("ER_LOW_QUAL" %>
        low quality bases
		<%
	  }
   %>
   </b></td>
    </tr>
    <tr> 
      <td  background="barbkgde.gif" >
      
    
  </td>
    </tr>
     <tr> 
      <td  background="barbkgde.gif" >
      <b>Mutation penalty for sequence scoring</b> 
         <P></P>
        <table border=0 width="90%" align="center">
          <tr > 
            <td width="30%"  background="barbkgde.gif" ><b>&nbsp;</b></td>
            <td width="30%" background="barbkgde.gif" ><b>High quality base</b></td>
            <td width="30%" background="barbkgde.gif" ><b>Low quality base</b></td>
          </tr>
          <tr> 
            <td  background="barbkgde.gif" ><b>Silent mutation</b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_HQ_SILENT") %>
              </b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_LQ_SILENT" %>
              </b></td>
          </tr>
          <tr> 
            <td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_HQ_CONSERVATIVE") %> </b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_LQ_CONSERVATIVE") %> </b></td>
          </tr>
          <tr> 
            <td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_HQ_NON_CONSERVATIVE") %> </b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_LQ_NON_CONSERVATIVE") %>
              </b></td>
          </tr>
          <tr> 
            <td  background="barbkgde.gif" ><b>Frameshift</b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_HQ_FRAMESHIFT") %></b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_LQ_FRAMESHIFT") %></b></td>
          </tr>
          <tr> 
            <td  background="barbkgde.gif" ><b>Stop codon</b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_HQ_STOP") %>
              </b></td>
            <td  background="barbkgde.gif" ><b> 
              <% =current_params.get("ER_LQ_STOP") %>
              </b></td>
          </tr>
         
        </table>
  		</td>
    </tr>
  </table>
<p> 
<hr>

<%}}%>
</body>

</html>


