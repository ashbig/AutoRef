<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<html>
<head>
<title>select report</title>
</head>
<body >
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td > <font color="#008000" size="5"><b> create / change Database settings </font>    <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%">
 <i>If you are not sure what action you would like to perform consult <a href="Help_ConfigureSystem.jsp"> help
</a>. </i> 
  </td>    </tr>
  </table>
  </center>
</div>

<html:form action="/DirectDatabaseCommunications.do" > 



<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Add Definitions Database</font></b></td></tr>  
 
<tr><td bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input checked type="radio" name="forwardName" value="<%=  Constants.PROCESS_ADD_SPECIES_DEFINITION%>" ><strong> Add Species definition</strong><br></td></tr>
<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp;  <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_NAME_TYPE%>"> <strong> Add Name type</strong>  <br> </td></tr>
<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp;  <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_PROJECT_DEFINITION%>"> <strong> Add Project definition</strong>  <br></td></tr>

<!--<tr><td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Add Definitions to Database</font></b></td></tr>  
-->
<tr><td  bgcolor="#DCE8FC">&nbsp;</td></tr>


<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_NEW_VECTOR%>"> <strong> Add Vector</strong>  <br></td></tr>
<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_NEW_COMMON_PRIMER%>"> <strong> Add Common Primer</strong>  <br></td></tr>
<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER%>"> <strong> Associate Vector with common primer</strong>  <br></td></tr>

<tr><td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Submit data into Database</font></b></td></tr>  
<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_ADD_NEW_LINKER%>"> <strong> Add Linker</strong>  <br></td></tr>

<tr><td  bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES%>"> <strong> Submit reference sequence information from file</strong>  <br></td></tr>
<tr><td bgcolor="#DCE8FC"> <font color="#000080">&nbsp; <input type="radio" name="forwardName" value="<%= Constants.PROCESS_SUBMIT_CLONE_COLLECTION%>"> <strong> Submit clone collection from file</strong>  <br></td></tr>

</table>
  

<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
  
</div>
</html:form> 
</body>

</html>


