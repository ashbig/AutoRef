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
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> select Plates and Configuration for the Process:  '<%= request.getAttribute("process_name") %>'  </font>
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

<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  
  <tr> 
    <td ><strong>Configuration:</strong></td>
    
  </tr>
<tr>
<td > <table border=0 >
<% 


ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION);
       ArrayList names = (ArrayList)  request.getAttribute(Constants.SPEC_TITLE_COLLECTION);
ArrayList control_names  = (ArrayList)  request.getAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION);
String control_name = null; String spec_name = null; ArrayList specs_arr = null; 
Spec spec = null;
Oligo oligo = null;
for (int count = 0; count <specs.size(); count ++)
{
    control_name = (String) control_names.get(count);
    spec_name= (String) names.get(count);
    specs_arr = (ArrayList)specs.get(count);

%>
   <tr> 
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong><%= spec_name %></strong></td>
    <td> 
        <SELECT NAME="<%= control_name %>" id="<%= control_name %>">
        <% 
        	
        	for (int count_spec = 0; count_spec < specs_arr.size(); count_spec++)
        	
        	{
                    if (specs_arr.get(count_spec) instanceof Spec)
                    {   
        		spec = (Spec)specs_arr.get(count_spec);	%>
        		<OPTION VALUE="<%= spec.getId() %>"><%= spec.getName() %>
                   <% }
                    else if (specs_arr.get(count_spec) instanceof  Oligo)
                    {   
                         oligo = (Oligo)specs_arr.get(count_spec);	%>
        		<OPTION VALUE="<%= oligo.getId() %>"><%= oligo.getName() %>
                   <% }    	
        	}%>
    	</SELECT>
   </td>
  </tr>
  <%}%>
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
for (int plate_count =0; plate_count < plate_labels.size(); plate_count++)
{
    label = (String)plate_labels.get(plate_count); 
    if (plate_count != 0 &&  plate_count % 4 == 0){%>   </tr> <%}
    if ( plate_count % 4 == 0){%>   <tr> <%}%>
<td> <input type="checkbox" name="chkLabel" value='<%= label%>'> <%= label %></td>

<%}%>
</tr></table>
<%}%>
</td></tr>
<tr><td align=center>
<html:submit property="submit" value="Run"/>
</html:form> 
</td></tr></table>
</body>
</html>