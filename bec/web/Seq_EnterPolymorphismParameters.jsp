<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>

<title>Polymorpism Detector Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> create new set of parameters for Polymorphism Detection</font>
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
	<tr>
        <td><i>If you are not sure about certain parameter settings, leave them 
          unchanged </i> <a href="Help_ConfigureSystem.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>

<html:form action="/Seq_SubmitSpec.do" > 
<input name="forwardName" type="hidden" value="<%=PolymorphismSpec.POLYMORPHISM_SPEC_INT%>" >

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td colspan =2><div align="right"><b> <a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT%>"> 
        View All </a></b> </div>
      <p> 
      <p> 
      
      </td>
  </tr>
  <tr> 
    <td colspan="2"> <p> <font  size="4"> <b>Set Name</b></font> 
        <input type="text" name="SET_NAME" size="53" value="">
      <p> </td>
  </tr>

  <tr > 
    <td bgColor="#e4e9f8" ><strong><font color="#000080">Please select species:</font></strong> 
    </td>
    <td bgColor="#e4e9f8"> <select name="PL_SPECIES" id="PL_SPECIES" width="70" disabledd>
        <option  value="GI">Human</option>
		<option  value="GI">Yeast</option>
		<option  value="GI">Pseudomonas</option>
        
      </select> </td>
  <tr> 
    <td bgcolor="#b8c6ed"><strong><font color="#000080">Please select Database:</font></strong></td>
    <td bgcolor="#b8c6ed"> <select name="PL_DATABASE" id="PL_DATABASE"  disabled>
        <option value="nr" selected>nr</option>
        <option  value="nr">a aaaaaaaaa</option>
      </select> </td>
  </tr>
  <tr >
    <td colspan="2">&nbsp; </td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Please specify number of bases 
      in flanking sequence:</font></b> </td>
    <td bgColor="#e4e9f8"><input name="PL_BASES" type="text" id="PL_BASES" value="20" width="120" ></td>
  </tr>
  <tr> 
    <td bgcolor="#b8c6ed"> <b><font color="#000080">Please select output format 
      <i>(optional)</i>:</font></b> </td>
    <td bgcolor="#b8c6ed"> <select name="PL_FORMAT" id="PL_FORMAT" disabled>
        <option selected value="XML">XML</option>
        <option  value="FLatFile">Flat File</option>
      </select> </p> </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <!-- <tr > 
    <td width="75%" nowrap background="barbkgde.gif"> <table border="0" width="100%">
        <tr>
          <Td><b>Upload a data file <em>(optional)</em>: </b> </Td>
          <Td align="right"><input name="PL_FILE" type="text" id="PL_FILE" disabled></Td>
        </tr>
      </table></td>
    <td align="left" nowrap background="barbkgde.gif"><input type="button" name="Browse" value="Browse..." ></td>
  </tr> -->
  <tr><td>&nbsp;</td></tr>
  <tr><td colspan="2">
  <div align="center">
  <input type="submit" name="Submit" value="Submit">
  &nbsp; 
  <input type="reset" name="Reset" value="Reset">
</div>
  </td></tr>
</table>


<p>

</html:form>
</p>
</body>

