<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.form.RefseqSearchForm" %> 
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
    <td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="864" align="left" valign="top">
	<jsp:include page="searchResultTitle.jsp" />

<bean:define id="size" name="refseqSearchForm" property="pagesize"/>
<bean:define id="p" name="refseqSearchForm" property="page"/>
<bean:define id="total" name="numOfFound"/>

<html:form action="SetDisplay.do">

<p class="mainbodytexthead">List of search terms found</p>
<table width="100%" border="0">
    <tr class="mainbodytexthead">
        <td align="left" class="mainbodytexthead">Page: 
            <html:select property="page">
                <%  int i=0;
                    while(i<Integer.parseInt(total.toString())/Integer.parseInt(size.toString())) {
                %>
                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
                <%      i++;
                    }
                    if((Integer.parseInt(total.toString())%Integer.parseInt(size.toString()))>0)
                %>
                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
            </html:select>
            <html:submit property="button" value="Display"/>
        </td>
        <td class="mainbodytexthead"><html:submit property="button" value="<%=Constants.DOWNLOAD%>"/></td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>

<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype">Clone Type</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid">Species Specific ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=genesymbol">Gene Symbol</td>
    <td class="tableheader">Keywords</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=mutdis">Mutation/ Discrepancy</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection">Selection Markers</a></td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i= Integer.parseInt(size.toString())*(Integer.parseInt(p.toString())-1);
  %>
  <logic:iterate name="found" id="clone" length="size" offset="<%=(new Integer(i)).toString()%>">

  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="refseqSearchForm" property="species"/>"><bean:write name="clone" property="name"/></a></td>
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
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.LOCUS_TAG%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=&term=<bean:write name="insert" property="geneid"/>&go=Go"><bean:write name="insert" property="geneid"/></a></td>
   </logic:equal>   
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.BANUMBER%>">
    <td><bean:write name="insert" property="geneid"/></td>
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
        <input type="hidden" name="displayPage" value="indirect"/>
        <input type="hidden" name="forward" value="vectorSearchResult"/>
        <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
    <logic:equal name="clone" property="isAddedToCart" value="true">
        <TD valign="center" bgcolor="blue">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:equal>
    <logic:notEqual name="clone" property="isAddedToCart" value="true">
        <TD valign="center">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:notEqual>
    </html:form>
    </tr>
  </logic:iterate> 
</table>
</html:form>

<html:form action="SetDisplay.do">
<table width="100%" border="0">
    <tr class="mainbodytexthead">
        <td align="left" class="mainbodytexthead">Page: 
            <html:select property="page">
                <%  int k=0;
                    while(k<Integer.parseInt(total.toString())/Integer.parseInt(size.toString())) {
                %>
                        <html:option value="<%=(new Integer(k+1)).toString()%>"/>
                <%      k++;
                    }
                    if((Integer.parseInt(total.toString())%Integer.parseInt(size.toString()))>0)
                %>
                        <html:option value="<%=(new Integer(k+1)).toString()%>"/>
            </html:select>
            <html:submit property="button" value="Display"/>
        </td>
        <td class="mainbodytexthead"><html:submit property="button" value="<%=Constants.DOWNLOAD%>"/></td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>
</html:form>

      </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

