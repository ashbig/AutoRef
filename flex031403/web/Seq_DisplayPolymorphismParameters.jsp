<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>

<html>

<head>

<title>Polymorphism Detector Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/>: Sequence Quality Assessment</h2>
<html:errors/>
 

<h3>All available sets of parameters for polymorphism detector.</h3>
  
<i> Parameter description</i> <a href="Help_SequenceEvaluation.html">[parameter 
help file]</a> 
<p></p>


<% ArrayList sets = (ArrayList)request.getAttribute("specs");
 if (sets.size()==0)
{%>
<p><b>No sets are available</b>
<%}
else if (sets.size() > 0 )
  {
%>
<%
    for (int count = 0; count < sets.size() ; count++)
    {
		PolymorphismSpec spec = (PolymorphismSpec) sets.get(count);
 
	%>
<P>
 <P> <font color="#2693A6" size="4"> <b>Set Name</b></font>
<%= spec.getName() %>
<P>
<p>


<table width="85%" border="0" align="center">
  <tr > 
    <td width="70%" background="barbkgde.gif" ><strong>Species:</strong> </td>
    <td background="barbkgde.gif"> <%= spec.getParameterByNameString("PL_SPECIES") %></td>
  <tr> 
    <td ><strong>Database:</strong></td>
    <td> <%= spec.getParameterByNameString("PL_DATABASE") %>
    </td>
  </tr>
  <tr ><td colspan="2">&nbsp;  </td>  </tr>
  <tr>
  	<td background="barbkgde.gif"><b>Number of bases in flanking sequence:</b> </td>
		<td background="barbkgde.gif"><%= spec.getParameterByNameString("PL_BASES") %></td>
	</tr>
	<tr>	
		<td><b>Output format <i>(optional)</i>:</b> </td>
		<td><%= spec.getParameterByNameString("PL_FORMAT") %>      </td>
	</tr>
 
</table>

<%}}%>
</body>
</html>
