
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.Constants" %>
<%@ page import="java.util.*" %>
<html>
<head>
    
    
</head>
<body>

	<p><P>
<br>
<div align="center">
  <center><table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr> <td width="100%"><html:errors/></td></tr></table></center></div>
    
    
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr><td > <font color="#008000" size="5"><b> Polymorphism hits.<hr><p></td></tr>
    <tr> <td width="19%"><strong>Sequence Id:</strong></td><td>
    
     <A HREF="" onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")%>Seq_GetItem.do?forwardName=<%=Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID=<%= request.getParameter("SEQUENCE_ID") %>','SEQ<%= request.getParameter("SEQUENCE_ID") %>','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">    	<%= request.getParameter("SEQUENCE_ID")%>	</a></td></tr> 
  <tr> <td ><strong>Discrepancy Id:</strong></td><td><%= request.getParameter("DISCR_ID")%></td></tr> 



<tr><td colspan=2 > Hits:</td></tr>
    <%
    if (  request.getParameter("POLYM_IDS")!= null )
        {
    ArrayList items = Algorithms.splitString( (String) request.getParameter("POLYM_IDS"),"|");
     for (int count = 0; count < items.size(); count++)
     {
        if ( count % 2 == 1) { %> 
<tr><td colspan=2><a  href="#" onclick="window.open('http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<%=  (String) items.get(count) %>','<%=  (String) items.get(count) %>','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;"> <%=  (String) items.get(count) %> </a>         </td></tr>
<%  } }}%>

 </table>
</body>
</html>