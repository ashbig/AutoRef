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

<title>view results</title>

<link href="application_styles.css" rel="stylesheet" type="text/css">
</head>

<body >

<div align="center">
  <center> <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr> <td  ><html:errors/></td> </tr> </table>
  </center></div>


<html:form action="/SelectProcess.do" > 
<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
  
  <tr class='headerRow'> 
    <td  height="25" > View options</td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input  type="radio" name="forwardName" CHECKED  value=<%= Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY %>>
      View availabile end reads  </td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_INTERNAL_PRIMERS  %> >
      View internal primers </td>
  </tr>
  
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID %> >
      View oligo order(s) for clone(s)  </td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input   type="radio" name="forwardName" value=<%= Constants.PROCESS_VIEW_OLIGO_PLATE %> >
      View oligo plate(s)</td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input   type="radio" name="forwardName" value=<%= Constants.STRETCH_COLLECTION_REPORT_ALL_INT  %> >
      View all contig collections </td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input   type="radio" name="forwardName" value=<%= Constants.STRETCH_COLLECTION_REPORT_INT  %> >
      View latest contig collection 
      </td>
  </tr>
  <tr class='evenRowColoredFontNotBold'> 
    <td   height="25"  >&nbsp; <input type="radio" name="forwardName" value=<%= Constants.LQR_COLLECTION_REPORT_INT%>>
      View low confidence regions for clone sequences 
      <P></td>
  </tr>
 
  <tr > 
    <td> <br> <b></b> <div align="center"> 
        <input type="submit" value="Continue" name="submit">
        <br>
      </div></td>
  </tr>
</table>


</html:form> 
</body>
</html>


