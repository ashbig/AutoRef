<%@ page language="java"%>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants"%> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
    </head>
    <div class="gridContainer clearfix">


        <jsp:include page="signinMenuBar.jsp" /><table width="80%"  border="0">

    <p class="mainbodytext"> 
        The Mission of the DNA Resource CORE is to provide member in the DF/HCC, Harvard Affiliates, and outside researchers with quality and inexpensive <a target="_blank" href="http://dnaseq.med.harvard.edu/">DNA sequencing services</a> and <a href="http://plasmid.med.harvard.edu/PLASMID/Home.jsp">sequence verified plasmid constructs</a>, paired with comprehensive quality control and customer support.</p>                          
    <p><a href="faces/GeneSearch.xhtml">Search here for the most up to date Human & Mouse cDNAs we have available! </a>
    <br><a href="OrderOverview.jsp">For other clone types or collection inquiries please see our Search & Order page. </a></br>
    Click <a href="TermAndCondition.jsp">here</a> for the <strong class="mainbodytext">terms and conditions</strong> for receiving plasmids from the repository. <br>
    Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the <strong>cloning methods</strong> used to create many of the  plasmids.          </p>        </td>
What's New?                          
<br/>&#149 Vector Collection organized by downstream use
<br/>&#149 Viral packaging plasmids and scramble hairpin now available [<a href="http://plasmid.med.harvard.edu/PLASMID/GetVectorsByType.do?type=viral%20production">go there</a>]                                   hORFV8.1 in pENTR223 now available [PMID: <a href="http://www.ncbi.nlm.nih.gov/pubmed?cmd=Search&db=pubmed&term=21706014" target="_blank">21706014]</a></li>
<br/>&#149 Complete collection from <a href="http://www.orfeomecollaboration.org/" target="_blank">ORFeome Collaboration</a> now available
<br></br> 
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>

<a href="brokenlink.jsp">Ashkans's Broken Link</a>

        <!--<table width="100%" border="0" cellspacing="1" cellpadding="4">
            <tr align="left" valign="top" bgcolor="#6699CC" class="homepageText3"> 
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
        -->
        
        
        <!--</TD>
        </TR>
        </TBODY>
        </table>-->
    <jsp:include page="footer.jsp" /></body>
    </div>
</html>




