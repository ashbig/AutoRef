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
<script language="JavaScript" src="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>scripts.js"></script>
</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> available sets of parameters Clone 
      Scoring</font> 
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
        Spec spec = (Spec)specs.get(spec_count); 
       
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td colspan=2>

<P> <font  size="4"> <b>Set Name</b></font> <%= spec.getName() %> 
      <P>&nbsp;</td></tr>
<tr> 
    <td  bgColor="#e4e9f8" width="60%"><font color="#000080"><b>Phred base score 
      (high quality cut-off)</font></td>
 <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("ER_PHRED_CUT_OFF") %>  </td>    </tr>
<tr> <td  bgColor="#b8c6ed" width="60%"><b>	 Phred base score (low quality cut-off)</td>
 <td bgColor="#b8c6ed">  <%= spec.getParameterByNameString("ER_PHRED_LOW_CUT_OFF") %></td>   </tr>
 
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
      
          <td width="16%" bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_S_H")%></div></td>
          <td width="14%" bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_S_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Conservative substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"><%= spec.getParameterByNameString("ER_C_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= spec.getParameterByNameString("ER_C_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Nonconservative substitution</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_NC_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_NC_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Frameshift</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"><%= spec.getParameterByNameString("ER_FR_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= spec.getParameterByNameString("ER_FR_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><font color="#000080"><strong>Inframe deletion</strong></font></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_IDEL_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_IDEL_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Inframe insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_IINS_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"><%= spec.getParameterByNameString("ER_IINS_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Truncation</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_TRANC_H")%>    </div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_TRANC_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">No translation(e.g., no ATG)</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_NOTRANSLATION_H")%></div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_NOTRANSLATION_L")%></div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Post-elongation (e.g., no stop codon)</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_PLONG_H")%></div></td>
          <td bgColor="#e4e9f8"><div align="center"><%= spec.getParameterByNameString("ER_PLONG_L")%></div></td>
        </tr>
      </table></td>
  </tr>

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
          <td width="16%" bgColor="#e4e9f8"><div align="center">              <%= spec.getParameterByNameString("ER_5S_H")%>            </div></td>
          <td width="14%" bgColor="#e4e9f8"><div align="center">              <%= spec.getParameterByNameString("ER_5S_L")%>            </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">5' deletion/insertion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center">              <%= spec.getParameterByNameString("ER_5DI_H")%>            </div></td>
          <td bgColor="#e4e9f8"><div align="center">               <%= spec.getParameterByNameString("ER_5DI_L")%>            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' substitution</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center">               <%= spec.getParameterByNameString("ER_3S_H")%>            </div></td>
          <td bgColor="#b8c6ed"><div align="center">              <%= spec.getParameterByNameString("ER_3S_L")%>            </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">3' deletion/insertion</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center">           <%= spec.getParameterByNameString("ER_3DI_H")%>            </div></td>
       	<td bgColor="#b8c6ed"><div align="center">               <%= spec.getParameterByNameString("ER_3DI_L")%>            </div></td>
      </table></td>
  </tr>

  <tr><td colspan="2">
  <p><b>Mutation penalty for sequence scoring (discrepancy introduced by ambiquous 
        bases)</b> </p>
		<input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show </p>
      <p></p>
	  <DIV ID="divShowHide" STYLE="  position:relative;  clip:rect(0px 120px 120px 0px); ">
      <table width="85%" border="0" align="center">
        <tr> 
          <td bgcolor="#1145A6"><div align="right"><strong><font color="#FFFFFF">Base 
              Confidence </font></strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">High 
              </font> </strong></div></td>
          <td bgcolor="#1145A6"><div align="center"><strong><font color="#FFFFFF">Low 
              </font></strong></div></td>
        </tr>
        <tr> 
          <td width="44%" bgcolor="#e4e9f8" ><strong><font color="#000080">Start 
            codon substitution</font></strong></td>
          <td width="16%" bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NSTART_PASS_H")%></div></td>
          <td width="14%"bgcolor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NSTART_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><b><strong><font color="#000080">Stop codon substitution</font></strong></b></td>
          <td bgColor="#e4e9f8"><div align="center">          <%= spec.getParameterByNameString("ER_NSTOP_PASS_H")%> </div></td>
          <td bgColor="#e4e9f8"><div align="center">               <%= spec.getParameterByNameString("ER_NSTOP_PASS_L")%> </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Substituttion cds 
            region </font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NCDS_PASS_H" )%>             </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NCDS_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Frameshift Insertion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NFRAME_PASS_H")%>             </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NFRAME_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Inframe Insertion</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NINFRAME_PASS_H" )%>             </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_NINFRAME_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Substitution 5' 
            linker region</font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_N5SUB_PASS_H")%>             </div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_N5SUB_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#b8c6ed"><strong><font color="#000080">Insertion 5' linker 
            region </font></strong></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_N5INS_PASS_H" )%>             </div></td>
          <td bgColor="#b8c6ed"><div align="center"> <%= spec.getParameterByNameString("ER_N5INS_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Substitution 3' 
            linker region</font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_N3SUB_PASS_H")%>             </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_N3SUB_PASS_L")%>             </div></td>
        </tr>
        <tr> 
          <td bgColor="#e4e9f8"><strong><font color="#000080">Insertion 3' linker 
            region </font></strong></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_N3INS_PASS_H" )%>             </div></td>
          <td bgColor="#e4e9f8"><div align="center"> <%= spec.getParameterByNameString("ER_N3INS_PASS_L")%>             </div></td>
        </tr>
      </table></div>
  </td></tr>
  </table>
<p> 
<hr>

<%}}%>
</body>

</html>


