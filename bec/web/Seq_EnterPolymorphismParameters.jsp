<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.core.spec.*" %>
<html>

<head>

<title>Polymorpism Detector Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/bec/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Set Polymorphism Detector Parameters</h2>
<hr>

<html:errors/> 
<html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=PolymorphismSpec.POLYMORPHISM_SPEC_INT%>" >



<h3 >Create new set of parameters for Polymorphism detector</h3>
<i>If you are not sure about certain parameter settings, leave them unchanged 
</i> <a href="Help_SequenceEvaluation.html">[parameter help file]</a>.</b> 
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="SET_NAME" size="53" value="">
<P><P>
<font size="4" color="#2693A6"><b>
<p>


<table width="85%" border="0" align="center">
  <tr > 
    <td background="barbkgde.gif" ><strong>Please select species:</strong> </td>
    <td background="barbkgde.gif"> <select name="PL_SPECIES" id="PL_SPECIES" width="70" disabledd>
        <option  value="GI">Human</option>
		<option  value="GI">Yeast</option>
		<option  value="GI">Pseudomonas</option>
        <option selected value="FlexSequence ID">FlexSequence ID</option>
      </select> </td>
  <tr> 
    <td ><strong>Please select Database:</strong></td>
    <td> <select name="PL_DATABASE" id="PL_DATABASE"  disabled>
        <option value="nr" selected>nr</option>
        <option  value="nr">a aaaaaaaaa</option>
      </select> </td>
  </tr>
  <tr >
    <td colspan="2">&nbsp; </td>
  </tr>
  <tr> 
    <td background="barbkgde.gif"><b>Please specify number of bases in flanking 
      sequence:</b> </td>
    <td background="barbkgde.gif"><input name="PL_BASES" type="text" id="PL_BASES" value="20" width="120" ></td>
  </tr>
  <tr> 
    <td> <b>Please select output format <i>(optional)</i>:</b> </td>
    <td> <select name="PL_FORMAT" id="PL_FORMAT" disabled>
        <option selected value="XML">XML</option>
        <option  value="FLatFile">Flat File</option>
      </select> </p> </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr > 
    <td width="75%" nowrap background="barbkgde.gif"> <table border="0" width="100%">
        <tr>
          <Td><b>Upload a data file <em>(optional)</em>: </b> </Td>
          <Td align="right"><input name="PL_FILE" type="text" id="PL_FILE" disabled></Td>
        </tr>
      </table></td>
    <td align="left" nowrap background="barbkgde.gif"><input type="button" name="Browse" value="Browse..." ></td>
  </tr>
  
</table>


<p>
<div align="center">
  <input type="submit" name="Submit" value="Submit">
</div>
</p>
</body>
</html:form>
