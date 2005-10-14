<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

<html>

<head>

<title>Clone Bioevaluation Parameters</title>
<LINK REL=STYLESHEET       HREF="application_styles.css"      TYPE="text/css">


<script language="JavaScript" src="<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr><td ><font color="#008000" size="5"><b> Available sets of parameters for Biological Evaluation of Clones</font>
    <hr><p></td></tr></table>

<div align="center">
  <center><table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr><td width="100%"><html:errors/></td>
    </tr><tr><td><i>If you are not sure about certain parameter settings use default settings 
           </i> <a href="<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td></tr></table></center>
</div>

 
 


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
 if (sets.size()==0)
{%>
<p><b>No sets are available</b>
<%}
else if (sets.size() > 0 )
  {

    for (int count = 0; count < sets.size() ; count++)
    {
	FullSeqSpec spec = (FullSeqSpec) sets.get(count);
 
	%>
<P>
 <P>
 
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr>  <td colspan="2"> <p> <font  size="4"> <b>Set Name &nbsp;&nbsp;</b></font><%= spec.getName() %>      <p> </td>  </tr>
 
  <tr>     <td colspan=2><b>Maximum acceptable number of discrepancies (gene region): <p></p>
      
      <table width="85%" border="0" align="center">
        <tr class='headerRow'> 
          <td><div align="left">&nbsp;</div></td>
          <td colspan=2><div align="center">PASS</div></td>
          <td colspan=2><div align="center">FAIL </div></td>
          <td ><div align="center">Ignore if polymorphism </div></td>
        </tr>
        <tr class='headerRow'> 
          <td ><div align="right">Base Confidence </div></td>
          <td ><div align="center">High </div></td>
          <td ><div align="center">Low </div></td>
          <td ><div align="center">High</div></td>
          <td><div align="center">&nbsp;</div></td>
          <td>&nbsp;</td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> > 
          <td width="44%" >Silent             mutation</td>
          <td width="16%" ><div align="center"> <%= spec.getParameterByNameString("FS_S_PASS_H")%></div></td>
          <td width="14%"><div align="center"> <%= spec.getParameterByNameString("FS_S_PASS_L")%></div></td>
          <td width="13%"><div align="center"> <%= spec.getParameterByNameString("FS_S_FAIL_H")%></div></td>
          <td width="13%"><div align="center"> <%= spec.getParameterByNameString("FS_S_FAIL_L")%> 
            </div></td>
          <td width="13%"> <div align="center">               <input type="checkbox"  disabled
            <% if (spec.getParameterByName("FS_S_POLM") != null                ) 
          {%>
          	checked
          <%}%> 
          >
            </div></td>
        </tr>
        <% if ( spec.getParameterByNameString("FS_C_PASS_H") == null || !((String)spec.getParameterByNameString("FS_C_PASS_H")).equalsIgnoreCase("not set"))
            {%>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Conservative substitution</td>
          <td><div align="center">          <%= spec.getParameterByNameString("FS_C_PASS_H")%> </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_C_PASS_L")%></div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_C_FAIL_H")%> </div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_C_FAIL_L")%> </div></td>
          <td > <div align="center">      <input type="checkbox"  disabled
	           
	           <% if (spec.getParameterByName("FS_C_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	          >
            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Non-conservative            substitution</font></strong></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NC_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NC_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NC_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NC_FAIL_L")%>             </div></td>
          <td > <div align="center"> 
              <input type="checkbox"  disabled
            <% if (spec.getParameterByName("FS_NC_POLM") != null) 
            {%>
                checked
            <%}%> 
             >
            </div></td>
        </tr>
        <% } else {%>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Missense             substitution</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_MISS_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_MISS_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_MISS_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_MISS_FAIL_L")%>             </div></td>
          <td> <div align="center"> 
              <input type="checkbox"  disabled
            <% if (spec.getParameterByName("FS_MISS_POLM") != null) 
            {%>                checked            <%}%>              >            </div></td>
        </tr>
        <%}%>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Frameshift</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_FR_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_FR_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_FR_FAIL_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_FR_FAIL_L")%>             </div></td>
          <td > <div align="center"> 
              <input type="checkbox" disabled 

            <% if (spec.getParameterByName("FS_FR_POLM") != null) 
            {%>                checked            <%}%>             >            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >In-frame deletion</font></strong></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_PASS_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_PASS_L")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IDEL_FAIL_L")%></div></td>
          <td > <div align="center"> 
              <input type="checkbox"  disabled
	            
	            <% if (spec.getParameterByName("FS_IDEL_POLM") != null) 
	            {%>
	            	checked
	            <%}%> 
	            >
            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >In-frame insertion</font></strong></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IINS_PASS_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IINS_PASS_L")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IINS_FAIL_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_IINS_FAIL_L")%>             </div></td>
          <td > <div align="center"> 
              <input type="checkbox"  disabled
          
          <% if (spec.getParameterByName("FS_IINS_POLM") != null) 
          {%>          	checked          <%}%>           >            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Truncation</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_PASS_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_PASS_L")%> 
            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_FAIL_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_TRANC_FAIL_L")%></div></td>
          <td > <div align="center"> 
              <input type="checkbox"  
	            
	            <% if (spec.getParameterByName("FS_TRANC_POLM") != null) 
	            {%>	            	checked	            <%}%>  disabled>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >No translation (no ATG)</font></strong></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_PASS_L")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NOTRANSLATION_FAIL_L") %></div></td>
          <td > <div align="center"> 
              <input type="checkbox" disabled  
	           
	           <% if (spec.getParameterByName("FS_NOTRANSLATION_POLM") != null) 
	           {%>
	           	checked
	           <%}%> 
	           
	           >
            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Post-elongation (no stop codon)</font></strong></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_PASS_L")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_FAIL_H")%> </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_PELONG_FAIL_L")%> </div></td>
          <td > <div align="center"> 
              <input type="checkbox" disabled   
	            
	            <% if (spec.getParameterByName("FS_PELONG_POLM") != null) 
	            {%>
	            	 checked 
	            <%}%> 
	           >
            </div></td>
        </tr>
      </table></td>
  </tr>
 <tr><td colspan="2"> &nbsp;</td></tr> 
 <% row_count=0 ;%>
