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
<jsp:include page="contactTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
<TBODY>
    <TR> 
        <TD width="100%">
<p class="homeMainText">PlasmID Contact Information</p>
<table width="100%" border="0">
  <tr> 
    <td width="58%" height="444" valign="top"> 
      <p class="mainbodytextlarge">The <a href="http://dnaseq.med.harvard.edu/" title="DNA Resource Core" target="_blank">DF/HCC 
        DNA Resource Core</a> provides DNA sequencing services in addition to 
        plasmid repository services. </p>
      <p class="mainbodytextlarge">The core is hosted by the Harvard Institute of 
        Proteomics (<a href="http://www.hip.harvard.edu/" title="Harvard Institute of Proteomics" target="_blank">HIP</a>) 
        at <a href="http://hms.harvard.edu/hms/home.asp" title="Harvard Medical School" target="_blank">Harvard 
        Medical School</a>.</p>
      <p class="mainbodytextlarge">We are located in Kendall Square, Cambridge.</p>
      <blockquote>
        <p class="mainbodytextlarge"><strong>Mailing address:</strong><br>
          320 Charles St.<br>
          Cambridge, MA 02141</p>
        <p class="mainbodytextlarge"><strong>Our billing (for your PO) is handled 
          by:</strong><br>
           DF/HCC DNA Resource Core<br>
          BCMP Department<br>
          240 Longwood Ave.<br>
          Boston, MA 02115</p>
          </blockquote>
          <p class="mainbodytextlarge"><strong>Contact Info:</strong><br>
          Questions about PlasmID? Please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.<br>
          Questions about Material Transfer Agreements (MTAs)? Contact <a href="mailto:plasmidMTA@hms.harvard.edu">MTA help</a>.</p>
          </td>
    <td width="42%" align="center" valign="middle"><img src="biobank_arm_no_effect.gif" width="250" height="396"> 
      <p class="homepageLink">&nbsp;</p></td>
  </tr>
</table>
        </TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

