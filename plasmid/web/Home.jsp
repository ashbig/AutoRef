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
        <jsp:include page="homeTitle.jsp" /><table width="973"  border="0">
            
            <tr>
                <td height="250"><table width="800" height="259" border="0" align="center" bgcolor="#EFEFEF" id="form">
                        <tr>
                            <td width="800" height="112">
 
                                <p align="left"><span class="mainbodytext">The Mission of the DNA Resource CORE is to provide member in the DF/HCC, Harvard Affiliates, and outside researchers with quality and inexpensive <a target="_blank" href="http://dnaseq.med.harvard.edu/">DNA sequencing services</a> and <a href="http://plasmid.med.harvard.edu/PLASMID/Home.jsp">sequence verified plasmid constructs</a>, paired with comprehensive quality control and customer support.&nbsp;        
                                    </span>
                                <hr> 
                                    <p align="center" class="formlabel" style="font-size: 16px"><a href="faces/GeneSearch.xhtml">Search here for the most up to date Human & Mouse cDNAs we have available! </a></p> 
                                    <p align="center" class="formlabel" style="font-size: 14px"><a href="OrderOverview.jsp">For other clone types or collection inquiries please see our Search & Order page. </a></p>
                                <hr>
                                <p align="left" class="mainbodytext">Click <a href="TermAndCondition.jsp">here</a> for the <strong class="mainbodytext">terms and conditions</strong> for receiving plasmids from the repository.</p>
                            <p align="left" class="mainbodytext"> Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the <strong>cloning methods</strong> used to create many of the  plasmids.          </p>        </td>
                        </tr>
                </table></td>
            </tr>
            <tr>
                <td><table width="800" border="5" align="center" bordercolor="#999999" id="gray">
                        <tr>
                            <td width="800" height="70">
                                <p/>
                                <p class="mainbodytext">What's New?
                                <ul class="mainbodytext">
                                    <li>Vector Collection organized by downstream use</li>
                                    <li>Viral packaging plasmids and scramble hairpin now available [<a href="http://plasmid.med.harvard.edu/PLASMID/GetVectorsByType.do?type=viral%20production">go there</a>]</li>
                                    <li>hORFV8.1 in pENTR223 now available [PMID: <a href="http://www.ncbi.nlm.nih.gov/pubmed?cmd=Search&db=pubmed&term=21706014" target="_blank">21706014]</a></li>
                                    <li>Complete collection from <a href="http://www.orfeomecollaboration.org/" target="_blank">ORFeome Collaboration</a> now available</li>
                                </ul>
                                </p>
                            </td>
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



