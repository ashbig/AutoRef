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
<h2><bean:message key="flex.name"/>: Biological Evaluation of Clones</h2>
<html:errors/>
 

<h3>All available sets of parameters for clone quality assessment.</h3>
  <i> Parameter discription</i> <a href="Help_SequenceEvaluation.html">[parameter 
  help file]</a>
  
  <p></p>


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 if (sets.size()==0)
{%>
<p><b>No sets are available</b>
<%}
else if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
	FullSeqSpec spec = (FullSeqSpec) sets.get(count);
 
	%>
<P>
 <P> <font color="#2693A6" size="4"> <b>Set Name</b></font>
<%= spec.getName() %>

<P>
<table width="85%" border="0" align="center">

  <tr> 
    <td > <table width="100%" border="0" cellpadding="2" cellspacing="2">
        <tr> 
          <td  nowrap><strong>Phred base score (high quality cut-off):</strong></td>
          <td  align="left" nowrap><%= spec.getParameterByNameString("FS_PHRED_CUT_OFF")%></td>
        </tr>
        <tr> 
          <td width="64%" height="26"  nowrap> <strong>Phred base score (low quality cut-off):</strong></td>
          <td width="36%"  align="left" nowrap><%= spec.getParameterByNameString("FS_PHRED_LOW_CUT_OFF")%></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td ><p><b>Maximum acceptable number of mutations:</b> 
    </td>
  </tr>
  <td align="center" ><table width="85%" border="1" >
        <tr> 
          <td background="barbkgde.gif"><strong>Threshold</strong></td>
          <td background="barbkgde.gif" colspan="2"><div align="center"><strong>PASS</strong></div></td>
          <td background="barbkgde.gif" colspan="2"><div align="center"><strong>FAIL</strong></div></td>
		   <td background="barbkgde.gif" ><div align="center"><strong>Ignor if polymorphism</strong></div></td>
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
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_S_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_S_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_S_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_S_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
          <input type="checkbox"  disabled
            <% if (spec.getParameterByNameString("FS_S_POLM") != null) 
          {%>
          	checked
          <%}%> 
          >
          
          </div></td>
        </tr>
        <tr> 
          <td><strong>Conservative substitution</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_C_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_C_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_C_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_C_FAIL_L")%></div></td>
         <td width="13%"><div align="center">
	           <input type="checkbox"  disabled
	           
	           <% if (spec.getParameterByNameString("FS_C_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	          >         </div></td>

        <tr> 
          <td><strong>Nonconservative substitution</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_NC_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_NC_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_NC_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_NC_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
            <input type="checkbox"  disabled
            <% if (spec.getParameterByNameString("FS_NC_POLM") != null) 
            {%>
                checked
            <%}%> 
             >           </div></td>

        </tr>
        <tr> 
          <td><strong>Frameshift</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_FR_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_FR_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_FR_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_FR_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
            <input type="checkbox" disabled 

            <% if (spec.getParameterByNameString("FS_FR_POLM") != null) 
            {%>
                checked
            <%}%> 
            >
            </div></td>

        </tr>
        <tr> 
          <td><strong>Inframe deletion</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_IDEL_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_IDEL_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_IDEL_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_IDEL_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
	            <input type="checkbox"  disabled
	            
	            <% if (spec.getParameterByNameString("FS_IDEL_POLM") != null) 
	            {%>
	            	checked
	            <%}%> 
	            >
	            </div></td>

        </tr>
        <tr> 
          <td><strong>Inframe insertion</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_IINS_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_IINS_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_IINS_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_IINS_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
          <input type="checkbox"  disabled
          
          <% if (spec.getParameterByNameString("FS_IINS_POLM") != null) 
          {%>
          	checked
          <%}%> 
          >
          </div></td>

        </tr>
        <tr> 
          <td><strong>Truncation </strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_TRANC_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_TRANC_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_TRANC_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_TRANC_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
	            <input type="checkbox"  
	            
	            <% if (spec.getParameterByNameString("FS_TRANC_POLM") != null) 
	            {%>
	            	checked
	            <%}%> 
	            
	            disabled">
	            </div></td>

        </tr>
        <tr> 
          <td><strong>No translation(e.g., no ATG)</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_L")%></div></td>
         <td width="13%"><div align="center">
	           <input type="checkbox" disabled  
	           
	           <% if (spec.getParameterByNameString("FS_NOTRANSLATION_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	           
	           >
	           </div></td>

        </tr>
        <tr> 
          <td><strong>Post-elongation(e.g., no stop codon)</strong></td>
          <td width="16%"><div align="center"><%= spec.getParameterByNameString("FS_PELONG_PASS_H")%></div></td>
          <td width="14%"><div align="center"><%= spec.getParameterByNameString("FS_PELONG_PASS_L")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_PELONG_FAIL_H")%></div></td>
          <td width="13%"><div align="center"><%= spec.getParameterByNameString("FS_PELONG_FAIL_L")%></div></td>
          <td width="13%"><div align="center">
	            <input type="checkbox" disabled   
	            
	            <% if (spec.getParameterByNameString("FS_PELONG_POLM") != null) 
	            {%>
	            	 checked 
	            <%}%> 
	           >
	            </div></td>

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
          <td  nowrap><strong>Number of ambiquous bases per 100 bases:</strong></td>
          <td  align="left" nowrap><%= spec.getParameterByNameString("FS_N_100")%></td>
        </tr>
        <tr> 
          <td width="64%" height="26"  nowrap> <strong>Max number of consequative 
            ambiquous bases :</strong></td>
          <td width="36%"  align="left" nowrap><%= spec.getParameterByNameString("FS_N_ROW")%></td>
        </tr>
      </table></td>
  </tr>
</table>


<%}}%>
</body>
</html>
