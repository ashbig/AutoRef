<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>


<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> select Plates and Configuration for the Process  </font>
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

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Process Name:</strong></td>
    <td width="81%"> 
      <%= request.getAttribute("process_name") %>
    </td>
  </tr>
  <tr> 
    <td colspan =2><strong>Configuration:</strong></td>
    
  </tr>
<% ArrayList specs = request.getAttribute(Constants.SPEC_COLLECTION);
       ArrayList names =   request.getAttribute(Constants.SPEC_TITLE_COLLECTION);
ArrayLis control_names  =   request.getAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION);
String control_name = null; String spec_name = null; ArrayList specs = null; 
Spec spec = null;
for (int count = 0; count <specs.size(); count ++)
{
    control_name = (String) control_names.get(count);
    spec_name= (String) names.get(count);
    specs = (ArrayList)specs.get(count);
%>
   <tr> 
    <td><strong><%= spec_name %></strong></td>
    <td>  
        <SELECT NAME="<%= control_name %>" id="<%= control_name %>">
        <% 
        	
        	for (int count_spec = 0; count_spec < specs.size(); count_spec++)
        	
        	{
        		spec = (Spec)specs.get(count_spec);
                           	%>
        		<OPTION VALUE=<%= spec.getId() %>><%= spec.getName() %>
        	<%
        	}%>
    	</SELECT>
   </td>
  </tr>
  <%}}%>
  
<br>
<% ArrayList plate_labels = request.getAttribute(Constants.PLATE_NAMES_COLLECTION);
STring label = null;
for (int plate_count =0; plate_count < plate_labels.size(); plate_count++)
{
label = (String)plate_labels.get(plate_count); %>
 <input type="checkbox" name="chkLabel" value='<%= label%>'> <%= label %>
<%}%>

<html:submit property="submit" value="Run"/>
</body>
</html>