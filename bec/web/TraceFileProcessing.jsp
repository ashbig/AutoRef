<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>
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
    <title> Process trace files</title>
     <% 

Object forwardName = ( request.getAttribute("forwardName") != null) ? 
     request.getAttribute("forwardName") :  request.getParameter("forwardName") ;

int forwardName_int = (forwardName instanceof String) ? 
 Integer.parseInt((String)forwardName) : ((Integer) forwardName).intValue();
System.out.println(forwardName_int);
%>
</head>
<body>


 <form name ="TraceFileProcessing" action="TraceFileProcessing.do" METHOD=POST ENCTYPE="multipart/form-data" 
	onsubmit="return validateForm(this);">

   
  
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 
<% if ( forwardName_int == Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER)
{%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    
    <tr><td width="30%" ><b>Plate labels:</td>
     <td> <textarea name="items"  rows="10"></textarea></td>  </tr>
           <tr><td  colspan=2>&nbsp;</td></tr>
           <tr><td>Please select read direction </td>
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
<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
<tr><td>Select the trace renaming file name</td><td><input type="file" name="fileName" id="fileName" value="">		</td></tr>
<%if ( BecProperties.getInstance().isInDebugMode() ) 
{%>

<tr><td>Please specify directory where trace files are located</td><td><input type="text" name="inputdir" id="inputdir" value="">		</td></tr>
<tr><td>Please specify directory where renamed trace files must go</td><td><input type="text" name="outputdir" id="outputdir" value="">		</td></tr>
<%}%>
<tr><td>Delete trace files from input directory after upload? </td>
<td><input type="radio" name="delete"  value="YES">Yes
	<input type="radio" name="delete"  value="NO" checked>	No
</td></tr>

</table>
<%} 
else if ( forwardName_int == Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER)
{%>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td>Please specify mapping file (format: sequencing facility plate name \t user plate label)</td><td><input type="file" name="fileName" id="fileName" value="">		</td></tr>
<%if ( BecProperties.getInstance().isInDebugMode() ) 
{%>

<tr><td>Please select directory where trace files are located</td><td><input type="text" name="inputdir" id="inputdir" value="">		</td></tr>
<%}%>
<tr><td>Please select trace files' naming format </td>
<td>
<table border='0'>

<% Hashtable formats =(Hashtable) DatabaseToApplicationDataLoader.getTraceFileFormats();
String format_name = null;
int item_count = 0;
for (Enumeration en = formats.keys(); en.hasMoreElements(); )
{
    format_name = (String)en.nextElement();
%>
 <tr><td><input type=radio name=sequencing_facility value="<%= format_name%>" <%if ( item_count ==0 ){%> checked <%}%>><%= format_name%> 

   <% item_count++;}%>

</table>
<tr><td>&nbsp;</td></tr>
</td></tr>
<tr><td><b>Please specify the read type </td>
<td><input type=radio name=read_type value="<%= Constants.READ_TYPE_ENDREAD_STR%>" checked>End Read
<input type=radio name=read_type value="<%= Constants.READ_TYPE_INTERNAL_STR%>" >Internal Read
</td></tr>

</table>
<%} %>
<div align="center"> <P></P>  <input type="submit" name="Submit" value="Submit"></div>


</form>

</td></tr></table>
</body>
</html>
