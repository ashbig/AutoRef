<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>

<%-- The container that was searched --%>
<logic:present name="<%=Constants.CONTAINER_KEY%>"> 
    <bean:define name="<%=Constants.CONTAINER_KEY%>" id="searchContainer"/>
</logic:present>
<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> container Process History  </font>
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
<% Container container = (Container)request.getAttribute("container") ;%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Label:</strong></td>
    <td width="81%"> 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Container Id:</strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Container Type:</strong></td>
    <td> 
      <%= container.getType() %>
    </td>
  </tr>
  
  
  <tr> 
    <td><strong>Cloning Startegy</strong></td>
    <td> 
      <a href="/BEC/Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategy() %>">
	    <%= container.getCloningStrategy() %></A>
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Id</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Status</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Refference Sequence</font></strong></th>
		 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Sequence</font></strong></th>
    </tr>
<%  
    String row_color = " bgColor='#e4e9f8'";
	Sample sample=null;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (Sample)container.getSamples().get(count);
		if (count % 2 == 0)
		{
		  row_color = " bgColor='#e4e9f8'";
		}
		else
		{
			row_color =" bgColor='#b8c6ed'";
		}
	%>
	<tr>

		<td <%= row_color %>><%= sample.getPosition() %> </td>
		<td <%= row_color %>> <%= sample.getType()%></td>
		<td <%= row_color %>> <%= sample.getId()%></td>
		<td <%= row_color %>> <%= sample.getIsolateTrackingEngine().getStatusAsString()%></td>
		<td <%= row_color %>> 
	<% if (sample.getRefSequenceId() == -1)
	{%>
	&nbsp;
	<%}else{%>

		<!--<a href="/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getRefSequenceId()%>" > 
		<%= sample.getRefSequenceId()%></a> -->
		<a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getRefSequenceId()%>');" > 
		<%= sample.getRefSequenceId()%></a>
		
	<%}%>	
		</td>
	    
		<td <%= row_color %>> 
		<% if ( sample.getCloneSequenceId() != -1)
		{%>
		<a href="/BEC/Seq_GetItem.do?forwardName=<%=Constants.CLONE_SEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getCloneSequenceId()%>" > <%= sample.getRefSequenceId()%></a>
		<%}else{%>&nbsp;
		<%}%></td>
		
	</tr>
	<%}%>
  
     
    
    </table>
</body>
</html>