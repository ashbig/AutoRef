<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<html>
<body>
<p><P><br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >    <font color="#008000" size="5"><b> 	stretch Details    <hr>     <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>  </center>
</div>
<% Stretch stretch =(Stretch) request.getAttribute("stretch"); %>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  <TR><TD width="35%" ><strong>Stretch Id:</strong></td>    <td ><%= stretch.getId()%></TD>  </TR>
  <TR><TD width="35%" ><strong>Stretch Type:</strong></td>    <td ><%= Stretch.getStretchTypeAsString( stretch.getType() ) %></TD>  </TR>
 <TR><TD width="35%" ><strong>Cds Start:</strong></td>    <td ><%= stretch.getCdsStart()%></TD>  </TR>
 <TR><TD width="35%" ><strong>Cds Stop:</strong></td>    <td ><%= stretch.getCdsStop()%></TD>  </TR>
<TR><TD width="35%" ><strong>Stretch Collection Id:</strong></td>    <td >
<!-- <A HREF="" onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.STRETCH_COLLECTION_REPORT_INT%>&amp;ID=<%= stretch.getCollectionId()%>','<%= stretch.getCollectionId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"> -->
		 <%= stretch.getCollectionId() %>	<!-- </a> </td></tr> -->
<%if ( stretch.getType() == Stretch.GAP_TYPE_CONTIG || stretch.getType() == Stretch.GAP_TYPE_LOW_QUALITY)
    {%>
<TR>     <TD colspan=2 >&nbsp; </TD>  </TR>
<TR><TD width="35%" ><strong>Orientation:</strong></td>    <td ><%= Constants.getOrientationAsStringFullName(stretch.getOrientation())%></TD>  </TR>
<TR><TD width="35%" ><strong>Stretch Sequence Id:</strong></td>    <td ><%= stretch.getSequence().getId()%></TD>  </TR>
<TR><TD width="35%" ><strong>Sequence Analysis Status:</strong></td>    <td ><%= BaseSequence.getSequenceAnalyzedStatusAsString(stretch.getAnalysisStatus())%></TD>  </TR>
 
<TR>     <TD colspan=2 >&nbsp; </TD>  </TR>
<TR> 
    <TD colspan =2 ><p><strong>Sequence:</strong></p>
      <table width="85%" border="0" align="center" cellpadding="2" cellspacing="2">
        <tr> <td colspan="2"> <PRE><%= stretch.getSequence().toHTMLString() %></PRE>
         <P></P>
            <table align=center width="100%" border=0>
              <tr><td colspan=4 align=center>phred generated qualities</td> </tr>
              <tr> <td colspan=4 align=center>&nbsp;</td>   </tr>
              <tr> 
                <td width="25%" bgColor =orange>&nbsp;</td>
                <td width="25%" bgColor =blue>&nbsp;</td>
                <td width="25%" bgColor = green>&nbsp;</td>
                <td  bgcolor=red>&nbsp;</td>
              </tr>
              <tr align=center> 
                <td><font size=-2><10</font></td>
                <td><font size=-2><20</font></td>
                <td><font size=-2><25</font></td>
                <td><font size=-2>>=25</font></td>
            </table></td>
        </tr>
      </table></td>
  </TR>
 <TR>     <TD colspan="2" ><strong>LQR Report: </strong> 
      <P></P><table align="center" border='1'>
    	<%= request.getAttribute("lqr_report") %></table></TD>
  </TR>
  <TR>     <TD colspan="2" ><strong>Discrepancy Report: </strong> 
      <P></P><table align="center" border='1'>
     <tr><td>Type</td><TD>Number</td><TD>Quality</td></tr></TR>
	<%= request.getAttribute("discrepancy_report") %></table></TD>
  </TR>
<%}%>
</TABLE>

</body>
</html>