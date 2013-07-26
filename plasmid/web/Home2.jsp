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
        <table width="1000" height="86" border="0" align="center">
  <tr> 
    <td width="100%" align="center" valign="bottom" class="title"> 
      <img height="83" width="77" src="dnacore.jpg"/><img height="81" width="222" src="PlasmID_logo.jpg"/></td>
  </tr>
  <tr>
    <td width="100%" rowspan="2" align="center" valign="top" class="homepageLink"><a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC DNA Resource Core</a></td>
    <td rowspan="2"> 
     <a target="_blank" href="FAQ.jsp" class="countrytext">FAQ</a></td>
  </tr>
  <tr>
    <td height="26" colspan="3">&nbsp;</td>
  </tr>
</table>
        
        <table width="1000" height="49" border="0" align="center">
  <tr> 
    <td height="45"> <table width="100%" height="36" border="0" cellpadding="0" cellspacing="0" bgcolor="#333333">
        <tr> 
          <td><table width="100%" height="36" border="0" cellpadding="0" cellspacing="0" bgcolor="#333333">
        <tr> 
          <td><table width="100%" height="33" border="0" cellpadding="4" cellspacing="1" bgcolor="#FFFFFF">
              <tr> 
                  <td width="16%" align="center" bgcolor="#6699CC"><a href="Home2.jsp" STYLE="text-decoration:none"><font><strong>Home</strong></font></a></td>
                <td width="28%" align="center" bgcolor="#6699CC"><font><strong>Plasmid 
                  Submission </strong></font></td>
                <td width="28%" align="center" bgcolor="#6699CC"><font>
                    <strong>Search & Order</strong></font></td>
                <td width="28%" align="center" bgcolor="#6699CC"><font><strong>Contact 
                  Us </strong></font></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
        
        <table width="973"  border="0">
            <tr>
                <td height="250"><table width="800" height="500" border="0" align="center" bgcolor="#EFEFEF" id="form">
                        <tr>
                            <td width="800" height="112">
                               
                                <p align="center">
                                    <span class="formlabel">Due to routine maintenance the PlasmID website will be unavailable on</span>
                                    </p>
                                    <p align="center"><span class="alert">Saturday July 27 from</span></p>
                                    <p align="center"><span class="alert">04:00 AM - 08:00 AM EDT</span></p>
                             
                            
                               <!-- 
                                <p align="left"><span class="formlabelitalic">Dear DF/HCC DNA Resource Core Users,
                                        <br><br>
                                        The Core would like to inform you that our facility will be closed from Friday, December 23rd, 2011 to January 2nd, 2012. PlasmID orders however will still be accepted online during the break for later processing. We will resume filling orders on Tuesday, January 3rd, 2012.
                                        <br><br>
                                        Happy Holidays!<br>
                                        Staff at DF/HCC DNA Resource Core
                                    </span>
                                </p>-->
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
    <hr align="center" width="1000" size="2">
<table width="1000" height="58" border="0" align="center">
  <tr> 
    <td height="54"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr align="center"> 
          <td height="52"> <p class="footertext">Home
              &#149; Plasmid Submission &#149; Search & Order
              &#149; Contact Us</p>
        <p align="center" class="homeMainText">PlasmID was created and is maintained 
          by the <a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC 
          DNA Resource Core</a></p>
            <p class="footertext">©2004-2010 Harvard Medical School</p></td>
        </tr>
      </table></td>
  </tr>
</table>
    </body>
</html>

