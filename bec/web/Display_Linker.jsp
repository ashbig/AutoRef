<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head><title>Linker</title></head>
<LINK REL=STYLESHEET       HREF="application_styles.css"      TYPE="text/css">

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr><td >
    <font color="#008000" size="5"><b> Linker Information </font>
    <hr>
    
    <p> </td></tr>
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
<div align="center">
<table border="0" cellpadding="0" cellspacing="0" width="75%" >
<% 
    BioLinker linker = null;
    ArrayList linkers = (ArrayList) request.getAttribute("linkers");
  
 for (int count = 0; count < linkers.size(); count++)
{
    linker = (BioLinker)linkers.get(count); 
   
//force sequence wrap
 char[] linkerseq = linker.getSequence().toCharArray();
StringBuffer linkerseq_formated = new StringBuffer();
 for (int count1 = 0; count1 < linkerseq.length; count1++)
{
linkerseq_formated.append(linkerseq[count1]);
if (count1 !=0 && count1 % 40 == 0 )linkerseq_formated.append("<BR>");
}

%>

<tr class='headerRow'>	<td  height="26"> Linker </td><td>&nbsp;</td></tr>
<tr class='evenRowColoredFont'><td width="25%" >Linker Name  </td>	<td  ><%= linker.getName() %></td></tr>
<tr class='evenRowColoredFont'> 	<td  nowrap >Sequence: </td>	<td > <%= linkerseq_formated.toString() %></td></tr>
<% if (linker.getFrameStart() != -1)
{%>
<tr class='evenRowColoredFont'> 
	<td>Frame Start: </td>	<td > <%= linker.getFrameStart() %></td></tr>
<%}%>

<tr><TD>&nbsp;</TD></TR>

<%}%>
  </table>
</div>
</body>

</html>


