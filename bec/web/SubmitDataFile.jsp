<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>



<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
    <script defer="defer" type="text/javascript"><!--
  /*Start of form validation:*/
  function validateForm(formElement)
   {
    //Check user name is at least 2 characters long

    var str =  trim(formElement.fileName.value);
    if (( formElement.fileName.value == null) || ( str == "" ) )
	{
		alert('Please submit valid file name.')
        formElement.fileName.focus()
        return false
   	}
   	return true
   }
   function trim(strText)
    { 
		// this will get rid of leading spaces 

		while (strText.substring(0,1) == ' ') 
			strText = strText.substring(1, strText.length); 

		// this will get rid of trailing spaces 
		while (strText.substring(strText.length-1,strText.length) == ' ') 
			strText = strText.substring(0, strText.length-1); 

	   return strText; 
	} 

 



	   /*End of functions.*/
//--></script>

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
          <td>



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
    
    
  
    <form name ="SubmitDataFile" action="SubmitDataFile.do" METHOD=POST ENCTYPE="multipart/form-data" 
	onreset="return confirm('Are you sure that you want to reset this form?');" 
	onsubmit="return validateForm(this);">
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

	
%>

<input name="forwardName" type="hidden" value="<%= forwardName %>" > 

                    
      
                   
        <tr>
            <td ><%= request.getAttribute( Constants.FILE_DESCRIPTION ) %><P></P><P></P></td>
        </tr>
		<tr><td><%= request.getAttribute(Constants.FILE_TITLE ) %>

		<input type="file" name="fileName" id="fileName" value="">
		</td></tr>
		<tr>
            <td >&nbsp;</td>
        </tr>
		<tr>
            <td ><%= request.getAttribute(Constants.ADDITIONAL_JSP)%><P></P><P></P></td>
        </tr>
       <tr><td>&nbsp; <P></P><div align="center"><input type="SUBMIT" value='Submit'/></div></td></tr>
   
   
    
    </form>
	 </table>
	 
	 
	 </td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
</html>
