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

<%-- The container that was searched --%>

<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<%
//System.out.println("container ");
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {
                forwardName = request.getAttribute("forwardName") ;
        }
        else
        {
                forwardName = request.getParameter("forwardName") ;
        }
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();
OligoContainer container = (OligoContainer)request.getAttribute("container") ;
//System.out.println("container "+forwardName +" samples "+ container.getSamples().size() );

%>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> <%= title%>  </font>
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

<% if (forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%><form action="RunProcess.do" >
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<input name="containerid" type="hidden" value="<%= container.getId()  %>" >
<%}%>


<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td ><strong>Plate Label:</strong></td>
    <td > 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Plate Id:</strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Order Date:</strong></td>
    <td> 
      <%= container.getDate() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Plate Status:</strong></td>
    <td><%= container.getStatusAsString() %></td>
  </tr> 
<% if ( forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%>
<tr> 
    <td><strong>Set Container Status:</strong></td>
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
    <td><strong>Order Comments</strong></td>
    <td> <% if ( forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%> <textarea name="order_comments" rows='6' cols='50' >
<% if ( container.getCommentOrder()!= null){%> <%= container.getCommentOrder() %>  <%}%> </TEXTAREA>
<%}else{ if ( container.getCommentOrder()!= null){%>  
     <%= container.getCommentOrder() %> <%}else{%> &nbsp; <%}}%>       
    </td>
  </tr>
 <tr> 
    <td><strong>Sequencing Comments</strong></td>
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
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Id</font></strong></th>
 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
 <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Id</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Name</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Sequence</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Position on Sequence</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Oligo Tm</font></strong></th>
       </tr>
<%  
    String row_color = " bgColor='#e4e9f8'";
    OligoSample sample=null; Oligo o = null;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (OligoSample)container.getSamples().get(count);
                o = sample.getOligo();
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
<td <%= row_color %>> <%= sample.getId() %></font></strong></th>
<td <%= row_color %>> <%= sample.getPosition() %></font></strong></th>
 <td <%= row_color %>>  <%= sample.getCloneId() %></font></strong></th>
<td <%= row_color %>> <%= o.getName() %></font></strong></th>
<td <%= row_color %>> <%= o.getSequence() %></font></strong></th>
<td <%= row_color %>> <%= o.getPosition() %></font></strong></th>
<td <%= row_color %>> <%= o.getTm() %></font></strong></th>
	</tr>
	<%}%>
    </table>
<%}%>

<% if (forwardName_int == Constants.PROCESS_PROCESS_OLIGO_PLATE)
{%><div align="center">  <p> <input type="submit" value="Submit" > </div> </form ><%}%>
</body>
</html>