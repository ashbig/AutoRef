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
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

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
<% Container container = (Container)request.getAttribute("container") ;
;%>
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
      <a href="/BEC/Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %> 
	  &amp; + ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
    </td>
  </tr>
  
</table>
<P><P></P></P>
<%  ArrayList process_items = (ArrayList) request.getAttribute("process_items");
  if (process_items == null)
  {
  %> <div align="center"></div><B>No processing history is available for the container.</b></div>
  <%}else{%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Protocol</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Specification</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Execution Date</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Researcher</font></strong></th>
       
    </tr>
<%
    String row_color =" bgColor='#e4e9f8'";
	ProcessHistory pr_history=null;
    for (int count = 0; count < process_items.size(); count ++)
	{
		pr_history = (ProcessHistory)process_items.get(count);
		if (count % 2 == 0)
		{
		  row_color =" bgColor='#e4e9f8'";
		}
		else
		{
			row_color =" bgColor='#b8c6ed'";
		}
	%>
	<tr>

		<td <%= row_color %>> <%= pr_history.getName() %> </td>
		<td <%= row_color %> >
		<% 
		for (int s = 0; s < pr_history.getConfigs().size();s++)
		{
			ConfigItem cs=(ConfigItem)pr_history.getConfigs().get(s);
		
		if ( cs.getType() == Spec.PRIMER3_SPEC_INT ||  cs.getType() == Spec.END_READS_SPEC_INT 
		||  cs.getType() == Spec.FULL_SEQ_SPEC_INT ||  cs.getType() == Spec.POLYMORPHISM_SPEC_INT )
		{%>
			<a href="/BEC/Seq_GetSpec.do?forwardName=<%= cs.getId()  * Spec.SPEC_SHOW_SPEC %> "> 
			 <%=cs.getId() %> </a>
		<%}
		else
		{%>
		&nbsp;
		<%}}%>
		</td>
		<td <%= row_color %>>	<%= pr_history.getDate () %></td>
		<td <%= row_color %>> <%= pr_history.getUsername () %></td>
	</tr>
	<%}%>
  
     
    
    </table>
	<%}%>
</body>
</html>