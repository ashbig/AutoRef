<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>Primer Calculating Parameters</title>

<link href="file:///C|/Program%20Files/Apache%20Tomcat%204.0/webapps/developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Select processes to execute</h2>
<hr>
<html:errors/>
 <html:form action="/SelectProcess.do" > 

<i>If you are not sure about certain parameters, please, consult help </i> <a href="file:///C|/Program%20Files/Apache%20Tomcat%204.0/webapps/FLEX/Help_SequenceEvaluation.html">[parameter 
help file]</a>.</b> <font color="#2693A6" size="4"> </font>
<p><strong>Specify what process(s) to execute</strong></p>

<table width="58%" border="1" cellspacing="2" cellpadding="2" align="center">
  <tr>
    <td><input type="checkbox" name="processid" value="1">
      Upload template plates</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="processid" value="2">
      Run sequencing for end reads (settings required)</td>
  </tr>
  <tr> 
    <td width="86%"><input type="checkbox" name="processid" value="3">
      Run end read wrapper </td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="processid" value="4">
      Check whether all reads are available</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="processid" value="5">
            Run isolate ranker (settings required)</td>
       </td>
  </tr>
  <tr> 
    <td><input name="processid" type="checkbox" id="processid" value="6">
      View, change(optional) and approve isolate ranking</td>
  </tr>
  <tr> 
    <td><table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr> 
          <td> <input name="processid" type="checkbox" id="processid" value="7">
            Send plates for sequencing to the outside facility</td>
        </tr>
        <tr> 
          <td> <input name="processid" type="checkbox" id="processid" value="8">
            Receive sequencing plates from the outside facility</td>
        </tr>
        <tr> 
          <td> <input name="processid" type="checkbox" id="processid" value="9">
            Upload data from the outside facility</td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td> <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr> 
          <td> <input name="processid" type="checkbox" id="processid" value="10">
            Run primer designer (settings required)</td>
        </tr>
        <tr> 
          <td><input name="processid" type="checkbox" id="processid" value="11">
            Receive oligo plates</td>
        </tr>
        <tr> 
          <td> <input name="processid" type="checkbox" id="processid" value="12">
            Run assembly wrapper </td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td><input name="processid" type="checkbox" id="processid" value="13">
      Run discrepancy finder for clone evaluation </td>
  </tr>
  <tr> 
    <td><input name="processid" type="checkbox" id="processid" value="14">
      Run polymorphism finder for clone evaluation (settings required)</td>
  </tr>
  <tr> 
    <td><input name="processid" type="checkbox" id="processid" value="15">
      Run decision tool (settings required)</td>
  </tr>
  <tr> 
 
  </tr>
</table>

<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>
</html>


