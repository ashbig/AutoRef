<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>

<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> sample Report  </font>
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
	
  </table>
  </center>
</div>
<p></p>
<% Sample sample = (Sample)request.getAttribute("sample") ;
	ArrayList reads = sample.getIsolateTrackingEngine().getEndReads();
	
	%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="39%"><strong>Container Label:</strong></td>
    <td width="61%"> <%= request.getAttribute("container_label") %> </td>
  </tr>
  <tr> 
    <td><strong>Sample Id:</strong></td>
    <td> <%= sample.getId() %> </td>
  </tr>
  <tr> 
    <td><strong>Position: </strong></td>
    <td> <%= sample.getPosition() %> </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td><strong>Reference Sequence Id:</strong></td>
    <td> <a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getRefSequenceId()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;" > 
      <%= sample.getRefSequenceId()%></a> </td>
  </tr>
  <tr> 
    <td><strong>Construct Type:</strong></td>
    <td> </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td colspan="2"><strong>Discrepancy Report:</strong>
<table align="center" border='1'>
<tr><td>Type</td><TD>Number</td><TD>Quality</td></tr></TR>
	<%= request.getAttribute("discrepancy_report") %></table> </td>
  </tr>
  <tr> 
    <td colspan="2"><p><strong>End Reads:</strong></p>
      <table width="74%" border="0" align="center" cellpadding="2" cellspacing="2">
	  <%for (int read_count = 0; read_count < reads.size(); read_count++)
	  {
	  		Read read  = (Read)reads.get(read_count);
			%>
        <tr>
          <td width="37%"><%= read.getTypeAsString() %></td>
          <td width="33%">
		 <A HREF="" onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.READ_REPORT_INT%>&amp;ID=<%= read.getId()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
		 <%= read.getId() %>
		 </a></td>
          <td width="30%">
 <input type=BUTTON value=Alignment onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= read.getSequenceId()%>&amp;BaseSequence.THEORETICAL_SEQUENCE_STR=<%= sample.getRefSequenceId ()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
		 </td>
        </tr>
       <%}%>
      </table>
      <p><strong></strong></p></td>
  </tr>
</table>
<P><P></P></P>
</body>
</html>