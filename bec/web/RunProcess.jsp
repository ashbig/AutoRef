<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>



<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : <bean:message key="title"/> </h2>
<hr>
<html:errors/>
<html:form action="/RunProcess.do" > 
<input name="forwardName" type="hidden" value="<%=forwardName%>" > 
<h3>Set process parameters </h3>

<table width="70%" border="1" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="44%" background="barbkgde.gif"> <div align="center"><strong>Process 
        Name </strong></div></td>
    <td width="56%" background="barbkgde.gif"> <div align="center"><strong>Process 
        settings</strong></div></td>
  </tr>
  <tr> 
    <td><strong>Primer designer</strong></td>
    <td><select name="specid">
        <option selected value='M13'> M13</option>
      </select></td>
  </tr>
</table>
<hr>
<h3>Plates to process (rearrayed plates)</h3>
<p> 
  <input type="checkbox" name="checkbox" value="checkbox">
  Select all</p>
<table width="25%" border="0" cellspacing="2" cellpadding="2">

  <tr>
    <td width="20%"><input type="checkbox" name="checkbox2" value="checkbox"></td>
    <td width="80%">MGS-forward</td>
  </tr>
 
</table>
<p>&nbsp;</p>

<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>
</html>


