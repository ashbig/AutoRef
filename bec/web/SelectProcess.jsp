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
<html:form action="/SelectProcess.do" > 

<%Object forwardName = null;
if ( request.getAttribute("forwardName") != null)
{
        forwardName = request.getAttribute("forwardName") ;
}
else
{
        forwardName = request.getParameter("forwardName") ;
}
 Object title = null;
 int forwardName_int = 0;
if (forwardName!= null && forwardName instanceof String  ) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName!= null && forwardName instanceof Integer ) forwardName_int = ((Integer) forwardName).intValue(); 
%>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align='center'>
<tr><td align="right"> <br> Please make selection and click 'Continue' button.   </td></tr>
 
<% if (forwardName_int == Constants.UI_SELECT_PROCESS_EREAD_MANIPULATION_PAGE){%>
  <tr class ='headerRow'>     <td  height="25" > End reads manipulation</td>  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td height="29">&nbsp; 
      <input  checked type="radio" name="forwardName" value="<%= Constants.PROCESS_SELECT_VECTOR_FOR_END_READS   %>" >
      Request end read sequencing (settings required)</td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td  height="25" >&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_END_READS_WRAPPER  %>" >
      Check quality and distribute end reads </td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td  height="25">&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_SUBMIT_EREADS_AS_INTERNALS  %>" >
      Submit low quality end reads as internal reads</td>
  </tr>
  
 <tr class='evenRowColoredFontNotBold'> 
    <td  height="25">&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS  %>" >
      Run assembler for end reads *</td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input    type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS  %>">
      Run generic assembler **</td>
  </tr>
  
  <tr  > 
    <td   height="25"  ><P>*Operates on plate level, but can evaluate clones with only one read.
      </td>
  </tr>
   <tr  > 
    <td   height="25"  ><P>**Operates on clone level and requiers more than one trace per clone.
      </td>
  </tr>
  <%}%>


 <% if (forwardName_int == Constants.UI_SELECT_PROCESS_INTERNAL_PRIMERS_PAGE){%>

  <tr class='headerRow'>     <td  height="25" >Internal primers design and order</td>  </tr>
  <tr> 
   <!-- <td   height="25"  >&nbsp; 
      <input disabled type="radio" name="forwardName" value= = Constants.PROCESS_PUT_CLONES_ON_HOLD %>>      Put clones on hold 
      <input disabled type="radio" name="forwardName" value=   = Constants.PROCESS_ACTIVATE_CLONES % >>      Activate clones <P>  </td>-->
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  checked type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_PRIMER3 %>" >
      Run primer designer (settings required) </td>  
  </tr>
 
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_APPROVE_INTERNAL_PRIMERS %>" >
      Approve internal primers </td>
  </tr>


<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_ORDER_INTERNAL_PRIMERS %>" >
      Order internal primers </td>
  </tr>
 <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_PROCESS_OLIGO_PLATE %>" >
      Track oligo plate<P> </td>
  </tr>
 
<%}%>

<% if (forwardName_int == Constants.UI_SELECT_PROCESS_PAGE){%>

<tr class='headerRow'>     <td  height="25"> Evaluate clones</td></tr>


  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input checked type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_DISCREPANCY_FINDER %>" >
      Run discrepancy finder  </td>
  </tr>
<tr class='evenRowColoredFontNotBold'> 
    <td height="25" >&nbsp; 
      <input type="radio" name="forwardName" value="<%= Constants.PROCESS_RUN_ISOLATE_RUNKER  %>">
      Run isolate ranker (settings required) </td>
  </tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE  %>" >
      Find low quality regions in clone sequences (settings required) </td>
  </tr>
  
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input    type="radio" name="forwardName" value="<%= Constants.PROCESS_FIND_GAPS %>" >
      Run Gap Mapper (settings required) </td>
  </tr>

  
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_RUNPOLYMORPHISM_FINDER %>" >
      Run polymorphism finder (settings required)  </td>
  </tr>

  
  <%}%>
 
 
  <% if (forwardName_int == Constants.UI_SELECT_PROCESS_DELETE_DATA_PAGE){%>

  <tr class='headerRow'>     <td  height="25" > Delete Options</td></tr>

<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
     Database operations </td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input   type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_PLATE %>">
      Delete plate </td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input checked type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_CLONE_READS %>">
      Delete clone forward and reverse end reads from database*</td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_CLONE_FORWARD_READ  %>">
      Delete clone forward end reads  from database*</td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_CLONE_REVERSE_READ  %>">
      Delete clone reverse end reads  from database*</td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_CLONE_SEQUENCE %>">
      Delete clone sequence  from database*</td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_REANALYZE_CLONE_SEQUENCE %>">
      Reanalyze clone sequence <P></td>
</tr>

<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
     Hard drive operations </td>
</tr>
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input type="radio" name="forwardName" value="<%= Constants.PROCESS_GET_TRACE_FILE_NAMES  %>">
      Get trace file names </td>
</tr>

<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input type="radio" name="forwardName" value="<%= Constants.PROCESS_MOVE_TRACE_FILES %>" >
      Move trace file from clone directory into temporary directory (allows trace files recovery)</td>
</tr> 

<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_DELETE_TRACE_FILES %>" >
      Delete trace files from hard drive (no recovery possible)</td>
</tr> 
<tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; 
      <input  type="radio" name="forwardName" value="<%= Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE %>" >
      Clean-up hard drive** </td>
</tr> 
<tr  > 
    <td     > <P><P>*  This removes sequence and analysis data, but the trace files themselves remain on hard drive.
    <P>** This deletes all intermidiate files from hard drive. </td>
</tr> 

<%}%>
  
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


