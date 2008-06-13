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
<jsp:include page="homeTitle.jsp" /><table width="973" height="603" border="0">
  <tr>
    <td width="550" height="300"><table width="532" height="259" border="0" align="center" bgcolor="#EFEFEF" id="form">
      <tr>
        <td width="526" height="112"><p align="center"><span class="mainbodytext">Plasmid Information Database (PlasmID) is a central repository for plasmid clone collections and distribution based at the <a href="http://www.hip.harvard.edu/" target="_blank">Harvard Institute of Proteomics (HIP)</a>. We are also the <a href="http://www.hip.harvard.edu/PSIMR/index.htm" target="_blank" class="style1">Protein Structure Initiative (PSI) Material Repository</a>.&nbsp;        
          </span>
        <p align="center"><span class="mainbodytext"><img src="HMS_logo.jpg" width="66" height="63">&nbsp;<img src="psilogo.jpg" width="150" height="63">&nbsp;<img src="DFHCC_logo_square.jpg" width="64" height="63"><br>
            </span>
        <p align="center" class="mainbodytext">To see an overview and to search for plasmids, please go to <a href="OrderOverview.jsp">Plasmid Request</a> or find a list of our large collections <a href="collection_overview.jsp" target="_blank">here</a>. </p>
        <p align="center" class="mainbodytext">To find more information on how to deposit clones with our repository, please go to <a href="Submission.jsp">Plasmid Submission</a>.</p>
        <p align="center" class="mainbodytext">Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the cloning methods used for the plasmids in the repository.          </p>
          <p align="center" class="mainbodytext">Registration is not required to view the collection. </p></td>
      </tr>
    </table></td>
    <td width="490"><img src="monday_4.gif" width="431" height="270" border="0" alt="" usemap="#monday_4_Map" />
      <map name="monday_4_Map" id="monday_4_Map">
        <area shape="rect" alt="" coords="110,87,314,114" href="Submission.jsp" />
        <area shape="rect" alt="" coords="81,175,346,201" href="collection_overview.jsp" target="_blank" />
        <area shape="rect" alt="" coords="96,132,330,157" href="OrderOverview.jsp" />
    </map></td>
  </tr>
  <tr>
    <td colspan="2"><table width="697" border="5" align="center" bordercolor="#999999" id="gray">
      <tr>
        <td width="679" height="234"><p align="center" class="homepageText3">What's New? </p>
          <ul type= circles>
            <li class="mainbodytext"><a href="http://www.hip.harvard.edu/PSIMR/PSIMRNews/EmptyVectors.html" target="_blank">Empty vectors</a> for cell free and bacterial expression from University of Wisconsin (CESG) now available!</li>
         
            <li class="mainbodytext"><a href="http://www.hip.harvard.edu/PSIMR/PSIMRNews/pMHTdelta238.html" target="_blank">TEV protease</a> plasmid now available!</li>
            <li class="mainbodytext">PSI CloneIDs now link to data in<a href="http://targetdb.rcsb.org/" target="_blank"> TargetDB</a>, <a href="http://pepcdb.rcsb.org/" target="_blank">PepcDB</a>, <a href="http://kb.psi-structuralgenomics.org/KB/" target="_blank">PSI Structural Genomics Knowledgebase</a> and <a href="http://www.topsan.org/WikiHome" target="_blank">TOPSAN</a></li>
            <li class="mainbodytext">New<a href="PrepareAdvancedSearch.do?psi=1" target="_self"> PSI specific searches</a> available. Search by TargetDB ID, PDB ID, protein expression, solubility or purification or by PSI site.</li>
         
            <li class="mainbodytext"><a href="GetCollection.do?collectionName=PSI">Over 1600 sequence-verified PSI plasmids available.</a></li>
          
            <li class="mainbodytext">Credit Card Payments now accepted! You can now use either a credit card or purchase order (PO) number to pay for your request.</li>
            
            <li class="mainbodytext">Sequence-verified, genome-scale ORF collections for F. tularensis, Y. pestis and V. cholerae (HIP/HMS).</li>
          
            <li class="mainbodytext">Human kinase collection now includes clones in entry vectors for recombinational cloning and <a href="GetCollection.do?collectionName=HIP%20human%20kinase%20collection%20(pJP1520,%20complete%20set)">clones in a mammalian expression vector</a> (HIP/HMS)</li>
          
            <li class="mainbodytext">Harvard  Institute of Proteomics named site of the Materials Repository of the <a href="http://www.nigms.nih.gov/Initiatives/PSI/">Protein Structure Initiative</a>, funded by <a href="http://www.nigms.nih.gov/">NIGMS/NIH</a> for  storage and distribution of more than 100,000 plasmid clones for protein expression.</li>
            <br>
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

