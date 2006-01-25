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
    project_code = DatabaseToApplicationDataLoader.getProjectCodeForLabel((String)labels.get(0));
        
     for (int index = 0; index < labels.size(); index ++)
    {
        cur_label = (String)labels.get(index);
        if (index == 0) %><tr>
        <%if ( cur_column == 6){cur_column =1;%></tr><tr><%}%>
        <% if ( prev_label == null || ( prev_label != null &&  !cur_label.startsWith(project_code)))
        {
            project_code = DatabaseToApplicationDataLoader.getProjectCodeForLabel(cur_label);
   
            { if ( index > 0 ){%>        
            </table></DIV></tr><tr><%}}%>
     <tr><td>&nbsp</td></tr>        <tr><td><INPUT onclick='javascript:showhide(<%= "\"" + cur_label.charAt(0) + "\"" %>, this.checked);' type=checkbox CHECKED value=1 name=show> <%= DatabaseToApplicationDataLoader.getProjectName(project_code)%>
             <DIV ID='<%= cur_label.charAt(0) %>' STYLE='position:relative;  clip:rect(0px 120px 120px 0px);'>
              <p><table border = 0>
                <% cur_column=1;   }%>
                
                
              <td><b> <%= cur_label %> </b></td>
              <%  cur_column ++;prev_label = cur_label;}%>
              </table></table> </table> <%}}%>
</table>
          
  


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
