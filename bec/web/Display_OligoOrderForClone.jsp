<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.Constants" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>

<%-- The container that was searched --%>

<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<%
String color[] = { "#e4e9f8","#b8c6ed" };
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}
//System.out.println("container "+forwardName +" samples "+ container.getSamples().size() );

%>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>        <td >    <font color="#008000" size="5"><b> <%= title%>  </font>
    <hr>   <p>  </td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr> </table>  </center></div>
<p></p>


<table border="1" cellpadding="1" cellspacing="1" width="84%" align=center>
<th bgcolor="#1145A6"><strong><font color=white>Clone Id</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Plate</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Plate Status</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Plate Order Date</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">User Id</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Id</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Name</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Sequence</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Position</font></strong></th>

<%
 ArrayList ui_items = (ArrayList)request.getAttribute("processing_items");
    UI_GeneOligo  ui_oligo = null;String row_color = color[0];
    String previous_plate_name = null;
    int prev_clone_id = 0;String clone_id = "";
    int color_count = 0;
for (int count = 0; count < ui_items.size(); count++)
    {
ui_oligo = (UI_GeneOligo)ui_items.get(count);
if ( count > 0 && !previous_plate_name.equalsIgnoreCase(ui_oligo.getPlateLabel ()))
    {
        color_count++; 
        row_color = color[color_count % 2];
        
     }
previous_plate_name = ui_oligo.getPlateLabel ();
%>
    <Tr>

<td bgcolor='<%= row_color %>'> 
<% 
if ( prev_clone_id != ui_oligo.getCloneId ()) 
{
     %> <%= ui_oligo.getCloneId () %>
 <% }else {%> &nbsp <%}

prev_clone_id = ui_oligo.getCloneId (); %>

 </td>

<td bgcolor='<%= row_color %>'><%= ui_oligo.getPlateLabel () %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.getWell () %></td>
<td bgcolor='<%= row_color %>'><%= OligoContainer.getStatusAsString( ui_oligo.getPlateStatus ()) %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.getOrderDate ().substring(0,10) %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.getUserId () %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.getOligoID () %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.getOligoName () %></td>

<td bgcolor='<%= row_color %>'><%= ui_oligo.getOligoSequence () %></td>
<td bgcolor='<%= row_color %>'><%= ui_oligo.   getOligoPosition() %></td>
    
    <%}%>
</table>
</body>
</html>