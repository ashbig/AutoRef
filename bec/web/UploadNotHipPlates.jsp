<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<html>
<head>
<title>Select Process</title>
<link href="application_styles.css" rel="stylesheet" type="text/css">
</head>
<body >

<div align="center">
  <center> <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr> <td  ><html:errors/></td> </tr> </table>
  </center></div>
<html:form action="/DirectDatabaseCommunications.do" > 



<table border="0" cellpadding="0" cellspacing="0" width="90%" align='center'>
<tr><td align="right"> <br> Please make selection and click 'Continue' button.   </td></tr>
  
<tr class='headerRow'>     <td  height="25" >Upload plate data</td></tr>


<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input   checked type="radio" name="forwardName" value="<%= Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES %>">
      Upload reference sequence information from file</td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_SUBMIT_CLONE_COLLECTION %>">
      Upload clone collection from file</td>
</tr>


<tr > 
    <td> <br>
      <b></b> <div align="center"> 
      <input type="submit" value="Continue" name="submit">    <br></div>
    </td>
</tr>
</table>


</html:form> 
</body>
</html>


