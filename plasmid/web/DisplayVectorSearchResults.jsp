<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Clone" %> 
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
    <td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="864" align="left" valign="top">
	<jsp:include page="searchByRefseqTitle.jsp" />
      <html:form action="SetDisplay.do">
<html:hidden property="pagesize"/>
        <html:hidden property="species"/>
<input type="hidden" name="forward" value="vectorSearchResult">

<bean:define id="size" name="vectorSearchForm" property="pagesize"/>
<bean:define id="total" name="numOfFound"/>
<p class="mainbodytexthead">List of search terms found</p>
<table width="100%" border="0">
    <tr class="mainbodytexthead">
        <td align="left" class="mainbodytexthead">Page: 
            <html:select property="page">
                <%  
                    for(int i=0; i<Integer.parseInt(total.toString())/Integer.parseInt(size.toString())+1; i++) {
                %>
                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
                <%  }
                %>
            </html:select>
            <html:submit property="button" value="Display"/>
        </td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>

<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Clone Type</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Species Specific ID</a></td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertmutation&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Mutation</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertdiscrepancy&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Discrepancy</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Selection Markers</a></td>
    <td class="tableheader">Special MTA</td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);
  %>
  <logic:iterate name="found" id="clone" length="pagesize" offset="<%=(new Integer(i)).toString()%>">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="species"/>"><bean:write name="clone" property="name"/></a></td>
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
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=genome&cmd=search&term=txid177416%5borgn"><bean:write name="insert" property="geneid"/></a></td>
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
    </logic:notEqual>
    </td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
    <html:form action="SetDisplay.do">
    <html:hidden property="pagesize"/>
    <html:hidden property="page"/>
    <html:hidden property="species"/>
    <html:hidden property="refseqType"/>
    <input type="hidden" name="displayPage" value="indirect"/>
    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
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

      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

