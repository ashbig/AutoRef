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
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : End Reads Evaluation Parameters</h2>
<hr>
<html:errors/> 
<h3 >All available sets of parameters for Sequence Trimming and Evaluation of End 
  Reads</h3>
<i>Parameters description 
</i> <a href="Help_SequenceEvaluation.html">[parameter help file]</a>.</b> 


<% ArrayList specs = (ArrayList)request.getAttribute("specs");
 if (specs.size()==0)
{%>
<p><b>No sets are available</b>
<%}
else if (specs.size() > 0 )
{
    for (int spec_count = 0; spec_count < specs.size() ; spec_count++)
    {
        Spec current_spec = (Spec)specs.get(spec_count); 
        Hashtable current_params = current_spec.getParameters();
%>

<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <%= current_spec.getName() %>
<P><b><font size="4" color="#2693A6">End Reads Analysis</font></b> 
<table border="0" cellspacing="4" width="95%">
    <tr> 
      <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off)
        <%= current_params.get("ER_PHRED_CUT_OFF") %>
        </b></td>
    </tr>
    <tr> 
      <td  background="barbkgde.gif" ><b>
	 Phred base score (low quality cut-off)
        <%= current_params.get("ER_PHRED_LOW_CUT_OFF") %>
         
	
   </b></td>
    </tr>
    <tr> 
      <td  background="barbkgde.gif" >
      
    
  </td>
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
          <td width="16%"><div align="center"><%= current_params.get("ER_S_H")%></div></td>
          <td width="14%"><div align="center"> <%= current_params.get("ER_S_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Conservative substitution</strong></td>
          <td><div align="center"><%= current_params.get("ER_C_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_C_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Nonconservative substitution</strong></td>
          <td><div align="center"><%= current_params.get("ER_NC_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_NC_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Frameshift</strong></td>
          <td><div align="center"><%= current_params.get("ER_FR_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_FR_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Inframe deletion</strong></td>
          <td><div align="center"><%= current_params.get("ER_IDEL_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_IDEL_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Inframe insertion</strong></td>
          <td><div align="center"> <%= current_params.get("ER_IINS_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_IINS_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Truncation</strong></td>
          <td><div align="center"> <%= current_params.get("ER_TRANC_H")%>    </div></td>
          <td><div align="center"><%= current_params.get("ER_TRANC_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>No translation(e.g., no ATG)</strong></td>
          <td><div align="center"> <%= current_params.get("ER_NOTRANSLATION_H")%></div></td>
          <td><div align="center"> <%= current_params.get("ER_NOTRANSLATION_L")%></div></td>
        </tr>
        <tr> 
          <td><strong>Post-elongation (e.g., no stop codon)</strong></td>
          <td><div align="center"><%= current_params.get("ER_PLONG_H")%></div></td>
          <td><div align="center"><%= current_params.get("ER_PLONG_L")%></div></td>
        </tr>
      </table></td>
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


