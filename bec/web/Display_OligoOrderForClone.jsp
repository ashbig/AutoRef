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
<table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html" %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp" %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
		  <jsp:include page="page_left_menu.jsp" /></td>
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- TemplateBeginEditable name="EditRegion1" -->
<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr> </table>  </center></div>
<p></p>


<table border="1" cellpadding="1" cellspacing="1" width="90%" align=center>
<tr class='headerRow'>
<td>Clone ID</td>
<td>Plate</td>
<td>Well</td>
<td>Plate Status</td>
<td>Plate Order Date</td>
<td>User ID</td>
<td>Oligo ID</td>
<td>Oligo Name</td>
<td>Oligo Sequence</td>
<td>Oligo Position</td>

<%
 ArrayList ui_items = (ArrayList)request.getAttribute("processing_items");
    UI_GeneOligo  ui_oligo = null;
    String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
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

<!-- TemplateEndEditable --></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
</html>