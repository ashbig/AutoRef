<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.Constants" %>

<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>

<html>

<body>

<%
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {
                forwardName = request.getAttribute("forwardName") ;
        }
        else
        {
                forwardName = request.getParameter("forwardName") ;
        }
	
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();
OligoContainer container = (OligoContainer)request.getAttribute("container") ;
String[] row_class = {"evenRow","oddRow"} ; int row_count = 0;

%>

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
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	
  </table>
  </center>
</div>

<% if (forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%><form action="RunProcess.do" >
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<input name="containerid" type="hidden" value="<%= container.getId()  %>" >
<%}%>


<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
  <tr> 
    <td ><strong>Plate label:</strong></td>
    <td > 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Plate ID:</strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Order date:</strong></td>
    <td> 
      <%= container.getCreateDate() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Plate status:</strong></td>
    <td><%= container.getStatusAsString() %></td>
  </tr> 
<% if ( forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%>
<tr> 
    <td><strong>Set plate status:</strong></td>
   <td>
     <select name="status">
      <!--  <option  value="<%= OligoContainer.STATUS_ORDER_SENT%>" >Ordered </option> -->
        <option value="<%=  OligoContainer.STATUS_RECIEVED%>">Recieved</option>
        <option value="<%=  OligoContainer.STATUS_SENT_FOR_SEQUENCING%>">Used for sequencing</option>
      </select>
   </td>
</tr>
<%}%>
 
  <tr> 
    <td><strong>Order comments:</strong></td>
    <td> <% if ( forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%> <textarea name="order_comments" rows='6' cols='50' >
<% if ( container.getCommentOrder()!= null){%> <%= container.getCommentOrder() %>  <%}%> </TEXTAREA>
<%}else{ if ( container.getCommentOrder()!= null){%>  
     <%= container.getCommentOrder() %> <%}else{%> &nbsp; <%}}%>       
    </td>
  </tr>
 <tr> 
    <td><strong>Sequencing comments:</strong></td>
    <td> 
<% if ( forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE )
{%> <textarea name="sequencing_comments" rows='6' cols='50' <% if ( container.getStatus() !=  OligoContainer.STATUS_RECIEVED){%>disabled<%}%>>
<% if ( container.getCommentSequencing()!= null){%>  <%= container.getCommentSequencing() %> <%}%>

</TEXTAREA>
<%}else{ if ( container.getCommentSequencing()!= null){%>  
     <%= container.getCommentSequencing() %> <%}else{%> &nbsp; <%}}%>    
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<%if (container.getSamples() != null && container.getSamples().size() > 0)
{%>
<table border="1" cellpadding="0" cellspacing="0" width="90%" align=center>
    <tr class='headerRow'>
    <!--   <td>Sample Id</td> -->
 <td><div align='centre'>Well</div></td>
 <td><div align='centre'>Clone ID</div></td>
<td><div align='centre'>Oligo Name</div></td>
<td><div align='centre'>Oligo Sequence</div></td>
<td><div align='centre'>Oligo Position on Sequence</div></td>
<td><div align='centre'>Oligo Tm</div></td>
       </tr>
<%  
    OligoSample sample=null; Oligo o = null;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (OligoSample)container.getSamples().get(count);
                o = sample.getOligo();
		
	%>
<tr class=<%= row_class[row_count++ % 2] %>>
<!--<td > < %=  sample.getId() %></td>-->
<td ><div align='right'> <%= sample.getPosition() %></div></td>
 <td ><div align='right'>  <%= sample.getCloneId() %></div></td>
<td >&nbsp; <%= o.getName() %></td>
<td >&nbsp; <%= o.getSequence() %></td>
<td > <div align='right'><%= o.getPosition() %></div></td>
<td ><div align='right'> <%= o.getTm() %></div></td>
</tr>
	<%}%>
    </table>
<%}%>

<% if (forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%><div align="center">  <p> <input type="submit" value="Submit" > </div> </form ><%}%>

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