<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

<html>

<head>

<title>Primer Calculating Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> available sets of parameters for Biological Evaluation of Clones</font>
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
 <P>
 
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan="2"> <p> <font  size="4"> <b>Set Name</b></font><%= spec.getName() %> 
      <p> </td>
  </tr>
  <tr> 
    <td> 
  <tr> 
    <td  bgcolor="#e4e9f8" width="64%"><font color="#000080"><b>Phred base score 
      (high quality cut-off) </b></font></td>
    <td bgcolor="#e4e9f8"><%= spec.getParameterByNameString("FS_PHRED_CUT_OFF")%> 
    </td>
  </tr>
  <tr> 
    <td bgcolor="#b8c6ed"> <font color="#000080"><b>Phred base score (low quality 
      cut-off) </b></font></td>
    <td bgcolor="#b8c6ed"><%= spec.getParameterByNameString("FS_PHRED_LOW_CUT_OFF")%></td>
  </tr>
  <tr> 
    <td >&nbsp; </td>
  </tr>
  <tr> 
    <td colspan=2><b>Maximum acceptable number of discrepancies (gene region):</b> 
      
      <p></p>
      
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="left"><strong><font color="#FFFFFF">Threshold</font></strong></div></td>
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
          <td width="44%" bgcolor="#e4e9f8" ><strong><font color="#000080">Silent 
            mutation</font></strong></td>
          <td width="16%" bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_S_PASS_H")%></div></td>
          <td width="14%"bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_S_PASS_L")%></div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_S_FAIL_H")%></div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_S_FAIL_L")%> 
            </div></td>
          <td width="13%"bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox"  disabled
            <% if (spec.getParameterByNameString("FS_S_POLM") != null) 
          {%>
          	checked
          <%}%> 
          >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><b><strong><font color="#000080">Conservative 
            substitution</font></strong></b></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_C_PASS_H")%> </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_C_PASS_L")%> </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_C_FAIL_H")%> </b></strong></font></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_C_FAIL_L")%> </b></strong></font></div></td>
          <td bgColor="#b8c6ed"> <div align="center"> <font color="#000080"><strong><b> 
              <input type="checkbox"  disabled
	           
	           <% if (spec.getParameterByNameString("FS_C_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	          >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Nonconservative 
            substitution</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_NC_PASS_H")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_NC_PASS_L")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_NC_FAIL_H")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_NC_FAIL_L")%> 
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox"  disabled
            <% if (spec.getParameterByNameString("FS_NC_POLM") != null) 
            {%>
                checked
            <%}%> 
             >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_FR_PASS_H")%> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_FR_PASS_L")%> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_FR_FAIL_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_FR_FAIL_L")%> 
            </div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox" disabled 

            <% if (spec.getParameterByNameString("FS_FR_POLM") != null) 
            {%>
                checked
            <%}%> 
            >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Inframe deletion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_PASS_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_PASS_L")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_FAIL_H")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_FAIL_L")%></div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox"  disabled
	            
	            <% if (spec.getParameterByNameString("FS_IDEL_POLM") != null) 
	            {%>
	            	checked
	            <%}%> 
	            >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Inframe insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_IINS_PASS_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_IINS_PASS_L")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_IINS_FAIL_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_IINS_FAIL_L")%> 
            </div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox"  disabled
          
          <% if (spec.getParameterByNameString("FS_IINS_POLM") != null) 
          {%>
          	checked
          <%}%> 
          >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Truncation</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_PASS_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_PASS_L")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_FAIL_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_FAIL_L")%></div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
              <input type="checkbox"  
	            
	            <% if (spec.getParameterByNameString("FS_TRANC_POLM") != null) 
	            {%>
	            	checked
	            <%}%> 
	            
	            disabled>
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">No translation (e.g., 
            no ATG)</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_L")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_H")%> 
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_L") %></div></td>
          <td bgColor="#b8c6ed"> <div align="center"> 
              <input type="checkbox" disabled  
	           
	           <% if (spec.getParameterByNameString("FS_NOTRANSLATION_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	           
	           >
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Post-elongation(e.g., 
            no stop codon)</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_PASS_H")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_PASS_L")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_FAIL_H")%> 
            </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_FAIL_L")%> 
            </div></td>
          <td bgColor="#e4e9f8"> <div align="center"> 
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
  <td colspan=2><b>Maximum acceptable number of discrepancies (linker region):</b> 
     
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
          <td width="16%" bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("FS_5S_PASS_H")%></div></td>
          <td width="14%"bgcolor="#e4e9f8"><div align="center"> 
              <%= spec.getParameterByNameString("FS_5DI_PASS_L")%>
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
              <%= spec.getParameterByNameString("FS_5S_FAIL_H")%>
            </div></td>
          <td width="13%"bgcolor="#e4e9f8"><div align="center"> 
             <%= spec.getParameterByNameString("FS_5S_FAIL_L")%>
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><b><strong><font color="#000080">5' deletion/insertion</font></strong></b></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_5DI_PASS_H")%>
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_5DI_PASS_L")%>
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
              <%= spec.getParameterByNameString("FS_5DI_FAIL_H")%>
              </b></strong></font></div></td>
          <td bgColor="#e4e9f8"><div align="center"> <font color="#000080"><strong><b> 
             <%= spec.getParameterByNameString("FS_5DI_FAIL_L" )%>
              </b></strong></font></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
             <%= spec.getParameterByNameString("FS_3S_PASS_H" )%>
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <%= spec.getParameterByNameString("FS_3S_PASS_L")%>           </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <%= spec.getParameterByNameString("FS_3S_FAIL_H" )%>
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <%= spec.getParameterByNameString("FS_3S_FAIL_L")%>
            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' deletion/insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> 
             <%= spec.getParameterByNameString("FS_3DI_PASS_H")%>            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <%= spec.getParameterByNameString("FS_3DI_PASS_L")%>
            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
             <%= spec.getParameterByNameString("FS_3DI_FAIL_H")%>            </div></td>
          <td bgColor="#b8c6ed"><div align="center"> 
              <%= spec.getParameterByNameString("FS_3DI_FAIL_L")%>
            </div></td>
        </tr>
      </table>
	  </td></tr>
  <tr> 
    <td > <P></P>
      &nbsp;</td>
  </tr>
  <tr> 
    <td  nowrap bgColor="#b8c6ed"><strong><font color="#000080">Maximum number 
      of ambiquous bases per 100 bases:</font></strong></td>
    <td  align="left"bgColor="#b8c6ed" nowrap><%= spec.getParameterByNameString("FS_N_100")%></td>
  </tr>
  <tr> 
    <td width="64%"  nowrap bgColor="#e4e9f8"> <strong><font color="#000080">Max 
      number of consequative ambiquous bases :</font></strong></td>
    <td width="26%"  align="left" nowrap bgColor="#e4e9f8"><%= spec.getParameterByNameString("FS_N_ROW")%></td>
  </tr></tr>
</table>


<%}}%>
</body>
</html>
