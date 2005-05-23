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
	Scored Sequence 
	
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
<% ScoredSequence scoredsequence = (ScoredSequence) request.getAttribute("scoredsequence"); %>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  
  <TR> 
    <TD width="35%" >Bec ID:</td>
    <td ><%= scoredsequence.getId()%></TD>
  </TR>
 
  
  <TR> 
    <TD >Sequence Length:</td>
    <td><%= scoredsequence.getText().length()%></TD>
  </TR>
<% if (request.getAttribute("trimstart") != null)
{%>
  <TR> 
    <TD >Trimmed Sequence starts from position:</td>
    <td><%= request.getAttribute("trimstart")%></TD>
  </TR>
<%}
if ( request.getAttribute("trimend") != null)
{%>
  <TD >Trimmed Sequence ends at position:</td>
  <td><%=  request.getAttribute("trimend") %></TD>
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
      
      <PRE><%= scoredsequence.toHTMLString() %></PRE> </TD>
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