<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

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
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Transfer 
      plate information from FLEX into BEC</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_UPLOAD_PLATES %> >
      Upload template plates information (settings required)&nbsp;<br>
      </font> <font color="#ECECFF">a</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Run 
      End reads and Rank isolates based on End Reads Evaluation</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_SELECT_VECTOR_FOR_END_READS   %> >
      Request end reads sequencing (settings required)</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_END_READS_WRAPPER  %> >
      Run end reads wrapper </font></td>
  </tr>
 <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS  %> >
      Run assemble for end reads</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY %>>
      Check whether all reads are available</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ISOLATE_RUNKER  %>>
      Run isolate ranker (settings required)</font></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"><font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_APROVE_ISOLATE_RANKER  %>>
      Approve isolate ranking</font> <p></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Design 
      of internal primers</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_PUT_CLONES_ON_HOLD %>>
      Put clones on hold </font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_ACTIVATE_CLONES %>>
      Activate clone</font> </td>
  </tr>
  <td width="100%" height="25" bgcolor="#DCE8FC"> &nbsp; </font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_PRIMER3 %>>
      Run primer designer (settings required)</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER  %>>
      Add new internal primer</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_APPROVE_INTERNAL_PRIMERS %>>
      Approve internal primers</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_INTERNAL_PRIMERS  %>>
      View internal primers 
      <P></font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Clone 
      Evaluation</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS  %>>
      Run assembly wrapper</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE  %>>
      Submit assembled sequences</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DISCREPANCY_FINDER %>>
      Run discrepancy finder</font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUNPOLYMORPHISM_FINDER %>>
      Run polymorphism finder(settings required)</font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DESIGION_TOOL%>>
      Run decision tool (settings required) </font> <P></td>
  </tr>
  <tr>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Stand 
      alone processes</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE %> >
      Run Discrepancy Finder on set of sequences<br>
      </font> <font color="#ECECFF">a</font></td>
  </tr>
  <tr> 
    <td> <br> <b><font color="#1145A6">Please make a selection and submit.</font></b> </center> 
      <p align="left"> 
        <input type="submit" value="continue" name="submit">
        <br>
    </td>
  </tr>
  <p> 
</table>


</html:form> 
</body>
</html>


