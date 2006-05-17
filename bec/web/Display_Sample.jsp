<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

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
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Sample Report  </font>
    <hr><p></td> </tr>
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
<% UICloneSample sample = (UICloneSample)request.getAttribute("sample") ;
ArrayList end_read = (ArrayList)request.getAttribute("end_read");
ArrayList contigs = (ArrayList)request.getAttribute("contigs");
ArrayList clone_sequences = (ArrayList)request.getAttribute("clone_sequences");
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="39%"><strong>Plate Label:</strong></td>
    <td width="61%"> <%= request.getAttribute("container_label") %> </td>
  </tr>
  
  <tr> 
    <td><strong>Position: </strong></td>
    <td> <%= sample.getPosition() %> </td>
  </tr>
   <tr>     <td clospan = 2>&nbsp;</td>  </tr>
  <tr> 
    <td><strong>Clone Id:</strong></td>
    <td> <%= sample.getCloneId() %> </td>
  </tr>
  <tr> 
    <td><strong>Clone Final Status:</strong></td>
    <td> <%= IsolateTrackingEngine.getCloneFinalStatusAsString(sample.getCloneFinalStatus()) %> </td>
  </tr>
  <tr>     <td clospan = 2>&nbsp;</td>  </tr>
  <tr> 
    <td><strong>Reference Sequence Id:</strong></td>
    <td> <a href="#" onCLick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getRefSequenceId()%>','newWndRefseqNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
      <%= sample.getRefSequenceId()%></a> </td>
  </tr>
    <tr>     <td clospan = 2>&nbsp;</td>  </tr>

  
  <tr> 
    <td colspan="2"><p><strong>End Reads:</strong></p>
      <table width="90%" border="1" align="center" cellpadding="2" cellspacing="2">
	 <th>Read Type </th>
        <th>Read Id </th>
        <th>Alignment </th>
        <th>Discrepancy Report </th>
        <%

        UIRead read   = null;
        for (int read_count = 0; read_count < end_read.size(); read_count++)
	  {
	  		 read  = (UIRead)end_read.get(read_count);
			%>
        <tr>
          <td width="20%"><%= read.getTypeAsString() %></td>
          <td width="15%">
		 <A HREF="" onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READ_REPORT_INT%>&amp;ID=<%= read.getId()%>','<%= read.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%= read.getId() %>
		 </a></td>
          <td >
<% if (read.isAlignmentExists())
{%>
 <input type=BUTTON value=Alignment onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= read.getSequenceId()%>&amp;TYPE=<%= BaseSequence.READ_SEQUENCE %>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= sample.getRefSequenceId ()%>','<%= "A"+read.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else
 {%>Not available<%}%>
		 </td>
        
<td width="30%">
<% if (read.isDiscrepancies())
{%>		
<input type=BUTTON value="Discrepancy Report"  onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= read.getSequenceId()%>','<%= "D"+read.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else {%>No discrepancies<%}%> 

</td>
        </tr>
       <%}%>
      </table>
</td></tr>
<% if (request.getAttribute("discrepancy_report_for_endread") != null)
{%>
<tr> 
    <td colspan="2"><strong>Discrepancy Report based on End Reads Analysis:</strong>
<table align="center" border='1'>
<tr><td>Type</td><TD>Number</td><TD>Quality</td></tr></TR>
	<%= request.getAttribute("discrepancy_report_for_endread") %></table>
 </td> </tr>
<%}%>
 <tr>     <td colspan="2">&nbsp;</td></tr>
 <% if ( contigs != null && contigs.size()>0)
{%>
  <tr> 
    <td colspan="2"><p><strong>Contigs:</strong></p>
      <table width="90%" border="1" align="center" cellpadding="2" cellspacing="2">
	 <th>Contig Name </th>
	 <th>Contig Type</th>
        <th>Contig Id </th>
        <th>Alignment </th>
        <th>Discrepancy Report </th>
        <%

        UIRead contig   = null;
        for (int contig_count = 0; contig_count < contigs.size(); contig_count++)
	  	{
	  		 contig  = (UIRead)contigs.get(contig_count);
			%>
        <tr>
          <td width="20%"><%= contig.getName() %></td>
		  <td ><%= Stretch.getStretchTypeAsString( contig.getType() ) %> </td>
          <td width="15%">
		 <A HREF="" onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.STRETCH_REPORT_INT%>&amp;ID=<%= contig.getId()%>','<%= contig.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%= contig.getId() %>	 </a></td>
          <td >
<% if (contig.isAlignmentExists())
{%> <input type=BUTTON value=Alignment onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= contig.getSequenceId()%>&amp;TYPE=<%= BaseSequence.CLONE_SEQUENCE %>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= sample.getRefSequenceId ()%>','<%= "A"+contig.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"><%}
else {%>Not available<%}%>		 </td>
<td width="30%"><% if (contig.isDiscrepancies())
{%>		
<input type=BUTTON value="Discrepancy Report"  onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= contig.getSequenceId()%>','<%= "D"+contig.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else {%>No discrepancies<%}%> 
</td>       </tr>       <%}%>      </table></td></tr>
 <tr>     <td colspan="2">&nbsp;</td></tr><%}%>

<% if ( clone_sequences != null && clone_sequences.size()>0)
{%>
<tr>
    <td colspan="2"><p><strong>Clone Sequences:</strong></p>

       <table width="90%" border="1" align="center" cellpadding="2" cellspacing="2">
        <th>Sequence Id </th>
        <th>Type </th>
        <th>Status </th>
        <th>Alignment </th>
        <th>Discrepancy Report </th>
	  <%UISequence clonesequence = null;
          for (int seq_count =0; seq_count < clone_sequences.size(); seq_count ++)
          {
            clonesequence = (UISequence) clone_sequences.get(seq_count);
%>
        <tr>
          <td width="17%"><a href="#" onCLick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT%>&amp;ID=<%= clonesequence.getId()%>','<%= clonesequence.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
		<%= clonesequence.getId() %></a></td>
         <td width="17%"><%= clonesequence.getCloneSequenceTypeAsString() %></td>
         <td width="17%"><%= clonesequence.getAnalysisStatusAsString() %></td>
          <td>
<%if (clonesequence.isAlignmentExists())
{%>		
<input type=BUTTON value=Alignment onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= clonesequence.getId() %>&amp;TYPE=<%= BaseSequence.CLONE_SEQUENCE%>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= sample.getRefSequenceId ()%>','<%= clonesequence.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else
 {%>Not available<%}%> 
 </td>
<td >
<% if (clonesequence.isDiscrepancies())
{%>		
<input type=BUTTON value="Discrepancy Report"  onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= clonesequence.getId()%>','<%= clonesequence.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else
 {%>No Discrepancies<%}%>  

</td>
        </tr>

       <%}}%>
      </table>
      <p><strong></strong></p></td>
  </tr>
</table>
<P><P></P></P>
</body>
</html>