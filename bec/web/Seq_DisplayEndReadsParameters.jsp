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
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> Available sets of parameters for Clone 
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
String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
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
<!-- <tr> 
    <td  bgColor="#e4e9f8" width="60%"><font color="#000080"><b>Phred base score 
      (high quality cut-off)</font></td>
 <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("ER_PHRED_CUT_OFF") %>  </td>    </tr>
<tr> 
    <td  bgColor="#b8c6ed" width="60%"><b>Phred base score (low quality cut-off)</td>
 <td bgColor="#b8c6ed">  <%= spec.getParameterByNameString("ER_PHRED_LOW_CUT_OFF") %></td>   </tr>
 
	<tr> 
    <td >&nbsp; </td>  </tr></td></tr> -->
  <tr> 
    <td colspan=2 ><p><b>Penalties for mutation in gene regions</b> </p> 
     
      <table width="85%" border="0" align="center">
        <tr class='headerRow'> 
          <td >Base Confidence</div></td>
          <td >High </div></td>
          <td >Low </div></td>
        </tr>
        
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td width="44%" class='coloredFont'>Silent mutation</td>
      
          <td width="16%" ><div align="center"><%= spec.getParameterByNameString("ER_S_H")%></div></td>
          <td width="14%" ><div align="center"> <%= spec.getParameterByNameString("ER_S_L")%></div></td>
        </tr>
        <%if ( spec.getParameterByNameString("ER_C_H") == null || !((String)spec.getParameterByNameString("ER_C_H")).equalsIgnoreCase("not set") )
        {%>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Conservative substitution</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_C_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_C_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Non-conservative    substitution</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_NC_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_NC_L")%></div></td>
        </tr>
        <%}
        else 
        {        %>
           <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Missense    substitution</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_MISS_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_MISS_L")%></div></td>
        </tr>
        <%}%>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Frameshift</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_FR_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_FR_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>In-frame deletion</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_IDEL_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_IDEL_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>In-frame insertion</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("ER_IINS_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_IINS_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Truncation</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("ER_TRANC_H")%>    </div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_TRANC_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>No translation(             no ATG)</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("ER_NOTRANSLATION_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("ER_NOTRANSLATION_L")%></div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>Post-elongation           ( no stop codon)</td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_PLONG_H")%></div></td>
          <td ><div align="center"><%= spec.getParameterByNameString("ER_PLONG_L")%></div></td>
        </tr>
      </table></td>
  </tr>
<% row_count = 0;%>
    </tr>
	<tr><td colspan="2"> &nbsp;</td></tr> <td colspan=2 ><p><b>Penalties for mutation in linker region</b> </p> 
     
      <table width="85%" border="0" align="center">
        <tr  class='headerRow'> 
          <td >Base Confidence</div></td>
          <td >High </div></td>
          <td >Low </div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2] %>> 
          <td width="44%" class='coloredFont'>5'             substitution </td>
          <td width="16%" ><div align="center">              <%= spec.getParameterByNameString("ER_5S_H")%>            </div></td>
          <td width="14%" ><div align="center">              <%= spec.getParameterByNameString("ER_5S_L")%>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>5' deletion/insertion</td>
          <td ><div align="center">              <%= spec.getParameterByNameString("ER_5DI_H")%>            </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("ER_5DI_L")%>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2] %>> 
          <td class='coloredFont'>3' substitution</td>
          <td ><div align="center">               <%= spec.getParameterByNameString("ER_3S_H")%>            </div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("ER_3S_L")%>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2] %>> 
          <td class='coloredFont'>3' deletion/insertion</td>
          <td ><div align="center">           <%= spec.getParameterByNameString("ER_3DI_H")%>            </div></td>
       	<td ><div align="center">               <%= spec.getParameterByNameString("ER_3DI_L")%>            </div></td>
      </table></td>
  </tr>
<tr><td colspan="2"> &nbsp;</td></tr>
  <tr><td colspan="2">
  <p><b>Penalties for mutation introdues by ambiguous bases</b><p>
		<input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show </p>
      <p></p>
	  <DIV ID="divShowHide" STYLE="  position:relative;  clip:rect(0px 120px 120px 0px); "> 
	  <% row_count = 0;%>
        <table width="85%" border="0" align="center">
          <tr  class='headerRow'> 
            <td ><div align="right">Base                 Confidence </div></td>
            <td ><div align="center">High</div></td>
            <td ><div align="center">Low</div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td width="44%" class='coloredFont'>Start               codon substitution</td>
            <td width="16%" ><div align="center"> <%= spec.getParameterByNameString("ER_NSTART_PASS_H")%></div></td>
            <td width="14%"><div align="center"> <%= spec.getParameterByNameString("ER_NSTART_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Stop codon               substitution</b></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NSTOP_PASS_H")%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NSTOP_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Substitution in               CDS region </td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NCDS_PASS_H" )%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NCDS_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Frameshift Insertion</font></strong></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NFRAME_PASS_H")%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NFRAME_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Inframe Insertion</td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NINFRAME_PASS_H" )%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_NINFRAME_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[++row_count % 2] %>> 
            <td class='coloredFont'>Substitution in               5' linker region</td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N5SUB_PASS_H")%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N5SUB_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Insertion in 5'               linker region </td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N5INS_PASS_H" )%>              </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N5INS_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[++row_count % 2] %>> 
            <td class='coloredFont'>Substitution in               3' linker region</td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N3SUB_PASS_H")%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N3SUB_PASS_L")%>               </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td class='coloredFont'>Insertion in 3'               linker region </td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N3INS_PASS_H" )%>               </div></td>
            <td ><div align="center"> <%= spec.getParameterByNameString("ER_N3INS_PASS_L")%>               </div></td>
          </tr>
        </table>
      </div>
  </td></tr>
  </table>
<p> 
<hr>

<%}}%>
</body>

</html>


