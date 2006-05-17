<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->


<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<html>
<head>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

 <title> <bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
   
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

%>
   
    
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
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="84%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>  </center>
</div>


<% 

Hashtable  stretch_collections = (Hashtable) request.getAttribute("lqr_clone_stretch_collections");
ArrayList items = (ArrayList)request.getAttribute("items");
int item_type = Integer.parseInt( (String)request.getAttribute("item_type")); 
String item_title =  "Clone Id: "; 

%>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<%
StretchCollection str_collection = null; 
for (int index = 0; index < items.size(); index ++)
{
   if ( stretch_collections != null)
   {
        str_collection = (StretchCollection)stretch_collections.get((String) items.get(index));
   }

%>
   <tr><td colspan='2'>&nbsp;</td></tr>
   <TR>     <td height='29'  bgColor="#b8c6ed" >
           <strong><font color="#000080"><%= item_title%></font></td>
           <td bgColor="#b8c6ed" ><strong><font color="#000080"><%= (String)items.get(index)%></font></strong></td></TR>
    <% if (  str_collection != null )
{   %>

<tr> <td  ><strong>&nbsp;&nbsp; Reference sequence ID: </strong></td>
<td   ><a href="#" onCLick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= str_collection.getRefSequenceId()%>','<%= str_collection.getRefSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <strong><%= str_collection.getRefSequenceId()%></a></strong></td></TR>
<tr> <td  ><strong>&nbsp;&nbsp; Clone sequence ID: </strong></td>
<td   ><a href="#" onCLick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT%>&amp;ID=<%= str_collection.getCloneSequenceId()%>','<%= str_collection.getCloneSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <%= str_collection.getCloneSequenceId() %></a></strong></td></TR>
<tr> <td  ><strong>&nbsp;&nbsp; Clone sequence analysis status: </strong></td>
<td >&nbsp </td></tr>
<tr> <td  ><strong>&nbsp;&nbsp; Clone sequence aligment: </strong></td>
<td   >
<input type=BUTTON value=Alignment onClick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= str_collection.getCloneSequenceId() %>&amp;TYPE=<%= BaseSequence.CLONE_SEQUENCE%>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= str_collection.getRefSequenceId()%>','<%= str_collection.getCloneSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
  
</TD></tr>

<tr> <td  ><strong>&nbsp;&nbsp; Specification for LQR definition: </strong></td>
<td ><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%= str_collection.getSpecId() * Spec.SPEC_SHOW_SPEC %>" > <%= str_collection.getSpecId() %></a></strong></td></TR>
<tr><td colspan='2'>&nbsp; </td></tr>
<tr><td colspan='2'>
<table width="90%" border="1" align="center" cellpadding="2" cellspacing="2">

<%= str_collection.getHTMLDescription() %>
</TABLE><P>
</td></tr>
<%}%>

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