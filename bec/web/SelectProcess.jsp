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

<title>Primer Calculating Parameters</title>

<link href="application_styles.css" rel="stylesheet" type="text/css">
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<% 
User user = (User)session.getAttribute(Constants.USER_KEY);
 boolean isAdmin = false;
 boolean is_eval_version =  false;
if (edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("IS_EVALUATION_VERSION") != null) is_eval_version = true;
int user_level = 0;
if (user.getUserGroup().equals("Researcher")) user_level = 0;
else if (user.getUserGroup().equals("Researcher2")) user_level = 1;
else if (user.getUserGroup().equals("Manager")) user_level =2;
else if (user.getUserGroup().equals("Administrator")) user_level = 3;
%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td ><font color="#008000" size="5"><b> Processes</font><hr>     </td> </tr></table>

<div align="center">
  <center> <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr> <td width="100%"><html:errors/></td> </tr> </table>
  </center></div>
<html:form action="/SelectProcess.do" > 
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td align="right"> <br> Please make selection and click 'Continue' button.   </td></tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Transfer 
      plate information from FLEX into ACE</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if (user_level < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_UPLOAD_PLATES %> <%if (isAdmin) {%>checked <%}%>  >
      Upload template plates information (settings required)&nbsp;<br>
      </font> <font color="#ECECFF">a</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Manipulate 
      end reads and perform isolate ranking</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_SELECT_VECTOR_FOR_END_READS   %> >
      Request end reads sequencing (settings required)</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_END_READS_WRAPPER  %> >
      Run end reads wrapper </font></td>
  </tr>
 <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS  %> >
      Run assembler for end reads</font></td>
  </tr>
 
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ISOLATE_RUNKER  %>>
      Run isolate ranker (settings required)</font></td>
  </tr>

<% if (! is_eval_version )
//edu.harvard.med.hip.utility.ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION)
{%>
  <tr> 
    <td width="100%" height="29" bgcolor="#DCE8FC"><font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_APROVE_ISOLATE_RANKER  %>>
      Approve isolate ranking</font> <p></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Design 
      internal primers</font></b></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input disabled type="radio" name="forwardName" value=<%= Constants.PROCESS_PUT_CLONES_ON_HOLD %>>
      Put clones on hold 
      <input disabled type="radio" name="forwardName" value=<%= Constants.PROCESS_ACTIVATE_CLONES %>>
      Activate clones
<P></font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_PRIMER3 %> >
      Run primer designer (settings required)</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input disabled <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER  %> >
      Add new internal primer</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_APPROVE_INTERNAL_PRIMERS %> >
      Approve internal primers<P></font></td>
  </tr>


<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_ORDER_INTERNAL_PRIMERS %> >
      Order internal primers</font></td>
  </tr>
 <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_PROCESS_OLIGO_PLATE %> >
      Process oligo plates<P></font></td>
  </tr>
 
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_FIND_GAPS %> >
      Run Gap Mapper (settings required)</font></td>
  </tr>
<%}%>
<tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF"> 
      Evaluate Clones</font></b></td>
</tr>

<% if (! is_eval_version)
{%>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS  %>>
      Run assembly wrapper</font></td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE  %> >
      Submit assembled sequences</font></td>
  </tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE  %> >
      Find low quality regions in clone sequences (settings required)</font></td>
  </tr>
<%}%>
<TR><TD COlspan=2 bgcolor="#DCE8FC" >&nbsp;</TD></TR>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DISCREPANCY_FINDER %> >
      Run discrepancy finder</font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  <%if ( user_level  < 3) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_RUNPOLYMORPHISM_FINDER %> >
      Run polymorphism finder (settings required)</font> </td>
  </tr>
  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level < 2) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DECISION_TOOL%>>
      Run decision tool (settings required) </font> <P></td>
  </tr>

  <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level < 2) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DECISION_TOOL_NEW%>>
      Run <i> new </i> decision tool  (settings required) </font> <P></td>
  </tr>
 <tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level < 2) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_NOMATCH_REPORT%>>
      Run report for clone that fail to match to the reference sequence</font> 
      <P></td>
  </tr>

<tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">View Options</font></b></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input  type="radio" name="forwardName" value=<%= Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY %>>
      View end reads availability</font></td>
</tr>


<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_INTERNAL_PRIMERS  %> >
      View internal primers </font></td>
  </tr>
  
 <% if (! is_eval_version  )
//edu.harvard.med.hip.utility.ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION)
{%>


<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID %> >
      View oligo order(s) for Clone<P></font></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_OLIGO_PLATE %> >
      View oligo plate<P></font></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input   type="radio" name="forwardName" value=<%= Constants.STRETCH_COLLECTION_REPORT_ALL_INT  %> >
      View all contig collections       </font></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input   type="radio" name="forwardName" value=<%= Constants.STRETCH_COLLECTION_REPORT_INT  %> >
      View contigs       <P></font></td>
</tr>

<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
    <input type="radio" name="forwardName" value=<%= Constants.LQR_COLLECTION_REPORT_INT%>>
    View Low Quality Regions for clone sequences </font> <P></td>
</tr>

<tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Stand alone processes</font></b></td>
</tr>
<tr> 
        <td width="100%" height="29" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE %> >
      Run Discrepancy Finder on a set of sequences<br>
      </font> <font color="#ECECFF">a</font></td>
</tr>
<%}%>
<tr> 
    <td width="100%" height="25" bgcolor="#1145A6"> <b><font color="#FFFFFF">Delete Options</font></b></td>
</tr>

<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_PLATE %>>
      Delete plate</font></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3){%>disabled <%}%>  type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_CLONE_READS %>>
      Delete clone end reads (forward and reverse)</font></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3){%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_CLONE_FORWARD_READ  %>>
      Delete clone forward end reads</font></td>
</tr><tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_CLONE_REVERSE_READ  %>>
      Delete clone reverse end reads</font></td>
</tr><tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_CLONE_SEQUENCE %>>
      Delete clone sequence</font><P></td>
</tr>
<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input type="radio" name="forwardName" value=<%= Constants.PROCESS_GET_TRACE_FILE_NAMES  %>>
      Get trace files' names</font></td>
</tr>

<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3) {%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_MOVE_TRACE_FILES %> >
      Move trace files from clone directory into temporary directory</font></td>
</tr> 

<tr> 
    <td width="100%" height="25" bgcolor="#DCE8FC"> <font color="#000080">&nbsp; 
      <input <%if ( user_level  < 3){%>disabled <%}%> type="radio" name="forwardName" value=<%= Constants.PROCESS_DELETE_TRACE_FILES %> >
      Delete trace files from hard drive</font></td>
</tr> 
<tr> 
    <td> <br>
      <b></b> <div align="center"> 
      <input type="submit" value="continue" name="submit">    <br></div>
    </td>
</tr>

</table>


</html:form> 
</body>
</html>


