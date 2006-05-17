<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/my_template.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceParam name="OptionalRegion1" type="boolean" value="true" -->
</head>

<body>
<table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html" %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp" %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
<jsp:include page="page_left_menu.jsp" /></td>
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- InstanceBeginEditable name="EditRegion1" -->
                   
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="90%">
    
    <tr>
      <td width="100%" colspan="2">
        <p><span style="font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA"><span style="mso-spacerun: yes; font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA">&nbsp;</span></span></p>
          <p align="left"><a><font size="3" color="#000080">With ACE, users can perform            the following steps:</font></a></p>
        </tr>
            <tr>
              <td width="7%" bgColor="#b8c6ed">
                <p align="center">1</p>
              </td>
                <td width="93%" bgColor="#b8c6ed"><font color="#000080" size="3">Transfer
                  plate inforamation from FLEX into ACE</font></td>
            </tr>
            <tr>
              <td width="7%" bgColor="#e4e9f8">
                <p align="center">2</p>
              </td>
                <td width="93%" bgColor="#e4e9f8"><font color="#000080">Manipulate end reads and rank isolates.</font></td>
            </tr>
            <tr>
              <td width="7%" bgColor="#b8c6ed">
                <p align="center">3</p>
              </td>
                <td width="93%" bgColor="#b8c6ed"><font color="#000080">Design internal primers</font></td>
            </tr>
            <tr>
              <td width="7%" bgColor="#e4e9f8">
                <p align="center">4</p>
              </td>
                <td width="93%" bgColor="#e4e9f8"><font color="#000080">Evaluate clones</font></td>
            </tr>
            <tr>
              <td width="7%" bgColor="#b8c6ed">
                <p align="center">5</p>
              </td>
                <td width="93%" bgColor="#b8c6ed"><font color="#000080">View alignments and quality reports</font></td>
            </tr>
            <tr>
              <td width="7%" bgColor="#e4e9f8">
                <p align="center">6</p>
              </td>
                <td width="93%" bgColor="#e4e9f8"><font color="#000080">Delete wrong data</font></td>
            </tr>
          <tr><td colspan=2><P>
          <p><font color="#000000"><span style="mso-spacerun: yes; font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA">If
            you have a question, please contact <a href="mailto:<%= BecProperties.getInstance().getACEEmailAddress() %>">ACE
            informatics team</a>.</span></font></td>    </tr>
  </table><br><br>
  </center>
</div>
            <!-- InstanceEndEditable --></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>
