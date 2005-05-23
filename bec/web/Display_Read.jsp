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
        <td >
    <font color="#008000" size="5"><b> 
	Read Details 
	
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
<% 
Read read =(Read) request.getAttribute("read"); %>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
  <TR> 
    <TD width="35%" ><strong>Read Id:</strong></td>
    <td ><%= read.getId()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Read Score:</strong></td>
    <td ><%= read.getScore()%></TD>
  </TR>
 <TR> 
    <TD width="35%" >&nbsp;</td>
    <td >&nbsp;</TD>
  </TR>
 <TR> 
    <TD width="35%" ><strong>Read Sequence Id:</strong></td>
    <td ><%= read.getSequence().getId()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Cds Start:</strong></td>
    <td ><%= read.getCdsStart()%></TD>
  </TR>
   <TR> 
    <TD width="35%" ><strong>Cds Stop:</strong></td>
    <td ><%= read.getCdsStop()%></TD>
  </TR>
  <TR> 
    <TD colspan=2 >&nbsp; </TD>
  </TR>
  <TR> 
    <TD colspan =2 ><p><strong>Read Sequence:</strong></p>
      <table width="85%" border="0" align="center" cellpadding="2" cellspacing="2">
        <tr> 
          <td width="71%"><strong>Trimmed Sequence starts from position:</strong></td>
          <td width="29%"><%= read.getTrimStart() %></td>
        </tr>
        <tr> 
          <td><strong>Trimmed Sequence ends at position:</strong></td>
          <td><%= read.getTrimEnd() %></td>
        </tr>
        <tr> 
          <td colspan="2"> <PRE><%= read.getSequence().toHTMLString() %></PRE>
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
      </table></td>
  </TR>
  <TR> 
    <TD colspan="2" ><strong>Discrepancy Report: </strong> 
      <P></P><table align="center" border='1'>
<tr><td>Type</td><TD>Number</td><TD>Quality</td></tr></TR>
	<%= request.getAttribute("discrepancy_report") %></table></TD>
  </TR>
 
</TABLE>

 
</body>
</html>