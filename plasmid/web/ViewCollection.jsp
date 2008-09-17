<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.coreobject.Clone" %> 

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
	<jsp:include page="collectionListTitle.jsp" />

<p>
<table width="100%" border="0">
  <tr> 
    <td width="10%" valign="top" class="formlabel">Collection:</td>
    <td width="40%" valign="top" class="itemtext"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="name"/></td>
    <td width="20%" valign="top" class="formlabel">Price for member:</td>
    <td width="30%" valign="top" class="itemtext">$<bean:write name="<%=Constants.SINGLECOLLECTION%>" property="displayMemberPrice"/></td>
  </tr>
  <tr> 
    <td width="10%" valign="top" class="formlabel">&nbsp;</td>
    <td width="40%" valign="top" class="itemtext">&nbsp;</td>
    <td width="20%" valign="top" class="formlabel">Price for non-member:</td>
    <td width="30%" valign="top" class="itemtext">$<bean:write name="<%=Constants.SINGLECOLLECTION%>" property="displayPrice"/></td>
  </tr>
  <tr> 
    <td colspan="4" valign="top" class="formlabel">Description:</td>
  </tr> 
  <tr> 
    <td colspan="4" valign="top" class="formlabel"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="description"/></td>
  </tr> 
  <tr> 
    </tr>
  <tr> 
    <td colspan="2" valign="top" class="formlabel">Use Restriction</td>
    <td colspan="2" valign="top" class="itemtext"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="restriction"/></td>
  </tr>
</table>

<p class="text">
Total number of clones in the collection: <bean:write name="refseqSearchForm" property="numOfClones"/>
</P>

<logic:equal name="refseqSearchForm" property="isDownload" value="<%=Constants.BOOLEAN_ISDOWNLOAD_NO%>">
<p class="text">
To request the entire collection, click "Add Collection To Cart".<br>
To request individual clones from the collection, view the list and click "add to cart" (far right-hand side of the table) to add individual clones to your cart.
</p>
</logic:equal>

<logic:notEqual name="refseqSearchForm" property="isDownload" value="<%=Constants.BOOLEAN_ISDOWNLOAD_NO%>">
<p class="text">
To request the entire collection, click "Add Collection To Cart.".<br>
To request individual clones from the collection, download the Excel file, find the clones of interest, copy the Clone IDs and use them in a "search by clone identifier" search at PlasmID.  You can then click to add individual clones to your cart (far right-hand side of the table).
</p>
</logic:notEqual>

<p>
<table width="100%" border="0">
  <tr>
    <td align="right" class="text">
        <html:form action="AddCollectionToCart.do">
        <html:hidden property="collectionName"/>
        <html:hidden property="pagesize"/>
        <html:hidden property="page"/>
        <input type="hidden" name="displayPage" value="indirect"/>
        <input name="button" type="submit" class="text" value="Add Collection To Cart"/>
        </html:form>
    </td>
    <td align="left" class="text">
        <html:form action="SetDisplay.do">
            <html:hidden property="pagesize"/>
            <html:hidden property="page"/>
            <input type="hidden" name="displayPage" value="indirect"/>
            <input type="hidden" name="forward" value="collection"/>
            <html:submit property="button" value="<%=Constants.DOWNLOAD%>"/>
        </html:form>
    </td>
  </tr>
</table>

<logic:equal name="refseqSearchForm" property="isDownload" value="<%=Constants.BOOLEAN_ISDOWNLOAD_NO%>">
<p>
<table width="100%" border="0">
    <tr>
        <td class="mainbodytexthead" align="left">List of clones:</td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>

<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid&displayPage=<bean:write name="displayPage"/>&forward=collection">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype&displayPage=<bean:write name="displayPage"/>&forward=collection">Clone Type</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&forward=collection">Species Specific ID</a></td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Keywords</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq&displayPage=<bean:write name="displayPage"/>&forward=collection">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=mutdis&displayPage=<bean:write name="displayPage"/>&forward=collection">Mutation/ Discrepancy</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat&displayPage=<bean:write name="displayPage"/>&forward=collection">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname&displayPage=<bean:write name="displayPage"/>&forward=collection">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection&displayPage=<bean:write name="displayPage"/>&forward=collection">Selection Markers</a></td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="<%=Constants.SINGLECOLLECTION%>" property="clones" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </logic:equal>
    <logic:notEqual name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <logic:iterate name="clone" property="inserts" id="insert">
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.VCNUMBER%>">
    <td><a target="_blank" href="http://www.tigr.org/tigr-scripts/CMR2/GenePage.spl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FTNUMBER%>">
    <td><bean:write name="insert" property="geneid"/></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FBID%>">
    <td><a target="_blank" href="http://www.flybase.org/.bin/fbidq.html?<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.WBGENEID%>">
    <td><a target="_blank" href="http://www.wormbase.org/db/gene/gene?name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.TAIR%>">
    <td><a target="_blank" href="http://arabidopsis.org/servlets/TairObject?type=locus&name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
   </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.BANUMBER%>">
    <td><bean:write name="insert" property="geneid"/></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.LOCUS_TAG%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=&term=<bean:write name="insert" property="geneid"/>&go=Go"><bean:write name="insert" property="geneid"/></a></td>
   </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="annotation"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqidForNCBI"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutdis"/></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    </logic:notEqual>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <html:form action="SetDisplay.do">
    <html:hidden property="pagesize"/>
    <html:hidden property="page"/>
    <input type="hidden" name="displayPage" value="indirect"/>
    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
    <input type="hidden" name="forward" value="collection"/>
    <logic:equal name="clone" property="isAddedToCart" value="true">
        <td valign="center" bgcolor="blue">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:equal>
    <logic:notEqual name="clone" property="isAddedToCart" value="true">
        <td valign="center">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:notEqual>
    </html:form>
    </td>
    </tr>
  </logic:iterate>
</table>

<html:form action="SetDisplay.do">
    <html:hidden property="pagesize"/>
    <html:hidden property="page"/>
    <input type="hidden" name="displayPage" value="indirect"/>
    <input type="hidden" name="forward" value="collection"/>
<table width="100%" border="0">
    <tr>
        <td width="70%">&nbsp;</td>
        <td class="mainbodytexthead"><html:submit property="button" value="<%=Constants.DOWNLOAD%>"/></td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>
</html:form>
</logic:equal>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

