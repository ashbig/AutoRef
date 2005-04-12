<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>

<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<%@ page import=" edu.harvard.med.hip.bec.coreobjects.spec.*"%>
<html>

<head>

<title><%= request.getAttribute(Constants.JSP_TITLE )%>
<%
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {                forwardName = request.getAttribute("forwardName") ;        }
        else        {                forwardName = request.getParameter("forwardName") ;        }
	
int forwardName_int =  ((Integer) forwardName).intValue();

%>
</title>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
<p><P><br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr> <td >  <font color="#008000" size="5"><b><%= request.getAttribute(Constants.JSP_TITLE) %></font><hr>   <p>  </td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>	  </table>
  </center></div>

<form action="Seq_GetSpec.do" > 
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 
    
<table border="0" cellpadding="1" cellspacing="1" width="74%" align=center>
<tr> <td colspan=2> <div align="right">   <i><b>Note:</b> you can view only one specification at a time</i></div></td><tr>

<% ArrayList specs = null; Spec spec = null;
String[] row_color = {"#b8c6ed","#e4e9f8"};int row_counter=0;



specs = (ArrayList)request.getAttribute("comparespec"); 
   if ( specs != null && specs.size() > 0 )      {%>

<tr> <td bgColor='<%= row_color[ row_counter++ % 2 ]%>' height='39' >End Reads Evaluation Specifications:</td>
 <td bgColor='<%= row_color[row_counter % 2]%>'><select name="er_specid"> 
<option value="0"> Select specification 
<%
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
 
 <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 

 <%}%></select> </td><tr> <%}%>
 
<% specs = (ArrayList)request.getAttribute("bioevaluation");
 if ( specs != null && specs.size() > 0 )      {%>
 
<tr> <td  bgColor='<%= row_color[row_counter++ % 2]%>'  height='39'>Biological Evaluation of Clones Specifications:</td>
<td  bgColor='<%= row_color[row_counter % 2]%>' ><select name="be_specid"> 
<option value="0"> Select specification 
<% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select></td><tr>  <%}%>


<% specs = (ArrayList)request.getAttribute("primer3");
 if ( specs != null && specs.size() > 0 )      {%>
 
<tr> <td bgColor='<%= row_color[row_counter++ % 2]%>'  height='39'>Primer Designer Specifications:</td>
<td bgColor='<%= row_color[row_counter % 2]%>'><select name="primer_specid"> 
<option value="0"> Select specification 
<% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr><%}%>

<% specs = (ArrayList)request.getAttribute("polymerphism");
 if ( specs != null && specs.size() > 0 )      {%>
 
<tr> <td  bgColor='<%= row_color[row_counter++ % 2]%>'  height='39'>Polymorphism Finder Specifications:</td>
 <td  bgColor='<%= row_color[row_counter % 2]%>' ><select name="polym_specid"> 
<option value="0"> Select specification 

<% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec)specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr><%}%>

    
    <% specs = (ArrayList)request.getAttribute("slidingwindow");
  
 if ( specs != null && specs.size() > 0 )      {%>

<tr> <td bgColor='<%= row_color[row_counter++ % 2]%>'  height='39'>Sequence Trimming Parameters Specifications:</td>
 <td bgColor='<%= row_color[row_counter % 2]%>'><select name="sl_specid"> 
<option value="0"> Select specification 
<% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr><%}%>

<tr> <td colspan=2> <div align="center">  
<% String submit_button_title = "Submit";
if ( forwardName_int == Spec.SPEC_DELETE_SPEC)
    {submit_button_title = "Delete";}%>
        
<input type="submit" name="Submit" value="<%= submit_button_title %>"></div></td><tr>
</table></form>


</body>
</html>