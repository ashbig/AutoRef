<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<html>

<head>
<title> <bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
  <script defer="defer" type="text/javascript"><!--
  

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
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}
	ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION);
        ArrayList names = (ArrayList)  request.getAttribute(Constants.SPEC_TITLE_COLLECTION);
        ArrayList control_names  = (ArrayList)  request.getAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION);
	 
	
%>

<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body> <jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> <%= title %> </b></font>
	
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
<html:form action="/RunProcess.do" onsubmit="return validateForm(this);"> 
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 

<table border="0" cellpadding="10" cellspacing="2" width="74%" align=center>

    <tr><td colspan=2>
         <jsp:include page="enter_items.jsp" /> </td>
    </tr>
<%if ((specs != null && names != null && control_names != null)
    && ( specs.size()> 0 && names.size()>0 && control_names.size()>0))
 {%>

	<tr><td colspan =2 bgColor="#1145A6" ><font color="#FFFFFF"><strong>Process Specification</strong></font></td></tr>
	
<%String control_name = null; 
String spec_name = null; 
ArrayList specs_arr = null; 
Spec spec = null;
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
                    spec = (Spec)specs_arr.get(count_spec);	%>
        		<OPTION VALUE="<%= spec.getId() %>"><%= spec.getName() %>
                     	
        	<%}%>
    	</SELECT></td>
	
    </tr>
<%}}%>

</table>
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" >
   
</div>
</html:form> 
</body>
</html>


