<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
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
    <font color="#008000" size="5"><b> available sets of parameters for End Rewads Evaluation </font>
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
	<tr><td><i>Parameters Description
</i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. </td></tr>
  </table>
  </center>
</div>


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
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td colspan=2>

<P> <font  size="4"> <b>Set Name</b></font>   <%= current_spec.getName() %>
</td></tr>
<tr> <td  bgColor="#e4e9f8" width="60%"><b>Phred base score (high quality cut-off)</td>
 <td bgColor="#e4e9f8"><%= current_params.get("ER_PHRED_CUT_OFF") %>  </td>    </tr>
<tr> <td  bgColor="#b8c6ed" width="60%"><b>	 Phred base score (low quality cut-off)</td>
 <td bgColor="#b8c6ed">  <%= current_params.get("ER_PHRED_LOW_CUT_OFF") %></td>   </tr>
 
	<tr> 
    <td >&nbsp; </td>
  </tr>
 
  </td></tr>
  <tr> 
    <td colspan=2 ><p><b>Mutation penalty for sequence scoring</b> </p> 
     
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base Confidence</font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgColor="#e4e9f8" ><strong><font color="#000080">Silent mutation</font></strong></td>
      
          <td width="16%" bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_S_H")%></div></td>
          <td width="14%" bgColor="#e4e9f8"><div align="center"> <%= current_params.get("ER_S_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Conservative substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"><%= current_params.get("ER_C_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= current_params.get("ER_C_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Nonconservative substitution</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_NC_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_NC_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"><%= current_params.get("ER_FR_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= current_params.get("ER_FR_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><font color="#000080"><strong>Inframe deletion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_IDEL_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_IDEL_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Inframe insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= current_params.get("ER_IINS_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= current_params.get("ER_IINS_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Truncation</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= current_params.get("ER_TRANC_H")%>    </div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_TRANC_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">No translation(e.g., no ATG)</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= current_params.get("ER_NOTRANSLATION_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= current_params.get("ER_NOTRANSLATION_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Post-elongation (e.g., no stop codon)</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_PLONG_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= current_params.get("ER_PLONG_L")%></div></td>
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


