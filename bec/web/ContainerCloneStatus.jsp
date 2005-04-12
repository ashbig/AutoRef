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
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> container Check End Reads Availability  </font>
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
//System.out.println(container.getLabel());%>
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
    <td><strong>Cloning Strategy</strong></td>
    <td> 
      <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")  %>Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Forward End Read</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Reverse End Read</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Assembled Sequence</font></strong></th>
		 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Status</font></strong></th>
    </tr>
<%  
    String row_color = " bgColor='#e4e9f8'";
	Sample sample=null;
Result result = null;
String forward_read_status = "";String reverse_read_status = "";
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (Sample)container.getSamples().get(count);
//System.out.println("position"+sample.getPosition());
                 forward_read_status = "&nbsp"; reverse_read_status = "&nbsp";
if (sample.getResults() != null)
{
                for (int result_count = 0; result_count < sample.getResults().size(); result_count++)
                {
                        result = (Result)sample.getResults().get(result_count);
//System.out.println(result.getType());
                        switch (result.getType())
                        {
                            case Result.RESULT_TYPE_ENDREAD_FORWARD:{ forward_read_status = "NOT PROCESSED"; break;}

                            case Result.RESULT_TYPE_ENDREAD_FORWARD_PASS:{ forward_read_status="Pass"; break;}
                            case Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL:{ forward_read_status="Fail";break;}

                            case Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL :{ reverse_read_status = "Fail"; break;}
                            case Result.RESULT_TYPE_ENDREAD_REVERSE_PASS: { reverse_read_status="Pass"; break;}
                            case Result.RESULT_TYPE_ENDREAD_REVERSE  :{ reverse_read_status="NOT PROCESSED";break;}
                        }
                        
                }
}
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
		<td <%= row_color %>> <%= forward_read_status%> </td>
                <td <%= row_color %>> <%= reverse_read_status%></td>
                <td <%= row_color %>>
                    <%if(sample.getIsolateTrackingEngine() != null && sample.getIsolateTrackingEngine().getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_PASS) 
                    {%>Clone Sequence Assembled  <%}
                    else if(sample.getIsolateTrackingEngine() != null &&  sample.getIsolateTrackingEngine().getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_CONFIRMED) 
                    {%> Clone Sequence Confirmed <%}
                    else
                    {%> &nbsp; <%}%>
</td>
		<td <%= row_color %>>
<% if (sample.getIsolateTrackingEngine() != null )
{%><%= sample.getIsolateTrackingEngine().getStatusAsString()%><%}
else{%>&nbsp;<%}%>
</td>
		
	
	</tr>
	<%}%>
  
     
    
    </table>
</body>
</html>