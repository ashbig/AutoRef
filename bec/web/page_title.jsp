<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
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
	int forwardName_int = 0;
        if (forwardName!= null && forwardName instanceof String  ) forwardName_int = Integer.parseInt((String)forwardName);
        else if (forwardName!= null && forwardName instanceof Integer ) forwardName_int = ((Integer) forwardName).intValue(); 
%>


<% if ( title != null){%>
<table width ='90%' border=0 align='center'><tr >
<td >
<h2 style='font: bold, 14px, Times New Roman ;text-align: left; color: #008000;white-space: nowrap; '>
<%= title %> </h2></td></tr><tr><td valign='top'>    
<hr ></td></tr> </table>
<%}%>