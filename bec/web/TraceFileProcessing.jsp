<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>



<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.utility.*"%>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
 <script defer="defer" type="text/javascript"><!--
  
 /*Start of form validation:*/
  function validateForm(formElement)
   {
    //Check user name is at least 2 characters long

    var str =  trim(formElement.fileName.value);
    if (( formElement.fileName.value == null) || ( str == "" ) )
	{
		alert('Please submit valid file name.')
        formElement.fileName.focus()
        return false
   	}
   	return true
   }
   function trim(strText)
    { 
		// this will get rid of leading spaces 

		while (strText.substring(0,1) == ' ') 
			strText = strText.substring(1, strText.length); 

		// this will get rid of trailing spaces 
		while (strText.substring(strText.length-1,strText.length) == ' ') 
			strText = strText.substring(0, strText.length-1); 

	   return strText; 
	} 

 



	   /*End of functions.*/
//--></script>
    <title><bean:message key="bec.name"/> : Process trace files</title>
     <% 
String title = null;
if (request.getParameter(Constants.JSP_TITLE ) != null)
 { 
    title = (String)request.getParameter(  Constants.JSP_TITLE  );
 }
else if  (request.getAttribute(Constants.JSP_TITLE ) != null)
{
    title = (String)request.getAttribute(  Constants.JSP_TITLE  );
 }
Object forwardName = null;
if ( request.getAttribute("forwardName") != null)
{
        forwardName = request.getAttribute("forwardName") ;
}
else
{
        forwardName = request.getParameter("forwardName") ;
}
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();

%>
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td > <font color="#008000" size="5"><b>  <%= title %>
    <hr>  <p>  </td> </tr></table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>

 <form name ="TraceFileProcessing" action="TraceFileProcessing.do" METHOD=POST ENCTYPE="multipart/form-data" 
	onsubmit="return validateForm(this);">

   
  
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<% if ( forwardName_int == Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER)
{%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    
    <tr><td width="30%" ><b>Container labels:</td>
     <td> <textarea name="items"  rows="10"></textarea></td>  </tr>
           <tr><td  colspan=2>&nbsp;</td></tr>
           <tr><td><b>Please specify read direction </td>
<td><input type=radio name=read_direction value="<%= Constants.READ_DIRECTION_FORWARD %>" checked>Forward
<input type=radio name=read_direction value="<%= Constants.READ_DIRECTION_REVERSE %>">Reverse
</td></tr>
 <tr><td><b>Please specify read type </td>
<td><input type=radio name=read_type value="<%= Constants.READ_TYPE_ENDREAD_STR%>" checked>End Read
<input type=radio name=read_type value="<%= Constants.READ_TYPE_INTERNAL_STR%>" >Internal Read
</td></tr>
</table>
<%} 
else if ( forwardName_int == Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER)
{%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td>Please specify renaming file</td><td><input type="file" name="fileName" id="fileName" value="">		</td></tr>
<%if ( BecProperties.getInstance().isInDebugMode() ) 
{%>

<tr><td>Please specify directory where trace files are located</td><td><input type="text" name="inputdir" id="inputdir" value="">		</td></tr>
<tr><td>Please specify directory where renamed trace files must go</td><td><input type="text" name="outputdir" id="outputdir" value="">		</td></tr>
<%}%>
<tr><td>Delete trace files from input directory</td>
<td><input type="radio" name="delete"  value="YES">Yes
	<input type="radio" name="delete"  value="NO" checked>	No
</td></tr>

</table>
<%} 
else if ( forwardName_int == Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER)
{%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td><b>Please specify maping file(format sequencing facility plate name&nbsp; &nbsp;&nbsp;FLEX label)</td><td><input type="file" name="fileName" id="fileName" value="">		</td></tr>
<%if ( BecProperties.getInstance().isInDebugMode() ) 
{%>

<tr><td><b>Please specify directory where trace files are located</td><td><input type="text" name="inputdir" id="inputdir" value="">		</td></tr>
<%}%>
<tr><td><b>Please specify sequencing facility </td>
<td>
<table border='0'>
<tr><td>
<input type=radio name=sequencing_facility value=<%=SequencingFacilityFileName.SEQUENCING_FACILITY_BROAD%> >Broad Institute
</td></tr><tr><td><input type=radio name=sequencing_facility value=<%=SequencingFacilityFileName.SEQUENCING_FACILITY_HTMBC%> checked>HTMBC
</td></tr><tr><td><input type=radio name=sequencing_facility value=<%= SequencingFacilityFileName.SEQUENCING_FACILITY_AGENCORD%> >Agencourt 
</td></tr><tr><td><input type=radio name=sequencing_facility value=<%= SequencingFacilityFileName.SEQUENCING_FACILITY_KOLODNER%> >Kolodner 
</td></tr></table>
<tr><td>&nbsp;</td></tr>
</td></tr>
<tr><td><b>Please specify read type </td>
<td><input type=radio name=read_type value="<%= Constants.READ_TYPE_ENDREAD_STR%>" checked>End Read
<input type=radio name=read_type value="<%= Constants.READ_TYPE_INTERNAL_STR%>" >Internal Read
</td></tr>

</table>
<%} %>
<div align="center"> <P></P><input type="SUBMIT" name="Submit"></div>


</form>
</body>
</html>
