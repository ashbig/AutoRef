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
    <td height="40" colspan="2" valign="top"><div align="center">
      <span class="homepageText2">You can search for clones using various criteria.</span><br>
      <span class="mainbodytext">PLEASE LOG IN BEFORE SEARCHING FOR CLONES! </span>
     
      </div></td>
    </tr>
  <tr bordercolor="#000000" bgcolor="#F5F5F5">
    <td width="55%" height="185" valign="top">
      <span align="left" class="homepageText2">Gene insert  </span>
            <span class="mainbodytext"><a href="GetDataForRefseqSearch.do">Search by gene</a></span> <br>
          <span class="mainbodytext"> First choose the organism of interest, and then search for your gene(s) of interest by inputting GI number,  GenBank  Accession, Gene ID or Gene Symbol (gene name).</span>          </p>
      <p><span align="left" class="homepageText2">Assay type,  Cloning Method or Expression  </span><span class="mainbodytext"><a href="GetVectorPropertyTypes.do">Search by vector</a></span><br>
        <span class="mainbodytext">Search based on properties of the vector (ex. mammalian expression).</span><br>
        </p>
      <p><span align="left" class="homepageText2">PlasmID, CloneID, FLH#, other ID  </span><span class="mainbodytext"><a href="SearchClone.jsp">Search by clone identifier</a></span><br>
        
          <span class="mainbodytext">Search by CloneID/PlasmID or all other IDs.</span></p></td>
    <td width="45%" valign="top">
      <p><span align="left" class="homepageText2">PSI search</span>
            <span class="mainbodytext"><a href="PrepareAdvancedSearch.do?psi=1">Advanced search</a></span><br>
        <span class="mainbodytext">Search by PDB ID, TargetDB ID, PSI site or by ability of protein to express, be solube or purify.</span><br>
        <br>
            <span align="left" class="homepageText2">Advanced search options</span> 
            <span class="mainbodytext"><a href="PrepareAdvancedSearch.do?psi=0">Advanced search</a></span> <br>
             <span class="mainbodytext">Search by gene name, gene symbol, vector name, vector feature, author name, publication, or species.</span><br>
          <br>
          <span class="homepageText2">Looking for an 'empty vector'? </span>
          <span class="mainbodytext"><a href="GetAllVectors.do">View all vectors</a></span></p>      </td>
  </tr>
  <tr>
    <td height="277" colspan="2" valign="top"><div align="center">
      
      
      <span class="homepageText2 style3"><a name="cost"></a><br>
      CLONE COSTS </span>   </div>
      <table width="61%" border="3" align="center" id="gray">
        <tr class="tableheader">
          <td width="40%">PI Status</td>
          <td width="32%">Order Type</td>
          <td width="28%">Price/Unit*</td>
        </tr>
        <tr class="tableinfo">
          <td rowspan="2"><p>DF/HCC Member or Harvard Affiliate</p></td>
          <td height="20">individual clone</td>
          <td>$30/clone</td>
        </tr>
        <tr class="tableinfo">
          <td>96-well plate of clones</td>
          <td>$1200/plate</td>
        </tr>
        <tr class="tableinfo">
          <td rowspan="2">PSI LaboratoryPSI Laboratory</td>
          <td>individual clone</td>
          <td>$30/clone</td>
        </tr>
        <tr class="tableinfo">
          <td>96-well plate of clones</td>
          <td>$1200/plate</td>
        </tr>
        <tr class="tableinfo">
          <td rowspan="2">All othersAll others</td>
          <td>individual clone</td>
          <td>$45/clone</td>
        </tr>
        <tr class="tableinfo">
          <td>96-well plate of clones</td>
          <td>$1500/plate</td>
        </tr>
      </table>
      <div align="center"><span class="footertext"> *Shipping is additional. Either provide a FedEx number or shipping is a flat fee of $10 for domestic orders or $20 for international orders. <br>
      No charge for shjpping to the Harvard Medical School community if order is picked up. Must have building access.</span></div>
      <pre><span class="homepageText2">
Can't find what you're looking for? </span>
<span class="mainbodytext">          1. Please be sure you logged in because log-in effects what clones you can view.
          2. <a href="plasmid_links.jsp" title="Plasmid Links Page" target="_self">CLICK HERE</a> to see other resources for clones.</span></pre>
      <p><span class="homepageText2">CloneID is your link to more info. </span>
          <br>
          <span class="mainbodytext">Click on the clone ID to view growth conditions, insert and vector info, insert sequence (if we have it), authors and references to cite, and to download 
            a clone map (if there is one). click on a vector name to get more information 
            about the vector. to return to this info after placing an order sign in, 
            then go to &quot;my account&quot; (top right of the page) and &quot;view 
            orders&quot; to monitor order status, view clones and download info.</span>        </p>
      <p>  <span class="homepageText2">Please give credit where credit is due!</span><br>
        
          <span class="mainbodytext">Click on the clone ID to find the reference(s) associated with the clone and the author(s) who donated the clone. The people who constructed, published, and donated the clone should be cited whenever you publish work that uses the clone that you ordered.</span><br>
          <p><span class="homepageText2">Don't recognize an organisms' 
            name? </span>      <br>
            
              <span class="mainbodytext">The <a target="_blank" href="collection_overview.jsp">collection overview</a> includes common 
              names for organisms in our collection. </span>
          <p><span class="homepageText2">Ready to check out?
            </span><br>
              <span class="mainbodytext">Click the <a href="ViewCart.do">shopping cart</a> icon to initiate 
          check-out (top right of any page)</span>      </td>
    </tr>
</table>

<table width="100%" border="0">
  <tr>
  <td height="28" colspan="8"> <div align="center" class="homeMainText">Questions about PlasmID? Please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.</div></td>
  </tr>
</table>
    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

