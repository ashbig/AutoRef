<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>Primer Calculating Parameters</title>

<link href="../developed/bec/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Set Project Parameters for Sequencing</h2>
<hr>
<html:errors/>
<html:form action="/logon.do" > 

<input name="projectid" type="hidden" value="<bean:write name="projectid" />" >
<input name="workflowid" type="hidden" value="<bean:write name="workflowid" />" >
<input name="projectname" type="hidden" value="<bean:write name="projectname" />" >
<input name="workflowname" type="hidden" value="<bean:write name="workflowname" />" >


<table >
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>

    </tr>

</table>

<h3>Parameters for End Reads</h3>
<table border=0 cellspacing="4" width="80%" align="center">
  <% if (isUniversal.equals("Y"))
  { 
 %>
  <tr> 
    <td  background="barbkgde.gif" width="50%" > <p><b>Select end reads primer 
        sets: </b> 
    </td>
    <td background="barbkgde.gif" > 
      <% 

 if(mode_debug)
{%>
      <div align="center">
        <SELECT NAME="MY" width="50" >
          <OPTION VALUE="Less than 1 year.">MY 
          <OPTION VALUE="1-5 years.">YM 
        </SELECT>
        <%}
else
{%>
        <html:select property="set_name"> <html:options
            collection="list_universal_primers_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
        <%}}%></p>
        </div></td>
  </tr>
  <tr> 
    <td colspan="2"  background="barbkgde.gif"> <b>Set parameters for isolate 
      ranker:</b> 
      <table width="100%" border="0" cellspacing="2" cellpadding="2" align="center">
        <tr> 
          <td width="50%"><div align="right"><strong>penalty values</strong></div></td>
          <td width="50%"> 
            <%
if(mode_debug)
{%>
            <div align="center"> 
              <SELECT NAME="select" >
                <OPTION VALUE="Less than 1 year.">Set 1 
                <OPTION VALUE="1-5 years.">Set 2 
              </SELECT>
              <%}
else
{%>
              <html:select property="set_name"> <html:options
            collection="list_end_reads_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
              <%}%>
            </div></td>
        </tr>
        <tr> 
          <td><div align="right"><strong>maximum allowable discrepancies</strong></div></td>
          <td> 
            <%
if(mode_debug)
{%>
            <div align="center"> 
              <SELECT NAME="select2" >
                <OPTION VALUE="Less than 1 year.">Set 1 
                <OPTION VALUE="1-5 years.">Set 2 
              </SELECT>
              <%}
else
{%>
              <html:select property="set_name"> <html:options
            collection="list_end_reads_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
              <%}%>
            </div></td>
        </tr>
        <tr>
          <td><div align="right"><strong>include polymorphism finder</strong></div></td>
          <td>&nbsp;</td>
        </tr>
      </table>
        </td>
  </tr>
</table>
<p>
<h3>Parameters for Full Squencing</h3>
<table border=0 cellspacing="4" width="80%" align="center">
  <tr> 
    <td  background="barbkgde.gif" width="50%"> <b>Parameters for primer calculations</b> 
    </td>
    <TD background="barbkgde.gif" > 
      <%  
 if(mode_debug)
{%>
      <div align="center"> 
        <SELECT NAME="Set 1" >
          <OPTION VALUE="Less than 1 year.">Set 1 
          <OPTION VALUE="1-5 years.">Set 2 
        </SELECT>
        <%}
else
{%>
        <html:select property="set_name"> <html:options
            collection="list_primer3_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
        <%}%>
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b>Parameters for sequence evaluation</b></td>
    <TD background="barbkgde.gif"> 
      <%  
 if(mode_debug)
{%>
      <div align="center"> 
        <SELECT NAME="Set 1" >
          <OPTION VALUE="Less than 1 year.">Set 1 
          <OPTION VALUE="1-5 years.">Set 2 
        </SELECT>
        <%}
else
{%>
        <html:select property="set_name"> <html:options
            collection="list_full_sequence_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
        <%}%>
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b>Parameters for polymorphism detector</b></td>
    <TD background="barbkgde.gif"> 
      <%  
 if(mode_debug)
{%>
      <div align="center"> 
        <SELECT NAME="Set 1" >
          <OPTION VALUE="Less than 1 year.">Set 1 
          <OPTION VALUE="1-5 years.">Set 2 
        </SELECT>
        <%}
else
{%>
        <html:select property="set_name"> <html:options
            collection="list_full_sequence_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        /> </html:select> 
        <%}%>
      </div></td>
  </tr>
</table>
  <p>
  <div align="center"> <p> 
        <input type="submit" value="Submit" name="B1">&nbsp;
      <input type="reset" value="Reset" name="B2">
  </div>
</html:form> 
</body>
</html>


