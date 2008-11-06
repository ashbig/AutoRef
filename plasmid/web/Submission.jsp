<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style5 {font-family: Arial, Helvetica, sans-serif}
.style6 {color: #000000; font-style : normal; font-weight : normal; font-size: 15px;}
.style7 {font-size: 14px}
-->
</style>
</head>

<body class="mainbodytext style5">
<jsp:include page="submissionTitle.jsp" />
<table width="673" border="0">
  <tr>
    <td><p class="homepageText2"><strong>Why submit your plasmids to our repository?</strong></p>
      <p class="mainbodytext">Sharing plasmids with the <a href="http://dnaseq.med.harvard.edu/">DF/HCC DNA Resource Core</a> benefits you  by alleviating the burden of storage, maintenance and distribution, and  benefits the general research community by making plasmids available from a  central source. 
        We encourage submission of  plasmids from researchers at any of the <a href="http://www.dfhcc.harvard.edu/">DF/HCC</a> parent institutions and from the research community at large.  Small and  large collections are equally welcome.</p>
      <p class="homepageText2"><strong> Submitting plasmids generally  involves the following three steps: </strong></p>
      <p class="mainbodytext">1) Gaining permission from your  institution to include the plasmids in the repository for distribution under  specific terms (the <a href="PSI_Depositor_Agreement_OCT2007_final_generic.pdf" target="_blank">Depositor Agreement</a>). NOTE that this permission has already  been granted by all seven <a href="http://www.dfhcc.harvard.edu/">DF/HCC</a> institutions as well as several other research institutes listed <a href="DA.html">here</a>. </p>
      <p class="mainbodytext">2)  Providing us with information about the plasmids, such as maps or sequence  files, growth conditions, relevant authors and publications, etc. so we are set  up to handle the plasmids when they arrive. We have <a href="#subguidlines">templates for submission</a> of  information that allow us to easily import your data into our database. </p>
      <p class="mainbodytext">3) Sumbit your samples. Please DO NOT submit samples until we confirm that i) your data is entered into our computers and ii) we are ready to receive your samples.</p>
      <p align="center" class="style6"><span class="homepageText2"><strong><a name="subguidlines" id="subguidlines"></a>Submission Guidelines</strong></span><br />
      </p>
      <pre class="mainbodytext">
We have standardized our submission process.  This  information is outlined in the following five files.  <br>
Please read through each of these  documents before you start.
<em>Last Updated: November 6, 2008</em></pre>
      <ul>
        <li><a href="Clone_Submission.xls" target="_blank">Clone_Submission.xls</a> This is an Excel spreadsheet containing two forms that allow you to enter the information about your vector and your clones (vector+insert).  The forms add your data into a spreadsheet that can be saved and sent back to me to be entered into the Plasmid Information Database (PlasmID) to be linked to your samples online. If you have a large number of samples, feel free to enter your plasmid information directly into the spreadsheet provided or <a href="Contacts.html">contact us</a> for alternate submission methods.</li>
        <br />
        <li><a href="File_Details_v2.doc" target="_blank">File_details</a> This document outlines the definition of the data we ask for in the excel forms. If there are elements here that do not work for you, additional information you would like to add or any questions, please contact me for further discussion.</li>
        <br />
        <li><a href="Submission checklist v4.doc">Submission Checklist</a> This document will guide you through the submission process.  The items in this checklist highlight a variety of problems we have frequently encountered and describe in detail how to prepare your data and samples. </li>
        </ul>
    </td>
  </tr>
</table>
<p class="mainbodytext">To get things started, please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.</p>

<jsp:include page="footer.jsp" /></body>
</html>

