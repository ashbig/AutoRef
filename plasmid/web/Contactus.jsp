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
    <td width="61%" valign="top" class="featuretext"> 
      <p class="underbullet">If you have questions about storing, receiving, or searching 
        for clones that are not answered in the FAQs, please <a href="http://dnaseq.med.harvard.edu/contact_us.htm">click 
        here</a> to contact Stephanie Mohr.</p>
      <p class="underbullet">PlasmID was created and is maintained by the staff of the 
        <a href="http://www.dfhcc.harvard.edu" target="_blank">Dana Farber/Harvard 
        Cancer Center</a> (DF/HCC) DNA Resource Core, hosted at the <a href="http://www.hip.harvard.edu" target="_blank">Harvard 
        Institute of Proteomics</a> and located at the Broad Institute.</p>
      <p class="underbullet">To learn more about the DF/HCC and other core facilities 
        at the DF/HCC, <a href="http://dfhcc.harvard.edu" target="_blank">click 
        here</a>.</span></p>
      <p class="underbullet">To learn more about the DNA Resource Core, including our 
        DNA sequencing services, <a href="http://dnaseq.med.harvard.edu">click 
        here</a>.</span></p>
            </span> </p>
      <p class="underbullet">Comments, suggestions and all other questions, please contact <a href="mailto:dongmei_zuo@hms.harvard.edu">PlasmID support</a>.</p>
</td>
    <td width="39%"><img src="Biobank_small.jpg" width="311" height="466"></td>
  </tr>
</table>
        </TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

