<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.feature.*" %>
<%-- The container that was searched --%>

<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> discrepancy Report  </font>
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
<% AnalyzedScoredSequence sequence = (AnalyzedScoredSequence)request.getAttribute("sequence") ;
 ArrayList discrepancies = null;
if ( sequence != null) discrepancies = sequence.getDiscrepancies() ;
else discrepancies = (ArrayList)request.getAttribute("discrepancies") ;
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<% if ( sequence != null)
  {%>
<tr> 
    <td width="19%"><strong>Sequence Id:</strong>
   
     <A HREF="" onClick="window.open('<%=edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION%>Seq_GetItem.do?forwardName=<%=Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sequence.getId()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
    	<%= sequence.getId()%>
	</a>
	</td>
  </tr>
<%}%>
  <tr> 
    <td> </P> 
        <%= Mutation.toHTMLString(discrepancies ) %>
     </td></tr></table>
</body>
</html>