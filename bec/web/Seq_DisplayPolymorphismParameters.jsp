<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

<html>

<head>

<title>Polymorphism Detector Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> available sets of parameters for Polymorphism Detection</font>
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
	<tr>
        <td><i>Parameters Definition </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 if (sets.size()==0)
{%>
<p><table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> <td><b>No sets are available</b></td></tr></table>
<%}
else if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
		PolymorphismSpec spec = (PolymorphismSpec) sets.get(count);
 
	%>

<p>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan =2><div align="right"><b>Set Name</b><%= spec.getName() %>
      <P></P></td>
  </tr>
  <tr > 
    <td width="70%" bgColor="#e4e9f8" ><font color="#000080"><strong>Species:</strong> 
      </font></td>
    <td bgColor="#e4e9f8"> <%= spec.getParameterByNameString("PL_SPECIES") %></td>
  <tr> 
    <td bgcolor="#b8c6ed"><font color="#000080"><strong>Database:</strong></font></td>
    <td bgcolor="#b8c6ed"> <%= spec.getParameterByNameString("PL_DATABASE") %> 
    </td>
  </tr>
  <tr > 
    <td colspan="2"><font color="#000080">&nbsp;</font> </td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><font color="#000080"><b>Number of bases in flanking 
      sequence:</b> </font></td>
    <td bgColor="#e4e9f8"><%= spec.getParameterByNameString("PL_BASES") %></td>
  </tr>
  <tr> 
    <td bgcolor="#b8c6ed"><font color="#000080"><b>Output format <i>(optional)</i>:</b> 
      </font></td>
    <td bgcolor="#b8c6ed"><%= spec.getParameterByNameString("PL_FORMAT") %> </td>
  </tr>
</table>

<%}}%>
</body>
</html>
