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
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>

<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" flush="true"/>
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> container End Reads Results</font> 
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


%>
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
      <a href="/BEC/Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Read Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Trace File</font></strong></th>
		 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Read Length</font></strong></th>
		 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Score</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Read Sequence</font></strong></th>
		 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Discrepancy Report</font></strong></th>
		  <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Show Alignment</font></strong></th>
    </tr>
<%  
    String row_colorB = " bgColor='#e4e9f8'"; String row_colorA =" bgColor='#b8c6ed'";
	String row_color = " bgColor='#e4e9f8'";
	Sample sample=null;
	Read read  = null;Result result =null;
	int constructid = -1;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
	// System.out.println("get "+System.currentTimeMillis());
		sample = (Sample)container.getSamples().get(count);
                read = null; result = null;
	//System.out.println(sample.getPosition());
		if ( sample.getResults() != null && sample.getResults().size() > 0)
		{
			result = (Result)sample.getResults().get(0);
			
			if (result != null)
			{
				//System.out.println("result id "+ result.getId() +" "+result.getType() );
				read = (Read) result.getValueObject();
				if (read != null)
				{
					System.out.println("read id "+ read.getId());
					}
			}
			
		}
	
		 if ( sample.getType().equals("CONTROL_POSITIVE") ||  sample.getType().equals("CONTROL_NEGATIVE"))
		{
		  row_color = " bgColor=blue";
		}
		else if (constructid != sample.getConstructId())
		{
			if (row_color.equals(row_colorA))
			{
		   		row_color = row_colorB;
				}
				else
				{
				row_color = row_colorA;
				}
		}
		
		constructid = sample.getConstructId();
	%>
	<tr>
		<td <%= row_color %>><%= sample.getPosition() %> </td>
		<td <%= row_color %>> <%= sample.getType()%></td>
		<td <%= row_color %>><% if ( result != null ) {%> <%= result.getTypeAsString()%> <%}else{%> &nbsp;<%}%></td>
		<td <%= row_color %>><% if (read != null && read.getTraceFileName() != null){%>	<%= read.getTraceFileName()%> <%}else{%>&nbsp; <%}%></td>
		<td align="right" <%= row_color %>> <%if (read != null ){%> <%= (read.getTrimEnd() - read.getTrimStart() )%><%}else{%> &nbsp;<%}%>  </td>
		<td  align="right" <%= row_color %>> <%if (read != null ){%> <%= read.getScore() %><%}else{%> &nbsp;<%}%>  </td>
		
		<td <%= row_color %>><%if (read != null ){%>
		 <A HREF="" onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID=<%= read.getSequenceId()%>&amp;trimstart=<%=read.getTrimStart()%>&amp;trimend=<%=read.getTrimEnd()%>','<%= read.getSequenceId() %>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%= read.getSequenceId() %>
		 </a>
		 
		 <%}else{%> &nbsp;<%}%>  </td>
		<td <%= row_color %> align="center"><%if (read != null ){%>
 <input type=BUTTON value=Report onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= read.getSequenceId()%>','<%= read.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%}else{%> &nbsp;<%}%>    </td>
		<td <%= row_color %>><%if (read != null ){%>
 <input type=BUTTON value=Alignment onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= read.getSequenceId()%>','alg<%= read.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%}else{%> &nbsp;<%}%>     </td>
	</tr>
	<%}%>
    </table>
</body>
</html>