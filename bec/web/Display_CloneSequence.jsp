<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<html>
<head>
    
    
</head>
<body>

	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> Clone Sequence Report</font>
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
<% CloneSequence clone_sequence =(CloneSequence) request.getAttribute("clone_sequence"); %>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  <TR> 
    <TD width="35%" ><strong>Clone Sequence Id:</strong></td>
    <td ><%= clone_sequence.getId()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Plate Label:</strong></td>
    <td ></TD>
  </TR>
 <TR> 
    <TD width="35%" ><strong>Well Id:</strong></td>
    <td >&nbsp;</TD>
  </TR>
  <TR> 
    <TD colspan=2>&nbsp;</TD>
  </TR>
 <TR> 
    <TD width="35%" ><strong>Reference Sequence Id:</strong></td>
    <td ><a href="#" onCLick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= clone_sequence.getRefSequenceId()%>','<%= clone_sequence.getRefSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
		<%= clone_sequence.getRefSequenceId()%></a></TD>
  </TR>
  <TR> 
    <TD width="35%" ><strong> Analysis Status:</strong></td>
    <td ><%= BaseSequence.getSequenceAnalyzedStatusAsString(clone_sequence.getStatus())%></TD>
  </TR>
 <TR> 
    <TD width="35%" ><strong> Sequence Type:</strong></td>
    <td ><%= BaseSequence.getCloneSequenceTypeAsString(clone_sequence.getCloneSequenceType())%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Cds Start:</strong></td>
    <td ><%= clone_sequence.getCdsStart()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Cds Stop:</strong></td>
    <td ><%= clone_sequence.getCdsStop()%></TD>
  </TR>
<TR> 
    <TD width="35%" ><strong>5'Linker Start:</strong></td>
    <td ><%= clone_sequence.getLinker5Start()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>3' Linker Stop:</strong></td>
    <td ><%= clone_sequence.getLinker3Stop()%></TD>
  </TR>
  <TR> 
    <TD width="35%" ><strong>Alignment:</strong></td>
    <td >
    <% if (clone_sequence.getStatus() != CloneSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ){%>
<input type=BUTTON value=Alignment onClick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= clone_sequence.getId()%>&amp;TYPE=<%= BaseSequence.CLONE_SEQUENCE%>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= clone_sequence.getRefSequenceId ()%>','<%= clone_sequence.getId()+clone_sequence.getRefSequenceId ()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
    <%}else {%>Not available<%}%> </TD>
 
 
</TR>
<tr> <TD width="35%" ><strong>Discrepancy Report:</strong></td>
<td><%
 if (clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES 
   || clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED 
   || clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ANALYSIS_CONFIRMED)
{

%>		
<input type=BUTTON value="Discrepancy Report"  onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= clone_sequence.getId()%>','Discr<%= clone_sequence.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else
 {%>No Discrepancies<%}%>  </td>
</tr>
  <TR><TD colspan=2 >&nbsp; </TD>  </TR>
  <TR> 
    <TD colspan =2 ><p><strong>Sequence:</strong></p>
       <PRE><%= clone_sequence.toHTMLString() %></PRE>
            <P></P>
            <table align=center width="100%" border=0>
              <tr> 
                <td colspan=4 align=center>phred generated qualities</td>
              </tr>
              <tr> 
                <td colspan=4 align=center>&nbsp;</td>
              </tr>
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
     </td>
  </TR>
 
 
</TABLE>

 
</body>
</html>