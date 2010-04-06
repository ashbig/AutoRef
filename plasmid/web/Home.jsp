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
<jsp:include page="homeTitle.jsp" /><table width="973" height="538" border="0">

  <tr>
    <td height="300"><table width="800" height="259" border="0" align="center" bgcolor="#EFEFEF" id="form">
      <tr>
        <td width="800" height="112">
            <p align="left"><span class="mainbodytext">The Mission of the DNA Resource CORE is to provide member in the DF/HCC, Harvard Affiliates, and outside researchers with quality and inexpensive <a href="http://dnaseq.med.harvard.edu/">DNA sequencing services</a> and <a href="http://plasmid.med.harvard.edu/PLASMID/Home.jsp">sequence verified plasmid constructs</a>, paired with comprehensive quality control and customer support.&nbsp;        
        </span>
        <hr>
        <p align="left" class="mainbodytext"><strong>PLASMID SEARCH:</strong>  Go to <a href="OrderOverview.jsp">Search & Order</a>
          or find a list of our large collections <a href="collection_overview.jsp" target="_blank">here</a>. </p>
        <p align="left" class="mainbodytext"><strong>PLASMID COST:</strong> Click <a href="http://plasmid.med.harvard.edu/PLASMID/OrderOverview.jsp#cost">here</a> for plasmid and shipping costs.</p>
        <p align="left" class="mainbodytext"><strong class="mainbodytext">DEPOSITING CLONES: </strong>Go to <a href="Submission.jsp">Plasmid Submission</a> or see a <a href="http://psimr.asu.edu/overview.html" target="_blank">slide show</a> describing the process.</p>
        <hr>
        <p align="left" class="mainbodytext">Click <a href="http://psimr.asu.edu/MTA.html" target="_blank">here</a> for the <strong class="mainbodytext">terms and conditions</strong> for receiving plasmids from the repository.</p>
        <p align="left" class="mainbodytext"> Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the <strong>cloning methods</strong> used to create many of the  plasmids           </p>        </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="800" border="5" align="center" bordercolor="#999999" id="gray">
      <tr>
        <td width="800" height="180"><h1 align="center">What's New? </h1>
        <ul type= circles>  
            <li class="mainbodytext">10/14/09 - PSI Clones have migrated to our sister site at <a href="http://dnasu.asu.edu/DNASU/" target="_blank">DNASU</a>.</li>
            <li class="mainbodytext">10/13/09 - Platinum Clones are now available! Please use this service to request additional QC data from your specific order.</li>
            <li class="mainbodytext">07/24/09 - Kinase shRNA hairpins Now Available! Browse collection <a href="http://plasmid.med.harvard.edu/PLASMID/GetCollection.do?collectionName=shRNA Kinase Collection (pLKO.1)">here</a>.</li>
          <li class="mainbodytext">07/01/09 - DF/HCC DNA Resource CORE under new leadership. Learn more about the CORE Director <a href="http://www.hms.harvard.edu/dms/bbs/fac/harlow.html">Dr. Edward Harlow</a>, CORE Operations Director <a href="http://www.hip.harvard.edu/Bios/Chan.html">Ms Li Chan</a>, and PlasmID Coordinator <a href="http://www.hip.harvard.edu/Bios/Beeman.html">Mr. Glenn Beeman</a>. </li>
          <li class="mainbodytext"> 02/15/09 - No charge for pick up orders. Orders will be available in second floor hallway freezer of the Seeley G. Mudd Building.<br>
                *You MUST have access to the building and select this option at check-out to take advantage of this option. </li> 
          <li class="mainbodytext">01/01/09 - PlasmID ships internationally! Here's a <a href="http://www.batchgeocode.com/map/?i=bb69e015b8bfb20e06150bb05f4c95e3">map</a> of where the repository has shipped plasmids. </li>
          <li class="mainbodytext">01/01/09 - Search our collection using primary sequence or accession number with our new <a href="PrepareBlast.do">Blast search</a>. </li>
 
         </ul></td>
      </tr>
    </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="4">
          <tr align="left" valign="top" bgcolor="#6699cc" class="homepageText3"> 
            <td width="12%" class="homeMainText"> DF/HCC Member Institutions:</td>
            <td width="12%" class="homeMainText"><a href="http://www.bidmc.harvard.edu/sites/bidmc/home.asp" title="Beth Israel Deaconess Medical Center" target="_blank">Beth 
              Israel Deaconess Medical Center</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.brighamandwomens.org/" title="Brigham & Women's Hospital" target="_blank">Brigham 
              & Women's Hospital</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.childrenshospital.org/" title="Children's Hospital Boston" target="_blank">Children's 
              Hospital Boston</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.dfci.harvard.edu/" title="Dana Farber Cancer Institute" target="_blank">Dana 
              Farber Cancer Institute</a></td>
            <td width="12%" class="homeMainText"><a href="http://hms.harvard.edu/hms/home.asp" title="Harvard Medical School" target="_blank">Harvard 
              Medical School</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.hsph.harvard.edu/" title="Harvard School of Public Health" target="_blank">Harvard 
              School of Public Health</a></td>
            <td width="12%"><a href="http://www.mgh.harvard.edu/" title="Massachusetts General Hospital" target="_blank" class="homeMainText">Massachusetts 
              General Hospital</a></td>
          </tr>
          <tr>
          <p align="center"><span class="mainbodytext">
          <a href="http://www.dfhcc.harvard.edu" target="_blank"><img src="DFHCC_NCI_logo.jpg" width="379"  height="46"></a>&nbsp;
          <a href="http://hms.harvard.edu" target="_blank"><img src="HMS_logo.jpg" width="66" height="63"></a></span>
          </tr>
        </table>
      </TD>
    </TR>
    <TR> 
      <TD width="100%"></TD>
    </TR>
    <TR>
      <TD width="100%" align="left" valign="top"> 
        <div align="center" class="homeMainText">PlasmID was created and is maintained 
          by the <a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC 
          DNA Resource Core</a></div></TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

