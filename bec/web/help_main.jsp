<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/my_template.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceParam name="OptionalRegion1" type="boolean" value="true" -->
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
          <td   ="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- InstanceBeginEditable name="EditRegion1" -->
          
          <% String jsp_redirection = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") ;%>
           <table border="0" cellpadding="0" cellspacing="0" width="90%" align='center' >

<tr><td align='center'><h3>ACE Installation (for application administrator only)</h3> </td></td>
<tr><td > <a href = "./help/help_installation.jsp"  target="_blank"> ACE installation instructions</td></tr>
<tr><td > <a href = "./help/help_ACEConfigurationFile.html" target="_blank" > ACE configuration </td></tr>

<tr><td align='center'><hr><h3>ACE user help</h3> </td></td>
<tr><td > <a href = "./help/help_ACE_overview.htm"  target="_blank">ACE overview</td></tr>

<tr><td > <a href = "./help/help_generalhelp.jsp"  target="_blank">ACE tutorial</td></tr>
<!--<tr><td > <a href = "<%= jsp_redirection %>help/help_troubleshooting.html"  target="_blank">ACE troubleshooting</td></tr>-->

<tr><td >&nbsp; </td></tr><tr><td><P></p><b>Module help</b></td></tr>


<tr><td > <a href = "<%= jsp_redirection %>help/help_ReportRunner.html"  target="_blank"> General Report</td></tr>
<tr><td > <a href = "<%= jsp_redirection %>help/help_TraceFilesNamingFormats.html"  target="_blank">How to create trace file name format</td></tr>

<tr><td >&nbsp; </td></tr><tr><td><P></p><b>File format for data submission</b></td></tr>

<tr><td > <a href = "./help/help_reference_sequence_xml_format.html" target="_blank" >Reference sequence submission XML format</td></tr>
<tr><td > <a href = "./help/help_vector_xml_format.html"  target="_blank">Vector submission XML format</td></tr>
<tr><td > <a href = "./help/help_clonecollection_xml_format.html"  target="_blank">Clone collection (plate) submission XML format</td></tr>
<tr><td > <a href = "./help/help_clone_sequence_xml_format.html"  target="_blank">Clone sequence submission FASTA format</td></tr>

</table>

            <!-- InstanceEndEditable --></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>
