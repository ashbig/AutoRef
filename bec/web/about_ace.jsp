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
                    <td width="193%"> <p><span style="font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA"><span style="mso-spacerun: yes; font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA"><strong>&nbsp;Automated 
                        Clone Evaluation (ACE) </strong>system is a comprehensive, 
                        multi-platform, web-based plasmid sequence verification 
                        software package. ACE automates all steps of the sequence 
                        verification process: primer design, sequence contig assembly, 
                        identification and annotation of discrepancies between 
                        the clone and reference sequences, gap mapping, polymorphism 
                        detection, and assignment of final clone quality based 
                        on user defined criteria. </span></span></p>
                      <p>The software dramatically reduced the amount of time 
                        and labor required to evaluate clone sequences and decreased 
                        the number of missed sequence discrepancies, which commonly 
                        occur during manual evaluation. ACE maintains a relational 
                        database to store information about clones at various 
                        completion stages, project processing parameters and acceptance 
                        criteria. ACE also helps to reduce the number of sequencing 
                        reads needed to achieve adequate coverage for making decisions 
                        on clones.</p>
                      <p><br>
                      </p></tr>
                  <tr> 
                    <td><P> 
                      <p><font color="#000000"><span style="mso-spacerun: yes; font-family: Times New Roman; mso-fareast-font-family: Times New Roman; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA">If 
                        you have a question, please contact <a href="mailto:<%= BecProperties.getInstance().getACEEmailAddress() %>">ACE 
                        informatics team</a>.</span></font></td>
                  </tr>
                </table>
                <br><br>
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
