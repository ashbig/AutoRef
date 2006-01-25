<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN"
			"http://www.w3.org/TR/REC-html40/strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

</head>
<body>
&nbsp;&nbsp;
<% 
String current_location = null;
if (request.getAttribute(Constants.JSP_CURRENT_LOCATION ) == null)
{ 
    current_location =  (String)request.getParameter(  Constants.JSP_CURRENT_LOCATION  );
}
else
{
    current_location = (String)request.getAttribute( Constants.JSP_CURRENT_LOCATION );
}
 if ( current_location != null)
     {%>
       <%= current_location %>  
        <% }%>

</body></html>