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
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- InstanceBeginEditable name="EditRegion1" -->
           <%ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION); %>
           <div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	<tr>
        <td><i>If you are not sure about certain settings, please, consult help</i> </i> <a href="Help_PlateUploader.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>


<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr><td colspan="2" bgcolor="#1145A6" height="29"> 
    <font color="#FFFFFF"><strong>Submit clone collection </strong></font> </td>
<tr><td><INPUT TYPE=RADIO NAME="data_type" VALUE="PLATE" checked>Plate Label:</td>
<td><INPUT TYPE=RADIO NAME="data_type" VALUE="CLONE">Clone List (separated by spaces, max 300 clone id)</td></tr>
</TR>
<TR>
<td valign="top"><INPUT name="plate_name" value="" type="text"></td>
<td> <textarea name="clone_collection" id="clone_collection" rows="10"> </textarea>  </td>
 </tr>
  
  <tr><td colspan=2>&nbsp;</td></tr>
  <tr>
    <td colspan="2" bgcolor="#1145A6" height="29"> 
    <font color="#FFFFFF"><strong>Select Configuration </strong></font> 
    </td>
  </tr>
   <tr>
   <td  bgcolor="#e4e9f8"> <strong>Bio Evaluation of Clones: </strong>    </td>
    <TD bgcolor="#e4e9f8" > 
    <SELECT NAME="<%= Spec.FULL_SEQ_SPEC %>" id="<%= Spec.FULL_SEQ_SPEC %>">
        <% 
        	Spec spec = null;
        	for (int count_spec = 0; count_spec < specs.size(); count_spec++)
        	
        	{
                    spec = (Spec)specs.get(count_spec);	%>
        		<OPTION VALUE="<%= spec.getId() %>"><%= spec.getName() %>
                   <% }%>
    	</SELECT>
       
      </td>
  </tr>
  
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
  
</div>
</html:form> 
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
