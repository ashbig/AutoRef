<%@ page errorPage="ProcessError.do"%>



<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="bec.name"/> : Plate Label</title>
    
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

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
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
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();
%>   
    <form action="Seq_GetItem.do" >
   
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<input name="<%= Constants.JSP_TITLE %>" type="hidden" value="<%= title %>" >
         <tr><td >
        <H3>Please enter plate label:</h3>
   
            <input type="text" name="<%=Constants.CONTAINER_BARCODE_KEY%>"/></td>
        </tr>
		<tr><td>&nbsp;</td></tr>
<%if (  forwardName_int== Constants.CONTAINER_RESULTS_VIEW)

{%>  

<tr><td><input type=radio name=show_action value="IR" checked>Show Isolate Ranker Output</td></tr>
<tr><td><input type=radio name=show_action value="FER">Show Forward End Reads</td></tr>
<tr><td><input type=radio name=show_action value="RER">Show Reverse End Reads</td></tr>

<%}%>
 		
       <tr><td align='center'> <P></P> <input type="submit" value="Submit" name="B1"></td></tr>
   
   
    
    </form>
	 </table>
	 
	 
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
