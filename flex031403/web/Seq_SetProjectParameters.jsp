<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>Primer Calculating Parameters</title>

<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/> : Set Project Parameters for Sequencing</h2>
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
 <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
 </tr>
 <tr>
     <td class="prompt">
    <% 
 
       boolean mode_debug=true;
        String isUniversal = null;
       if (mode_debug)
          isUniversal = "Y";
       else
          isUniversal = (String)request.getAttribute("is_requere_universal_primers");
      
   if (isUniversal.equals("Y"))
    {%>
     This workflow requeres use of universal primers for end reads.
      <%
}
else
{%>     
This workflow requeres use of PCR primers for end reads.
<%}%>
</td>
</tr>
</table>

<h3>Parameters for End Reads</h3>
<table border=0 cellspacing="4" width="90%">

 <% if (isUniversal.equals("Y"))
  { 
 %>
<tr>
<td  background="barbkgde.gif" width="50%" >
     <p><b>End reads primers: Universal </b> 
      </td><td background="barbkgde.gif" >
        <% 

 if(mode_debug)
{%>
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
        <%}}%>
      </p></td>
    
</tr>
     
<tr>
<td  background="barbkgde.gif">
     <b>Parameters for sequence trimming and analisys:</b>
</td>

<TD background="barbkgde.gif">
<%
if(mode_debug)
{%>
<SELECT NAME="Set 1" >
<OPTION VALUE="Less than 1 year.">Set 1
<OPTION VALUE="1-5 years.">Set 2
</SELECT>
<%}
else
{%>
<html:select property="set_name">
        <html:options
            collection="list_end_reads_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        />
    </html:select>
<%}%>
</td>
</tr>
</table>
<p>
<h3>Parameters for Full Squencing</h3>
<table border=0 cellspacing="4" width="90%">
<tr>
<td  background="barbkgde.gif" width="50%">
     <b>Parameters for primer calculations</b>
</td>

<TD background="barbkgde.gif" >
<%  
 if(mode_debug)
{%>
<SELECT NAME="Set 1" >
<OPTION VALUE="Less than 1 year.">Set 1
<OPTION VALUE="1-5 years.">Set 2
</SELECT>
<%}
else
{%>
<html:select property="set_name">
        <html:options
            collection="list_primer3_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        />
    </html:select>
<%}%>
</td>
</tr>
<tr>
<td  background="barbkgde.gif">
     <b>Parameters for full sequence analysis and assembling</b>
</td>

<TD background="barbkgde.gif">
<%  
 if(mode_debug)
{%>
<SELECT NAME="Set 1" >
<OPTION VALUE="Less than 1 year.">Set 1
<OPTION VALUE="1-5 years.">Set 2
</SELECT>
<%}
else
{%>
<html:select property="set_name">
        <html:options
            collection="list_full_sequence_parameters_sets"
            property="set_id"
            labelProperty="set_name"
        />
    </html:select>
<%}%>
</td>
</tr>
  </table>
  <p>
  <div align="center"> <p> 
        <input type="submit" value="Submit" name="B1">
      
  </div>
</html:form> 
</body>
</html>


