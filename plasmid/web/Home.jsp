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
        <td width="800" height="112"><p align="center"><span class="mainbodytext">Plasmid Information Database (PlasmID) is a central repository for plasmid clone collections and distribution as part of the DF/HCC DNA Resource Core based at the <a href="http://www.hip.harvard.edu/" target="_blank">Harvard Institute of Proteomics (HIP)</a>. We are also the <a href="http://www.hip.harvard.edu/PSIMR/index.htm" target="_blank">Protein Structure Initiative (PSI) Material Repository</a>.&nbsp;        
        </span>
        <p align="center"><span class="mainbodytext">
          <a href="http://hms.harvard.edu" target="_blank"><img src="HMS_logo.jpg" width="66" height="63"></a>&nbsp;<a href="http://www.hip.harvard.edu/PSIMR" target="_blank"><a href="http://www.hip.harvard.edu/PSIMR" target="_blank"><img src="psilogo.jpg" width="150" height="63"></a><a href="http://www.dfhcc.harvard.edu" target="_blank"><a href="http://www.dfhcc.harvard.edu" target="_blank"><img src="DFHCC_NCI_logo.jpg" width="379"  height="46"></a></span>
          <br><hr>
        <p align="center" class="mainbodytext"><strong>PLASMID SEARCH:</strong>  Go to <a href="OrderOverview.jsp">Plasmid Request</a>
          or find a list of our large collections <a href="collection_overview.jsp" target="_blank">here</a>. </p>
        <p align="center" class="mainbodytext"><strong>PLASMID COST:</strong> Click <a href="http://plasmid.med.harvard.edu/PLASMID/OrderOverview.jsp#cost">here</a> for plasmid and shipping costs.</p>
        <p align="center" class="mainbodytext"><strong class="mainbodytext">DEPOSITING CLONES: </strong>Go to <a href="Submission.jsp">Plasmid Submission</a> or see a <a href="http://www.hip.harvard.edu/PSIMR/overview.html" target="_blank">slide show</a> describing the process.</p>
        <hr>
        <p align="center" class="mainbodytext">Click <a href="http://www.hip.harvard.edu/PSIMR/MTA.html" target="_blank">here</a> for the <strong class="mainbodytext">terms and conditions</strong> for receiving plasmids from the repository.</p>
        <p align="center" class="mainbodytext"> Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the <strong>cloning methods</strong> used to create many of the  plasmids           </p>        </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="800" border="5" align="center" bordercolor="#999999" id="gray">
      <tr>
        <td width="800" height="180"><h1 align="center">What's New? </h1>
        <ul type= circles>
          <li class="mainbodytext">No charge for shipping for the Harvard Medical School community. Pick up your order in the Seeley G. Mudd Building in the <a href="http://www.hip.harvard.edu/Location.html#Quad" target="_blank">HMS Quad</a> in the second floor hallway freezer. You MUST have access to the building and select this option at check-out to take advantage of this option.</li>
            <li class="mainbodytext">Can't find the plasmid you're interested in? Search our collection using our new <a href="PrepareBlast.do">Blast search</a>.</li>
            <li class="mainbodytext">Yeast genes now available in  two new expression vectors (pBY011 and ZM552). </li>
            <li class="mainbodytext">Over 40 <a href="GetAllVectors.do?PSI=true" target="_self">Empty vectors</a> for cell free and bacterial expression from PSI sites now available!</li>
            <li class="mainbodytext"><a href="http://www.hip.harvard.edu/PSIMR/pMHTdelta238.html" target="_blank">TEV protease</a> expressing plasmid now available!</li>
            <li class="mainbodytext">PSI CloneIDs now link to data in<a href="http://targetdb.rcsb.org/" target="_blank"> TargetDB</a>, <a href="http://pepcdb.rcsb.org/" target="_blank">PepcDB</a>, <a href="http://kb.psi-structuralgenomics.org/KB/" target="_blank">PSI Structural Genomics Knowledgebase</a> and <a href="http://www.topsan.org/WikiHome" target="_blank">TOPSAN</a></li>
            <li class="mainbodytext">New<a href="PrepareAdvancedSearch.do?psi=1" target="_self"> PSI specific searches</a> available. Search by TargetDB ID, PDB ID, protein expression, solubility or purification or by PSI site.</li>
         
            <li class="mainbodytext"><a href="GetCollection.do?collectionName=PSI">Nearly 14,000  sequence-verified PSI plasmids available.</a></li>
          
            <li class="mainbodytext">Credit Card Payments now accepted! You can now use either a credit card or purchase order (PO) number to pay for your request.</li>
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

