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
<link href="layout.css" rel="stylesheet" type="text/css" />
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
</head>
<div class="gridContainer clearfix">
<body>
<jsp:include page="contactTitle.jsp" />
<!--
old pic of biobank
<div class="aside">
    <img src="biobank_arm_no_effect.gif" width="250" height="396"> 
            </div>-->
<div class="content">
<!--<p class="mainbodytexthead">PlasmID Contact Information</p>

      <p class="mainbodytext">The <a href="http://dnaseq.med.harvard.edu/" title="DNA Resource Core" target="_blank">DF/HCC 
        DNA Resource Core</a> provides DNA sequencing services in addition to 
        plasmid repository services. </p>
      <p class="mainbodytext">The core is hosted by the Department of Biological Chemistry and Molecular Pharmacology 
        and the Department of Cell Biology
        at <a href="http://hms.harvard.edu/hms/home.asp" title="Harvard Medical School" target="_blank">Harvard 
        Medical School</a>.</p>
      <p class="mainbodytext">We are located  in the Seeley G. Mudd building at the Harvard Medical School in Boston.</p>
     -->
 <p class="mainbodytexthead">Contact Info:</p>
 
 <p class="mainbodytext"><u>Mailing address:</u><br>
        C2 Room 204<br>
        240 Longwood Ave<br>
        Boston, MA 02115</p>
        <p class="mainbodytext"><u>Remit Payment To:</u><br>
          Harvard Medical School<br>
          Dept. BCMP, C1-214<br>
          240 Longwood Ave.<br>
          Boston, MA 02115</p>
 

          <p class="mainbodytext">
          <u>Email</u><br>
          <a href="mailto:plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>: General Help Email<br>
           <a href="mailto:plasmidMTA@hms.harvard.edu">plasmidMTA@hms.harvard.edu</a>: Questions about Material Transfer Agreements (MTAs)<br>
          <a href='dnaresource_finance@hms.harvard.edu'>dnaresource_finance@hms.harvard.edu</a>: Payment or Financial Questions</a></p>
          
          <p class="mainbodytext">
          <u>Phone</u><br>
          Lab: 617-432-7708</p>

          </div>
            
 
  
<jsp:include page="footer.jsp" /></body></div>
</html>

