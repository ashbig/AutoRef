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
    <td width="58%" rowspan="3" valign="top"> <p class="text"><span class="controlpaneltext">HAVE 
        YOU LOGGED IN? LOG-IN STATUS AFFECTS YOUR SEARCH RESULTS.</span><br>
        Register and log in to search the complete set of clones you are eligible 
        to order (for example, if you are an academic researcher, log in to see 
        the set of clones that can be distributed only to academic and non-profit 
        labs). Prior to log-in, you'll only be able to search the set of clones 
        available to all users.
      <p class="text">After log-in, use the menu bar on the left to choose a search 
        type or view a list of clones in a special collection.
<P class="itemtext"><u>Search for plasmid clones</u></P>
      <ul>
        <li class="underbullet">Choose a search type from the menu bar on the 
          left</li>
        <li class="underbullet">&quot;Search by reference sequence&quot; queries 
          insert-associated information. Use this search to find cDNAs, genomic 
          fragments, shRNAs, etc. inserted into various vectors using a gene symbol, 
          GenBank number, or an organism-specific ID)</li>
      </ul>
      <p class="itemtext"><u>View clone lists</u></p>
      <ul>
        <li class="underbullet">Certain donor labs (for example, the Harvard Institute 
          of Proteomics or HIP) have special collections listed here that group 
          a set of clones by molecular function, disease association, or organism</li>
        <li class="underbullet">Click &quot;view clone lists&quot; then click 
          on a specific set to view the clones in that set</li>
        <li class="underbullet">Order individual clones or the entire set (special 
          pricing available for some sets)</li>
      </ul></td>
    <td width="42%" height="142" valign="top"> 
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
    <td height="164" valign="middle" class="footertext"> 
      <p class="homepageLink"><span class="alert">quick hint to understanding 
        the database:</span> <span class="underbullet">the core curates information 
        about vectors, inserts and clones, with the idea that vector + insert 
        = clone in most cases. the &quot;search by reference sequence&quot; tool 
        searches insert info (use this to find your gene of interest) and conversely, 
        the &quot;search by vector&quot; tool searches vector info. at the search 
        results table (or from the summary table created after you've placed an 
        order), click on the clone ID to find information about vector, insert, 
        growth conditions, host strains, appropriate references, and more. Downloadable 
        maps &amp; sequences are associated with the clone and/or the vector.</span></p></td>
  </tr>
</table>

<table width="100%" border="0">
  <tr>
    <td class="homepageText2">Please give credit where credit is due! <span class="breadcrumbtextactive">When 
      you have searched for and ordered a clone, be sure to click on the clone 
      ID and make note of both the reference(s) associated with the clone and 
      the author(s) who donated the clone. The people who constructed, published, 
      and donated the clone should be cited whenever you publish work that uses 
      the clone that you ordered.</span></td>
  </tr>
</table>
    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

