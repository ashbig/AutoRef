<!--Copyright 2003-2005, 2006 President and Fellows of Harvard College. All Rights Reserved -->
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>

<html>
<head>
<title>verify trace file format</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

</head>

<body> 


<p>
<br>

<input name="FORMATNAME" type="hidden" value="<%= request.getAttribute("FORMATNAME")%>" > 
<input name="READING_DIRECTION"   type="hidden" value="<%= request.getAttribute("READING_DIRECTION")%>" > 
<input name="PLATE_SEPARATOR" type="hidden" value="<%= request.getAttribute("PLATE_SEPARATOR")%>" >  
<input name="POSITION_SEPARATOR" type="hidden" value="<%= request.getAttribute("POSITION_SEPARATOR")%>" > 
<input name="DIRECTION_SEPARATOR" type="hidden" value="<%= request.getAttribute("DIRECTION_SEPARATOR")%>" > 
<input name="PLATE_LABEL_COLUMN" type="hidden" value="<%= request.getAttribute("PLATE_LABEL_COLUMN")%>" > 
<input name="PLATE_LABEL_START" type="hidden" value="<%= request.getAttribute("PLATE_LABEL_START")%>" >     
<input name="PLATE_LABEL_LENGTH" type="hidden" value="<%= request.getAttribute("PLATE_LABEL_LENGTH")%>" >    
<input name="POSITION_COLUMN" type="hidden" value="<%= request.getAttribute("POSITION_COLUMN")%>" >    
<input name="POSITION_START" type="hidden" value="<%= request.getAttribute("POSITION_START")%>" > 
<input name="POSITION_LENGTH" type="hidden" value="<%= request.getAttribute("POSITION_LENGTH")%>" >    
<input name="DIRECTION_FORWARD" type="hidden" value="<%= request.getAttribute("DIRECTION_FORWARD")%>" >   
<input name="DIRECTION_REVERSE" type="hidden" value="<%= request.getAttribute("DIRECTION_REVERSE")%>" >    
<input name="DIRECTION_COLUMN" type="hidden" value="<%= request.getAttribute("DIRECTION_COLUMN")%>" >    
<input name="DIRECTION_LENGTH" type="hidden" value="<%= request.getAttribute("DIRECTION_LENGTH")%>" >  
<input name="DIRECTION_START" type="hidden" value="<%= request.getAttribute("DIRECTION_START")%>" >    
<input name="EXAMPLE_TRACE_FILE_NAME" type="hidden" value="<%= request.getAttribute("EXAMPLE_TRACE_FILE_NAME")%>" > 




<table width="74%" border="0" align="center">
<tr><td>Trace file name: </td>
  <td><%= request.getAttribute("EXAMPLE_TRACE_FILE_NAME")%>     </td>
</tr>
<%


ArrayList arr = (ArrayList) request.getAttribute("EXAMPLE_FILE_NAME_RESULT");
 if ( arr != null)
   {
       %><tr><td colspan='2' align='center'>was parsed as follows:</td></tr>  <%
String[] item = null;
for (int count = 0; count < arr.size(); count++)
{
item = (String[]) arr.get(count);
%>
    <tr><td><%= item[0]%></td><td><% if (item[1]== null){%> none <%}else{%> <%= item[1] %> <%}%></td></tr>
<%}}else{%> 
<tr><td><hr><i><font color='red'>Error in format definition: <%= request.getAttribute("ERROR_MESSAGE")%></font></error></td></tr>
<%}%>
           
           
<tr><td colspan='2' ><div align="center"><hr>Format Description</div></td></tr>
<tr> 
    <td width="50%">Format Name:</td>
    <td ><%= request.getAttribute("FORMATNAME")%></td>
  </tr>
  <tr> 
    <td>Trace file name reading direction:</td>
    <td><%= request.getAttribute("READING_DIRECTION")%></td>

  </tr>
  <tr> 
    <td colspan="2"><hr></td>
  </tr>
  <tr> 
    <td width="50%"><div align="center"><strong>Plate Label</strong></div>
      <div align="center"> 
        <table width="84%" border="0">
          <tr> 
            <td width="48%"><div align="left">Separator:</div></td>
            <td width="52%"><div align="left">                <%= request.getAttribute("PLATE_SEPARATOR")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> <%= request.getAttribute("PLATE_LABEL_COLUMN")%> </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> <%= request.getAttribute("PLATE_LABEL_START")%></div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> <%= request.getAttribute("PLATE_LABEL_LENGTH")%>              </div></td>
          </tr>
        </table>
      </div></td>
    <td><div align="center"><strong>Well</strong></div>
      <div align="center"> 
        <table width="84%" border="0">
          <tr> 
            <td width="48%"><div align="left">Separator:</div></td>
            <td width="52%"><div align="left"><%= request.getAttribute("POSITION_SEPARATOR")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> <%= request.getAttribute("POSITION_COLUMN")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> <%= request.getAttribute("POSITION_START")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> <%= request.getAttribute("POSITION_LENGTH")%>              </div></td>
          </tr>
        </table>
      </div></td>
  </tr>
  <tr> 
    <td colspan="2"> <p align="center"><strong>Read Direction</strong></p>
      <div align="center"> 
        <table width="54%" border="0">
          <tr> 
            <td width="48%"><div align="left">Separator:</div></td>
            <td width="52%"><div align="left"> <%= request.getAttribute("DIRECTION_SEPARATOR")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> <%= request.getAttribute("DIRECTION_COLUMN")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> <%= request.getAttribute("DIRECTION_START")%>              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> <%= request.getAttribute("DIRECTION_LENGTH")%>              </div></td>
          </tr>
          <tr> 
            <td>Forward Definition:</td>
            <td><%= request.getAttribute("DIRECTION_FORWARD")%></td>
          </tr>
          <tr> 
            <td>Reverse Definition:</td>
            <td><%= request.getAttribute("DIRECTION_REVERSE")%></td>
          </tr>
        </table>
      </div></td>
  </tr>
</table>



</body>
</html>
