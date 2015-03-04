<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%-- 10/15/2014 PlasmID removed "NO" Orders as an option, any commented out code below reflects this--%>
<html>
<head>
<title>Search Unique Inserts</title>
<meta name='description' content='Search tools to locate unique inserts such as cDNA, ORF, or shRNA.'>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="orderTitle.jsp" />
<%--<table width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
        <tr></tr>--%> 
        <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
        <jsp:include page="menu.jsp" />
	</td>
        <td width="83%" align="left" valign="top">--%>
	<%--<jsp:include page="orderoverviewTitle.jsp" />--%>

        <%--<table width="100%" border="0">
        <td height="225" colspan="2" valign="top"><div align="center">
        <br>
        <span class="homepageText2 style3" style="font-size:20px"><a name="cost"></a><br>
      CLONE COSTS </span>       
        <div align="center"><span class="itemtext"> *You may consider these prices a quote for all clones in any quantity.</span></div></div>
            <table width="90%" border="3" align="center" id="gray">
            <tr class="tableheader" style="font-size:20px">
            <td width="50%" bgcolor="#6699CC">Pricing Category</td>
          <%--<td width="25%">Individual Clone</td>--%>
            <%--<td width="50%" bgcolor="#6699CC" >Individual Clone with QC Test</td>
            </tr>
            <tr class="tableinfo" style="font-size:18px">
            <td>Harvard University and DF/HCC Members</td>
            <%--<td align="right">$46.00</td>--%>
            <%--<td align="right">$52.00</td>
            </tr>
            <tr class="tableinfo" style="font-size:18px">
            <td>Other Academics and Nonprofits</td>
            <%--<td align="right">$58.00</td>--%>
            <%--<td align="right">$65.00</td>
            </tr>
            <tr class="tableinfo" style="font-size:18px">
            <td>Commercial Users</td>
            <%--<td align="right">$70.00</td>--%>
            <%--<td align="right">$78.00</td>
            </tr>
            </table>
        <div align="center"><span class="itemtext"> *Shipping is additional. Either provide a FedEx number or shipping is a flat fee of $10 for domestic orders or $20 for international orders. <br>
      No charge for shipping to the Harvard Medical School community if order is picked up. Must have building access.</span></div>
        <div align="center"><span class="itemtext">Please click <a target="_blank" href="http://plasmid.med.harvard.edu/PLASMID/PIList.html">here</a> for the list of DF/HCC members.</span></div>
        <br>
        <tr>
    <td height="40" colspan="2" valign="top">--%>
            <p class="mainbodytexthead">You can search for clones using various criteria</p>
      <p class="mainbodytext">PLEASE LOG IN BEFORE SEARCHING FOR CLONES! </p>  
  <br>
  <br>
      <table width="100%" align="left">
          <tr class="tableheader">
            <td width="30%">Search Method</td>
          <td width="70%">Description</td>
         </tr>
        <tr class="tableinfo">
            <td><a href="GeneSearch.xhtml">Human & Mouse Gene</a></td>
            <td align="left">Our recommended method of searching the PlasmID database. Simply enter your gene of interest and query our database against the latest reference sequence!</td>
        </tr>
        <tr class="tableinfo">
        <td><a href="PrepareAdvancedSearch.do">Advanced Search</a></td>
          <td align="left">Search by gene name, gene symbol, vector name, vector feature, author name, publication, or species.</td>
        </tr>
        <tr class="tableinfo">
            <td><a href="PrepareBlast.do">Blast Search</a></td>
          <td align="left">Search our database by a similarity alignment of your nucleotide or amino acid sequence.</td>
        </tr>
        <tr class="tableinfo">
            <td><a href="SearchClone.jsp">Search by Clone Identifier</a></td>
          <td align="left">Search by PlasmID, CloneID, FLH#, or other IDs. </td>
        </tr>
        <tr class="tableinfo">
            <td><a href="GetVectorPropertyTypes.do">Search by Vector</a></td>
          <td align="left">Search based on the properties of the vector such as, assay type, cloning method or expression characteristics.</td>
        </tr>
        <tr class="tableinfo">
            <td><a href="GetVectorsByType.do">Empty Vectors</a></td>
          <td align="left">View our full list of empty vectors. To narrow down the list view the search and order tool bar.</td>
        </tr>
        <tr class="tableinfo">
            <td><a href="GetCollectionList.do">Available Collections</a></td>
          <td align="left">View the full list of all of PlasmID's available collections. The <a href="GetCollectionList.do">collection overview</a> includes common 
              names for organisms in our collection.</td>
        </tr>
      </table>
      <br>
      <br>        
      <span class="mainbodytext">Have additional questions? Please see our <a href="FAQ.jsp">FAQ!</a><br></span>
      <br>        
      <span class="mainbodytext">Please give credit where credit is due!</b></span>
      <br>
      <span class="mainbodytext"> Click on the clone ID to find the reference(s) associated with the clone and the author(s) who donated the clone.</span><br>
      <span class="mainbodytext">The people who constructed, published,  and donated the clone should be cited whenever you publish work that uses the clone that you ordered.<br></span>
     </td>
    </tr>
    <br>
<jsp:include page="footer.jsp" /></body>
</div>
</html>