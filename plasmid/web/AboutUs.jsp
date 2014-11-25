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
<jsp:include page="signinMenuBar.jsp" />

<p class="mainbodytexthead">About Us</p>
<p class="mainbodytext">
    The PlasmID Repository is one service of the <a href="http://dnaseq.med.harvard.edu/index.html">DNA Resource Core</a> at <a href="http://hms.harvard.edu/">Harvard Medical School</a>. Our Core facility is part of the largest comprehensive cancer center in the world; the <a href="http://www.dfhcc.harvard.edu/">DF/HCC</a>, bringing together the cancer research efforts of our seven member institutions: <a href="http://www.bidmc.org/">Beth Israel Deaconess Medical Center</a>, <a href="http://www.childrenshospital.org/">Boston Children's Hospital</a>, <a href="http://www.brighamandwomens.org/">Brigham and Women's Hospital</a>, <a href="http://www.dana-farber.org/">Dana-Farber Cancer Institute</a>, <a href="http://hms.harvard.edu/">Harvard Medical School</a>, <a href="http://www.hsph.harvard.edu/">Harvard School of Public Health</a>, and <a href="http://www.massgeneral.org/">Massachusetts General Hospital</a>.</p>
<p class="mainbodytext">
Funded by a grant from the National Cancer Institute, we have joined these seven renowned Harvard-affiliated medical centers into one collective force dedicated to the fight against cancer. Based in Boston, DF/HCC consists of more than 1,000 researchers with a singular goal -- to find new and innovative ways to combat cancer.</p>
<p class="mainbodytext">
<p class="mainbodytexthead">Our Mission</p>
<p class="mainbodytext">
    The mission of the DNA Resource Core is to provide researchers with inexpensive sequencing and plasmid services paired with comprehensive quality control and customer support.</p>
<p class="mainbodytexthead">Our History</p>
PlasmID was added to the DNA Resource Core in the spring of 2004 to reduce the burden on individual labs to store, maintain and distribute plasmid clones and supporting information. Currently, our repository has ~350,000 plasmids, including most human cDNAs as well as shRNA libraries. We distribute clones widely to the community using a <a href="Pricing.jsp">cost-recovery</a> mechanism.</p>
<p class="mainbodytext">
    In addition to our PlasmID Repository service the DF/HCC DNA Resource Core also provides DNA sequencing services. To learn more click <a href="http://dnaseq.med.harvard.edu/">here</a>. </p>
<p class="mainbodytexthead">Our Host</p>
<p class="mainbodytext">
    The core is hosted by the <a href="http://bcmp.med.harvard.edu/"> Department of Biological Chemistry and Molecular Pharmacology </a> and the <a href="https://harper.hms.harvard.edu/">Harper Lab</a> in the Department of Cell Biology at Harvard Medical School.</p>

  <jsp:include page="footer.jsp" />
</body>
</div>
</html>
