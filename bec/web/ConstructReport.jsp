<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>

<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> construct Report  </font>
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
 <html:form action="/RunProcess.do" > 
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" >

<% Construct construct = (Construct)request.getAttribute("construct") ;
 ArrayList clones_data = (ArrayList) request.getAttribute("clones_data");	%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="39%"><strong>Construct Id:</strong></td>
    <td width="61%"> <%= construct.getId() %> </td>
  </tr>
  <tr> 
    <td><strong>Construct Format: </strong></td>
    <td> <%= construct.getFormatAsString() %> </td>
  </tr>
   <tr> 
    <td><strong>Reference Sequence Id:</strong></td>
    <td> <a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= construct.getRefSeqId()%>','newWndRefseqNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
      <%= construct.getRefSeqId()%></a> </td>
  </tr>
  
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  
  
  <tr> 
    <td colspan="2"><strong>Clones:</strong>
<P>
<table align="center" border='1'>
<tr>
<th>Plate Label </TH>
<TH>Position</TH>
<TH>Current Rank</TH>
<TH>Isolate Status</TH>
<TH>Rank</TH>
</tr>
        <%
        UICloneSample clone = null;
int rank = -1; String rank_as_string = null;
int status = -1;
       for (int count = 0; count < clones_data.size(); count++)
        {
            clone = (UICloneSample) clones_data.get(count); 
            status = clone.getCloneStatus();
            rank = clone.getRank();
            switch (rank)
{
     case 1: case 2: case 3: case 4: {rank_as_string = String.valueOf(rank); break;}
     case IsolateTrackingEngine.RANK_NOT_APPLICABLE:{ rank_as_string = "Not applicable"; break;}
     case IsolateTrackingEngine.RANK_BLACK:{ rank_as_string = "Bad clone"; break;}
     case -1: { rank_as_string = "Not calculated"; break;}
}
%>
<tr>
<TD width="30%"><%= clone.getPlateLabel() %></TD>
<TD width="15%" align=center><%= clone.getPosition() %></TD>
<TD width="20%" align=center><%= rank_as_string %></TD>
<TD width="20%" align=center><%= IsolateTrackingEngine.getStatusAsString(status) %></TD>
<TD align=center>

<% if (status == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED ||
    status == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH )
{%>
<select name="<%=  clone.getIsolateTrackingId() %>" >
    <option value=1 <% if (rank == 1) {%> selected <%}%> >Best
    <option value=2 <% if (rank == 2) {%> selected <%}%> >Second
    <option value=3 <% if (rank == 3) {%> selected <%}%> >Third
    <option value=4 <% if (rank == 4) {%> selected <%}%> >Four
    <option value=-2 <% if (rank == -2) {%> selected <%}%> >Bad Clone
</select>
<%}
  else 
{%>
    &nbsp;
<%}%>
</TD>
</TR>

       <%}%>
</table>
     
  </tr>
</table>
<P><P><div align="center"> <input type="SUBMIT"/></DIV>
</html:form> 
</body>
</html>