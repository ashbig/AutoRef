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
      <TD width="100%"><div align="center"> 
          <p><font color="#000000"><font size=4>Welcome to the Plasmid Information 
            Database (PlasmID)</font><br align="center">
            <font size=4>of the Dana Farber/Harvard Cancer Center DNA Resource 
            Core</font></font> </p>
          <p align="left" class="homeMainText">The goal of PlasmID is to allow 
            researchers in academic and non-profit laboratories to search for 
            and order plasmid clones in the DF/HCC DNA Resource Core collection. 
            In addition, PlasmID is a source for supporting information about 
            the clones (vector maps, sequence files, growth conditions, etc.). 
          </p>
          <p align="left" class="homeMainText">Please click &quot;log in&quot; 
            above to sign-in or register as a new user to begin a new search and 
            order clones. </p>
          <p align="left" class="homeMainText">For more informaiton about storing 
            clones at our facility, <a href="http://dnaseq.med.harvard.edu/plasmid_repository.htm" target="_blank">click 
            here.</a></p>
        </div>
        <P align=center><FONT size=4>THE COLLECTION INCLUDES</FONT></P>
        <UL>
          <LI class="homeMainText">Large sets of sequence verified cDNA clones 
            from Human, Yeast, Pseudomonas, and other species 
          <LI class="homeMainText">Frequently requested plasmid clones from laboratories 
            in <a href="http://www.dfhcc.harvard.edu/" target="_blank">DF/HCC</a> 
            member institutions: 
            <UL>
              <LI><A href="http://www.bidmc.harvard.edu/sites/bidmc/home.asp" target="_blank">Beth 
                Israel Deaconess Medical Center</A> 
              <LI><A href="http://www.brighamandwomens.org/" target="_blank">Brigham 
                &amp; Women's Hospital</A> 
              <LI><A href="http://www.childrenshospital.org/" target="_blank">Children's 
                Hospital Boston</A> 
              <LI><A href="http://www.dfci.harvard.edu/" target="_blank">Dana 
                Farber Cancer Institute</A> 
              <LI><A href="http://www.hms.harvard.edu/" target="_blank">Harvard 
                Medical School</A> 
              <LI><A href="http://www.hsph.harvard.edu/" target="_blank">Harvard 
                School of Public Health</A> 
              <LI><A href="http://www.mgh.harvard.edu/" target="_blank">Massachusetts 
                General Hospital</A>. </LI>
            </UL>
          <LI class="homeMainText">Frequently requested plasmid clones from other 
            researchers who have submitted their plasmids to our facility for 
            centralized long-term storage, maintenance, and distribution of plasmid 
            clones</LI>
        </UL>
        <P class="homeMainText">Please set up a new account or sign-in as a returning 
          user in order to start your search and order clones (please click the 
          &quot;log in&quot; link at the top of the page).</P>
        <P class="homeMainText">For more information about the repository and 
          how it works, or if you are interested in submitting clones to the facility, 
          please visit the DNA Resource Core homepage at <A 
      href="http://dnaseq.med.harvard.edu/" target="_blank">http://dnaseq.med.harvard.edu/</A> 
          or contact <a href="http://dnaseq.med.harvard.edu/contact_us.htm" target="_blank">Stephanie 
          Mohr</a>.</P></TD>
    </TR>
    <TR> 
      <TD width="100%"> <TABLE id=AutoNumber3 style="BORDER-COLLAPSE: collapse" 
      borderColor=#111111 cellSpacing=3 cellPadding=3 width="100%" border=0>
          <TBODY>
            <TR> 
              <TD width="50%" height="288" valign="top"> 
                <table width="100%" border="0">
                  <tr>
                    <td valign="top">
<p class="tableheader"><B>New in the collection:</B></p>
                      <p class="tablebody">Several cDNAs for human ubiquitin pathway 
                        components, including tagged and mutant forms in mammalian 
                        expression vectors (from the collection of the Howley 
                        lab, Harvard Medical School).</p>
                      <p class="tablebody">Fluorescent tagging vectors for yeast 
                        (from the collection of K. Thorn at the Bauer CGR, Harvard 
                        University).</p>
                      </td>
                  </tr>
                </table>
                <p>&nbsp;</p>
                </TD>
              <TD width="50%" valign="top">
<table width="100%" border="0">
                  <tr>
                    <td valign="top">
<p class="tableheader"><B>Frequently ordered from the collection:</B></p>
                      <p class="tableinfo">to be added</p></td>
                  </tr>
                </table>
                <p>&nbsp;</p>
                </TD>
            </TR>
          </TBODY>
        </TABLE></TD>
    </TR>
    <TR> 
      <TD width="100%"><div align="center">
          <p class="underbullet">PlasmID was created and is maintained by the 
            <a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC DNA 
            Resource Core</a></p>
        </div></TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

