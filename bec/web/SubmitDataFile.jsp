<%@page contentType="text/html"%>
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
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> 
	 <% 
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
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
            <td >&nbsp;<P></P></td>
        </tr>
		<tr>
            <td ><%= request.getAttribute(Constants.ADDITIONAL_JSP)%><P></P><P></P></td>
        </tr>
       <tr><td>&nbsp; <P></P><div align="center"><input type="SUBMIT"/></div></td></tr>
   
   
    
    </form>
	 </table>
</body>
</html>
