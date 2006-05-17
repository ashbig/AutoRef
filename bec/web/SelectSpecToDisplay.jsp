<!--Copyright 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

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

<title><%= request.getAttribute(Constants.JSP_TITLE )%></title>
<%
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {                forwardName = request.getAttribute("forwardName") ;        }
        else        {                forwardName = request.getParameter("forwardName") ;        }
	
int forwardName_int =  ((Integer) forwardName).intValue();

%>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- TemplateParam name="OptionalRegion1" type="boolean" value="true" -->
</head>

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
	     <center>  <table border="0" cellpadding="0" cellspacing="0" width="90%">
	       <tr>      <td width="100%"><html:errors/></td>    </tr>	  </table>
	     </center></div>
	   
	   <form action="Seq_GetSpec.do" > 
	   <input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 
	       
	   <table border="0" cellpadding="1" cellspacing="1" width="90%" align=center>
	   <tr> <td colspan=2> <div align="right">   <i><b>Note:</b> you can view only one specification at a time</i></div></td><tr>
	   
	   <% ArrayList specs = null; Spec spec = null;
	   String[] row_color = {"#b8c6ed","#e4e9f8"};int row_counter=0;
	   
	   
	   
	   specs = (ArrayList)request.getAttribute("comparespec"); 
	      if ( specs != null && specs.size() > 0 )      {%>
	   
	   <tr> <td bgColor='<%= row_color[ row_counter % 2 ]%>' height='39' >Clone Ranking Specifications:</td>
	    <td bgColor='<%= row_color[row_counter++ % 2]%>'><select name="er_specid"> 
	   <option value="0"> Select specification 
	   <%
	   for (int spec_count = 0; spec_count < specs.size(); spec_count++)
	       { spec = (Spec) specs.get(spec_count);%>
	    
	    <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
	   
	    <%}%></select> </td><tr> <%}%>
	    
	   <% specs = (ArrayList)request.getAttribute("bioevaluation");
	    if ( specs != null && specs.size() > 0 )      {%>
	    
	   <tr> <td  bgColor='<%= row_color[row_counter % 2]%>'  height='39'>Clone Acceptance Criteria Specifications:</td>
	   <td  bgColor='<%= row_color[row_counter++ % 2]%>' ><select name="be_specid"> 
	   <option value="0"> Select specification 
	   <% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
	       { spec = (Spec) specs.get(spec_count);%>
	     <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
	       <%}%></select></td><tr>  <%}%>
	   
	   
	   <% specs = (ArrayList)request.getAttribute("primer3");
	    if ( specs != null && specs.size() > 0 )      {%>
	    
	   <tr> <td bgColor='<%= row_color[row_counter % 2]%>'  height='39'>Primer Designer Specifications:</td>
	   <td bgColor='<%= row_color[row_counter++ % 2]%>'><select name="primer_specid"> 
	   <option value="0"> Select specification 
	   <% for (int spec_count = 0; spec_count < specs.size(); spec_count++)
	       { spec = (Spec) specs.get(spec_count);%>
	     <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
	       <%}%></select> </td><tr><%}%>
	   
	   <% specs = (ArrayList)request.getAttribute("polymerphism");
	    if ( specs != null && specs.size() > 0 )      {%>
	    
	   <tr> <td  bgColor='<%= row_color[row_counter % 2]%>'  height='39'>Polymorphism Finder Specifications:</td>
	    <td  bgColor='<%= row_color[row_counter++ % 2]%>' ><select name="polym_specid"> 
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
	       {submit_button_title = "Delete";}
	   
	   %>
	           
	   <input type="submit" name="Submit" value="<%= submit_button_title %>"></div></td><tr>
</table></form>
           
           
           
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
