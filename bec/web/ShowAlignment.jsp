<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<html>
<head>
    
    
</head>
<body>

	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> 
	sequence Alignment
	
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
<% BaseSequence exp_sequence = (BaseSequence) request.getAttribute("exp_sequence");
%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  
  <TR> 
    <TD width="35%" >Experimental Sequence Id:</td>
    <td ><%= exp_sequence.getId()%></TD>
  </TR>
 
  
  <TR> 
    <TD >Experimental Sequence Length:</td>
    <td><%= exp_sequence.getText().length()%></TD>
  </TR>
 <TR> 
    <TD >Reference Sequence Id:</td>
    <td>
        <a href="#" onCLick="window.open('<%=edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= request.getAttribute("refsequence_id")%>');" > 
        <%= request.getAttribute("refsequence_id") %>
        </a></TD>
  </TR>

  <TR> 
    <TR> 
    <TD >&nbsp;</TD> <TD >&nbsp;</TD>
  </TR>
  <TR> 
    <TD >Alingment:</TD> <TD >&nbsp;</TD>
  </TR>
  <TR>
    <TD COLSPAN=2> 
      
      <PRE><%= request.getAttribute("alingment") %></PRE> </TD>
  </TR>
</TABLE>

<% 
	 Object text = null;
	 if (request.getAttribute(Constants.ADDITIONAL_JSP ) == null)
	 { 
	 	text =  request.getParameter(  Constants.ADDITIONAL_JSP  );
	}
	else
	{
		text = request.getAttribute( Constants.ADDITIONAL_JSP );
	}
        if (text != null)
        {
	%>
	 
		<%= title %>
<%}%>
</body>
</html>