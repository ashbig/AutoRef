<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>

<%@ page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/my_template.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceParam name="OptionalRegion1" type="boolean" value="true" -->
</head>
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
        if (forwardName!= null && forwardName instanceof String  ) forwardName_int = Integer.parseInt((String)forwardName);
        else if (forwardName!= null && forwardName instanceof Integer ) forwardName_int = ((Integer) forwardName).intValue(); 
%>
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
          <td><!-- InstanceBeginEditable name="EditRegion1" -->
          

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>
  
<table width ='90%' border=0 align='center'>
<tr ><td > <%=request.getAttribute( Constants.ADDITIONAL_JSP) %></td></tr></table>


	   
	  
<%if (    forwardName_int==  Constants.AVAILABLE_CONTAINERS_INT)
{
     int cur_column=1;String cur_label = null;String prev_label = null;
     String project_code = null;
     ArrayList labels = (ArrayList) request.getAttribute("labels");
    if ( labels == null || labels.size() == 0)
     {%>
      <table border="0" cellpadding="0" cellspacing="0" width="90%" >
     <tr><td> <h3> No containers are available.</h3></td></tr><table>
<%}
else
{%>
<table border="0" cellpadding="0" cellspacing="0" width="80%" >
<%
    ArrayList project_labels = null;   
    for (int index = 0; index < labels.size(); index ++)
    {
        project_labels = (ArrayList)labels.get(index);
        for (int item_count = 0; item_count < project_labels.size(); item_count++)
        {
             if ( item_count == 0 ){%>
<tr><td><INPUT onclick='javascript:showhide(<%= "\"" + index + "\"" %>, this.checked);' type=checkbox CHECKED value=1 name=show> <%= project_labels.get(0) %>
<DIV ID='<%= index %>' STYLE='position:relative;  clip:rect(0px 120px 120px 0px);'>
<p><table border = 0>
   
                 
              <%continue;
                } if ( item_count % 5 == 0 )
              {%>  </tr><tr>  <%}%>
              <td><b> <%= (String)project_labels.get(item_count) %> </b></td>
              <% if ( item_count == project_labels.size() -1 )
              {%> </table></DIV></tr><tr>
                    <tr><td>&nbsp</td></tr>    
              <%}
          }}%>
        
       
       
</table>
          
  
<%}}%>

<!-- InstanceEndEditable --></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>
