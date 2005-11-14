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
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td > <font color="#008000" size="5"><b> Create new set of parameter for Clone       Scoring</font> 
      <hr><p></td></tr></table>

<div align="center">
  <center><table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr><td width="100%"><html:errors/></td>
    </tr><tr><td><i>If you are not sure about certain parameter settings, use default settings  
</i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. </td></tr>
  </table></center></div>

<html:form action="/Seq_SubmitSpec.do" > 

<input name="forwardName" type="hidden" value="<%=EndReadsSpec.END_READS_SPEC_INT%>" >


<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>

<tr><td colspan =2><div align="right"><b>
<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> View Mine </a>
&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT%>">  View All </a>

<% String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;%>

</b> </div><P><P><P></P></P></P></td></tr>
<TR><td colspan="2">

<P> <font  size="4"> <b>Set Name</b></font> 
  <input type="text" name="SET_NAME" size="53" value="">
<P>
</td></tr>

  <tr> 
    <td colspan=2 ><p><b>Penalty for mutation in the gene region</b> </p> 
     <input type='radio' name='isMissense' value='1' checked>Process conservative & non-conservative substitutions together (set penalties for Missense substitution, penalties for conservative & non-conservative substitutions will not be taken into account)
 <P><input type='radio' name='isMissense' value='0'>Process conservative & non-conservative substitutions separately (set penalties for Conservative and Non-conservative substitution, penalties for Missense substitution will not be taken into account)
      <table width="85%" border="0" align="center">
        <tr class='headerRow'> 
	 <td ><div align="right">Base Confidence</div></td>
          <td><div align="center">High </div></td>
          <td ><div align="center">Low </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>>
          <td width="44%"  >Silent substitution</td>
          <td width="16%" ><div align="center">        <input name="ER_S_H" type="input" id="ER_S_H" value="3" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td width="14%" ><div align="center">               <input name="ER_S_L" type="input" id="ER_S_L" value="1" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
	<tr class=<%= row_class[row_count++ % 2]%>> 
		  
          <td >Missense substitution</td>
          <td ><div align="center">               <input name="ER_MISS_H" type="input" id="ER_MISS_H" value="20" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_MISS_L" type="input" id="ER_MISS_L" value="10" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
    
        <tr class=<%= row_class[row_count++ % 2]%>> 
	     <td >Conservative substitution</td>
          <td ><div align="center">               <input name="ER_C_H" type="input" id="ER_C_H" value="20" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_C_L" type="input" id="ER_C_L" value="10" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>> 
                <td >Non-conservative substitution</td>
            <td ><div align="center">               <input name="ER_NC_H" type="input" id="ER_NC_H" value="50" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
            <td ><div align="center">               <input name="ER_NC_L" type="input" id="ER_NC_L" value="30" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> >  
          <td >Frameshift</td>
          <td ><div align="center">               <input name="ER_FR_H" type="input" id="ER_FR_H" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_FR_L" type="input" id="ER_FR_L" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> >
          <td >In-frame deletion</td>
          <td ><div align="center">               <input name="ER_IDEL_H" type="input" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_IDEL_L" type="input" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
         <tr class=<%= row_class[row_count++ % 2]%> >
          <td >In-frame insertion</td>
          <td ><div align="center">               <input name="ER_IINS_H" type="input" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td><div align="center">               <input name="ER_IINS_L" type="input" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>>  
            <td >Truncation</font></strong></td>
          <td ><div align="center">               <input name="ER_TRANC_H" type="input" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">
            </div></td>
          <td ><div align="center">               <input name="ER_TRANC_L" type="input" id="ER_TRANC_L" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">
            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%>>  
          <td >No translation (no ATG)</td>
          <td ><div align="center">               <input name="ER_NOTRANSLATION_H" type="input" id="ER_NOTRANSLATION_H" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_NOTRANSLATION_L" type="input" id="ER_NOTRANSLATION_L" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class=<%= row_class[row_count++ % 2]%> >
            <td ><strong><font color="#000080">Post-elongation (no stop codon)</font></strong></td>
          <td ><div align="center">               <input name="ER_PLONG_H" type="input" id="ER_PLONG_H" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_PLONG_L" type="input" id="ER_PLONG_L" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
      </table></td>
  </tr>
  <tr><td colspan="2"> &nbsp;</td></tr><td colspan=2 ><p><b>Penalty for mutation in the linker region</b> </p> 
     <% row_count = 0;%>
      <table width="85%" border="0" align="center">
        <tr class='headerRow'> 
          <td ><div align="right">Base Confidence</div></td>
          <td ><div align="center">High </div></td>
          <td ><div align="center">Low </div></td>
        </tr>
        <tr class='oddRow'> 
          <td width="44%" >5' substitution</td>
          <td width="16%" ><div align="center">     <input name="ER_5S_H" type="input" id="ER_5S_H" value="3" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td width="14%"><div align="center">               <input name="ER_5S_L" type="input" id="ER_5S_L" value="1" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class='oddRow'> 
          <td >5' deletion/insertion</font></strong></td>
          <td ><div align="center">               <input name="ER_5DI_H" type="input" id="ER_5DI_H" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_5DI_L" type="input" id="ER_5DI_L" value="500" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class='evenRow'> 
          <td >3' substitution</font></strong></td>
          <td ><div align="center">              <input name="ER_3S_H" type="input" id="ER_3S_H" value="50" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
          <td ><div align="center">               <input name="ER_3S_L" type="input" id="ER_3S_L" value="30" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
        </tr>
        <tr class='evenRow'> 
          <td >3' deletion/insertion</font></strong></td>
          <td ><div align="center">               <input name="ER_3DI_H" type="input" id="ER_3DI_H" value="1000" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
       	<td ><div align="center">               <input name="ER_3DI_L" type="input" id="ER_3DI_L" value="500" size="10" onBlur="checkNumeric(this,0,1000,'','','');">            </div></td>
      </table></td>
  </tr>
