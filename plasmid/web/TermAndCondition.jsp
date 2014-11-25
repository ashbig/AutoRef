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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="homeTitle.jsp" />

<table width="100%" border="0">
    <tr></tr>
  <tr>
    <td>
        <table width="80%" height="30" border="2" align="center" bgcolor="#EFEFEF">
            <tr>
                <td width="80%"><h1 align="center">Terms & Conditions / Material Transfer Agreements (MTA)</h1></td>
            </tr>
        </table>
    </td>
  </tr>
    <tr></tr>
  <tr>
    <td>
        <table width="80%" align="center" border="1" bgcolor="#EFEFEF">
            <tr class="mainbodytext">
                <td width="60%"><i>Material Transfer Agreement</i></td>
                <td width="10%"><i>Pdf File</i></td>
                <td width="10%"><i>Word .doc</i></td>
            </tr>
            <tr class="mainbodytext">
                <td width="60%"><b>Standard Plasmid Transfer Agreement:</b> This generic MTA is used to govern the transfer of the majority of clones in the PlasmID Repository.</td>
                <td width="10%"><a href="StandardPlasmidTransferAgreementRev03-10-10.pdf">Download</a></td>
                <td width="10%"><a href="StandardPlasmidTransferAgreementRev03-10-10.doc">Download</a></td>
            </tr>
            <tr class="mainbodytext">
                <td width="60%"><b>Expedited Process Agreement:</b> In conjunction with our Standard Plasmid Transfer Agreement this document will allow your institution to join our Expedited MTA Network. Members of this network agree to the terms of our Standard Plasmid Transfer Agreement on a continual basis and therefore do not need to obtain a signed MTA for each order. Instead the institution will receive a yearly report containing a list of clones and researchers who received materials from our repository in the previous year. </td>
                <td width="10%"><a href="ExpeditedProcessAgreement.pdf">Download</a></td>
                <td width="10%"><a href="ExpeditedProcessAgreement.doc">Download</a></td>
            </tr>
            <tr class="mainbodytext">
                <td width="60%"><b>FLEXGene:</b> This MTA is used to transfer clones made by the Harvard Institute of Proteomics to Non-profit or academic research laboratories. </td>
                <td width="10%"><a href="FLEX_MTA_signatures.pdf">Download</a></td>
                <td width="10%"><a href="FLEX_MTA_signatures.doc">Download</a></td>
            </tr>
            <tr class="mainbodytext">
                <td width="60%"><b>FLEXGene_Co:</b> This MTA is used to transfer clones made by the Harvard Institute of Proteomics to companies.</td>
                <td width="10%"><a href="FlexGENEMTA_co_use.pdf">Download</a></td>
                <td width="10%"><a href="FlexGENEMTA_co_use.doc">Download</a></td>
            </tr>
            <tr class="mainbodytext">
                <td width="60%"><b>Tsien:</b> This MTA is used for select clones provided by Dr. Tsein's laboratory. </td>
                <td width="10%"><a href="tsienMTA.pdf">Download</a></td>
                <td width="10%"><a href="tsienMTA.doc">Download</a></td>
            </tr>
        </table>        
    </td>
  </tr>
    <tr></tr>
  <tr>
    <td>
        <table width="80%" border="2" align="center" bgcolor="#EFEFEF">
            <tr>
                <td width="80%" class="mainbodytext"><i>To expedite orders please feel free to email completed MTAs to plasmidMTA@hms.harvard.edu and then mail the original signed document to:</i> <br>
                    Glenn Beeman<br>
                    C2 Room 204<br> 
                    240 Longwood Ave<br> 
                    Boston, MA 02115
                </td>
            </tr>
        </table>
    </td>
  </tr>
</table>
         
<jsp:include page="footer.jsp" /></body>
</div>
</html>

