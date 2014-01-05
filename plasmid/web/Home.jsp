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
                                <!-- 
                                <p align="left"><span class="mainbodytext">The Mission of the DNA Resource CORE is to provide member in the DF/HCC, Harvard Affiliates, and outside researchers with quality and inexpensive <a target="_blank" href="http://dnaseq.med.harvard.edu/">DNA sequencing services</a> and <a href="http://plasmid.med.harvard.edu/PLASMID/Home.jsp">sequence verified plasmid constructs</a>, paired with comprehensive quality control and customer support.&nbsp;        
                                </span>
                                -->
                                 <!--
                                //<p align="left"><span class="formlabelitalic">
                                The PlasmID website will be unavailable on Saturday 11/13/2013 from 4:00AM - 6:00 AM EST for routine maintenance.</span></p>
                              -->
                               
                              <p align="justify" <span class="formlabelitalic">Dear DF/HCC DNA Resource Core Users,
                                      <br><br>
                                        IM MAKING CHANGESThe Core would like to inform you that our facility will be closed from Tuesday, December 23rd, 2013 to Thursday, January 2nd, 2014 in observance of Harvard University's Winter Recess. Our last international shipment will occur on Wednesday, December 18th 2013 and our last domestic shipment will occur on Thursday, December 19th 2013. As we work to complete existing orders please note that new orders placed after Wednesday, December 11th may not be completed prior to this recess. For urgent requests, you may wish to place your order with a commercial provider. As always, please contact
                                        <a href="mailto:plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a> with any questions or concerns.<br>
                                        <br> Happy Holidays!<br>
                                        Your DF/HCC DNA Resource Core Staff
                                    </span>
                                </p>
                                <!--
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
              <table width="70%" border="3" align="center" id="gray">
        <tr class="tableheader">
          <td width="30%">Pricing Category</td>
          <td width="25%">Individual Clone</td>
          <td width="45%">Individual Clone with Platinum QC</td>
        </tr>
        <tr class="tableinfo">
          <td>DF/HCC Members</td>
          <td align="right">$45.00</td>
          <td align="right">$55.00</td>
        </tr>
        <tr class="tableinfo">
          <td>Academics and Nonprofits</td>
          <td align="right">$55.00</td>
          <td align="right">$65.00</td>
        </tr>
        <tr class="tableinfo">
          <td>Commercial Users</td>
          <td align="right">$60.50</td>
          <td align="right">$70.50</td>
        </tr>
      </table>
            
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
                                                    <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
                                                    <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
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



