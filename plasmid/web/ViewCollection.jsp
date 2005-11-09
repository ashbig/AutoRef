<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 

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
    <td width="10%" class="formlabel">Collection:</td>
    <td width="40%" class="itemtext"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="name"/></td>
    <td width="7%" class="formlabel">Price for member:</td>
    <td width="38%" class="itemtext"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="memberprice"/></td>
  </tr>
  <tr> 
    <td width="10%" class="formlabel">&nbsp;</td>
    <td width="40%" class="itemtext">&nbsp;</td>
    <td width="7%" class="formlabel">Price for non-member:</td>
    <td width="38%" class="itemtext"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="price"/></td>
  </tr>
  <tr> 
    <td colspan="4" class="formlabel"><bean:write name="<%=Constants.SINGLECOLLECTION%>" property="description"/></td>
  </tr> 
</table>

<p>
<html:form action="AddCollectionToCart.do">
<html:hidden property="collectionName"/>
<html:hidden property="pagesize"/>
<html:hidden property="page"/>
<input type="hidden" name="displayPage" value="indirect"/>
<table width="100%" border="0">
  <tr> 
    <td width="20%" class="text">List of clones:</td>
    <td width="80%"><input name="button" type="submit" class="text" value="Add Collection To Cart"/></td>
  </tr>
</table>
</html:form>

<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid&displayPage=<bean:write name="displayPage"/>&forward=collection">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype&displayPage=<bean:write name="displayPage"/>&forward=collection">Clone Type</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&forward=collection">Species Specific ID</a></td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq&displayPage=<bean:write name="displayPage"/>&forward=collection">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertmutation&displayPage=<bean:write name="displayPage"/>&forward=collection">Mutation</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertdiscrepancy&displayPage=<bean:write name="displayPage"/>&forward=collection">Discrepancy</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat&displayPage=<bean:write name="displayPage"/>&forward=collection">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname&displayPage=<bean:write name="displayPage"/>&forward=collection">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection&displayPage=<bean:write name="displayPage"/>&forward=collection">Selection Markers</a></td>
    <td class="tableheader">Special MTA</td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="<%=Constants.SINGLECOLLECTION%>" property="clones" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
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
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
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

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

