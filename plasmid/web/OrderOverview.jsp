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
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="orderoverviewTitle.jsp" />

<table width="100%" border="0">
  <tr> 
    <td width="58%" rowspan="3" valign="top"> <p><span class="mainbodytextlarge">Looking 
        for a specific gene insert? </span> <span class="morehit"><br>
        </span><span class="alert"><a href="GetDataForRefseqSearch.do">search by reference sequence</a></span></p>
      <p><span class="mainbodytextlarge">Looking for an 'empty vector'? </span><span class="morehit"><br>
        </span><span class="alert"><a href="GetAllVectors.do">view all vectors</a></span></p>
      <p><span class="mainbodytextlarge">Ready to check out? </span><span class="morehit"><br>
        </span><span class="alert">click the shopping cart icon to initiate check-out 
        (top right of any page)</span><br>
        <br>
        <span class="mainbodytextlarge">Can't find what you're looking for? </span><span class="morehit"><br>
        </span><span class="alert">please be sure you logged in because log-in 
        effects what clones you can view.<br>
        <a href="http://dnaseq.med.harvard.edu/plasmid-related_links.htm" title="Plasmid Links Page" target="_blank">CLICK 
        HERE</a> to see other resources for clones.</span></p>
      <p class="text"><span class="mainbodytextlarge">Don't recognize an organisms' 
        name? </span><span class="morehit"><br>
        </span><span class="alert">the <a target="_blank" href="collection_overview.jsp">collection overview</a> includes common names 
        for organisms in our collection</span></td>
    <td width="42%" height="140" valign="top"> 
      <table width="100%" border="3">
        <tr class="tableheader"> 
          <td width="32%">PI Status</td>
          <td width="40%">Order Type</td>
          <td width="28%">Price/Unit*</td>
        </tr>
        <tr class="tableinfo"> 
          <td>DF/HCC Member</td>
          <td>individual clones</td>
          <td>30$/clone</td>
        </tr>
        <tr class="tableinfo"> 
          <td>DF/HCC Member</td>
          <td>96-well plate of clones</td>
          <td>1200$/plate</td>
        </tr>
        <tr class="tableinfo"> 
          <td>Harvard Affiliate</td>
          <td>individual clones</td>
          <td>30$/clone</td>
        </tr>
        <tr class="tableinfo"> 
          <td>Harvard Affiliate</td>
          <td>96-well plate of clones</td>
          <td>1200$/plate</td>
        </tr>
        <tr class="tableinfo"> 
          <td>All others</td>
          <td>individual clones</td>
          <td>45$/clone</td>
        </tr>
        <tr class="tableinfo"> 
          <td>All others</td>
          <td>96-well plate of clones</td>
          <td>1500$/plate</td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td valign="top" class="footertext">* Shipping is additional -- details at 
      checkout.</td>
  </tr>
  <tr> 
    <td height="137" valign="middle" class="footertext"> 
      <p class="homepageLink"><span class="homepageText3">Please 
        give credit where credit is due!</span><span class="mainbodytexthead"> 
        </span><span class="morehit">When you have searched for and ordered a 
        clone, be sure to click on the clone ID and make note of both the reference(s) 
        associated with the clone and the author(s) who donated the clone. The 
        people who constructed, published, and donated the clone should be cited 
        whenever you publish work that uses the clone that you ordered.</span></p></td>
  </tr>
</table>

<table width="100%" border="0">
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
    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

