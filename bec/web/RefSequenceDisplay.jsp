<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<html>
<head>
    <title><bean:message key="bec.name"/> : Container Process History</title>
    
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> 
	reference Sequence 
	
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
<% RefSequence refsequence = (RefSequence) request.getAttribute("refsequence"); %>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  
  <TR> 
    <TD width="35%" >Bec ID:</td>
    <td ><%= refsequence.getId()%></TD>
  </TR>
 
  <TR> 
    <TD >Species:</td>
    <td><%= refsequence.getSpeciesAsString()%></TD>
  </TR>
 
  <TR> 
    <TD >Sequence Length:</td>
    <td><%= refsequence.getText().length()%></TD>
  </TR>
  <TR> 
    <TD >CDS Length:</td>
    <td><%= refsequence.getCdsStop() - refsequence.getCdsStart()%></TD>
  </TR>
  <TD >CDS Start:</td>
  <td><%=  refsequence.getCdsStart()%></TD>
  </TR>
  <TD >CDS Stop:</td>
  <td><%= refsequence.getCdsStop()%></TD>
  </TR>
   <TR> 
    <TD >GC Content:</td>
    <td><%= refsequence.getGCcontent()%></TD>
  </TR>
  <% 
  PublicInfoItem item = null;
  for (int count = 0; count< refsequence.getPublicInfo().size(); count++)
  {
		  item=			(PublicInfoItem)refsequence.getPublicInfo().get(count);
		  %>
    <TR> 
     
    	<TD colspan=2 ><table width="90%">
			<TR>
			<TD width="35%"><%= item.getName()%></td>
			<TD><%= item.getValue()%></td>
			</tr>
		</table></TD>
  	</TR>
  <%}%>
  <TR> 
    <TR> 
    <TD >&nbsp;</TD> <TD >&nbsp;</TD>
  </TR>
  <TR> 
    <TD >Sequence:</TD> <TD >&nbsp;</TD>
  </TR>
  <TR>
    <TD COLSPAN=2> 
      
      <PRE><%= refsequence.getHTMLText() %></PRE> </TD>
  </TR>
</TABLE>
</body>
</html>