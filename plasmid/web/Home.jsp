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
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">

  <TBODY>
    <TR> 
      <TD width="100%" height="519"> 
        <div align="center">
<table width="100%" height="104" border="0">
            <tr valign="top"> 
              <td width="32%" height="100" align="center" valign="middle" bgcolor="#FFFFFF" class="tableinfo"><font color=#000000><font size=4>Welcome 
                to the Plasmid Information Database (PlasmID)<br>
                at the Dana Farber/Harvard Cancer Center DNA Resource Core</font></font></td>
            </tr>
          </table>

        </div>
        <table width="100%" height="301" border="0" cellpadding="3" cellspacing="3">
          <tr> 
            <td width="31%" height="235" rowspan="2" valign="top" bgcolor="#FFFFFF"> 
              <p class="homepageText3"><strong>Collection 
                Includes</strong></p>
              <ul>
                <li class="alert"><strong>genome-scale ORF collections from:</strong><br>
                  <span class="mainbodytexthead"><em>Pseudomonas (&gt;2300 clones)<br>
                  Yeast (&gt;4500 clones)<br>
                  Y. pestis (&gt;3900 clones)<br>
                  V. cholera (&gt;2000 clones)<br>
                  F. tularensis (&gt;1200 clones)</em></span><br>
                </li>
                <li class="alert"><strong>&gt;6,000 human clones, including &gt;500 
                  kinase ORFs </strong><strong>in recombinational cloning vectors</strong><br>
                </li>
                <li class="alert"><strong>genomic fragments, cDNAs and other constructs 
                  from viruses, mouse, humans and more</strong><br>
                </li>
                <li class="alert"><strong>vectors for routine and cutting-edge 
                  molecular biology techniques</strong></li>
              </ul></td>
            <td width="36%" height="62" class="mainbodytextlarge"> 
              <p align="center">Please 
                register and sign in.<br>
                <span class="mainbodytext">Log-in status affects what you can 
                view from the collection.</span></p>
              </td>
            <td width="33%" rowspan="2" align="left" valign="top"> <p class="homepageText3">Getting 
                Started</p>
              <ul>
                <li><span class="homepageText3"><a href="OrderOverview.jsp">Search and Order Plasmids</a></span><br>
                </li>
                <li><span class="homepageText3"><a href="Submission.jsp">Donate Plasmids</a></span><span class="morehit"><br>
                  </span></li>
                <li><span class="homepageText3"><a target="_blank" href="collection_overview.jsp">Overview of the Collection</a><br>
                  </span></li>
                <li><span class="homepageText3"><a href="Contactus.jsp">Contact Us</a><br>
                  </span></li>
                <li><span class="homepageText3"><a href="http://dnaseq.med.harvard.edu" title="DNA Resource Core Homepage" target="_blank">DNA 
                  Resource Core</a><br>
                  </span></li>
                <li><span class="homepageText3"><a href="http://www.dfhcc.harvard.edu" title="Dana Farber Harvard Cancer Center" target="_blank">DF/HCC</a></span></li>
              </ul></td>
          </tr>
          <tr>
            <td height="189" align="center" valign="middle" class="mainbodytextlarge"><img src="tubesninecartoon.jpg" width="303" height="183"></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="3" cellpadding="3">
         
          <tr> 
            <td bgcolor="#CCCCCC"> <p class="morehit">DFHCC<br>
                Member Institutions:</p></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.hms.harvard.edu" title="Harvard Medical School" target="_blank">Harvard 
              Medical School</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.hsph.harvard.edu" title="Harvard School of Public Health" target="_blank">Harvard 
              School of Public Health</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.bidmc.harvard.edu/sites/bidmc/home.asp" title="Beth Israel Deaconess Medical Center" target="_blank">Beth 
              Israel Deaconess Medical Center</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.brighamandwomens.org/" title="Brigham & Women's Hospital" target="_blank">Brigham 
              & Women's Hospital</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.dfci.harvard.edu/" title="Dana-Farber Cancer Institute" target="_blank">Dana-Farber 
              Cancer Institute</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="http://www.mgh.harvard.edu/" title="Massachusetts General Hospital" target="_blank">Massachusetts 
              General Hospital</a></td>
            <td bgcolor="#CCCCCC" class="morehit"><a href="Massachusetts General Hospital" title="Children's Hospital Boston" target="_blank">Children's 
              Hospital Boston</a></td>
          </tr>
		   <tr> 
            <td height="45" colspan="8"> 
              <div align="center" class="homepageLink">For 
                information on DNA sequencing and other DNA Resource Core services, 
                please <a href="http://dnaseq.med.harvard.edu" title="DNA Resource Core Home Page" target="_blank">CLICK 
                HERE</a>. <span class="morehit"><br>
                Have questions not answered in our <a target="_blank" href="FAQ.jsp">faq</a>? Please 
                contact <a href="http://dnaseq.med.harvard.edu/contact_us.htm" title="Contact DNA Resource Core" target="_blank">stephanie 
                mohr</a>.</span></div></td>
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