<tr>  <td colspan=2><b>Maximum acceptable number of discrepancies (linker region):</b> <p></p>
      <table width="85%" border="0" align="center">
        <tr class ='headerRow'> 
          <td ><div align="center">&nbsp;</div></td>
          <td colspan=2><div align="center">PASS </div></td>
          <td colspan=2><div align="center">FAIL </div></td>
        </tr>
        <tr class ='headerRow'> 
          <td> <div align="right">Base               Confidence: </div></td>
          <td ><div align="center">High </div></td>
          <td ><div align="center">Low </div></td>
          <td ><div align="center">High </div></td>
          <td ><div align="center">Low </div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2]%>> 
          <td width="44%" >5' substitution</td>
          <td width="14%" ><div align="center"> <%= spec.getParameterByNameString("FS_5S_PASS_H")%></div></td>
          <td width="14%"><div align="center">               <%= spec.getParameterByNameString("FS_5DI_PASS_L")%>            </div></td>
          <td width="14%"><div align="center">               <%= spec.getParameterByNameString("FS_5S_FAIL_H")%>            </div></td>
          <td width="14%"><div align="center">              <%= spec.getParameterByNameString("FS_5S_FAIL_L")%>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >5' deletion/insertion</td>
          <td ><div align="center">                <%= spec.getParameterByNameString("FS_5DI_PASS_H")%>            </div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_5DI_PASS_L")%>             </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_5DI_FAIL_H")%>              </div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_5DI_FAIL_L" )%>         </div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2]%>> 
          <td >3' substitution</td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_3S_PASS_H" )%>            </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_3S_PASS_L")%>           </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_3S_FAIL_H" )%>            </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_3S_FAIL_L")%>            </div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2]%>> 
          <td >3' deletion/insertion</td>         
            <td ><div align="center">              <%= spec.getParameterByNameString("FS_3DI_PASS_H")%>            </div></td>
          <td><div align="center">               <%= spec.getParameterByNameString("FS_3DI_PASS_L")%>            </div></td>
          <td ><div align="center">              <%= spec.getParameterByNameString("FS_3DI_FAIL_H")%>            </div></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_3DI_FAIL_L")%>            </div></td>
        </tr>
      </table>
	  </td></tr>
  <tr> <td colspan="2"> &nbsp;</td></tr> 
  
  <% row_count = 0;%>
 <tr> <td colspan=2> <b>Maximum acceptable number of discrepancies introduced by ambiquous 
        bases:</b>
		<input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show </p>
      <p></p>
	  <DIV ID="divShowHide" STYLE="
  position:relative;
  clip:rect(0px 120px 120px 0px);
 ">
      <table width="85%" border="0" align="center">
        <tr class ='headerRow'> 
          <td ><div align="center">&nbsp;</div></td>
          <td  colspan=2><div align="center">PASS   </div></td>
          <td colspan=2><div align="center">FAIL </div></td>
        </tr>
        <tr class ='headerRow'> 
          <td ><div align="right">Base               Confidence </div></td>
          <td ><div align="center">High</div></td>
          <td ><div align="center">Low </div></td>
          <td ><div align="center">High </div></td>
          <td ><div align="center">Low</div></td>
        </tr>
        <tr class=<%= row_class[row_count % 2]%>> 
          <td width="44%" >Start             codon substitution</td>
          <td width="16%" ><div align="center"> <%= spec.getParameterByNameString("FS_NSTART_PASS_H")%></div></td>
          <td width="14%"><div align="center"> <%= spec.getParameterByNameString("FS_NSTART_PASS_L")%>             </div></td>
          <td width="13%"><div align="center"> <%= spec.getParameterByNameString("FS_NSTART_FAIL_H")%>             </div></td>
          <td width="13%"><div align="center"> <%= spec.getParameterByNameString("FS_NSTART_FAIL_L")%>             </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Stop codon substitution</b></td>
          <td ><div align="center">               <%= spec.getParameterByNameString("FS_NSTOP_PASS_H")%></div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NSTOP_PASS_L")%> </div></td>
          <td ><div align="center">    <%= spec.getParameterByNameString("FS_NSTOP_FAIL_H")%></div></td>
          <td ><div align="center">      <%= spec.getParameterByNameString("FS_NSTOP_FAIL_L" )%> </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Substitution cds             region </td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NCDS_PASS_H" )%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NCDS_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NCDS_FAIL_H" )%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NCDS_FAIL_L")%>             </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Frameshift Insertion</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NFRAME_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NFRAME_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NFRAME_FAIL_H")%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NFRAME_FAIL_L")%>             </div></td>
        </tr>
		<tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Inframe Insertion</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NINFRAME_PASS_H" )%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NINFRAME_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NINFRAME_FAIL_H" )%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_NINFRAME_FAIL_L")%>             </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Substitution in 5'             linker region</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5SUB_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5SUB_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5SUB_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5SUB_FAIL_L")%>             </div></td>
        </tr>
		<tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Insertion in 5' linker             region </td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5INS_PASS_H" )%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5INS_PASS_L")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5INS_FAIL_H" )%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N5INS_FAIL_L")%>             </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Substitution in 3'             linker region</td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3SUB_PASS_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3SUB_PASS_L")%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3SUB_FAIL_H")%>             </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3SUB_FAIL_L")%>             </div></td>
        </tr>
	<tr class=<%= row_class[row_count++ % 2]%>> 
          <td >Insertion in 3' linker             region </td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3INS_PASS_H" )%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3INS_PASS_L")%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3INS_FAIL_H" )%>            </div></td>
          <td ><div align="center"> <%= spec.getParameterByNameString("FS_N3INS_FAIL_L")%>             </div></td>
        </tr>
        
      </table></div></td>
  </tr>

</table>
<P>
<hR>

<%}}%>
</body>
</html>
