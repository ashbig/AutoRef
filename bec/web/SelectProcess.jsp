<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>Primer Calculating Parameters</title>

<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>


<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Processes</font>
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


 <html:form action="/SelectProcess.do" > 

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<!--
<tr><td>
<p><strong>Please select process to execute</strong></p><p><P>
</td></tr> -->
<tr><td width="100%" height="25" bgcolor="#1145A6">
       <b><font color="#FFFFFF">Transfer plate information from FLEX into BEC</font></b></td></tr>
 
 <tr><td width="100%" height="29" bgcolor="#DCE8FC">
      <font color="#000080">&nbsp;              
      <input type="radio" name="processid" value="1">  Upload template plates 
                information (settings required)&nbsp;<br></font>  
				  <font color="#ECECFF">a</font></td></tr>
  <tr><td width="100%" height="25" bgcolor="#1145A6">
         <b><font color="#FFFFFF">Run End reads and Rank isolates based on End Reads Evaluation</font></b></td></tr>
   
   <tr><td width="100%" height="29" bgcolor="#DCE8FC">
        <font color="#000080">&nbsp; <input type="radio" name="processid" value="2"> 
      Run end reads sequencing (settings required)</td>
  </tr>
  
  <tr><td width="100%" height="25" bgcolor="#DCE8FC">
          <font color="#000080">&nbsp; <input type="radio" name="processid" value="3"> 
        Run end reads wrapper (settings required)</td>
  </tr>
  <tr><td width="100%" height="25" bgcolor="#DCE8FC">
            <font color="#000080">&nbsp; <input type="radio" name="processid" value="4"> 
         Check whether all reads are available</td>
  </tr>
  <tr><td width="100%" height="25" bgcolor="#DCE8FC">
             <font color="#000080">&nbsp; <input type="radio" name="processid" value="5"> 
          Run isolate ranker (settings required)</td>
  </tr>
 <tr><td width="100%" height="29" bgcolor="#DCE8FC">
              <font color="#000080">&nbsp; <input type="radio" name="processid" value="6"> 
           Approve isolate ranking<p></td>
  </tr>
 
 
 <tr><td width="100%" height="25" bgcolor="#1145A6">
        <b><font color="#FFFFFF">Design of internal primers</font></b></td></tr>


	<td width="100%" height="25" bgcolor="#DCE8FC">
	             <font color="#000080">&nbsp; <input type="radio" name="processid" value="10"> 
	         Run primer designer (settings required)</td>
  	</tr>
  	<td width="100%" height="25" bgcolor="#DCE8FC">
		             <font color="#000080">&nbsp; <input type="radio" name="processid" value="0"> 
		 Add new internal primer</td>
  	</tr>
  	<td width="100%" height="25" bgcolor="#DCE8FC">
		             <font color="#000080">&nbsp; <input type="radio" name="processid" value="0"> 
		 Approve internal primers</td>
  	</tr>
  	<td width="100%" height="25" bgcolor="#DCE8FC">
			             <font color="#000080">&nbsp; <input type="radio" name="processid" value="0"> 
		View internal primers<P></td>
  	</tr>
       
   <tr><td width="100%" height="25" bgcolor="#1145A6">
           <b><font color="#FFFFFF">Clone Evaluation</font></b></td></tr>

   
   	 <tr><td width="100%" height="25" bgcolor="#DCE8FC">
	  <font color="#000080">&nbsp; <input type="radio" name="processid" value="12"> 
	           Run assembly wrapper</td>
  	</tr>
        <tr><td width="100%" height="25" bgcolor="#DCE8FC">
      	  <font color="#000080">&nbsp; <input type="radio" name="processid" value="13"> 
      	           Run discrepancy finder </td>
  	</tr>
 	 <tr><td width="100%" height="25" bgcolor="#DCE8FC">
	      	  <font color="#000080">&nbsp; <input type="radio" name="processid" value="14"> 
	      	           Run polymorphism finder(settings required) </td>
  	</tr>
  	 <tr><td width="100%" height="25" bgcolor="#DCE8FC">
	      	  <font color="#000080">&nbsp; <input type="radio" name="processid" value="15"> 
	      	           Run decision tool (settings required) <P></td>
  	</tr>
 
 
 <tr><td>
 <br>
         <b><font color="#1145A6">Please make a selection and submit.</font></b>
 </center>
                   <p align="left"><input type="submit" value="continue" name="submit"><br>
                  <font color="#FFFFFF">s</font>
                  
                  
            </td></tr>
<p> 
</table>


</html:form> 
</body>
</html>


