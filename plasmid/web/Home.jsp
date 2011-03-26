<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

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
            <p align="left"><span class="mainbodytext">The Mission of the DNA Resource CORE is to provide member in the DF/HCC, Harvard Affiliates, and outside researchers with quality and inexpensive <a target="_blank" href="http://dnaseq.med.harvard.edu/">DNA sequencing services</a> and <a href="http://plasmid.med.harvard.edu/PLASMID/Home.jsp">sequence verified plasmid constructs</a>, paired with comprehensive quality control and customer support.&nbsp;        
        </span>
        
        <p align="left" class="formlabelitalic">
            Dear Valued PlasmID User,<br><br>
            We are happy to announce that we are no longer experiencing a two week delay on new orders.
            To better serve your plasmid needs in the future we are happy to announce our upcoming 
            plans for expansion. In the coming days please look forward to the addition of many new 
            clone collections, new personnel to fill orders, and faster turn-around times. To enable 
            this expansion we are also adjusting our price structure for the first time in almost 
            10 years! We are committed to providing valuable plasmid constructs to the research 
            community at the lowest possible rate, and are happy to announce new rates far below 
            those found elsewhere on the market. Below please find our new pricing structure which 
            will take effect on April 1, 2011. If you have any questions or concerns please feel free 
            to contact <a href="mailto:plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>. 
        </p>
            <!--
        <p class="alertbigger">NOTICE: Due to high volume all incoming orders are experiencing a TWO WEEK delay. We are working rapidly to improve our capacity and value your patronage. If you have any further questions or concerns please contact <a href="mailto:plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>.</p>
            <!--p class="alertheader">Special Notice: In observance of the holidays the PlasmID Repository will be closed from 12/23/2010 - 01/03/2011. </p-->
            <!--p class="alert">Our website will continue to accept new orders, but no new orders will be processed until the new year. To avoid shipping complications our last shipping date will be 12/20/2010.Please contact plasmidhelp@hms.harvard.edu with any questions or concerns. </p-->
        
            <!--
        <p align="left" class="mainbodytext"><strong>PLASMID SEARCH:</strong>  Go to <a href="OrderOverview.jsp">Search & Order</a>
          or find a list of our large collections <a href="collection_overview.jsp" target="_blank">here</a>. </p>
        <p align="left" class="mainbodytext"><strong>PLASMID COST:</strong> Click <a href="http://plasmid.med.harvard.edu/PLASMID/OrderOverview.jsp#cost">here</a> for plasmid and shipping costs.</p>
        <p align="left" class="mainbodytext"><strong class="mainbodytext">DEPOSITING CLONES: </strong>Go to <a href="Submission.jsp">Plasmid Submission</a> or see a <a href="http://psimr.asu.edu/overview.html" target="_blank">slide show</a> describing the process.</p>       
        -->
       <hr> 
      <html:form action="AdvancedSearch.do">
        <p class="formlabel">Search PlasmID collection [<a href="PrepareAdvancedSearch.do?psi=0">Advanced Search</a>]</p>
        <table width="100%" border="0">
            <tr> 
                <td class="formlabel" width="30%">Gene name or symbol</td>
                <td>
                    <html:select property="geneNameOp" styleClass="itemtext">
                      <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
                      <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
                    </html:select>
                </td>
                <td>
                    <html:text styleClass="itemtext" property="geneName" size="50"/>
                </td>
                <td><html:submit styleClass="formlabel" value="Search"/></td>
            </tr>
        </table>
        </html:form>
    
        <hr>
        <p align="left" class="mainbodytext">Click <a href="TermAndCondition.jsp">here</a> for the <strong class="mainbodytext">terms and conditions</strong> for receiving plasmids from the repository.</p>
        <p align="left" class="mainbodytext"> Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the <strong>cloning methods</strong> used to create many of the  plasmids           </p>        </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="800" border="5" align="center" bordercolor="#999999" id="gray">
      <tr>
        <td width="800" height="180"><h1 align="center">What's New? </h1>
        <ul type= circles>  
          <li class="mainbodytext">06/01/10 - <a href="http://www.hms.harvard.edu/dms/bbs/fac/harper.html" target="_blank">Dr. Wade Harper</a> has joined the DNA Resource CORE as the CORE Director. </li>
            <li class="mainbodytext">10/14/09 - PSI Clones have migrated to our sister site at <a href="http://dnasu.asu.edu/DNASU/" target="_blank">DNASU</a>.</li>
            <li class="mainbodytext">10/13/09 - Platinum Clones are now available! Please use this service to request additional QC data from your specific order.</li>
            <li class="mainbodytext">07/24/09 - Kinase shRNA hairpins Now Available! Browse collection <a href="http://plasmid.med.harvard.edu/PLASMID/GetCollection.do?collectionName=shRNA Kinase Collection (pLKO.1)">here</a>.</li>
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
        </table>
        
          <p align="center"><span class="mainbodytext">
          <a href="http://www.dfhcc.harvard.edu" target="_blank"><img src="DFHCC_NCI_logo.jpg" width="379"  height="46"></a>&nbsp;
          <a href="http://hms.harvard.edu" target="_blank"><img src="HMS_logo.jpg" width="66" height="63"></a></span>
         
      </TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

