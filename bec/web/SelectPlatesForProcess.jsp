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
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>

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
<% 


ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION);
       ArrayList names = (ArrayList)  request.getAttribute(Constants.SPEC_TITLE_COLLECTION);
ArrayList control_names  = (ArrayList)  request.getAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION);
String control_name = null; String spec_name = null; ArrayList specs_arr = null; 
Spec spec = null;

for (int count = 0; count <specs.size(); count ++)
{
    control_name = (String) control_names.get(count);
    spec_name= (String) names.get(count);
    specs_arr = (ArrayList)specs.get(count);
System.out.println("-----"); 
%>
   <tr> 
    <td><strong><%= spec_name %></strong></td>
    <td>  
        <SELECT NAME="<%= control_name %>" id="<%= control_name %>">
        <% 
        	
        	for (int count_spec = 0; count_spec < specs_arr.size(); count_spec++)
        	
        	{
        		spec = (Spec)specs_arr.get(count_spec);
                         System.out.println("A"+spec.getName());  	%>
        		<OPTION VALUE="<%= spec.getId() %>"><%= spec.getName() %>
        	<%
        	}%>
    	</SELECT>
   </td>
  </tr>
  <%}%>
  
<br>
<P><P>
<% 


ArrayList plate_labels = (ArrayList)request.getAttribute(Constants.PLATE_NAMES_COLLECTION);
String label = null;
if ( plate_labels.size()> 0)
{%>
<table border='0' align='center'>
<%
for (int plate_count =0; plate_count < plate_labels.size(); plate_count++)
{
    label = (String)plate_labels.get(plate_count); 
    if (plate_count != 0 &&  plate_count % 3 == 0){%>   </tr> <%}
    if ( plate_count % 3 == 0){%>   <tr> <%}%>
<td> <input type="checkbox" name="chkLabel" value='<%= label%>'> <%= label %></td>

<%}%>
</tr></table>
<%}%>
<html:submit property="submit" value="Run"/>
</body>
</html>