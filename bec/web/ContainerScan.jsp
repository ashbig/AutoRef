<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>



<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="bec.name"/> : Container Process History</title>
    
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> 
	 <% 
String title = null;
if (request.getParameter(Constants.JSP_TITLE ) != null)
 { 
    title = (String)request.getParameter(  Constants.JSP_TITLE  );
 }
else if  (request.getAttribute(Constants.JSP_TITLE ) != null)
{
    title = (String)request.getAttribute(  Constants.JSP_TITLE  );
 }

%>
        <%= title %>
	
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

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    
    <tr><td >
    <H3>Please Scan the Container</h3>
    <form action="Seq_GetItem.do" >
    <%
		Object forwardName = null;
		if ( request.getAttribute("forwardName") != null)
		{
			forwardName = request.getAttribute("forwardName") ;
		}
		else
		{
			forwardName = request.getParameter("forwardName") ;
		}%>

<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<input name="<%= Constants.JSP_TITLE %>" type="hidden" value="<%= title %>" >
        <tr>
            <td class="prompt" width="30%">Label:
            <input type="text" name="<%=Constants.CONTAINER_BARCODE_KEY%>"/></td>
        </tr>
		<tr><td>&nbsp;</td></tr>
<% 
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof String) forwardName_int = ((Integer) forwardName).intValue();
if (  forwardName_int== Constants.CONTAINER_RESULTS_VIEW)

{%>
<tr><td><input type=radio name=show_action value="FER">Show Forward End Reads</td></tr>
<tr><td><input type=radio name=show_action value="RER">Show Reverse End Reads</td></tr>

<tr><td><input type=radio name=show_action value="IR" checked>Show Isolate Ranker Output</td></tr>
<%}%>
 		
       <tr><td>&nbsp; <P></P><input type="SUBMIT"/></td></tr>
   
   
    
    </form>
	 </table>
</body>
</html>
