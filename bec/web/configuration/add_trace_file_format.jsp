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
<title>add trace file format</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>

<body>
<table width="74%" border="0" align="center">
<tr><td> Example trace file name (extension REQUIRED): </td>
  <td><input type='text' name='EXAMPLE_TRACE_FILE_NAME' size='50' value=''>     </td>
  </tr>
  <tr> 
    <td width="50%">Format Name:</td>
    <td ><input type='text' name='FORMATNAME' size='50' value=''></td>
  </tr>
  <tr> 
    <td>Trace file name reading direction:</td>
    <td><select name='READING_DIRECTION' >
        <option value='<%= TraceFileNameFormat.READING_LEFT_TO_RIGHT%>'>Left to right 
       <option value='<%= TraceFileNameFormat.READING_RIGHT_TO_LEFT%>'>Right to         left</select></td>
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
            <td width="52%"><div align="left"> 
                <input type='text' name='PLATE_SEPARATOR' size='10' value=''>
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> 
                <input type='text' name='PLATE_LABEL_COLUMN' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> 
                <input type='text' name='PLATE_LABEL_START' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> 
                <input type='text' name='PLATE_LABEL_LENGTH' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
        </table>
      </div></td>
    <td><div align="center"><strong>Well</strong></div>
      <div align="center"> 
        <table width="84%" border="0">
          <tr> 
            <td width="48%"><div align="left">Separator:</div></td>
            <td width="52%"><div align="left">
                <input type='text' name='POSITION_SEPARATOR' size='10' value=''>
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> 
                <input type='text' name='POSITION_COLUMN' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> 
                <input type='text' name='POSITION_START' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> 
                <input type='text' name='POSITION_LENGTH' size='10' value='-1'onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
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
            <td width="52%"><div align="left"> 
                <input   type='text' name='DIRECTION_SEPARATOR' size='10' value=''>
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Column Number:</div></td>
            <td><div align="left"> 
                <input  type='text' name='DIRECTION_COLUMN' size='10' value='-1' onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Start:</div></td>
            <td><div align="left"> 
                <input  type='text' name='DIRECTION_START' size='10' value='-1' onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td><div align="left">Length:</div></td>
            <td><div align="left"> 
                <input type='text' name='DIRECTION_LENGTH' size='10' value='-1' onBlur= "checkNumeric(this,-1,100,'','',''); ">
              </div></td>
          </tr>
          <tr> 
            <td>Forward Definition:</td>
            <td><input type='text' name='DIRECTION_FORWARD' size='10' value='F'></td>
          </tr>
          <tr> 
            <td>Reverse Definition:</td>
            <td><input type='text' name='DIRECTION_REVERSE' size='10' value='R'></td>
          </tr>
        </table>
      </div></td>
  </tr>



</body>
</html>
