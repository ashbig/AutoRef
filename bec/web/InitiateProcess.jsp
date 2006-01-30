<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

<title> <bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- TemplateParam name="OptionalRegion1" type="boolean" value="true" -->
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
	ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION);
        ArrayList names = (ArrayList)  request.getAttribute(Constants.SPEC_TITLE_COLLECTION);
        ArrayList control_names  = (ArrayList)  request.getAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION);
	int forwardName_int = 0;
        if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
        else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue(); 
	
%>
<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>
    </tr>  </table>  </center></div>


<form action="RunProcess.do" method='POST' onsubmit="return validate_initprocess(this, <%= Constants.ITEM_TYPE_PROJECT_NAME %>);"> 
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 

<table border="0" cellpadding="10" cellspacing="2" width="90%" align=center>

    <tr><td colspan=2>
         <jsp:include page="enter_items.jsp" /> </td>
    </tr>
<%
if ((specs != null && names != null && control_names != null)
    && ( specs.size()> 0 && names.size()>0 && control_names.size()>0))
 {
        String control_name = null;         String spec_name = null; 
        ArrayList specs_arr = null;         Spec spec = null;
%>
	<tr><td colspan =2 bgColor="#1145A6" ><font color="#FFFFFF"><strong>Process specification</strong></font></td></tr>
<%
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
    <%}%>

<%}%>


 <jsp:include page="additional_jsp_for_initiateprocess.jsp" />
</table>

<div align="center">   <p>     <input type="submit" value="Submit" ></DIV>
</form> 
          
          
          
          
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
