<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>

<title>linker</title>

</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> linker Information </font>
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

<% BioLinker linker = (BioLinker) request.getAttribute("linker");%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr>
	<td width="25%" bgColor="#b8c6ed">  <b>Linker Name</b>  </td>
	<td width="75%" bgColor="#b8c6ed"><%= linker.getName() %></td>
</tr>
<tr> 
	<td  bgColor="#e4e9f8" ><strong>Sequence: </strong></td>
	<td bgColor="#e4e9f8"> <%= linker.getSequence() %></td>
</tr>
<tr> 
	<td  bgColor="#b8c6ed" ><b>Linker Id:</b></td>
	 <td bgColor="#b8c6ed"><%= linker.getId() %></td>   </tr>
 
  </table>
<p> 
<hr>

</body>

</html>


