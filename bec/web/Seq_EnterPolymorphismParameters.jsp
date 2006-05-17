<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>

<head>

<title>Polymorpism Detector Parameters</title>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="application_styles.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>
<body>

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
    <td colspan =2><div align="right"><b> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a>&nbsp;&nbsp;<a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT%>"> 
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

  <tr> 
    <td bgcolor="#b8c6ed"><strong><font color="#000080">Please select GenBank 
      database to search in:</font></strong></td>
    <td bgcolor="#b8c6ed">
 <select name="PL_DATABASE" id="PL_DATABASE"  MULTIPLE size=2>

 <%String dbpath = null;String dbname = null;
 Hashtable ft =  BecProperties.getInstance().getPolymFinderBlastableDatabases();     
       
for (Enumeration e =ft.keys() ; e.hasMoreElements() ;)
{
	dbname = (String) e.nextElement();
  	dbpath = (String)ft.get(dbname);
  	%> <OPTION VALUE='<%=dbpath %>'> <%= dbname %>
<%}%>
</select> </td>
  </tr>
  <tr >
    <td colspan="2">&nbsp; </td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><b><font color="#000080">Number of flanking bases to 
      append on both sides of discrepancy for search: </font></b> </td>
	
    <td bgColor="#e4e9f8"><input name="PL_BASES" type="text" id="PL_BASES" value="20" width="120" ></td>
  </tr>
  <tr><td colspan=2 >&nbsp;</td></tr>
  <tr> 
    <td bgcolor="#b8c6ed" colspan = 2> <b><font color="#000080">Parameters to confirm similarity 
      between the query and any database hits to confirm they are the same gene: 
      </font></b></td>
   
  </tr>
  <tr>
    <td bgColor="#e4e9f8"><div align="right"><font color="#000080">minimum match 
        length:&nbsp;&nbsp;&nbsp;</font></div></td>
	<td bgColor="#e4e9f8"><font color="#000080"><input name="PL_MATCH_LENGTH" type="text" value="100"  width="120">&nbsp;</font></td>
  </tr>
  <tr>
    <td bgColor="#e4e9f8"><div align="right"><font color="#000080">required percent 
        identity:&nbsp;&nbsp;&nbsp;</font></div></td>
	<td bgColor="#e4e9f8"><font color="#000080">
      <input name="PL_MATCH_IDENTITY" type="text" value="95"  width="120">
      &nbsp;</font></td>
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

