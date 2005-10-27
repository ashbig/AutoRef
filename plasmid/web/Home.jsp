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
      <TD width="100%">
<div align="center">
<table width="100%" height="104" border="0">
            <tr valign="top"> 
              <td width="32%" height="100" align="center" valign="middle" class="tableinfo"><font color=#000000><font size=4>Welcome 
                to the Plasmid Information Database (PlasmID)<br>
                at the Dana Farber/Harvard Cancer Center DNA Resource Core</font></font></td>
            </tr>
          </table>

        </div>
        <table width="100%" height="241" border="0" cellpadding="3" cellspacing="3">
          <tr> 
            <td width="31%" height="235" valign="top"> <p class="homepageText3"><strong>Collection 
                Includes</strong></p>
              <ul>
                <li class="alert">genome-scale ORF collections from:<br>
                  <span class="morehit">Pseudomonas<br>
                  Yeast<br>
                  Y. pestis<br>
                  F. tularensis</span><br>
                </li>
                <li class="alert">more than 500 human kinase ORFs<br>
                </li>
                <li class="alert">other human ORFs in recombinational cloning 
                  vectors <br>
                </li>
                <li class="alert">genomic fragments, cDNAs and other constructs 
                  from viruses, mouse, humans and more<br>
                </li>
                <li class="alert">vectors for routine and cutting-edge molecular 
                  biology techniques</li>
              </ul></td>
            <td width="36%" class="mainbodytextlarge"><p align="center">Please 
                register and sign in. </p>
              <p align="center" class="morehit">Log-in status affects what you 
                can view from the collection.</p></td>
            <td width="33%"><p class="homepageText3">Getting Started</p>
              <p>Looking for a specific gene insert? <span class="morehit"><br>
                search by reference sequence (left-hand side, search &amp; order 
                page)</span></p>
              <p>Looking for an 'empty vector'? <span class="morehit"><br>
                click to view all vectors (left-hand side, search &amp; order 
                page)</span></p>
              <p>Ready to check out? <span class="morehit"><br>
                click the shopping cart icon to initiate check-out (top right 
                of any page)</span><br>
                <br>
                Don't see what you expect? <span class="morehit"><br>
                please be sure you logged in because log-in effects what clones 
                you can view. <a href="http://dnaseq.med.harvard.edu/plasmid-related_links.htm" title="Plasmid Links Page" target="_blank">CLICK 
                HERE</a> to see other resources for clones.</span></p>
              </td>
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
            <td height="28" colspan="8"> <div align="center" class="homepageLink">For 
                information on DNA sequencing and other DNA Resource Core services, 
                please <a href="http://dnaseq.med.harvard.edu" title="DNA Resource Core Home Page" target="_blank">CLICK 
                HERE</a>. <span class="morehit"><br>
                Have questions not answered in our faq (top right of page)? Please 
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
      <TD width="100%"><div align="center" class="homeMainText">PlasmID was created
          and is maintained by the <a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC
          DNA Resource Core</a></div></TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

