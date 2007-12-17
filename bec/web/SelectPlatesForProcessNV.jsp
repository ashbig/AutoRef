<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
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
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	
  </table>
  </center>
</div>
<p></p>

<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 
<input name="3p_primerid" type="hidden" value="<%= request.getAttribute("3p_primerid") %>" > 
<input name="5p_primerid" type="hidden" value="<%= request.getAttribute("5p_primerid") %>" > 

      
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  
  <tr> 
    <td ><strong>Configuration:</strong></td>
    
  </tr>
<tr>
<td >

    
    <table border=0 >

   <tr>    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5' sequencing primer: </strong></td>
    <td>  <%= request.getAttribute(UI_Constants.SEQUENCING_PRIMER_5P_ID_KEY) %>   </td>  </tr>
   <tr>    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3' sequencing primer: </strong></td>
    <td>  <%= request.getAttribute(UI_Constants.SEQUENCING_PRIMER_3P_ID_KEY) %>    </td>  </tr>
  
  </table></td></tr>
<tr><td ><hr><hr></td></tr>
<tr><td >
<P><P>
<% 


ArrayList plate_labels = (ArrayList)request.getAttribute(Constants.PLATE_NAMES_COLLECTION);
String label = null;
if ( plate_labels.size()> 0)
{%>
<table border='0' align='center'>
<%
boolean isStart = false; int plates_in_row = 1;
char project_letter = ((String)plate_labels.get(0)).toUpperCase().charAt(0);
for (int plate_count =0; plate_count < plate_labels.size(); plate_count++)
{
    label = (String)plate_labels.get(plate_count); 
    if ( plates_in_row % 5 == 0 || project_letter != label.toUpperCase().charAt(0) )
    {
            %> </tr><tr> <%
            plates_in_row = 0;
            
    }
    project_letter = label.toUpperCase().charAt(0);
    plates_in_row+=1;
    %>
 
    
<td> <input type="checkbox" name="chkLabel" value='<%= label%>'> <%= label %></td>

<%}%>
</tr></table>
<%}%>
</td></tr>
<tr><td align=center>
<html:submit property="submit" value="Submit"/>
</html:form> 
</td></tr></table>

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