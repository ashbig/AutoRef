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
</head>

<body>
<jsp:include page="submissionTitle.jsp" />
<TABLE borderColor=#ffffff width=1000 align=center 
      bgColor=#ffffff border=0>
  <TBODY>
    <TR> 
      <TD align="left" valign="top"> <p class="homepageText3">What You Do ...</p>
        <ul>
          <li class="homepageText3">Provide Plasmids as DNA</li>
          <li class="homepageText3">Provide Supporting Information</li>
          <li class="homepageText3">Contact us to arrange drop-off or pick-up</li>
          <li class="homepageText3">Forward Requests by e-mail or refer requestors 
            directly to PlasmID</li>
        </ul></TD>
      <TD align="left" valign="top"> <span class="homepageText3">... What We Do 
        </span> <div align="left"> 
          <ul>
            <li class="homepageText3">Transform DNA into a phage-resistant host 
              strain</li>
            <li class="homepageText3">Create working and archival glycerol stocks</li>
            <li class="homepageText3">Store stocks in our automated freezer storage 
              system</li>
            <li class="homepageText3">Curate information to be added to PlasmID</li>
            <li class="homepageText3">Handle MTAs, obtain institutional permissions, 
              and accomodate restrictions</li>
            <li class="homepageText3">Distribute the Plasmids to Academic and 
              Non-Profit labs who request them (U.S. and abroad)</li>
          </ul>
        </div></TD>
    </TR>
    <TR align="left" valign="top" class="homepageText3"> 
      <TD width="50%" bordercolor="#999999"> <div align="left">THE OLD WAY ...</div></TD>
      <TD width="50%" height="20"> <div align="left">... THE NEW WAY</div></TD>
    </TR>
    <TR align="left" valign="top"> 
      <TD><img src="file://///Hipfs/hipdata/smohr/PlasmID%20November%202005%20Update/oldway.gif" width="466" height="195"></TD>
      <TD><img src="file://///Hipfs/hipdata/smohr/PlasmID%20November%202005%20Update/newway.gif" width="467" height="194"></TD>
    </TR>
    <TR> 
      <TD height="99" colspan="2"> <p align="center" class="homeMainText">If you 
          would like to share clones with the repository, please contact Stephanie 
          Mohr <a 
            href="mailto:stephanie_mohr@hms.harvard.edu">by email</a> or by phone 
          617-324-4251. </p>
        <p class="homeMainText">Useful Downloads for Plasmid Submission<br>
<ul>
  <li class="underbullet"><a target="_blank" href="MQF.pdf">Material Qualification Form</a></li>
  <li class="underbullet"><a target="_blank" href="PIT.pdf">Plasmid Intake Template</a></li>
</ul></p>
        <p></p></TD>
    </TR>
  </TBODY>
</TABLE>
<jsp:include page="footer.jsp" /></body>
</html>

