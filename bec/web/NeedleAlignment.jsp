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
	needle Alignment
	
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

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  
  <TR> 
    <TD width="35%" >Bec ID:</td>
    <td ><%= request.getAttribute("expsequenceid")  %></TD>
  </TR>
 
  

<% if (request.getAttribute("trimstart") != null)
{%>
  <TR> 
    <TD >Trimmer Sequence starts from position:</td>
    <td><%= request.getAttribute("trimstart")%></TD>
  </TR>
<%}
if ( request.getAttribute("trimend") != null)
{%>
  <TD >Trimmed Sequence ends at position:</td>
  <td><%=  request.getAttribute("trimend") %></TD>
  </TR>
<%}%>
  <% if (request.getAttribute("refsequenceid") != null)
{%>
  <TR> 
    <TD >Reference Sequence:</td>
    <td><a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= request.getAttribute("refsequenceid") %>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;" > 
		<%= request.getAttribute("refsequenceid")%></a></TD>
  </TR>
<%}%>
  <TR> 
    <TR> 
    <TD >&nbsp;</TD> <TD >&nbsp;</TD>
  </TR>
  
  <TR>
    <TD COLSPAN=2> 
      
      <PRE><%= request.getAttribute("alignment") %></PRE> </TD>
  </TR>
</TABLE>

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
            </table>
</body>
</html>