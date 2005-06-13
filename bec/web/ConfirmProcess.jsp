<%@ page language="java" %>

<%@ page import="java.util.*" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<html>
<head>
<%Object forwardName = null;
if ( request.getAttribute("forwardName") != null)
{
			forwardName = request.getAttribute("forwardName") ;
}
else
{
        forwardName = request.getParameter("forwardName") ;
}
int forwardName_int = -1;
if (forwardName != null && forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName != null && forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();

%>
<title>ConfirmProcess</title></head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td ><font color="#008000" size="5"><%=request.getAttribute( Constants.JSP_TITLE) %> </font>
<hr><p> </td></tr>

<tr><td> <h3> <%=request.getAttribute( Constants.ADDITIONAL_JSP) %></h3></td></tr>


<%if ( forwardName_int != -1 &&   forwardName_int==  Constants.AVAILABLE_CONTAINERS_INT)
{
     int cur_column=1;String cur_label = null;String prev_label = null;
     String project_code = null;
     ArrayList labels = (ArrayList) request.getAttribute("labels");
    if ( labels == null || labels.size() == 0)
     {%>
     <tr><td> <h3> No containers are available.</h3></td></tr>
<%}
else
{
    project_code = DatabaseToApplicationDataLoader.getProjectCodeForLabel((String)labels.get(0));
        
     for (int index = 0; index < labels.size(); index ++)
    {
        cur_label = (String)labels.get(index);
        if (index == 0) %><tr>
        <%if ( cur_column == 6){cur_column =1;%></tr><tr><%}%>
        <% if ( prev_label == null || ( prev_label != null &&  !cur_label.startsWith(project_code)))
        {
            project_code = DatabaseToApplicationDataLoader.getProjectCodeForLabel(cur_label);
   
            { if ( index > 0 ){%>        
            </table></DIV></tr><tr><%}}%>
             <tr><td><INPUT onclick='javascript:showhide(<%= "\"" + cur_label.charAt(0) + "\"" %>, this.checked);' type=checkbox CHECKED value=1 name=show> <%= DatabaseToApplicationDataLoader.getProjectName(project_code)%>
             <DIV ID='<%= cur_label.charAt(0) %>' STYLE='position:relative;  clip:rect(0px 120px 120px 0px);'>
              <p><table border = 0>
                <% cur_column=1;   }%>
                
                
              <td><b> <%= cur_label %> </b></td>
              <%  cur_column ++;prev_label = cur_label;}%>
              </table></table>  <%}}%>
</table>
      
</body>
</html> 