<tr><td colspan="2"> &nbsp;</td></tr>  <tr><td colspan="2">
      <p><b>Penalty for mutation introduced by ambiguous bases
</b> <P><input type="checkbox" name="show" value="1" checked onclick="javascript:showhide('divShowHide', this.checked);">
		Show </p>
      <p></p>
	  <DIV ID="divShowHide" STYLE="  position:relative;  clip:rect(0px 120px 120px 0px); "> 
   <% row_count = 0;%>
     <table width="85%" border="0" align="center">
          <tr class='headerRow'> 
            <td ><div align="right">Base Confidence</div></td>
            <td ><div align="center">High </div></td>
            <td ><div align="center">Low  </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td width="44%" >Start              codon substitution</td>
            <td width="16%" ><div align="center">                 <input name="ER_NSTART_PASS_H" type="input" id="ER_NSTART_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td width="14%"><div align="center">                 <input name="ER_NSTART_PASS_L" type="input" id="ER_NSTART_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[row_count % 2] %>> 
            <td >Stop codon               substitution</b></td>
            <td ><div align="center">                 <input name="ER_NSTOP_PASS_H" type="input" id="ER_NSTOP_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_NSTOP_PASS_L" type="input" id="ER_NSTOP_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[row_count % 2] %>> 
            <td >Substituttion               cds region </font></strong></td>
            <td ><div align="center">                 <input name="ER_NCDS_PASS_H" type="input" id="ER_NCDS_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');" >              </div></td>
            <td ><div align="center">                 <input name="ER_NCDS_PASS_L" type="input" id="ER_NCDS_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[row_count % 2] %>> 
            <td >Frameshift Insertion</font></strong></td>
            <td ><div align="center">                 <input name="ER_NFRAME_PASS_H" type="input" id="ER_NFRAME_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_NFRAME_PASS_L" type="input" id="ER_NFRAME_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[row_count % 2] %>> 
            <td >Inframe Insertion</font></strong></td>
            <td ><div align="center">                 <input name="ER_NINFRAME_PASS_H" type="input" id="ER_NINFRAME_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_NINFRAME_PASS_L" type="input" id="ER_NINFRAME_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[++row_count % 2] %>> 
            <td >Substitution in 5'               linker region</font></strong></td>
            <td ><div align="center">                 <input name="ER_N5SUB_PASS_H" type="input" id="ER_N5SUB_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center"><input name="ER_N5SUB_PASS_L" type="input" id="ER_N5SUB_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr class=<%= row_class[row_count % 2] %>> 
            <td >Insertion in 5' linker               region </font></strong></td>
            <td ><div align="center">                 <input name="ER_N5INS_PASS_H" type="input" id="ER_N5INS_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_N5INS_PASS_L" type="input" id="ER_N5INS_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[++row_count % 2] %>> 
            <td >Substitution in 3'               linker region</td>
            <td ><div align="center">                 <input name="ER_N3SUB_PASS_H" type="input" id="ER_N3SUB_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_N3SUB_PASS_L" type="input" id="ER_N3SUB_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
          <tr  class=<%= row_class[row_count % 2] %>> 
            <td >Insertion in 3' linker               region </font></strong></td>
            <td ><div align="center">                 <input name="ER_N3INS_PASS_H" type="input" id="ER_N3INS_PASS_H" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
            <td ><div align="center">                 <input name="ER_N3INS_PASS_L" type="input" id="ER_N3INS_PASS_L" value="0" size="10" onBlur="checkNumeric(this,0,1000,'','','');">              </div></td>
          </tr>
        </table>
      </div>
		</td>
  </tr>
</table>
</p>
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>

</html>


