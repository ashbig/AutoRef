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

<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>

<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<%
String[] row_class = {"evenRow","oddRow"} ; int row_count = 0;
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}

%>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>        <td >    <font color="#008000" size="5"><b> <%= title%>  </font>
    <hr>   <p>  </td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr> </table>  </center></div>
<p></p>


<table border="1" cellpadding="1" cellspacing="1" width="84%" align=center>
<tr class='headerRow'>
<td>Clone Id</td>
<td>Plate</td>
<td>Position</td>
<td>Plate Status</td>
<td>Plate Order Date</td>
<td>User Id</td>
<td>Oligo Id</td>
<td>Oligo Name</td>
<td>Oligo Sequence</td>
<td>Oligo Position</td>

<%
 ArrayList ui_items = (ArrayList)request.getAttribute("processing_items");
    UI_GeneOligo  ui_oligo = null;
    String previous_plate_name = null;
    int prev_clone_id = 0;String clone_id = "";
    
for (int count = 0; count < ui_items.size(); count++)
    {
ui_oligo = (UI_GeneOligo)ui_items.get(count);
if ( count > 0 && !previous_plate_name.equalsIgnoreCase(ui_oligo.getPlateLabel ()))
    {
        row_count++;
        
     }
previous_plate_name = ui_oligo.getPlateLabel ();
%>
<Tr class=<%= row_class[row_count % 2] %>>
<td > 
<% 
if ( prev_clone_id != ui_oligo.getCloneId ()) 
{
     %> <%= ui_oligo.getCloneId () %>
 <% }else {%> &nbsp <%}

prev_clone_id = ui_oligo.getCloneId (); %>

 </td>

<td ><%= ui_oligo.getPlateLabel () %></td>
<td ><%= ui_oligo.getWell () %></td>
<td ><%= OligoContainer.getStatusAsString( ui_oligo.getPlateStatus ()) %></td>
<td ><%= ui_oligo.getOrderDate ().substring(0,10) %></td>
<td ><%= ui_oligo.getUserId () %></td>
<td ><%= ui_oligo.getOligoDesignType () %><%= ui_oligo.getOligoID () %></td>
<td ><%= ui_oligo.getOligoName () %></td>

<td ><%= ui_oligo.getOligoSequence () %></td>
<td ><%= ui_oligo.   getOligoPosition() %></td>
    
    <%}%>
</table>
</body>
</html>